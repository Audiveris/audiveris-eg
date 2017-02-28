//----------------------------------------------------------------------------//
//                                                                            //
//                     B r o k e n L i n e C o n t e x t                      //
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
package org.audiveris.omr.sheet;

import org.audiveris.omr.util.BrokenLine;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class {@code BrokenLineContext} gathers a broken line with its system
 * context (system above if any, and system below if any).
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "limit")
public class BrokenLineContext
{
    //~ Instance fields --------------------------------------------------------

    /** Id of system above, if any */
    @XmlAttribute(name = "system-above")
    public final int systemAbove;

    /** Id of system below, if any */
    @XmlAttribute(name = "system-below")
    public final int systemBelow;

    /** The broken line */
    @XmlElement(name = "line")
    public final BrokenLine line;

    //~ Constructors -----------------------------------------------------------
    //
    //-------------------//
    // BrokenLineContext //
    //-------------------//
    /**
     * Creates a new BrokenLineContext object.
     *
     * @param systemAbove Id of system above the line, or zero
     * @param systemBelow Id of system below the line, or zero
     * @param line        The broken line
     */
    public BrokenLineContext (int systemAbove,
                              int systemBelow,
                              BrokenLine line)
    {
        this.systemAbove = systemAbove;
        this.systemBelow = systemBelow;
        this.line = line;
    }

    //-------------------//
    // BrokenLineContext // No-arg constructor for JAXB only
    //-------------------//
    private BrokenLineContext ()
    {
        this.systemAbove = 0;
        this.systemBelow = 0;
        this.line = null;
    }

    //~ Methods ----------------------------------------------------------------
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder("{Context");
        sb.append(" above:")
                .append(systemAbove);
        sb.append(" below:")
                .append(systemBelow);
        sb.append(" line:")
                .append(line);

        return sb.toString();
    }
}
