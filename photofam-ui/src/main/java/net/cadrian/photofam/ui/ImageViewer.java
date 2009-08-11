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

import net.cadrian.photofam.Services;
import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.album.Image;
import net.cadrian.photofam.services.album.ImageFilter;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * @author Cyril ADRIAN
 */
class ImageViewer extends UIComponent {

	private ScreenChanges screen;
	private String noImageMessage;

	private final ImageViewport viewport = new ImageViewport();
	private final JLabel imageName = new JLabel();
	private Album album;
	private List<Image> images = Collections.emptyList();
	private int currentImage;
	private ImageFilter filter;

	@Override
	void init (ScreenChanges a_screen, Services services) {
		assert SwingUtilities.isEventDispatchThread();

		screen = a_screen;
		viewport.init();

		setLayout(new BorderLayout());
		add(viewport, BorderLayout.CENTER);

		URL firstImageLocation = ImageViewer.class.getClassLoader().getResource("img/first-image.png");
		URL previousImageLocation = ImageViewer.class.getClassLoader().getResource("img/previous-image.png");
		URL nextImageLocation = ImageViewer.class.getClassLoader().getResource("img/next-image.png");
		URL lastImageLocation = ImageViewer.class.getClassLoader().getResource("img/last-image.png");
		URL rotateLeftLocation = ImageViewer.class.getClassLoader().getResource("img/rotate-left.png");
		URL rotateRightLocation = ImageViewer.class.getClassLoader().getResource("img/rotate-right.png");
		URL tagsLocation = ImageViewer.class.getClassLoader().getResource("img/image-tags.png");
		URL deleteLocation = ImageViewer.class.getClassLoader().getResource("img/delete-image.png");

		JToolBar tools = new JToolBar();
		add(tools, BorderLayout.SOUTH);
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(firstImageLocation)) {

			@Override
			public void actionPerformed (ActionEvent a_e) {
				showFirstImage();
			}
		}));
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(previousImageLocation)) {

			@Override
			public void actionPerformed (ActionEvent a_e) {
				showPreviousImage();
			}
		}));
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(nextImageLocation)) {

			@Override
			public void actionPerformed (ActionEvent a_e) {
				showNextImage();
			}
		}));
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(lastImageLocation)) {

			@Override
			public void actionPerformed (ActionEvent a_e) {
				showLastImage();
			}
		}));
		tools.add(new JSeparator(JSeparator.VERTICAL) {
			@Override
			protected void paintComponent (Graphics a_g) {
				// nothing
			}
		});
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(rotateLeftLocation)) {

			@Override
			public void actionPerformed (ActionEvent a_e) {
				rotateLeft();
			}
		}));
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(rotateRightLocation)) {

			@Override
			public void actionPerformed (ActionEvent a_e) {
				rotateRight();
			}
		}));
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(tagsLocation)) {

			@Override
			public void actionPerformed (ActionEvent a_e) {
				// TODO Auto-generated method stub

			}
		}));
		tools.add(new JSeparator(JSeparator.VERTICAL) {
			@Override
			protected void paintComponent (Graphics a_g) {
				// nothing
			}
		});
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(deleteLocation)) {

			@Override
			public void actionPerformed (ActionEvent a_e) {
				deleteImage();
			}
		}));

		noImageMessage = services.getTranslationService().get("imageviewer.label.noimage");
		imageName.setHorizontalAlignment(JLabel.CENTER);
		add(imageName, BorderLayout.NORTH);
		imageName.setText(noImageMessage);
	}

	private JButton toolButton (Action a) {
		JButton result = new JButton(a);
		result.setBorderPainted(false);
		result.setOpaque(false);
		return result;
	}

	@Override
	void prepare (PanelData a_data) {
		assert a_data == null;
	}

	@Override
	void showAlbum (Album a_album) {
		album = a_album;
		filter = ImageFilter.ALL;
		if (a_album == null) {
			images = Collections.emptyList();
		} else {
			images = a_album.getImages(filter);
		}
		showFirstImage();
	}

	@Override
	void showImage (Image a_image) {
		viewport.showImage(a_image);
		if (a_image != null) {
			imageName.setText(a_image.getName() + " (" + (currentImage + 1) + " / " + images.size() + ")");
		} else {
			imageName.setText(noImageMessage);
		}
	}

	private void showCurrentImage (int c) {
		if (!images.isEmpty()) {
			currentImage = c;
			screen.showImage(images.get(c));
		} else {
			screen.showImage(null);
		}
	}

	void showFirstImage () {
		showCurrentImage(0);
	}

	void showPreviousImage () {
		if (currentImage > 0) {
			showCurrentImage(currentImage - 1);
		}
	}

	void showNextImage () {
		if (currentImage < images.size()) {
			showCurrentImage(currentImage + 1);
		}
	}

	void showLastImage () {
		showCurrentImage(images.size() - 1);
	}

	void rotateLeft () {
		if (!images.isEmpty()) {
			images.get(currentImage).rotate(-90);
			showCurrentImage(currentImage);
		}
	}

	void rotateRight () {
		if (!images.isEmpty()) {
			images.get(currentImage).rotate(90);
			showCurrentImage(currentImage);
		}
	}

	void deleteImage () {
		album.remove(images.get(currentImage));
		images = album.getImages(filter);
	}

}
