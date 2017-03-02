//----------------------------------------------------------------------------//
//                                                                            //
//                           T a r g e t S t a f f                            //
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

import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code TargetStaff} is an immutable perfect destination
 * object for a staff.
 *
 * @author Hervé Bitteur
 */
public class TargetStaff
{
    //~ Instance fields --------------------------------------------------------

    /** Initial raw information */
    public final StaffInfo info;

    /** Id for debug */
    public final int id;

    /** Ordinate of top in containing page */
    public final double top;

    /** Sequence of staff lines */
    public final List<TargetLine> lines = new ArrayList<>();

    /** Containing system */
    public final TargetSystem system;

    //~ Constructors -----------------------------------------------------------
    //-------------//
    // TargetStaff //
    //-------------//
    /**
     * Creates a new TargetStaff object.
     *
     * @param info initial raw information
     * @param top  Ordinate of top in containing page
     */
    public TargetStaff (StaffInfo info,
                        double top,
                        TargetSystem system)
    {
        this.info = info;
        this.top = top;
        this.system = system;

        id = info.getId();
    }

    //~ Methods ----------------------------------------------------------------
    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder("{Staff");
        sb.append("#")
                .append(id);
        sb.append(" top:")
                .append(top);
        sb.append("}");

        return sb.toString();
    }
}
