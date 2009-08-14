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
package net.cadrian.photofam.model;

/**
 * @author Cyril ADRIAN
 */
public interface ImageAlbumListener {

	/**
	 * Called when the image name changed
	 * 
	 * @param image
	 *            the modified image
	 * @param oldName
	 *            the old name (use {@link Image#getName() image.getName()} to get the new name)
	 */
	void imageNameChanged (Image image, String oldName);

	/**
	 * Called when the image rotation changed
	 * 
	 * @param image
	 *            the modified image
	 * @param oldRotation
	 *            the old rotation (use {@link Image#getRotation() image.getRotation()} to get the new rotation)
	 */
	void imageRotated (Image image, int oldRotation);

	/**
	 * Called when a tag was added to an image
	 * 
	 * @param image
	 *            the modified image
	 * @param tag
	 *            the added tag
	 */
	void imageTagAdded (Image image, Tag tag);

	/**
	 * Called when a tag was removed to an image
	 * 
	 * @param image
	 *            the modified image
	 * @param tag
	 *            the removed tag
	 */
	void imageTagRemoved (Image image, Tag tag);

	/**
	 * Called when an image visibility changed
	 * 
	 * @param image
	 *            the modified image
	 * @param wasVisible
	 *            the previous visibility status (use {@link Image#isVisible() image.isVisible()} to get the new status)
	 */
	void imageVisibilityChanged (Image image, boolean wasVisible);

}
