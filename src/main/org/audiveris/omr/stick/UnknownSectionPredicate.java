//----------------------------------------------------------------------------//
//                                                                            //
//               U n k n o w n S e c t i o n P r e d i c a t e                //
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

import org.audiveris.omr.lag.Section;

import org.audiveris.omr.util.Predicate;

/**
 * Class {@code UnknownSectionPredicate} is a basic predicate on
 * sections used to build sticks, for which we just check if not section
 * isKnown().
 *
 * @author Hervé Bitteur
 */
public class UnknownSectionPredicate
    implements Predicate<Section>
{
    //~ Constructors -----------------------------------------------------------

    //-------------------------//
    // UnknownSectionPredicate //
    //-------------------------//
    /**
     * Creates a new instance of UnknownSectionPredicate
     */
    public UnknownSectionPredicate ()
    {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean check (Section section)
    {
        // Check if this section is not already assigned to a recognized glyph
        return !section.isKnown();
    }
}
