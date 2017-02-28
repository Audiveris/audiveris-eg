//----------------------------------------------------------------------------//
//                                                                            //
//                                G e o U t i l                               //
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

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Class {@code GeoUtil} gathers simple utilities related to geometry.
 *
 * @author Hervé Bitteur
 */
public class GeoUtil
{
    //~ Methods ----------------------------------------------------------------

    //----------//
    // vectorOf //
    //----------//
    /**
     * Report the vector that goes from 'from' point to 'to' point.
     *
     * @param from the origin point
     * @param to   the target point
     * @return the vector from origin to target
     */
    public static Point vectorOf (Point from,
                                  Point to)
    {
        return new Point(to.x - from.x, to.y - from.y);
    }

    //----------//
    // centerOf //
    //----------//
    /**
     * Report the center of the provided rectangle
     *
     * @param rect the provided rectangle
     * @return the geometric rectangle center
     */
    public static Point centerOf (Rectangle rect)
    {
        return new Point(rect.x + (rect.width / 2), rect.y + (rect.height / 2));
    }
}
