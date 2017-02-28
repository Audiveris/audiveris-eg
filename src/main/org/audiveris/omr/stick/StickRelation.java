//----------------------------------------------------------------------------//
//                                                                            //
//                         S t i c k R e l a t i o n                          //
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

/**
 * Class {@code StickRelation} complements {@link org.audiveris.omr.lag.Section}
 * for easy stick elaboration.
 *
 * @author Hervé Bitteur
 */
public class StickRelation
{
    //~ Instance fields --------------------------------------------------------

    /**
     * The role of the section in the enclosing stick.
     * Not final, since it may be modified afterhand.
     */
    public SectionRole role;

    /**
     * Position with respect to line core center
     */
    public int direction;

    /**
     * Layer of this section in the stick
     */
    public int layer;

    //~ Constructors -----------------------------------------------------------
    //---------------//
    // StickRelation //
    //---------------//
    /**
     * Creates a new StickRelation.
     */
    public StickRelation ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //----------//
    // getColor //
    //----------//
    /**
     * Define a color, according to the data at hand, that is according
     * to the role of this section in the enclosing stick.
     *
     * @return the related color
     */
    public Color getColor ()
    {
        if (role != null) {
            return role.getColor();
        } else {
            return null;
        }
    }

    //-------------//
    // isCandidate //
    //-------------//
    /**
     * Checks whether the section is a good candidate to be a member
     * of a stick
     *
     * @return the result of the test
     */
    public boolean isCandidate ()
    {
        return (role != null)
               && (role.ordinal() < SectionRole.BORDER.ordinal());
    }

    //-----------//
    // setParams //
    //-----------//
    /**
     * Assign the various parameters (kind, layer and direction)
     *
     * @param role      the role of this section in stick elaboration
     * @param layer     the layer from stick core
     * @param direction the direction when departing from the stick core
     */
    public void setParams (SectionRole role,
                           int layer,
                           int direction)
    {
        this.role = role;
        this.layer = layer;
        this.direction = direction;
    }

    //----------//
    // toString //
    //----------//
    /**
     * A readable description of this entity
     *
     * @return the string
     */
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder(256);

        sb.append("[");

        sb.append("L=")
                .append(layer);
        sb.append(" D=")
                .append(direction);

        if (role != null) {
            sb.append(" ")
                    .append(role);
        }

        sb.append("]");

        return sb.toString();
    }
}
