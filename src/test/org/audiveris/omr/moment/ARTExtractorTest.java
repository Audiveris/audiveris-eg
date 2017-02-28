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

import org.audiveris.omr.moments.ARTMoments;
import org.audiveris.omr.moments.BasicARTExtractor;
import org.audiveris.omr.moments.BasicARTMoments;

import org.junit.*;

/**
 * Unit test for (Basic) ARTExtractor.
 *
 * @author Hervé Bitteur
 */
public class ARTExtractorTest
    extends MomentsExtractorTestHelper<ARTMoments>
{
    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ARTExtractorTest object.
     */
    public ARTExtractorTest ()
    {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Test of generate method, of class ARTMoments.
     */
    @Test
    public void testAllShapes ()
        throws Exception
    {
        super.testAllShapes(
            new BasicARTExtractor(),
            BasicARTMoments.class);
    }
}
