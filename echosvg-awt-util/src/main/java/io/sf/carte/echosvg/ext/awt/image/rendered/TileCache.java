/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.ext.awt.image.rendered;

import java.awt.image.RenderedImage;

/**
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TileCache {
	private static LRUCache cache = new LRUCache(50);

	public static void setSize(int sz) {
		cache.setSize(sz);
	}

	public static TileStore getTileGrid(int minTileX, int minTileY, int xSz, int ySz, TileGenerator src) {
		return new TileGrid(minTileX, minTileY, xSz, ySz, src, cache);
	}

	public static TileStore getTileGrid(RenderedImage img, TileGenerator src) {
		return new TileGrid(img.getMinTileX(), img.getMinTileY(), img.getNumXTiles(), img.getNumYTiles(), src, cache);
	}

	public static TileStore getTileMap(TileGenerator src) {
		return new TileMap(src, cache);
	}
}
