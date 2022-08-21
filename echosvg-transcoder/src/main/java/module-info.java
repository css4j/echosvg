/*
 * Copyright (c) 2020-2022 Carlos Amengual
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * SVG transcoder.
 * 
 * @provides io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter
 * @provides io.sf.carte.echosvg.ext.awt.image.spi.RegistryEntry
 */
module io.sf.carte.echosvg.transcoder {
	exports io.sf.carte.echosvg.transcoder;
	exports io.sf.carte.echosvg.transcoder.image;
	exports io.sf.carte.echosvg.transcoder.image.resources;
	exports io.sf.carte.echosvg.transcoder.keys;
	exports io.sf.carte.echosvg.transcoder.print;
	exports io.sf.carte.echosvg.transcoder.svg2svg;
	exports io.sf.carte.echosvg.transcoder.wmf;
	exports io.sf.carte.echosvg.transcoder.wmf.tosvg;

	exports io.sf.carte.echosvg.ext.awt.image.codec.imageio;
	exports io.sf.carte.echosvg.ext.awt.image.codec.png;
	exports io.sf.carte.echosvg.ext.awt.image.codec.util;

	requires transitive io.sf.carte.echosvg.bridge;
	requires transitive io.sf.carte.echosvg.svggen;
	requires io.sf.carte.echosvg.i18n;
	requires io.sf.carte.echosvg.xml;

	provides io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter
			with io.sf.carte.echosvg.ext.awt.image.codec.imageio.ImageIOPNGImageWriter,
			io.sf.carte.echosvg.ext.awt.image.codec.imageio.ImageIOTIFFImageWriter,
			io.sf.carte.echosvg.ext.awt.image.codec.imageio.ImageIOJPEGImageWriter;

	provides io.sf.carte.echosvg.ext.awt.image.spi.RegistryEntry
			with io.sf.carte.echosvg.ext.awt.image.codec.png.PNGRegistryEntry,
			io.sf.carte.echosvg.ext.awt.image.codec.imageio.ImageIOJPEGRegistryEntry,
			io.sf.carte.echosvg.ext.awt.image.codec.imageio.ImageIOPNGRegistryEntry,
			io.sf.carte.echosvg.ext.awt.image.codec.imageio.ImageIOTIFFRegistryEntry;

}
