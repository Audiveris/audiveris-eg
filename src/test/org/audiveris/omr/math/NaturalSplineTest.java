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

import org.audiveris.omr.math.NaturalSpline;
import org.audiveris.omr.util.BaseTestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Hervé Bitteur
 */
public class NaturalSplineTest
    extends BaseTestCase
{
    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NaturalSplineTest object.
     */
    public NaturalSplineTest ()
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

    @Before
    public void setUp ()
    {
    }

    @After
    public void tearDown ()
    {
    }

    /**
     * Test of interpolate method, of class NaturalSpline.
     */
    @Test
    public void testInterpolate0 ()
    {
        double[] xx = new double[] {  };
        double[] yy = new double[] {  };

        try {
            NaturalSpline spline = NaturalSpline.interpolate(xx, yy);
            fail("Exception should be raised when no points are defined");
            System.out.println(spline.toString());
        } catch (IllegalArgumentException expected) {
            checkException(expected);
        }
    }

    /**
     * Test of interpolate method, of class NaturalSpline.
     */
    @Test
    public void testInterpolate1 ()
    {
        double[] xx = new double[] { 1 };
        double[] yy = new double[] { 1 };

        try {
            NaturalSpline spline = NaturalSpline.interpolate(xx, yy);
            fail("Exception should be raised when only one point is defined");
            System.out.println(spline.toString());
        } catch (IllegalArgumentException expected) {
            checkException(expected);
        }
    }

    /**
     * Test of interpolate method, of class NaturalSpline.
     */
    @Test
    public void testInterpolate4 ()
    {
        double[]         xx = new double[] { 0, 12, 19, 30 };
        double[]         yy = new double[] { 0, 1, 2, 3 };
        NaturalSpline spline = NaturalSpline.interpolate(xx, yy);
        System.out.println(spline.toString());

        for (double x = xx[0]; x <= xx[xx.length - 1]; x++) {
            double y = spline.yAtX(x);
            System.out.println("x=" + x + " y=" + (float) y);
        }
    }

    /**
     * Test of interpolate method, of class NaturalSpline.
     */
    @Test
    public void testInterpolate5 ()
    {
        double[]         xx = new double[] { 1, 11, 20, 30, 40 };
        double[]         yy = new double[] { 1, 2, 3, 4, 3 };
        NaturalSpline spline = NaturalSpline.interpolate(xx, yy);
        System.out.println(spline.toString());

        for (double x = xx[0]; x <= xx[xx.length - 1]; x++) {
            double y = spline.yAtX(x);
            System.out.println("x=" + x + " y=" + (float) y);
        }
    }

    /**
     * Test of interpolate method, of class NaturalSpline.
     */
    @Test
    public void testInterpolateDiff ()
    {
        double[] xx = new double[] {  };
        double[] yy = new double[] { 1 };

        try {
            NaturalSpline spline = NaturalSpline.interpolate(xx, yy);
            fail("Exception should be raised when no points are defined");
            System.out.println(spline.toString());
        } catch (IllegalArgumentException expected) {
            checkException(expected);
        }
    }

    /**
     * Test of interpolate method, of class NaturalSpline.
     */
    @Test
    public void testInterpolateFour ()
    {
        double[]         xx = new double[] { 0, 10, 10, 20 };
        double[]         yy = new double[] { 0, 2, 2, 2 };
        NaturalSpline spline = NaturalSpline.interpolate(xx, yy);
        System.out.println(spline.toString());

        for (double x = xx[0]; x <= xx[xx.length - 1]; x++) {
            double y = spline.yAtX(x);
            System.out.println("x=" + x + " y=" + (float) y);
        }
    }

    /**
     * Test of interpolate method, of class NaturalSpline.
     */
    @Test
    public void testInterpolateLine ()
    {
        double[]         xx = new double[] { 0, 10 };
        double[]         yy = new double[] { 0, 1 };
        NaturalSpline spline = NaturalSpline.interpolate(xx, yy);
        System.out.println(spline.toString());

        for (double x = xx[0]; x <= xx[xx.length - 1]; x++) {
            double y = spline.yAtX(x);
            System.out.println("x=" + x + " y=" + (float) y);
        }
    }

    /**
     * Test of interpolate method, of class NaturalSpline.
     */
    @Test
    public void testInterpolateNull ()
    {
        double[] xx = new double[] {  };
        double[] yy = null;

        try {
            NaturalSpline spline = NaturalSpline.interpolate(xx, yy);
            fail("Exception should be raised when no points are defined");
            System.out.println(spline.toString());
        } catch (IllegalArgumentException expected) {
            checkException(expected);
        }
    }

    /**
     * Test of interpolate method, of class NaturalSpline.
     */
    @Test
    public void testInterpolateQuad ()
    {
        double[]         xx = new double[] { 0, 20, 30 };
        double[]         yy = new double[] { 0, 10, 10};
        NaturalSpline spline = NaturalSpline.interpolate(xx, yy);
        System.out.println(spline.toString());

        for (double x = xx[0]; x <= xx[xx.length - 1]; x++) {
            double y = spline.yAtX(x);
            System.out.println("x=" + x + " y=" + (float) y);
        }
    }
}
