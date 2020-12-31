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
package io.sf.carte.echosvg.util;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Generic class to dispatch events in a highly reliable way.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @version $Id$
 */
public class EventDispatcher {

    public interface Dispatcher {
        void dispatch(Object listener,
                             Object event);
    }


    public static void fireEvent(final Dispatcher dispatcher,
                                 final List listeners,
                                 final Object evt,
                                 final boolean useEventQueue) {
        if (useEventQueue && !EventQueue.isDispatchThread()) {
            Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        fireEvent(dispatcher, listeners, evt, useEventQueue);
                    }
                };
            try {
                EventQueue.invokeAndWait(r);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                // Assume they will get delivered????
                // be nice to wait on List but how???
            } catch (ThreadDeath td) {
                throw td;
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return;
        }

        Object [] ll = null;
        Throwable err = null;
        int retryCount = 10;
        while (--retryCount != 0) {
            // If the thread has been interrupted this can 'mess up'
            // the class loader and cause this otherwise safe code to
            // throw errors.
            try {
                synchronized (listeners) {
                    if (listeners.size() == 0)
                        return;
                    ll = listeners.toArray();
                    break;
                }
            } catch(Throwable t) {
                err = t;
            }
        }
        if (ll == null) {
            if (err != null)
                err.printStackTrace();
            return;
        }
        dispatchEvent(dispatcher, ll, evt);
    }

    protected static void dispatchEvent(final Dispatcher dispatcher,
                                        final Object [] ll,
                                        final Object evt) {
        ThreadDeath td = null;
        try {
            for (int i = 0; i < ll.length; i++) {
                try {
                    Object l;
                    synchronized (ll) {
                        l = ll[i];
                        if (l == null) continue;
                        ll[i] = null;
                    }
                    dispatcher.dispatch(l, evt);
                } catch (ThreadDeath t) {
                    // Keep delivering messages but remember to throw later.
                    td = t;
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        } catch (ThreadDeath t) {
            // Remember to throw later.
            td = t;
        } catch (Throwable t) {
            if (ll[ll.length-1] != null)
                dispatchEvent(dispatcher, ll, evt);
            t.printStackTrace();
        }
        if (td != null) throw td;
    }
}
