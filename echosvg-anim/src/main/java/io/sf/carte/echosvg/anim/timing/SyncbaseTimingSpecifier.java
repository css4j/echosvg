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
package io.sf.carte.echosvg.anim.timing;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * A class to handle syncbase SMIL timing specifiers.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SyncbaseTimingSpecifier extends OffsetTimingSpecifier {

    /**
     * The ID of the syncbase element.
     */
    protected String syncbaseID;

    /**
     * The syncbase element.
     */
    protected TimedElement syncbaseElement;

    /**
     * Whether this specifier specifies a sync to the begin or the end
     * of the syncbase element.
     */
    protected boolean syncBegin;

    /**
     * Map of {@link Interval}s to <!--a {@link WeakReference} to -->an
     * {@link InstanceTime}.
     */
    protected HashMap<Interval, InstanceTime> instances = new HashMap<>();

    /**
     * Creates a new SyncbaseTimingSpecifier object.
     */
    public SyncbaseTimingSpecifier(TimedElement owner, boolean isBegin,
                                   float offset, String syncbaseID,
                                   boolean syncBegin) {
        super(owner, isBegin, offset);
        // Trace.enter(this, null, new Object[] { owner, new Boolean(isBegin), Float.valueOf(offset), syncbaseID, new Boolean(syncBegin) } ); try {
        this.syncbaseID = syncbaseID;
        this.syncBegin = syncBegin;
        this.syncbaseElement = owner.getTimedElementById(syncbaseID);
        syncbaseElement.addDependent(this, syncBegin);
        // } finally { Trace.exit(); }
    }

    /**
     * Returns a string representation of this timing specifier.
     */
    @Override
    public String toString() {
        return syncbaseID + "." + (syncBegin ? "begin" : "end")
            + (offset != 0 ? super.toString() : "");
    }

    /**
     * Initializes this timing specifier by adding the initial instance time
     * to the owner's instance time list or setting up any event listeners.
     */
    @Override
    public void initialize() {
    }

    /**
     * Returns whether this timing specifier is event-like (i.e., if it is
     * an eventbase, accesskey or a repeat timing specifier).
     */
    @Override
    public boolean isEventCondition() {
        return false;
    }

    /**
     * Called by the timebase element when it creates a new Interval.
     */
    @Override
    float newInterval(Interval interval) {
        // Trace.enter(this, "newInterval", new Object[] { interval } ); try {
        if (owner.hasPropagated) {
            return Float.POSITIVE_INFINITY;
        }
        InstanceTime instance =
            new InstanceTime(this, (syncBegin ? interval.getBegin()
                                              : interval.getEnd()) + offset,
                             true);
        instances.put(interval, instance);
        interval.addDependent(instance, syncBegin);
        return owner.addInstanceTime(instance, isBegin);
        // } finally { Trace.exit(); }
    }

    /**
     * Called by the timebase element when it deletes an Interval.
     */
    @Override
    float removeInterval(Interval interval) {
        // Trace.enter(this, "removeInterval", new Object[] { interval } ); try {
        if (owner.hasPropagated) {
            return Float.POSITIVE_INFINITY;
        }
        InstanceTime instance = instances.get(interval);
        interval.removeDependent(instance, syncBegin);
        return owner.removeInstanceTime(instance, isBegin);
        // } finally { Trace.exit(); }
    }

    /**
     * Called by an {@link InstanceTime} created by this TimingSpecifier
     * to indicate that its value has changed.
     */
    @Override
    float handleTimebaseUpdate(InstanceTime instanceTime, float newTime) {
        // Trace.enter(this, "handleTimebaseUpdate", new Object[] { instanceTime, Float.valueOf(newTime) } ); try {
        if (owner.hasPropagated) {
            return Float.POSITIVE_INFINITY;
        }
        return owner.instanceTimeChanged(instanceTime, isBegin);
        // } finally { Trace.exit(); }
    }
}
