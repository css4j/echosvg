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
package io.sf.carte.echosvg.anim.values;

import io.sf.carte.echosvg.anim.dom.AnimationTarget;

/**
 * An SVG point list value in the animation system.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AnimatablePointListValue extends AnimatableNumberListValue {

    /**
     * Creates a new, uninitialized AnimatablePointListValue.
     */
    protected AnimatablePointListValue(AnimationTarget target) {
        super(target);
    }

    /**
     * Creates a new AnimatablePointListValue.
     */
    public AnimatablePointListValue(AnimationTarget target, float[] numbers) {
        super(target, numbers);
    }

    /**
     * Performs interpolation to the given value.
     */
    @Override
    public AnimatableValue interpolate(AnimatableValue result,
                                       AnimatableValue to,
                                       float interpolation,
                                       AnimatableValue accumulation,
                                       int multiplier) {
        if (result == null) {
            result = new AnimatablePointListValue(target);
        }
        return super.interpolate
            (result, to, interpolation, accumulation, multiplier);
    }

    /**
     * Returns whether two values of this type can have their distance
     * computed, as needed by paced animation.
     */
    @Override
    public boolean canPace() {
        return false;
    }

    /**
     * Returns the absolute distance between this value and the specified other
     * value.
     */
    @Override
    public float distanceTo(AnimatableValue other) {
        return 0f;
    }

    /**
     * Returns a zero value of this AnimatableValue's type.
     */
    @Override
    public AnimatableValue getZeroValue() {
        float[] ns = new float[numbers.length];
        return new AnimatablePointListValue(target, ns);
    }
}
