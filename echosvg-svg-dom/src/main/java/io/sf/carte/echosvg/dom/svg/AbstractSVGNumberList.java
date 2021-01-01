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
package io.sf.carte.echosvg.dom.svg;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGNumber;
import org.w3c.dom.svg.SVGNumberList;

import io.sf.carte.echosvg.parser.NumberListHandler;
import io.sf.carte.echosvg.parser.NumberListParser;
import io.sf.carte.echosvg.parser.ParseException;

/**
 * This class is the implementation of {@link SVGNumberList}.
 *
 * @author <a href="mailto:tonny@kiyut.com">Tonny Kohar</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractSVGNumberList
        extends AbstractSVGList
        implements SVGNumberList {

    /**
     * Separator for a length list.
     */
    public static final String SVG_NUMBER_LIST_SEPARATOR
        = " ";

    /**
     * Return the separator between values in the list.
     */
    @Override
    protected String getItemSeparator() {
        return SVG_NUMBER_LIST_SEPARATOR;
    }

    /**
     * Create an SVGException when the {@link #checkItemType(Object)} fails.
     */
    protected abstract SVGException createSVGException(short type,
                                                       String key,
                                                       Object[] args);

    /**
     * Returns the element associated with this SVGNumberList.
     */
    protected abstract Element getElement();

    /**
     * Creates a new SVGNumberList.
     */
    protected AbstractSVGNumberList() {
    }

    /**
     * <b>DOM</b>: Implements {@link SVGNumberList#initialize(SVGNumber)}.
     */
    @Override
    public SVGNumber initialize(SVGNumber newItem)
        throws DOMException, SVGException {

        return (SVGNumber)initializeImpl(newItem);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGNumberList#getItem(int)}.
     */
    @Override
    public SVGNumber getItem(int index) throws DOMException {

        return (SVGNumber)getItemImpl(index);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * SVGNumberList#insertItemBefore(SVGNumber,int)}.
     */
    @Override
    public SVGNumber insertItemBefore(SVGNumber newItem, int index)
        throws DOMException, SVGException {

        return (SVGNumber)insertItemBeforeImpl(newItem,index);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGNumberList#replaceItem(SVGNumber,int)}.
     */
    @Override
    public SVGNumber replaceItem(SVGNumber newItem, int index)
        throws DOMException, SVGException {

        return (SVGNumber)replaceItemImpl(newItem,index);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGNumberList#removeItem(int)}.
     */
    @Override
    public SVGNumber removeItem(int index) throws DOMException {
        return (SVGNumber)removeItemImpl(index);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGNumberList#appendItem(SVGNumber)}.
     */
    @Override
    public SVGNumber appendItem(SVGNumber newItem)
        throws DOMException, SVGException {

        return (SVGNumber)appendItemImpl(newItem);
    }

    /**
     * Creates a new {@link SVGNumberItem} from the given {@link SVGNumber}.
     */
    @Override
    protected SVGItem createSVGItem(Object newItem) {
        SVGNumber l = (SVGNumber)newItem;
        return new SVGNumberItem(l.getValue());
    }

    /**
     * Parse the attribute associated with this SVGNumberList.
     * @param value attribute value
     * @param handler list handler
     */
    @Override
    protected void doParse(String value, ListHandler handler)
        throws ParseException{

        NumberListParser NumberListParser = new NumberListParser();
        NumberListBuilder builder = new NumberListBuilder(handler);

        NumberListParser.setNumberListHandler(builder);
        NumberListParser.parse(value);
    }

    /**
     * Asserts that the given item object is an {@link SVGNumber}.
     */
    @Override
    protected void checkItemType(Object newItem) throws SVGException {
        if (!(newItem instanceof SVGNumber)) {
            // XXX Fix error code.
            createSVGException(SVGException.SVG_WRONG_TYPE_ERR,
                               "expected SVGNumber",
                               null);
        }
    }

    /**
     * Helper class to interface the {@link NumberListParser} and the
     * {@link NumberListHandler}.
     */
    protected static class NumberListBuilder implements NumberListHandler {

        /**
         * The ListHandler to notify of parsed numbers.
         */
        protected ListHandler listHandler;

        /**
         * The number just parsed.
         */
        protected float currentValue;

        /**
         * Creates a new NumberListBuilder.
         */
        public NumberListBuilder(ListHandler listHandler) {
            this.listHandler = listHandler;
        }

        /**
         * Implements {@link NumberListHandler#startNumberList()}.
         */
        @Override
        public void startNumberList() throws ParseException{
            listHandler.startList();
        }

        /**
         * Implements {@link NumberListHandler#startNumber()}.
         */
        @Override
        public void startNumber() throws ParseException {
            currentValue = 0.0f;
        }

        /**
         * Implements {@link NumberListHandler#numberValue(float)}.
         */
        @Override
        public void numberValue(float v) throws ParseException {
            currentValue = v;
        }

        /**
         * Implements {@link NumberListHandler#endNumber()}.
         */
        @Override
        public void endNumber() throws ParseException {
            listHandler.item(new SVGNumberItem(currentValue));
        }

        /**
         * Implements {@link NumberListHandler#endNumberList()}.
         */
        @Override
        public void endNumberList() throws ParseException {
            listHandler.endList();
        }
    }
}
