//----------------------------------------------------------------------------//
//                                                                            //
//                   L i n e a r E v a l u a t o r T e s t                    //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//
// Copyright © Hervé Bitteur and others 2000-2017. All rights reserved.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//----------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.math;

import org.audiveris.omr.math.LinearEvaluator;
import org.audiveris.omr.math.LinearEvaluator.Printer;
import org.audiveris.omr.math.LinearEvaluator.Sample;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Class <code>LinearEvaluatorTest</code> gathers unitary tests on
 * LinearEvaluator class
 *
 * @author Hervé Bitteur
 * @version $Id$
 */
public class LinearEvaluatorTest
{
    //~ Static fields/initializers ---------------------------------------------

    private static final String[]     inNames = new String[] { "first", "second" };
    private static final String       dirName = "data/temp";
    private static final String       fileName = "linear.xml";
    private static final List<Sample> samples = Arrays.asList(
        new Sample("A", new double[] { 10, 20 }),
        new Sample("A", new double[] { 11, 23 }),
        new Sample("A", new double[] { 9, 20 }),
        new Sample("B", new double[] { 5, 40 }),
        new Sample("B", new double[] { 7, 50 }),
        new Sample("C", new double[] { 100, 200 }),
        new Sample("C", new double[] { 90, 205 }),
        new Sample("C", new double[] { 95, 220 }),
        new Sample("C", new double[] { 98, 210 }),
        new Sample("D", new double[] { 30, 60 }),
        new Sample("E", new double[] { 80, 20 }),
        new Sample("E", new double[] { 80, 25 }));

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new LinearEvaluatorTest object.
     */
    public LinearEvaluatorTest ()
    {
    }

    //~ Methods ----------------------------------------------------------------

    @BeforeClass
    public static void setUpClass ()
        throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass ()
        throws Exception
    {
    }

    /**
     * Test of categoryDistance method, of class LinearEvaluator.
     */
    @Test
    public void testCategoryDistance ()
    {
        System.out.println("\n--categoryDistance");

        double[]        pattern = new double[] { 14, 26 };
        String          category = "A";
        LinearEvaluator instance = createTrainedInstance();
        double          expResult = 18.25;
        double          result = instance.categoryDistance(pattern, category);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of dump method, of class LinearEvaluator.
     */
    @Test
    public void testDump ()
    {
        System.out.println("\n--dump");

        LinearEvaluator instance = createTrainedInstance();
        instance.dump();
    }

    /**
     * Test of dumpDistance method, of class LinearEvaluator.
     */
    @Test
    public void testDumpDistance ()
    {
        System.out.println("\n--dumpDistance");

        double[]        pattern = new double[] { 14, 26 };
        String          category = "A";
        LinearEvaluator instance = createTrainedInstance();
        instance.dumpDistance(pattern, category);
    }

    /**
     * Test of patternDistance method, of class LinearEvaluator.
     */
    @Test
    public void testManyPatternDistance ()
    {
        System.out.println("\n--manyPatternDistance");

        LinearEvaluator instance = createTrainedInstance();
        double[]        one = new double[] { 10, 20 };
        System.out.println("Distances to " + Arrays.toString(one));

        for (Sample sample : samples) {
            double result = instance.patternDistance(one, sample.pattern);
            System.out.println("dist:" + result + " " + sample);
        }
    }

    /**
     * Test of marshal method, of class LinearEvaluator.
     * @throws Exception
     */
    /////////@Test
    public void testMarshal ()
        throws Exception
    {
        System.out.println("\n--marshal");

        File dir = new File(dirName);
        File file = new File(dir, fileName);
        dir.mkdirs();

        OutputStream    os = new FileOutputStream(file);
        LinearEvaluator instance = createTrainedInstance();
        instance.marshal(os);
        os.close();
    }

    /**
     * Test of patternDistance method, of class LinearEvaluator.
     */
    @Test
    public void testPatternDistance ()
    {
        System.out.println("\n--patternDistance");

        double[]        one = new double[] { 10, 20 };
        double[]        two = new double[] { 5, 40 };
        LinearEvaluator instance = createTrainedInstance();
        double          expResult = 0.03;
        double          result = instance.patternDistance(one, two);
        assertEquals(expResult, result, 0.1);
    }

    /**
     * Test of Printer methods, of class LinearEvaluator.
     */
    @Test
    public void testPrinter ()
    {
        System.out.println("\n--Printer");

        double[]        one = new double[] { 10, 20 };
        double[]        two = new double[] { 5, 40 };
        LinearEvaluator instance = createTrainedInstance();
        Printer         printer = instance.new Printer(12);

        System.out.println("defaults: " + printer.getDefaults());
        System.out.println("   names: " + printer.getNames());
        System.out.println("          " + printer.getDashes());
        System.out.println("  deltas: " + printer.getDeltas(one, two));
        System.out.println(" wDeltas: " + printer.getWeightedDeltas(one, two));
    }

    /**
     * Test of train method, of class LinearEvaluator.
     */
    @Test
    public void testTrain ()
    {
        System.out.println("\n--train");

        LinearEvaluator instance = createTrainedInstance();
        instance.dump();
    }

    /**
     * Test of unmarshal method, of class LinearEvaluator.
     * @throws Exception
     */
    ////////@Test
    public void testUnmarshal ()
        throws Exception
    {
        System.out.println("\n--unmarshal");

        File            dir = new File(dirName);
        File            file = new File(dir, fileName);
        InputStream     in = new FileInputStream(file);

        LinearEvaluator result = LinearEvaluator.unmarshal(in);
        result.dump();
    }

    /**
     * Test of marchal THEN unmarshal methods.
     * @throws Exception
     */
    @Test
    public void testMarshalThenUnmarshal ()
        throws Exception
    {
        testMarshal();
        testUnmarshal();
    }

    private LinearEvaluator createRawInstance ()
    {
        LinearEvaluator le = new LinearEvaluator(inNames);

        return le;
    }

    private LinearEvaluator createTrainedInstance ()
    {
        LinearEvaluator le = createRawInstance();
        le.train(samples);

        return le;
    }
}
