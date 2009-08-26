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

import net.cadrian.photofam.model.Image;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * @author Cyril ADRIAN
 */
class ThumbnailViewport extends ImageViewport {

	private final int size;

	static final Border selectedBorder = BorderFactory.createLineBorder(Color.ORANGE);
	static final Border pressedBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	static final Border relievedBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);

	// the thumbnails play the role of cache by keeping hard references to the AWT thumbnail image
	private transient java.awt.Image thumbnail;

	private boolean selected;

	ThumbnailViewport (int a_size, final Runnable whenClick) {
		size = a_size;
		addMouseListener(new MouseListener() {
			private volatile boolean pressed;

			@Override
			public void mouseReleased (MouseEvent a_e) {
				pressed = false;
				setBorder(null);
			}

			@Override
			public void mousePressed (MouseEvent a_e) {
				pressed = true;
				setBorder(pressedBorder);
			}

			@Override
			public void mouseExited (MouseEvent a_e) {
				if (pressed) {
					setBorder(relievedBorder);
				} else if (isSelected()) {
					setBorder(selectedBorder);
				} else {
					setBorder(null);
				}
			}

			@Override
			public void mouseEntered (MouseEvent a_e) {
				if (pressed) {
					setBorder(pressedBorder);
				} else {
					setBorder(relievedBorder);
				}
			}

			@Override
			public void mouseClicked (MouseEvent a_e) {
				whenClick.run();
			}
		});
	}

	@Override
	void init () {
		setDoubleBuffered(true);
		setFocusable(true);
		setBorder(null);
	}

	@Override
	void showImage (Image a_image) {
		if (a_image == null) {
			// let the GC work
			thumbnail = null;
		} else {
			thumbnail = a_image.getThumbnail(size);
		}
		super.showImage(a_image);
	}

	@Override
	protected java.awt.Image getAWTImage () {
		if (log.isDebugEnabled()) {
			log.debug("TRACE", new Exception("TRACE"));
		}
		return thumbnail;
	}

	boolean isSelected () {
		return selected;
	}

	void setSelected (boolean a_selected) {
		selected = a_selected;
		repaint();
	}
}
