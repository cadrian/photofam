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

import net.cadrian.photofam.services.album.Image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;

/**
 * @author Cyril ADRIAN
 */
public class ImageViewport extends JComponent {

	private Image image;

	void init () {
		setPreferredSize(new Dimension(640, 480));
		setDoubleBuffered(true);
		setFocusable(false);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	void showImage (Image a_image) {
		image = a_image;
		repaint();
	}

	@Override
	protected void paintComponent (Graphics g) {
		super.paintComponent(g);
		if (g != null && image != null) {
			Graphics2D scratchGraphics = (Graphics2D) g.create();
			try {
				int angle = image.getRotation();
				if (angle != 0) {
					scratchGraphics.rotate(angle * Math.PI / 180, getWidth() / 2, getHeight() / 2);
				}
				scratchGraphics.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
			} finally {
				scratchGraphics.dispose();
			}
		}
	}
}
