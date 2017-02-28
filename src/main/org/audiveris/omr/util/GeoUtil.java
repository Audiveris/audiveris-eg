//----------------------------------------------------------------------------//
//                                                                            //
//                               G e o U t i l                                //
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

import java.awt.Rectangle;

/**
 * This class gathers static utilities related to geometry.
 *
 * @author Hervé Bitteur
 */
public class GeoUtil
{
    //~ Methods ----------------------------------------------------------------

    //------//
    // xGap //
    //------//
    /**
     * Report the abscissa gap between the provided rectangles
     *
     * @param one first provided rectangle
     * @param two the other provided rectangle
     * @return a negative value if the rectangles overlap horizontally or a
     *         positive value if there is a true horizontal gap
     */
    public static int xGap (Rectangle one,
                            Rectangle two)
    {
        return -xOverlap(one, two);
    }

    //----------//
    // xOverlap //
    //----------//
    /**
     * Report the abscissa overlap between the provided rectangles
     *
     * @param one first provided rectangle
     * @param two the other provided rectangle
     * @return a positive value if the rectangles overlap horizontally or a
     *         negative value if there is a true horizontal gap
     */
    public static int xOverlap (Rectangle one,
                                Rectangle two)
    {
        final int commonLeft = Math.max(one.x, two.x);
        final int commonRight = Math.min(one.x + one.width, two.y + two.width);

        return commonRight - commonLeft;
    }

    //------//
    // yGap //
    //------//
    /**
     * Report the ordinate gap between the provided rectangles
     *
     * @param one first provided rectangle
     * @param two the other provided rectangle
     * @return a negative value if the rectangles overlap vertically or a
     *         positive value if there is a true vertical gap
     */
    public static int yGap (Rectangle one,
                            Rectangle two)
    {
        return -yOverlap(one, two);
    }

    //----------//
    // yOverlap //
    //----------//
    /**
     * Report the ordinate overlap between the provided rectangles
     *
     * @param one first provided rectangle
     * @param two the other provided rectangle
     * @return a positive value if the rectangles overlap vertically or a
     *         negative value if there is a true vertical gap
     */
    public static int yOverlap (Rectangle one,
                                Rectangle two)
    {
        final int commonTop = Math.max(one.y, two.y);
        final int commonBot = Math.min(one.y + one.height, two.y + two.height);

        return commonBot - commonTop;
    }
}
