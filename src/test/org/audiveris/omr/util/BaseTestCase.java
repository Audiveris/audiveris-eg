//----------------------------------------------------------------------------//
//                                                                            //
//                          B a s e T e s t C a s e                           //
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
package org.audiveris.omr.util;

import static junit.framework.Assert.*;

import junit.framework.*;

/**
 * Class <code>BaseTestCase</code> is a customized version of TestCase, in
 * order to factor additional test features.
 *
 * @author Hervé Bitteur
 * @version $Id$
 */
public class BaseTestCase
    extends TestCase
{
    public BaseTestCase()
    {
    }

    public BaseTestCase (String name)
    {
        super(name);
    }
    
    // ----------------------------------------------------------------//
    // Provide one valid JUnit3-style test method to keep JUnit4 quite 
    // ----------------------------------------------------------------//
    public void testNothing ()
    {
    }

    //---------//
    // runTest //
    //---------//
    @Override
    protected void runTest() throws Throwable {
        System.out.println("\n---\n" + getName() +":");
        super.runTest();
        System.out.println("+++ End " + toString());
    }

    //-------//
    // print //
    //-------//
    public static void print (Object o)
    {
        System.out.println(o);
    }

    //----------------//
    // checkException //
    //----------------//
    public static void checkException (Exception ex)
    {
        System.out.println("Got " + ex);
        assertNotNull(ex.getMessage());
    }

    //-------------//
    // assertNears //
    //-------------//
    public static void assertNears (String msg,
                                    double a,
                                    double b)
    {
        assertNears(msg, a, b, 1E-5);
    }

    //-------------//
    // assertNears //
    //-------------//
    public static void assertNears (String msg,
                                    double a,
                                    double b,
                                    double maxDiff)
    {
        System.out.println("Comparing " + a + " and " + b);
        assertTrue(msg,
                   Math.abs(a - b) < maxDiff);
    }
}
