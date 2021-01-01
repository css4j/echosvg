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
package io.sf.carte.echosvg.bridge;

/**
 * This is an adapter for the UpdateManagerListener interface.
 * It's methods do nothing.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class UpdateManagerAdapter implements UpdateManagerListener {

    /**
     * Called when the manager was started.
     */
    @Override
    public void managerStarted(UpdateManagerEvent e) { }

    /**
     * Called when the manager was suspended.
     */
    @Override
    public void managerSuspended(UpdateManagerEvent e) { }
    
    /**
     * Called when the manager was resumed.
     */
    @Override
    public void managerResumed(UpdateManagerEvent e) { }

    /**
     * Called when the manager was stopped.
     */
    @Override
    public void managerStopped(UpdateManagerEvent e) { }

    /**
     * Called when an update started.
     */
    @Override
    public void updateStarted(UpdateManagerEvent e) { }

    /**
     * Called when an update was completed.
     */
    @Override
    public void updateCompleted(UpdateManagerEvent e) { }

    /**
     * Called when an update failed.
     */
    @Override
    public void updateFailed(UpdateManagerEvent e) { }

}
