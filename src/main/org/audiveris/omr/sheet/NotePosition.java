//----------------------------------------------------------------------------//
//                                                                            //
//                          N o t e P o s i t i o n                           //
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

import org.audiveris.omr.grid.StaffInfo;
import org.audiveris.omr.grid.StaffInfo.IndexedLedger;

/**
 * Class {@code NotePosition} handles the precise position of a
 * note-like entity, with respect to its related staff.
 *
 * @author Hervé Bitteur
 */
public class NotePosition
{
    //~ Instance fields --------------------------------------------------------

    /** The related staff. */
    private final StaffInfo staff;

    /** The precise pitch position wrt the staff. */
    private final double pitchPosition;

    /** The closest ledger if any. */
    private final IndexedLedger indexedLedger;

    //~ Constructors -----------------------------------------------------------
    //--------------//
    // NotePosition //
    //--------------//
    /**
     * Creates a new NotePosition object.
     *
     * @param staff         the related staff
     * @param pitchPosition the precise pitch position
     * @param indexedLedger the closest ledger if any
     */
    public NotePosition (StaffInfo staff,
                         double pitchPosition,
                         IndexedLedger indexedLedger)
    {
        this.staff = staff;
        this.indexedLedger = indexedLedger;
        this.pitchPosition = pitchPosition;
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // getLedger //
    //-----------//
    /**
     * @return the ledger
     */
    public IndexedLedger getLedger ()
    {
        return indexedLedger;
    }

    //------------------//
    // getPitchPosition //
    //------------------//
    /**
     * @return the pitchPosition
     */
    public double getPitchPosition ()
    {
        return pitchPosition;
    }

    //----------//
    // getStaff //
    //----------//
    /**
     * @return the staff
     */
    public StaffInfo getStaff ()
    {
        return staff;
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder("{");
        sb.append(getClass().getSimpleName());

        sb.append(" staff#")
                .append(staff.getId());

        sb.append(" pitch:")
                .append((float) pitchPosition);

        if (indexedLedger != null) {
            sb.append(" ledger#")
                    .append(indexedLedger.glyph.getId());
        }

        sb.append("}");

        return sb.toString();
    }
}
