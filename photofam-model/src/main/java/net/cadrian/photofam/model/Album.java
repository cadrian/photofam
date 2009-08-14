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

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @author Cyril ADRIAN
 */
public interface Album extends Comparable<Album> {

	/**
	 * @return the album name
	 */
	String getName ();

	/**
	 * @param name
	 *            the new album name
	 */
	void setName (String name);

	/**
	 * @param filter
	 *            the image filter
	 * 
	 * @return the images in the album; never <code>null</code>.
	 */
	List<Image> getImages (ImageFilter filter);

	/**
	 * @param albumListener
	 *            the listener to add
	 */
	void addAlbumListener (AlbumListener albumListener);

	/**
	 * @param imageAlbumListener
	 *            the listener to add
	 */
	void addImageAlbumListener (ImageAlbumListener imageAlbumListener);

	/**
	 * @return the list of all the tags on every image; never <code>null</code>.
	 */
	Collection<Tag> getAllTags ();

	/**
	 * Add images from the given directory. Does not duplicate already loaded images from that directory (useful for scanning
	 * modified directories)
	 * 
	 * @param directory
	 *            the directory to scan, never <code>null</code>
	 */
	void addImages (File directory);

}
