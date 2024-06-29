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
package io.sf.carte.echosvg.extension.svg;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.BridgeExtension;

/**
 * This is a Service interface for classes that want to extend the functionality
 * of the Bridge, to support new tags in the rendering tree.
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class EchoSVGBridgeExtension implements BridgeExtension {

	/**
	 * Return the priority of this Extension. Extensions are registered from lowest
	 * to highest priority. So if for some reason you need to come before/after
	 * another existing extension make sure your priority is lower/higher than
	 * theirs.
	 */
	@Override
	public float getPriority() {
		return 1.0f;
	}

	/**
	 * This should return the list of extensions implemented by this
	 * BridgeExtension.
	 * 
	 * @return An iterator containing strings one for each implemented extension.
	 */
	@Override
	public Iterator<String> getImplementedExtensions() {
		String[] extensions = { "http://xml.apache.org/batik/ext/poly/1.0", "http://xml.apache.org/batik/ext/star/1.0",
				"http://xml.apache.org/batik/ext/histogramNormalization/1.0",
				"http://xml.apache.org/batik/ext/colorSwitch/1.0", "http://xml.apache.org/batik/ext/flowText/1.0", };
		List<String> v = Arrays.asList(extensions);
		return Collections.unmodifiableList(v).iterator();
	}

	/**
	 * This should return the individual or company name responsible for the this
	 * implementation of the extension.
	 */
	@Override
	public String getAuthor() {
		return "Thomas DeWeese";
	}

	/**
	 * This should contain a contact address (usually an e-mail address).
	 */
	@Override
	public String getContactAddress() {
		return "deweese@apache.org";
	}

	/**
	 * This should return a URL where information can be obtained on this extension.
	 */
	@Override
	public String getURL() {
		return "http://xml.apache.org/batik";
	}

	/**
	 * Human readable description of the extension. Perhaps that should be a
	 * resource for internationalization? (although I suppose it could be done
	 * internally)
	 */
	@Override
	public String getDescription() {
		return "Example extension to standard SVG shape tags";
	}

	/**
	 * This method should update the BridgeContext with support for the tags in this
	 * extension. In some rare cases it may be necessary to replace existing tag
	 * handlers, although this is discouraged.
	 *
	 * @param ctx The BridgeContext instance to be updated
	 */
	@Override
	public void registerTags(BridgeContext ctx) {
		ctx.putBridge(new EchoSVGRegularPolygonElementBridge());
		ctx.putBridge(new EchoSVGStarElementBridge());
		ctx.putBridge(new EchoSVGHistogramNormalizationElementBridge());
		ctx.putBridge(new EchoSVGFlowTextElementBridge());
		ctx.putBridge(new ColorSwitchBridge());
	}

	/**
	 * Whether the presence of the specified element should cause the document to be
	 * dynamic. If this element isn't handled by this BridgeExtension, just return
	 * false.
	 *
	 * @param e The element to check.
	 */
	@Override
	public boolean isDynamicElement(Element e) {
		return false;
	}

}
