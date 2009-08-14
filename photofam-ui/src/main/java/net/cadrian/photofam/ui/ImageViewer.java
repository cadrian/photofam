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
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * @author Cyril ADRIAN
 */
class ImageViewer extends JPanel implements UIComponent {

	private ScreenChanges screen;
	private String noImageMessage;

	private final ImageViewport viewport = new ImageViewport();
	private final JLabel imageName = new JLabel();
	private Album album;
	private List<Image> images = Collections.emptyList();
	private int currentImageIndex;
	private ImageFilter filter;

	@Override
	public void init (ScreenChanges a_screen, AlbumDAO a_dao, ResourceBundle a_bundle) {
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
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				showFirstImage();
			}
		}));
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(previousImageLocation)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				showPreviousImage();
			}
		}));
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(nextImageLocation)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				showNextImage();
			}
		}));
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(lastImageLocation)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				showLastImage();
			}
		}));
		tools.add(new JSeparator(JSeparator.VERTICAL) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent (Graphics a_g) {
				// nothing
			}
		});
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(rotateLeftLocation)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				rotateLeft();
			}
		}));
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(rotateRightLocation)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				rotateRight();
			}
		}));
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(tagsLocation)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				// TODO Auto-generated method stub

			}
		}));
		tools.add(new JSeparator(JSeparator.VERTICAL) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent (Graphics a_g) {
				// nothing
			}
		});
		tools.add(toolButton(new AbstractAction(null, new ImageIcon(deleteLocation)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				deleteImage();
			}
		}));

		noImageMessage = a_bundle.getString("imageviewer.label.noimage");
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
	public void prepare (PanelData a_data) {
		assert a_data == null;
	}

	@Override
	public void showAlbum (Album a_album) {
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
	public void filterTag (final Tag a_tag) {
		filter = new ImageFilter() {

			@Override
			public boolean accept (Image a_image) {
				boolean result = false;
				Collection<Tag> tags = a_image.getTags();
				Set<Tag> cache = new HashSet<Tag>();
				Iterator<Tag> i = tags.iterator();
				while (!result && i.hasNext()) {
					Tag imageTag = i.next();
					while (!result && imageTag != null && !cache.contains(imageTag)) {
						result = a_tag.equals(imageTag);
						if (!result) {
							cache.add(imageTag);
							imageTag = imageTag.getParent();
						}
					}
				}
				return result;
			}
		};
		if (album != null) {
			Image currentImage = images.get(currentImageIndex);
			images = album.getImages(filter);
			if (!images.contains(currentImage)) {
				showFirstImage();
			} else {
				// redisplay because the viewport title and the toolbar buttons may change
				screen.showImage(currentImage);
			}
		}
	}

	@Override
	public void showImage (Image a_image) {
		viewport.showImage(a_image);
		if (a_image != null) {
			imageName.setText(a_image.getName() + " (" + (currentImageIndex + 1) + " / " + images.size() + ")");
		} else {
			imageName.setText(noImageMessage);
		}
	}

	private void showCurrentImage (int c) {
		if (!images.isEmpty()) {
			currentImageIndex = c;
			screen.showImage(images.get(c));
		} else {
			screen.showImage(null);
		}
	}

	void showFirstImage () {
		showCurrentImage(0);
	}

	void showPreviousImage () {
		if (currentImageIndex > 0) {
			showCurrentImage(currentImageIndex - 1);
		}
	}

	void showNextImage () {
		if (currentImageIndex < images.size() - 1) {
			showCurrentImage(currentImageIndex + 1);
		}
	}

	void showLastImage () {
		showCurrentImage(images.size() - 1);
	}

	void rotateLeft () {
		if (!images.isEmpty()) {
			images.get(currentImageIndex).rotate(-90);
			showCurrentImage(currentImageIndex);
		}
	}

	void rotateRight () {
		if (!images.isEmpty()) {
			images.get(currentImageIndex).rotate(90);
			showCurrentImage(currentImageIndex);
		}
	}

	void deleteImage () {
		images.get(currentImageIndex).setVisible(false);
		images = album.getImages(filter);
	}

}
