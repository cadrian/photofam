/**
 * This file is part or PhotoFam.
 * 
 * PhotoFam is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.cadrian.photofam.ui;

import net.cadrian.photofam.dao.AlbumDAO;
import net.cadrian.photofam.model.Album;
import net.cadrian.photofam.model.Image;
import net.cadrian.photofam.model.ImageFilter;
import net.cadrian.photofam.model.Tag;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
class Thumbnails extends JPanel implements UIComponent {

	private static final Logger log = LoggerFactory.getLogger(Thumbnails.class);

	private static final int THUMBNAIL_SIZE = 128;
	private static final int THUMBNAIL_GAP = 3;
	private static final int THUMBNAIL_PAYLOAD = THUMBNAIL_SIZE + 2 * THUMBNAIL_GAP;

	private final JPanel view = new JPanel(new FlowLayout(FlowLayout.CENTER, THUMBNAIL_GAP, THUMBNAIL_GAP));
	private final JScrollBar scroll = new JScrollBar(JScrollBar.VERTICAL);
	private Album album;
	private transient List<Image> images;
	private transient ThumbnailViewport[] viewports;
	private transient int imagesPerRow;
	private transient int imageIndexInRow;
	private transient int numberOfVisibleRows;
	private transient volatile int currentSelectedImage = -1;
	private volatile int currentScrollValue = -1;

	ScreenChanges screen;

	@Override
	public void init (ScreenChanges a_screen, AlbumDAO a_dao, ResourceBundle a_bundle) {
		assert SwingUtilities.isEventDispatchThread();

		screen = a_screen;
		view.setPreferredSize(new Dimension(2 * THUMBNAIL_PAYLOAD, 4 * THUMBNAIL_PAYLOAD));

		scroll.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged (AdjustmentEvent a_e) {
				scrollChanged();
			}
		});

		setLayout(new BorderLayout());
		add(view, BorderLayout.CENTER);
		add(scroll, BorderLayout.EAST);
	}

	@Override
	public void setSize (int a_width, int a_height) {
		super.setSize(a_width, a_height);
		if (images != null) {
			showImages();
		}
	}

	@Override
	public void prepare (PanelData a_data) {
		assert a_data == null;
	}

	@Override
	public void showAlbum (Album a_album) {
		if (log.isDebugEnabled()) {
			log.debug("showing album: " + a_album);
		}
		album = a_album;
		images = album.getImages(ImageFilter.ALL);
		showImages();
	}

	@Override
	public void showImage (Image a_image) {
		if (log.isDebugEnabled()) {
			log.debug("showing image: " + a_image);
		}
		setCurrentImage(a_image);
	}

	@Override
	public void filterTag (Tag a_tag) {
		if (log.isDebugEnabled()) {
			log.debug("filtering tag: " + a_tag);
		}
		images = album.getImages(new TagImageFilter(a_tag));
		showImages();
	}

	private void showImages () {
		if (log.isDebugEnabled()) {
			log.debug("images: " + images);
		}
		Image currentImage = getCurrentImage();
		int width = getWidth();
		int height = getHeight();
		imagesPerRow = width / THUMBNAIL_PAYLOAD;
		int n = ((width % THUMBNAIL_PAYLOAD) == 0) ? 0 : 1;
		int rows = n + images.size() / imagesPerRow;
		numberOfVisibleRows = height / THUMBNAIL_PAYLOAD;
		if (log.isDebugEnabled()) {
			log.debug("imagesPerRow: " + width + "/" + THUMBNAIL_PAYLOAD + " = " + imagesPerRow);
			log.debug("numberOfVisibleRows: " + height + "/" + THUMBNAIL_PAYLOAD + " = " + numberOfVisibleRows);
		}
		int pos = 0;
		if (currentImage != null) {
			pos = images.indexOf(currentImage);
		}
		int nv = numberOfVisibleRows * imagesPerRow;
		if (viewports == null || nv != viewports.length) {
			view.removeAll();
			viewports = new ThumbnailViewport[nv];
			for (int i = 0; i < nv; i++) {
				final int viewportPosition = i;
				final ThumbnailViewport viewport = new ThumbnailViewport(THUMBNAIL_SIZE, new Runnable() {
					public void run () {
						showImage(viewportPosition);
					}
				});
				viewports[i] = viewport;
				viewport.setPreferredSize(new Dimension(THUMBNAIL_SIZE, THUMBNAIL_SIZE));
				view.add(viewport);
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("scroll(" + pos + ", " + numberOfVisibleRows + ", 0, " + rows + ")");
		}
		scroll.setValues(pos, numberOfVisibleRows, 0, rows);
		scroll.setBlockIncrement(numberOfVisibleRows);
		if (currentImage != null) {
			showImage(currentImage);
		}
		scrollChanged();
	}

	void showImage (int viewportNumber) {
		if (currentSelectedImage != viewportNumber) {
			if (currentSelectedImage != -1) {
				viewports[currentSelectedImage].setSelected(false);
			}
			viewports[viewportNumber].setSelected(true);
			currentSelectedImage = viewportNumber;
			screen.showImage(images.get(scroll.getValue() * imagesPerRow + viewportNumber));
		}
	}

	private Image getCurrentImage () {
		return images.get(scroll.getValue() * imagesPerRow + imageIndexInRow);
	}

	private void setCurrentImage (Image image) {
		int i = images.indexOf(image);
		imageIndexInRow = i % imagesPerRow;
		int value = i / imagesPerRow;
		int scrollValue = scroll.getValue();
		if (value < scrollValue) {
			scroll.setValue(value);
		} else if (value > scrollValue + numberOfVisibleRows) {
			scroll.setValue(value - numberOfVisibleRows);
		} else {
			refreshViewports();
		}
	}

	void scrollChanged () {
		if (images != null) {
			int value = scroll.getValue();
			if (value != currentScrollValue) {
				currentScrollValue = value;
				refreshViewports();
			}
		}
	}

	private void refreshViewports () {
		Image currentImage = getCurrentImage();
		int nRows = numberOfVisibleRows;
		int nCols = imagesPerRow;
		int value = currentScrollValue;
		for (int row = 0; row < nRows; row++) {
			for (int col = 0; col < nCols; col++) {
				ThumbnailViewport viewport = viewports[row * nCols + col];
				Image image = images.get((row + value) * nCols + col);
				viewport.showImage(image);
				viewport.setSelected(image == currentImage);
			}
		}
	}

}
