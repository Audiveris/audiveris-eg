//----------------------------------------------------------------------------//
//                                                                            //
//                                L e d g e r                                 //
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

import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.grid.StaffInfo;

/**
 * Class {@code Ledger} is a physical {@link Dash} which is logically
 * a Ledger (to represents portions of virtual staff lines)
 *
 * @author Hervé Bitteur
 */
@Deprecated
public class Ledger
        extends Dash
{
    //~ Instance fields --------------------------------------------------------

    /** Precise line index outside of staff nearby. */
    private final int lineIndex;

    //~ Constructors -----------------------------------------------------------
    //--------//
    // Ledger //
    //--------//
    /**
     * Create a Ledger, from its underlying horizontal stick
     *
     * @param stick     the related retrieved stick
     * @param staff     the staff nearby
     * @param lineIndex the precise line index wrt staff
     *                  ( -1, -2, ... above staff and +1, +2, ... below staff)
     */
    public Ledger (Glyph stick,
                   StaffInfo staff,
                   int lineIndex)
    {
        super(stick, staff);
        this.lineIndex = lineIndex;
    }

    //~ Methods ----------------------------------------------------------------
    //--------------//
    // getLineIndex //
    //--------------//
    /**
     * @return the precise line index for this ledger
     */
    public int getLineIndex ()
    {
        return lineIndex;
    }

    //------------------//
    // getPitchPosition //
    //------------------//
    /**
     * Report the pitch position of this ledger WRT the related staff
     *
     * @return the pitch position
     */
    public int getPitchPosition ()
    {
        //        // Safer, for the time being...
        //        if (getStaff()
        //                .getLines()
        //                .size() != 5) {
        //            throw new RuntimeException("Only 5-line staves are supported");
        //        }
        if (lineIndex > 0) {
            return 4 + (2 * lineIndex);
        } else {
            return -4 + (2 * lineIndex);
        }
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder(super.internalsString());
        sb.append(" index:")
                .append(lineIndex);
        sb.append(" pitch:")
                .append(getPitchPosition());

        return sb.toString();
    }
}
