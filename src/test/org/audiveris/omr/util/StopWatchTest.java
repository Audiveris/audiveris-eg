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

import org.audiveris.omr.util.StopWatch;
import org.junit.Test;

/**
 *
 * @author Hervé Bitteur
 */
public class StopWatchTest
{
    //~ Instance fields --------------------------------------------------------

    StopWatch instance = new StopWatch("Utility Watch");
    int       j;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new StopWatchTest object.
     */
    public StopWatchTest ()
    {
    }

    //~ Methods ----------------------------------------------------------------

    @Test
    public void testOne ()
    {
        System.out.println("void");
        instance.start("task #1");

        instance.print();
    }

    @Test
    public void testSimple ()
    {
        System.out.println("simple");

        instance.start("task #1");
        waste();
        instance.stop();

        instance.start("task #2");
        waste();
        waste();
        instance.stop();

        instance.start("task #3");
        waste();
        waste();
        waste();
        instance.print();
    }

    @Test
    public void testOverlap ()
    {
        System.out.println("overlap");

        instance.start("task #1");
        waste();
        
        instance.start("task #2");
        waste();
        waste();
        
        instance.start("task #3");
        waste();
        waste();
        waste();
        instance.print();
    }

    @Test
    public void testVoid ()
    {
        System.out.println("void");

        instance.print();
    }

    private void waste ()
    {
        for (int i = 0; i < 10000000; i++) {
            j = i / 2;
        }
    }
}
