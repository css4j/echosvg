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
package io.sf.carte.echosvg.anim.dom;

import io.sf.carte.echosvg.anim.values.AnimatableValue;

/**
 * An interface for animatable elements to expose their underlying values
 * to the compositing functions in
 * {@link io.sf.carte.echosvg.anim.AbstractAnimation}.
 * 
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id$
 */
public interface AnimatableElement {

    /**
     * Returns the underlying value of the animated attribute.  Used for
     * composition of additive animations.
     */
    AnimatableValue getUnderlyingValue();
}
