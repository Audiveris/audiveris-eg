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
package org.audiveris.omr.moment;

import org.audiveris.omr.moments.BasicLegendreExtractor;
import org.audiveris.omr.moments.BasicLegendreMoments;
import org.audiveris.omr.moments.LegendreMoments;

import org.junit.*;

/**
 *
 * @author Hervé Bitteur
 */
public class LegendreMomentsTest
    extends MomentsExtractorTestHelper<LegendreMoments>
{
    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new LegendreMomentsTest object.
     */
    public LegendreMomentsTest ()
    {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Test of generate method, of class LegendreMoments.
     */
    @Test
    public void testAllShapes ()
    {
        try {
            BasicLegendreExtractor instance = new BasicLegendreExtractor();
            super.testAllShapes(instance, BasicLegendreMoments.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
