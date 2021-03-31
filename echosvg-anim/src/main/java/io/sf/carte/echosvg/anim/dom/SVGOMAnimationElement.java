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
package io.sf.carte.echosvg.anim.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimationElement;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGStringList;

import io.sf.carte.echosvg.anim.timing.TimedElement;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.svg.SVGAnimationContext;
import io.sf.carte.echosvg.dom.svg.SVGTestsSupport;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides an implementation of the {@link SVGAnimationElement}
 * interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGOMAnimationElement extends SVGOMElement implements SVGAnimationElement {

	private static final long serialVersionUID = 1L;
	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGOMElement.xmlTraitInformation);
		t.put(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_BOOLEAN));
		xmlTraitInformation = t;
	}

	/**
	 * The 'externalResourcesRequired' attribute value.
	 */
	protected SVGOMAnimatedBoolean externalResourcesRequired;

	/**
	 * Creates a new SVGOMAnimationElement.
	 */
	protected SVGOMAnimationElement() {
	}

	/**
	 * Creates a new SVGOMAnimationElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	protected SVGOMAnimationElement(String prefix, AbstractDocument owner) {
		super(prefix, owner);
		initializeLiveAttributes();
	}

	/**
	 * Initializes all live attributes for this element.
	 */
	@Override
	protected void initializeAllLiveAttributes() {
		super.initializeAllLiveAttributes();
		initializeLiveAttributes();
	}

	/**
	 * Initializes the live attribute values of this element.
	 */
	private void initializeLiveAttributes() {
		externalResourcesRequired = createLiveAnimatedBoolean(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, false);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimationElement#getTargetElement()}.
	 */
	@Override
	public SVGElement getTargetElement() {
		return ((SVGAnimationContext) getSVGContext()).getTargetElement();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimationElement#getStartTime()}.
	 */
	@Override
	public float getStartTime() {
		return ((SVGAnimationContext) getSVGContext()).getStartTime();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimationElement#getCurrentTime()}.
	 */
	@Override
	public float getCurrentTime() {
		return ((SVGAnimationContext) getSVGContext()).getCurrentTime();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimationElement#getSimpleDuration()}.
	 */
	@Override
	public float getSimpleDuration() throws DOMException {
		float dur = ((SVGAnimationContext) getSVGContext()).getSimpleDuration();
		if (dur == TimedElement.INDEFINITE) {
			throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "animation.dur.indefinite", null);
		}
		return dur;
	}

	/**
	 * Returns the time that the document would seek to if this animation element
	 * were hyperlinked to, or <code>NaN</code> if there is no such begin time.
	 */
	public float getHyperlinkBeginTime() {
		return ((SVGAnimationContext) getSVGContext()).getHyperlinkBeginTime();
	}

	// ElementTimeControl ////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.smil.ElementTimeControl#beginElement()}.
	 */
	@Override
	public boolean beginElement() throws DOMException {
		return ((SVGAnimationContext) getSVGContext()).beginElement();
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.smil.ElementTimeControl#beginElementAt(float)}.
	 */
	@Override
	public boolean beginElementAt(float offset) throws DOMException {
		return ((SVGAnimationContext) getSVGContext()).beginElementAt(offset);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.smil.ElementTimeControl#endElement()}.
	 */
	@Override
	public boolean endElement() throws DOMException {
		return ((SVGAnimationContext) getSVGContext()).endElement();
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.smil.ElementTimeControl#endElementAt(float)}.
	 */
	@Override
	public boolean endElementAt(float offset) throws DOMException {
		return ((SVGAnimationContext) getSVGContext()).endElementAt(offset);
	}

	// SVGExternalResourcesRequired support /////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGExternalResourcesRequired#getExternalResourcesRequired()}.
	 */
	@Override
	public SVGAnimatedBoolean getExternalResourcesRequired() {
		return externalResourcesRequired;
	}

	// SVGTests support ///////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGTests#getRequiredFeatures()}.
	 */
	@Override
	public SVGStringList getRequiredFeatures() {
		return SVGTestsSupport.getRequiredFeatures(this);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGTests#getRequiredExtensions()}.
	 */
	@Override
	public SVGStringList getRequiredExtensions() {
		return SVGTestsSupport.getRequiredExtensions(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGTests#getSystemLanguage()}.
	 */
	@Override
	public SVGStringList getSystemLanguage() {
		return SVGTestsSupport.getSystemLanguage(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGTests#hasExtension(String)}.
	 */
	@Override
	public boolean hasExtension(String extension) {
		return SVGTestsSupport.hasExtension(this, extension);
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
