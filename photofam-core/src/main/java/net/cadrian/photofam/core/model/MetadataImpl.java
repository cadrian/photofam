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
package net.cadrian.photofam.core.model;

import net.cadrian.photofam.model.Metadata;
import net.cadrian.photofam.model.metadata.Coordinates;
import net.cadrian.photofam.model.metadata.TypedMetadataKey;
import net.cadrian.photofam.xml.albumdata.Image;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.exif.ExifDirectory;
import com.drew.metadata.iptc.IptcDirectory;

/**
 * @author Cyril ADRIAN
 */
class MetadataImpl implements Metadata {

	private static final Logger log = LoggerFactory.getLogger(MetadataImpl.class);

	private static final DateFormat[] TAG_DATE_FORMATS = { new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"),
			new SimpleDateFormat("yyyy:MM:dd HH:mm"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
			new SimpleDateFormat("yyyy-MM-dd HH:mm") };

	private final com.drew.metadata.Metadata extract;
	private transient TypedMetadataKey<Date> date;
	private transient TypedMetadataKey<Coordinates> geolocalization;
	private transient TypedMetadataKey<Integer> orientation;

	/**
	 * @param a_file
	 *            the image file
	 */
	public MetadataImpl (File a_file) {
		com.drew.metadata.Metadata meta;
		try {
			meta = ImageMetadataReader.readMetadata(a_file);
		} catch (ImageProcessingException x) {
			meta = null;
		}
		extract = meta;
	}

	/**
	 * @param a_image
	 */
	public MetadataImpl (Image a_image) {
		List<Date> dates = new ArrayList<Date>();
		for (Date d : a_image.getDate()) {
			dates.add(d);
		}
		date = new TypedMetadataKeyImpl<Date>("Date", dates);
		extract = null;
	}

	@Override
	public TypedMetadataKey<Date> getDate () {
		TypedMetadataKey<Date> result = date;
		if (result == null) {
			result = new TypedMetadataKeyImpl<Date>("Date", findDate());
		}
		return null;
	}

	private List<Date> findDate () {
		List<Date> result = new ArrayList<Date>();
		if (extract != null) {
			Directory iptc = extract.getDirectory(IptcDirectory.class);
			if (iptc != null && iptc.containsTag(IptcDirectory.TAG_DATE_CREATED)) {
				addDate(result, iptc.getString(IptcDirectory.TAG_DATE_CREATED));
			}
			Directory exif = extract.getDirectory(ExifDirectory.class);
			if (exif != null) {
				if (exif.containsTag(ExifDirectory.TAG_DATETIME)) {
					addDate(result, exif.getString(ExifDirectory.TAG_DATETIME));
				}
				if (exif.containsTag(ExifDirectory.TAG_DATETIME_ORIGINAL)) {
					addDate(result, exif.getString(ExifDirectory.TAG_DATETIME_ORIGINAL));
				}
				if (exif.containsTag(ExifDirectory.TAG_DATETIME_DIGITIZED)) {
					addDate(result, exif.getString(ExifDirectory.TAG_DATETIME_DIGITIZED));
				}
			}
		}
		return result;
	}

	private void addDate (List<Date> dates, String dateToParse) {
		Date metadataDate = null;
		if (dateToParse != null && !dateToParse.isEmpty()) {
			for (int i = 0, n = TAG_DATE_FORMATS.length; metadataDate == null && i < n; i++) {
				try {
					metadataDate = TAG_DATE_FORMATS[i].parse(dateToParse);
				} catch (ParseException px) {
					// ok, try next
				}
			}
			if (metadataDate == null) {
				log.warn("date '" + dateToParse + "' not in any known date format");
			} else {
				dates.add(metadataDate);
			}
		}
	}

	@Override
	public TypedMetadataKey<Coordinates> getGeolocalization () {
		TypedMetadataKey<Coordinates> result = geolocalization;
		if (result == null) {
			geolocalization = result = new TypedMetadataKeyImpl<Coordinates>("Geolocalization");
		}
		return result;
	}

	@Override
	public TypedMetadataKey<Integer> getOrientation () {
		TypedMetadataKey<Integer> result = orientation;
		if (result == null) {
			orientation = result = new TypedMetadataKeyImpl<Integer>("Orientation");
		}
		return result;
	}

}
