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
package io.sf.carte.echosvg.transcoder;

import java.util.HashMap;
import java.util.Map;

/**
 * The <code>TranscodingHints</code> class defines a way to pass transcoding
 * parameters or options to any transcoders.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TranscodingHints extends HashMap<TranscodingHints.Key, Object> {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new empty <code>TranscodingHints</code>.
	 */
	public TranscodingHints() {
		this(null);
	}

	/**
	 * Constructs a new <code>TranscodingHints</code> with keys and values
	 * initialized from the specified Map object (which may be null).
	 *
	 * @param init a map of key/value pairs to initialize the hints or null if the
	 *             object should be empty
	 */
	public TranscodingHints(Map<TranscodingHints.Key, ?> init) {
		super(7);
		if (init != null) {
			putAll(init);
		}
	}

	/**
	 * Returns <code>true</code> if this <code>TranscodingHints</code> contains a
	 * mapping for the specified key, false otherwise.
	 *
	 * @param key key whose present in this <code>TranscodingHints</code> is to be
	 *            tested.
	 * @exception ClassCastException key is not of type
	 *                               <code>TranscodingHints.Key</code>
	 */
	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(key);
	}

	/**
	 * Returns the value to which the specified key is mapped.
	 *
	 * @param key a trancoding hint key
	 * @exception ClassCastException key is not of type
	 *                               <code>TranscodingHints.Key</code>
	 */
	@Override
	public Object get(Object key) {
		return super.get(key);
	}

	/**
	 * Maps the specified <code>key</code> to the specified <code>value</code> in
	 * this <code>TranscodingHints</code> object.
	 *
	 * @param key   the trancoding hint key.
	 * @param value the trancoding hint value.
	 * @exception IllegalArgumentException value is not appropriate for the
	 *                                     specified key.
	 * @exception ClassCastException       key is not of type
	 *                                     <code>TranscodingHints.Key</code>
	 */
	@Override
	public Object put(TranscodingHints.Key key, Object value) {
		if (!key.isCompatibleValue(value)) {
			throw new IllegalArgumentException(value + " incompatible with " + key);
		}
		return super.put(key, value);
	}

	/**
	 * Removes the key and its corresponding value from this
	 * <code>TranscodingHints</code> object.
	 *
	 * @param key the trancoding hints key that needs to be removed
	 * @exception ClassCastException key is not of type
	 *                               <code>TranscodingHints.Key</code>
	 */
	@Override
	public Object remove(Object key) {
		return super.remove(key);
	}

	/**
	 * Copies all of the keys and corresponding values from the specified
	 * <code>TranscodingHints</code> object to this <code>TranscodingHints</code>
	 * object.
	 */
	public void putAll(TranscodingHints hints) {
		super.putAll(hints);
	}

	/**
	 * Copies all of the mappings from the specified <code>Map</code> to this
	 * <code>TranscodingHints</code>.
	 *
	 * @param m mappings to be stored in this <code>TranscodingHints</code>.
	 * @exception ClassCastException key is not of type
	 *                               <code>TranscodingHints.Key</code>
	 */
	@Override
	public void putAll(Map<? extends TranscodingHints.Key, ? extends Object> m) {
		if (m instanceof TranscodingHints) {
			putAll(((TranscodingHints) m));
		} else {
			for (Map.Entry<? extends TranscodingHints.Key, ? extends Object> entry : m.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Defines the base type of all keys used to control various aspects of the
	 * transcoding operations.
	 */
	public abstract static class Key {

		/**
		 * Constructs a key.
		 */
		protected Key() {
		}

		/**
		 * Returns true if the specified object is a valid value for this key, false
		 * otherwise.
		 */
		public abstract boolean isCompatibleValue(Object val);
	}
}
