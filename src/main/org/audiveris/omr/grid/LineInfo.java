//----------------------------------------------------------------------------//
//                                                                            //
//                              L i n e I n f o                               //
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
package org.audiveris.omr.grid;

import org.audiveris.omr.lag.Section;

import org.audiveris.omr.math.Line;

import org.audiveris.omr.util.HorizontalSide;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * Interface {@code LineInfo} describes the handling of one staff line.
 *
 * @author Hervé Bitteur
 */
public interface LineInfo
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Report the absolute contour rectangle
     *
     * @return the contour box (with minimum height of 1)
     */
    public Rectangle getBounds ();

    /**
     * Selector for the left or right ending point of the line
     *
     * @param side proper horizontal side
     * @return left point
     */
    public Point2D getEndPoint (HorizontalSide side);

    /**
     * Report the id of this line
     *
     * @return the line id (debugging info)
     */
    public int getId ();

    /**
     * Selector for the left point of the line
     *
     * @return left point
     */
    public Point2D getLeftPoint ();

    /**
     * Selector for the right point of the line
     *
     * @return right point
     */
    public Point2D getRightPoint ();

    /**
     * Report the lag sections that compose the staff line
     *
     * @return a collection of the line sections
     */
    public Collection<Section> getSections ();

    /**
     * Paint the computed line on the provided environment.
     *
     * @param g the graphics context
     */
    public void render (Graphics2D g);

    /**
     * Retrieve the precise intersection with a rather vertical line.
     *
     * @param vertical the rather vertical line
     * @return the precise intersection
     */
    public Point2D verticalIntersection (Line vertical);

    /**
     * Retrieve the staff line ordinate at given abscissa x, using int
     * values
     *
     * @param x the given abscissa
     * @return the corresponding y value
     */
    public int yAt (int x);

    /**
     * Retrieve the staff line ordinate at given abscissa x, using
     * double values
     *
     * @param x the given abscissa
     * @return the corresponding y value
     */
    public double yAt (double x);
}
