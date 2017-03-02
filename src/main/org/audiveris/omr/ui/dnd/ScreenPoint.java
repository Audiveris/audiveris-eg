//----------------------------------------------------------------------------//
//                                                                            //
//                           S c r e e n P o i n t                            //
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
package org.audiveris.omr.ui.dnd;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.SwingUtilities;

/**
 * Class {@code ScreenPoint} encapsulates a point defined with respect
 * to the screen.
 *
 * @author Hervé Bitteur
 */
public class ScreenPoint
        extends Point
{
    //~ Constructors -----------------------------------------------------------

    //-------------//
    // ScreenPoint //
    //-------------//
    /**
     * Create a new ScreenPoint object.
     */
    public ScreenPoint ()
    {
    }

    //-------------//
    // ScreenPoint //
    //-------------//
    /**
     * Creates a new ScreenPoint object, by cloning an untyped point
     *
     * @param x abscissa
     * @param y ordinate
     */
    public ScreenPoint (int x,
                        int y)
    {
        super(x, y);
    }

    //-------------//
    // ScreenPoint //
    //-------------//
    /**
     * Creates a new ScreenPoint object, using a local component-based
     * point.
     *
     * @param component  the component to use as the base
     * @param localPoint the component-based point
     */
    public ScreenPoint (Component component,
                        Point localPoint)
    {
        this(localPoint.x, localPoint.y);
        SwingUtilities.convertPointToScreen(this, component);
    }

    //~ Methods ----------------------------------------------------------------
    //---------------//
    // getLocalPoint //
    //---------------//
    /**
     * Get the corresponding local point wrt a containing component
     *
     * @param component the provided component
     * @return the local point, wrt component top left corner
     */
    public Point getLocalPoint (Component component)
    {
        Point localPoint = new Point(x, y);
        SwingUtilities.convertPointFromScreen(localPoint, component);

        return localPoint;
    }

    //---------------//
    // isInComponent //
    //---------------//
    /**
     * Check whether this screen point lies within the bound of the
     * provided component.
     *
     * @param component the provided component
     * @return true if within the component, false otherwise
     */
    public boolean isInComponent (Component component)
    {
        Rectangle bounds = new Rectangle(
                0,
                0,
                component.getWidth(),
                component.getHeight());

        return bounds.contains(getLocalPoint(component));
    }
}
