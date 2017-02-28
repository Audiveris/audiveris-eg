//----------------------------------------------------------------------------//
//                                                                            //
//                          S e c t i o n E v e n t                           //
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

import org.audiveris.omr.lag.Section;

/**
 * Class {@code SectionEvent} represents a Section selection.
 *
 * @author Hervé Bitteur
 */
public class SectionEvent
        extends LagEvent
{
    //~ Instance fields --------------------------------------------------------

    /** The selected section, which may be null */
    private final Section section;

    //~ Constructors -----------------------------------------------------------
    //--------------//
    // SectionEvent //
    //--------------//
    /**
     * Creates a new SectionEvent object.
     *
     * @param source   the entity that created this event
     * @param hint     hint about event origin
     * @param movement the mouse movement
     * @param section  the selected section (or null)
     */
    public SectionEvent (Object source,
                         SelectionHint hint,
                         MouseMovement movement,
                         Section section)
    {
        super(source, hint, movement);
        this.section = section;
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // getEntity //
    //-----------//
    @Override
    public Section getData ()
    {
        return section;
    }
}
