//----------------------------------------------------------------------------//
//                                                                            //
//                                E n d i n g                                 //
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
 * Class {@code Ending} is a physical {@link Dash} that is the
 * horizontal part of an alternate ending.
 *
 * @author Hervé Bitteur
 */
public class Ending
        extends Dash
{
    //~ Constructors -----------------------------------------------------------

    //--------//
    // Ending //
    //--------//
    /**
     * Create an Ending entity, with its underlying horizontal stick.
     *
     * @param stick the underlying stick
     * @param staff the related staff
     */
    public Ending (Glyph stick,
                   StaffInfo staff)
    {
        super(stick, staff);
    }
}
