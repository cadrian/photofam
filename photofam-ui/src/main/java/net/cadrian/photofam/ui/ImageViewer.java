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
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * @author Cyril ADRIAN
 */
class ImageViewer extends UIComponent {

	private static class FirstImageFilter implements ImageFilter {

		private boolean first = true;

		@Override
		public boolean accept (Image a_image) {
			boolean result = first;
			first = false;
			return result;
		}

		public void reset () {
			first = true;
		}
	}

	private static final FirstImageFilter FIRST = new FirstImageFilter();

	private ScreenChanges screen;
	private String noImageMessage;

	private final ImageViewport viewport = new ImageViewport();
	private final JLabel imageName = new JLabel();

	@Override
	void init (ScreenChanges a_screen, Services services) {
		assert SwingUtilities.isEventDispatchThread();

		screen = a_screen;
		viewport.init();

		setLayout(new BorderLayout());
		add(viewport, BorderLayout.CENTER);

		JToolBar tools = new JToolBar();
		add(tools, BorderLayout.SOUTH);

		noImageMessage = services.getTranslationService().get("imageviewer.label.noimage");
		imageName.setHorizontalAlignment(JLabel.CENTER);
		add(imageName, BorderLayout.NORTH);
		imageName.setText(noImageMessage);
	}

	@Override
	void prepare (PanelData a_data) {
		assert a_data == null;
	}

	@Override
	void showAlbum (Album a_album) {
		if (a_album != null) {
			FIRST.reset();
			List<Image> images = a_album.getImages(FIRST);
			if (!images.isEmpty()) {
				screen.showImage(images.get(0));
			} else {
				screen.showImage(null);
			}
		} else {
			screen.showImage(null);
		}
	}

	@Override
	void showImage (Image a_image) {
		viewport.showImage(a_image);
		if (a_image != null) {
			imageName.setText(a_image.getName());
		} else {
			imageName.setText(noImageMessage);
		}
	}

}
