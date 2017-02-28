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

import org.audiveris.omr.math.IntegerHistogram;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.PrintStream;

/**
 *
 * @author Hervé Bitteur
 */
public class IntegerHistogramTest
{
    //~ Instance fields --------------------------------------------------------

    private IntegerHistogram histo;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new IntegerHistogramTest object.
     */
    public IntegerHistogramTest ()
    {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Test of print method, of class IntegerHistogram.
     */
    @Test
    public void testPrint ()
    {
        System.out.println("print");
        
        

        PrintStream      stream = System.out;
        IntegerHistogram instance = createHistogram();
        instance.print(stream);
    }

    private IntegerHistogram createHistogram ()
    {
        histo = new IntegerHistogram();
        histo.increaseCount(1,1250);
        histo.increaseCount(2,1400);
        histo.increaseCount(3,2000);
        histo.increaseCount(4,1950);
        histo.increaseCount(5,2125);
        histo.increaseCount(6,1800);
        histo.increaseCount(7,1800);
        histo.increaseCount(8,2500);
        histo.increaseCount(9,3000);
        histo.increaseCount(10,20000);
        histo.increaseCount(11,12000);
        histo.increaseCount(12,1100);
        histo.increaseCount(13,1300);
        histo.increaseCount(14,11000);
        histo.increaseCount(15,23800);
        histo.increaseCount(16,3000);
        histo.increaseCount(17,600);
        

        return histo;
    }
}
