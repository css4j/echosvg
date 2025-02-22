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
package io.sf.carte.echosvg.svggen.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ICC_ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;

import io.sf.carte.echosvg.ext.awt.color.StandardColorSpaces;

/**
 * This test validates drawImage conversions with profiled colors.
 *
 * <p>
 * Based on {@code DrawImage} by Vincent Hardy.
 * </p>
 * 
 * @author See Git history.
 * @version $Id$
 */
public class DrawImageICC implements Painter {

	@Override
	public void paint(Graphics2D g) {
		ICC_ColorSpace cs = StandardColorSpaces.getDisplayP3();

		// Create the image
		int[] bits = { 16, 16, 16, 16 };
		ComponentColorModel cm = new ComponentColorModel(cs, bits, true, false,
				Transparency.TRANSLUCENT, DataBuffer.TYPE_USHORT);
		WritableRaster raster = cm.createCompatibleWritableRaster(100, 75);
		BufferedImage image = new BufferedImage(cm, raster, false, null);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Graphics2D ig = image.createGraphics();
		ig.scale(.5, .5);

		float[] comps = { 0.5f, 0.07f, 0.02f }; // rgb(54.8% -3.58% -3.84%)
		Color color1 = new Color(cs, comps, 1f);

		ig.setPaint(color1);
		ig.fillRect(0, 0, 100, 50);

		comps = new float[]{ 0.52f, 0.38f, 0.9f }; // rgb(54.6% 37.3% -5.68%)
		Color colorOrangish = new Color(cs, comps, 1f);

		ig.setPaint(colorOrangish);
		ig.fillRect(100, 0, 100, 50);

		comps = new float[]{ 1f, 1f, 0.3f }; // rgb(100% 100% -14.2%)
		Color colorYellowish = new Color(cs, comps, 1f);

		ig.setPaint(colorYellowish);
		ig.fillRect(0, 50, 100, 50);

		comps = new float[]{ 0.91f, 0.18f, 0.13f }; // rgb(100% -7.22% 0.608%)
		Color colorReddish = new Color(cs, comps, 1f);

		ig.setPaint(colorReddish);
		ig.fillRect(100, 50, 100, 50);

		comps = new float[]{ 0.98f, 0.525f, 0.512f }; // rgb(105% 49.4% 49.6%)
		Color colorPinkish = new Color(cs, comps, 1f);

		ig.setPaint(colorPinkish);
		ig.fillRect(0, 100, 100, 50);

		comps = new float[]{ 0f, 0.06f, 0f }; // rgb(-1.42% 6.2% -0.497%)
		Color colorBlackish = new Color(cs, comps, 1f);

		ig.setPaint(colorBlackish);
		ig.draw(new Rectangle2D.Double(0.5, 0.5, 199, 149));
		ig.dispose();

		/*
		 * Now draw the image
		 */

		comps = new float[]{ 0.05f, 0.49f, 0.97f }; // rgb(-22.5% 49.9% 100%)
		Color colorBluish = new Color(cs, comps, 1f);

		// drawImage(img,x,y,bgcolor,observer);
		g.drawImage(image, 5, 10, colorBluish, null);
		g.translate(150, 0);

		// drawImage(img,x,y,w,h,observer)
		g.drawImage(image, 5, 10, 50, 40, null);
		g.translate(-150, 80);

		// drawImage(img,dx1,dy1,dx2,dy2,sx1,sy1,sx2,sy2,observer);
		g.drawImage(image, 5, 10, 45, 40, 50, 0, 100, 25, null);
		g.translate(150, 0);

		// drawImage(img,dx1,dy1,dx2,dy2,sx1,sy1,sx2,sy2,bgcolor,observer);
		g.drawImage(image, 5, 10, 45, 40, 50, 50, 100, 75, colorBluish, null);
		g.translate(-150, 80);

		// drawImage(img,xform,obs)
		AffineTransform at = new AffineTransform();
		at.scale(.5, .3);
		at.translate(5, 10);
		g.drawImage(image, at, null);

		g.translate(150, 0);

		// drawImage(img,op,x,y);
		RescaleOp op = new RescaleOp(.5f, 0f, null);
		g.drawImage(image, op, 5, 10);

		g.translate(-150, 0);

		g.translate(0, 80);

		// drawImage(x,y,w,y,bgcolor,observer)
		g.drawImage(image, 5, 10, 50, 40, colorBluish, null);

	}

}
