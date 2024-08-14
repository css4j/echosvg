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
package io.sf.carte.echosvg.ext.awt.image.codec.png;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;

import io.sf.carte.echosvg.ext.awt.image.codec.util.ImageDecodeParam;
import io.sf.carte.echosvg.ext.awt.image.codec.util.ImageDecoderImpl;
import io.sf.carte.echosvg.ext.awt.image.codec.util.PropertyUtil;

/**
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PNGImageDecoder extends ImageDecoderImpl {

	public PNGImageDecoder(InputStream input, PNGDecodeParam param) {
		super(input, param);
	}

	@Override
	public void setParam(ImageDecodeParam param) {
		if (!(param instanceof PNGDecodeParam)) {
			throw new IllegalArgumentException("param must be a PNGDecodeParam.");
		}
		super.setParam(param);
	}

	@Override
	public PNGDecodeParam getParam() {
		return (PNGDecodeParam) super.getParam();
	}

	@Override
	public RenderedImage decodeAsRenderedImage(int page) throws IOException {
		if (page != 0) {
			throw new IOException(PropertyUtil.formatMessage("PNGImageDecoder.unknown.page",
					new Object[] { page }));
		}
		return new PNGImage(input, (PNGDecodeParam) param);
	}

}
