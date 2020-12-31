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
package io.sf.carte.echosvg.test;

/**
 * Classes in the test package and subpackages should throw 
 * <code>TestException</code> to reflect internal failures in their
 * operation.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @version $Id$
 */
public class TestException extends Exception {
    /**
     * Error code
     */
    protected String errorCode;

    /**
     * Parameters for the error message
     */
    protected Object[] errorParams;

    /**
     * Exception, if any, that caused the error
     */
    protected Exception sourceError;

    public TestException(String errorCode,
                         Object[] errorParams,
                         Exception e){
        this.errorCode = errorCode;
        this.errorParams = errorParams;
        this.sourceError = e;
    }

    public String getErrorCode(){
        return errorCode;
    }

    public Object[] getErrorParams(){
        return errorParams;
    }

    public Exception getSourceError(){
        return sourceError;
    }

    @Override
    public String getMessage(){
        return Messages.formatMessage(errorCode,
                                      errorParams);
    }
}
