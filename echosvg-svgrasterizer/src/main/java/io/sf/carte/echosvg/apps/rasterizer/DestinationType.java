/*

   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.apps.rasterizer;

import io.sf.carte.echosvg.transcoder.Transcoder;
import io.sf.carte.echosvg.transcoder.image.JPEGTranscoder;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoder;
import io.sf.carte.echosvg.transcoder.image.TIFFTranscoder;

/**
 * Describes the type of destination for an <code>SVGConverter</code> operation.
 *
 * @author Henri Ruini
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public final class DestinationType {

	public static final String PNG_STR = "image/png";
	public static final String JPEG_STR = "image/jpeg";
	public static final String TIFF_STR = "image/tiff";

	public static final int PNG_CODE = 0;
	public static final int JPEG_CODE = 1;
	public static final int TIFF_CODE = 2;

	public static final String PNG_EXTENSION = ".png";
	public static final String JPEG_EXTENSION = ".jpg";
	public static final String TIFF_EXTENSION = ".tif";

	public static final DestinationType PNG = new DestinationType(PNG_STR, PNG_CODE, PNG_EXTENSION);
	public static final DestinationType JPEG = new DestinationType(JPEG_STR, JPEG_CODE, JPEG_EXTENSION);
	public static final DestinationType TIFF = new DestinationType(TIFF_STR, TIFF_CODE, TIFF_EXTENSION);

	private String type;
	private int code;
	private String extension;

	private DestinationType(String type, int code, String extension) {
		this.type = type;
		this.code = code;
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}

	@Override
	public String toString() {
		return type;
	}

	public int toInt() {
		return code;
	}

	/**
	 * Returns a transcoder object of the result image type.
	 *
	 * @return Transcoder object or <code>null</code> if there isn't a proper
	 *         transcoder.
	 */
	Transcoder getTranscoder() {
		switch (code) {
		case PNG_CODE:
			return new PNGTranscoder();
		case JPEG_CODE:
			return new JPEGTranscoder();
		case TIFF_CODE:
			return new TIFFTranscoder();
		default:
			return null;
		}

	}

	/**
	 * Defines valid image types.
	 *
	 * @return Array of valid values as strings.
	 */
	public DestinationType[] getValues() {
		return new DestinationType[] { PNG, JPEG, TIFF };
	}

	public Object readResolve() {
		switch (code) {
		case PNG_CODE:
			return PNG;
		case JPEG_CODE:
			return JPEG;
		case TIFF_CODE:
			return TIFF;
		default:
			throw new RuntimeException("unknown code:" + code);
		}
	}

}
