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
package io.sf.carte.echosvg.svggen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.net.URL;

import io.sf.carte.echosvg.test.DefaultTestSuite;
import io.sf.carte.echosvg.test.Test;
import io.sf.carte.echosvg.test.TestReport;
import io.sf.carte.echosvg.test.TestReportValidator;

/**
 * Validates the operation of the <code>SVGAccuractyTest</code> class
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGAccuracyTestValidator extends DefaultTestSuite {
    /**
     * Checks that test fails if:
     * + Rendering sequence generates an exception
     * + There is no reference image
     * + Reference SVG differs from the generated SVG
     * Checks that test works if SVG and reference SVG 
     * are identical
     */
    public SVGAccuracyTestValidator(){
        addTest(new NullPainter());
        addTest(new PainterWithException());
        addTest(new NullReferenceURL());
        addTest(new InexistantReferenceURL());
        addTest(new DiffWithReferenceImage());
        addTest(new SameAsReferenceImage());
    }

    static class NullPainter extends TestReportValidator {
        @Override
        public TestReport runImpl() throws Exception {
            Painter painter = null;
            URL refURL = new URL("http",
                                 "dummyHost",
                                 "dummyFile.svg");

            Test t 
                = new SVGAccuracyTest(painter, refURL);
            
            setConfig(t,
                      false,
                      SVGAccuracyTest.ERROR_CANNOT_GENERATE_SVG);

            return super.runImpl();
        }
    }

    static class PainterWithException extends TestReportValidator 
        implements Painter {

        @Override
        public void paint(Graphics2D g){
            g.setComposite(null); // Will cause the exception
            g.fillRect(0, 0, 20, 20);
        }

        @Override
        public TestReport runImpl() throws Exception {
            Painter painter = this;
            URL refURL = new URL("http",
                                 "dummyHost",
                                 "dummyFile.svg");
            Test t = new SVGAccuracyTest(painter, refURL);
            
            setConfig(t,
                      false,
                      SVGAccuracyTest.ERROR_CANNOT_GENERATE_SVG);

            return super.runImpl();
        }
    }

    static class ValidPainterTest extends TestReportValidator 
        implements Painter{
        
        @Override
        public void paint(Graphics2D g){
            g.setPaint(Color.red);
            g.fillRect(0, 0, 40, 40);
        }
    }

    static class NullReferenceURL extends ValidPainterTest {
        @Override
        public TestReport runImpl() throws Exception {
            Test t = new SVGAccuracyTest(this, null);

            setConfig(t,
                      false,
                      SVGAccuracyTest.ERROR_CANNOT_OPEN_REFERENCE_SVG_FILE);

            return super.runImpl();
        }
    }

    static class InexistantReferenceURL extends ValidPainterTest {
        @Override
        public TestReport runImpl() throws Exception {
            Test t = new SVGAccuracyTest(this,
                                         new URL("http",
                                                 "dummyHost",
                                                 "dummyFile.svg"));

            setConfig(t,
                      false,
                      SVGAccuracyTest.ERROR_CANNOT_OPEN_REFERENCE_SVG_FILE);

            return super.runImpl();
        }
    }

    static class DiffWithReferenceImage extends ValidPainterTest {
        @Override
        public TestReport runImpl() throws Exception {
            File tmpFile = File.createTempFile("EmptySVGReference",
                                               null);
            tmpFile.deleteOnExit();

            Test t = new SVGAccuracyTest(this,
                                         tmpFile.toURI().toURL());

            setConfig(t,
                      false,
                      SVGAccuracyTest.ERROR_GENERATED_SVG_INACCURATE);

            return super.runImpl();
        }
    }

    static class SameAsReferenceImage extends ValidPainterTest {
        @Override
        public TestReport runImpl() throws Exception {
            File tmpFile = File.createTempFile("SVGReference",
                                               null);
            tmpFile.deleteOnExit();

            SVGAccuracyTest t = new SVGAccuracyTest(this,
                                                    tmpFile.toURI().toURL());
            
            t.setSaveSVG(tmpFile);

            setConfig(t,
                      false,
                      SVGAccuracyTest.ERROR_GENERATED_SVG_INACCURATE);

            // This first run should fail but it should
            // have created the reference image in tmpFile
            super.runImpl();

            // Second run should work because the reference
            // image should match 
            setConfig(t,
                      true,
                      null);

            return super.runImpl();
        }
    }

}
