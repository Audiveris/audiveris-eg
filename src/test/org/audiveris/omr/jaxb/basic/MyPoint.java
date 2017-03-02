//----------------------------------------------------------------------------//
//                                                                            //
//                               M y P o i n t                                //
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
package org.audiveris.omr.jaxb.basic;

import java.awt.Point;

import javax.xml.bind.annotation.*;

/**
 *
 * @author hb115668
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "point")
public class MyPoint
{
    //~ Instance fields --------------------------------------------------------

    private Point p;

    //~ Constructors -----------------------------------------------------------

    /** Creates a new instance of MyPoint */
    public MyPoint ()
    {
    }

    /**
     * Creates a new MyPoint object.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public MyPoint (Point p)
    {
        this.p = p;
    }

    //~ Methods ----------------------------------------------------------------

    public Point getPoint ()
    {
        return p;
    }

    @XmlElement
    public void setX (int x)
    {
        p.x = x;
    }

    public int getX ()
    {
        return p.x;
    }

    @XmlElement
    public void setY (int y)
    {
        p.y = y;
    }

    public int getY ()
    {
        return p.y;
    }
}
