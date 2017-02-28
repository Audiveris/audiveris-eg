//----------------------------------------------------------------------------//
//                                                                            //
//                           P o i n t F a c a d e                            //
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

import java.awt.Point;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class {@code PointFacade} is a (hopefully temporary) fix to allow Xml
 * binding of standard class Point that we cannot annotate
 *
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "point")
public class PointFacade
{
    //~ Instance fields --------------------------------------------------------

    /** The interfaced Point instance */
    private Point point;

    //~ Constructors -----------------------------------------------------------
    //-------------//
    // PointFacade //
    //-------------//
    /**
     * Creates a new instance of PointFacade
     */
    public PointFacade ()
    {
    }

    //-------------//
    // PointFacade //
    //-------------//
    /**
     * Creates a new PointFacade object.
     *
     * @param point the interfaced point
     */
    public PointFacade (Point point)
    {
        this.point = point;
    }

    //~ Methods ----------------------------------------------------------------
    //----------//
    // getPoint //
    //----------//
    /**
     * Report the interfaced point
     *
     * @return the actual point
     */
    public Point getPoint ()
    {
        return point;
    }

    //------//
    // getX //
    //------//
    public int getX ()
    {
        return point.x;
    }

    //------//
    // getY //
    //------//
    public int getY ()
    {
        return point.y;
    }

    //------//
    // setX //
    //------//
    @XmlAttribute
    public void setX (int x)
    {
        if (point == null) {
            point = new Point();
        }

        point.x = x;
    }

    //------//
    // setY //
    //------//
    @XmlAttribute
    public void setY (int y)
    {
        if (point == null) {
            point = new Point();
        }

        point.y = y;
    }
}
