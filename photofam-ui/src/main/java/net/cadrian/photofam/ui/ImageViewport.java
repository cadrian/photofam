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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class ImageViewport extends JComponent {

	private static final Logger log = LoggerFactory.getLogger(ImageViewport.class);

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
			Graphics2D g2d = (Graphics2D) g.create();
			try {
				paintImage(g2d);
			} finally {
				g2d.dispose();
			}
		}
	}

	private void paintImage (Graphics2D g2d) {
		assert g2d != null;
		assert image != null;

		java.awt.Image theImage = image.getImage();

		int angle = image.getRotation();
		if (angle != 0) {
			g2d.rotate(angle * Math.PI / 180, getWidth() / 2, getHeight() / 2);
		}

		int imageHeight = theImage.getHeight(this);
		int imageWidth = theImage.getWidth(this);

		Rectangle rect = g2d.getClipBounds();
		int h0 = rect.height;
		int w0 = rect.width;
		int x, y, w, h;

		double rh = (double) h0 / imageHeight;
		double rw = (double) w0 / imageWidth;
		if (rh < rw) {
			h = (int) (imageHeight * rh + .5);
			w = (int) (imageWidth * rh + .5);
		} else {
			h = (int) (imageHeight * rw + .5);
			w = (int) (imageWidth * rw + .5);
		}
		x = (getWidth() - w) / 2;
		y = (getHeight() - h) / 2;

		if (log.isDebugEnabled()) {
			log.debug("Image:     " + imageWidth + "x" + imageHeight + " (rotation: " + angle + "°)");
			log.debug("Component: " + w0 + "x" + h0);
			if (rh < rw) {
				log.debug("Using height scale " + rh);
			} else {
				log.debug("Using width scale " + rw);
			}
			log.debug("x=" + x + ", y=" + y + ", w=" + w + ", h=" + h);
		}

		g2d.drawImage(theImage, x, y, w, h, this);
	}
}
