//----------------------------------------------------------------------------//
//                                                                            //
//                           S e c t i o n R o l e                            //
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
package org.audiveris.omr.stick;

import java.awt.Color;
import static java.awt.Color.*;

/**
 * Class {@code SectionRole} handles the role of a section in the
 * building of a stick.
 *
 * @author Hervé Bitteur
 */
public enum SectionRole
{

    /**
     * Flags a section which represents the starting Core (the most central
     * part) of a stick
     */
    CORE(orange),
    /**
     * Flags a section, which has been added to a stick when filling holes,
     * typically when retrieving long staff lines
     */
    HOLE(pink),
    /**
     * Flags an section, which is adjacent to a stick and to a crossing object
     */
    INTERNAL(blue),
    /**
     * Flags a section, at the peripheral of the stick
     */
    PERIPHERAL(yellow),
    /**
     * Flags a section, which is adjacent to a stick
     */
    BORDER(cyan),
    // Below: Sections which failed the stick building
    //
    /**
     * No stick member: Section too thick to be part of a stick
     */
    TOO_THICK(null),
    /**
     * No stick member: Section too far from the median line of the stick to be
     * part of it
     */
    TOO_FAR(null),
    /**
     * No stick member: Section not slim enough to be considered in the building
     * of the stick
     */
    TOO_FAT(null),
    /**
     * No stick member: Section discarded for various reasons
     */
    DISCARDED(null),
    /**
     * No stick member: Formerly internal section which has been purged
     */
    PURGED(null);

    /**
     * The color used when rendering the related section
     */
    private final Color color;

    //-------------//
    // SectionRole //
    //-------------//
    /**
     * Define a section role.
     *
     * @param color color used to render the section
     */
    SectionRole (Color color)
    {
        this.color = color;
    }

    //----------//
    // getColor //
    //----------//
    /**
     * Define a color, according to the role at hand, that is according to the
     * role of this section in the enclosing stick.
     *
     * @return the related color
     */
    public Color getColor ()
    {
        return color;
    }
}
