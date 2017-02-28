//----------------------------------------------------------------------------//
//                                                                            //
//                       S e c t i o n S e t E v e n t                        //
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
import org.audiveris.omr.lag.Sections;

import java.util.Set;

/**
 * Class {@code SectionSetEvent} represents a Section Set selection.
 *
 * @author Hervé Bitteur
 */
public class SectionSetEvent
        extends LagEvent
{
    //~ Instance fields --------------------------------------------------------

    /** The selected section set, which may be null */
    private final Set<Section> sections;

    //~ Constructors -----------------------------------------------------------
    //-----------------//
    // SectionSetEvent //
    //-----------------//
    /**
     * Creates a new SectionSetEvent object.
     *
     * @param source   the entity that created this event
     * @param hint     hint about event origin (or null)
     * @param movement the mouse movement
     * @param sections the selected collection of sections (or null)
     */
    public SectionSetEvent (Object source,
                            SelectionHint hint,
                            MouseMovement movement,
                            Set<Section> sections)
    {
        super(source, hint, movement);
        this.sections = sections;
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // getEntity //
    //-----------//
    @Override
    public Set<Section> getData ()
    {
        return sections;
    }

    //----------------//
    // internalString //
    //----------------//
    @Override
    protected String internalString ()
    {
        if (sections != null) {
            return Sections.toString(sections);
        } else {
            return "";
        }
    }
}
