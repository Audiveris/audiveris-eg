//----------------------------------------------------------------------------//
//                                                                            //
//                            S h e e t E v e n t                             //
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
package org.audiveris.omr.selection;

import org.audiveris.omr.sheet.Sheet;

/**
 * Class {@code SheetEvent} represent a Sheet selection event, used to
 * call attention about a selected sheet.
 *
 * @author Hervé Bitteur
 */
public class SheetEvent
        extends UserEvent
{
    //~ Instance fields --------------------------------------------------------

    /** The selected sheet, which may be null */
    private final Sheet sheet;

    //~ Constructors -----------------------------------------------------------
    //------------//
    // SheetEvent //
    //------------//
    /**
     * Creates a new SheetEvent object.
     *
     * @param source   the entity that created this event
     * @param hint     hint about event origin
     * @param movement the mouse movement
     * @param sheet    the selected sheet (or null)
     */
    public SheetEvent (Object source,
                       SelectionHint hint,
                       MouseMovement movement,
                       Sheet sheet)
    {
        super(source, null, null);
        this.sheet = sheet;
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // getEntity //
    //-----------//
    @Override
    public Sheet getData ()
    {
        return sheet;
    }
}
