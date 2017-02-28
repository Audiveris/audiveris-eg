//----------------------------------------------------------------------------//
//                                                                            //
//                            G l y p h E v e n t                             //
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

import org.audiveris.omr.glyph.facets.Glyph;

/**
 * Class {@code GlyphEvent} represents a Glyph selection.
 *
 * @author Hervé Bitteur
 */
public class GlyphEvent
        extends NestEvent
{
    //~ Instance fields --------------------------------------------------------

    /** The selected glyph, which may be null */
    private final Glyph glyph;

    //~ Constructors -----------------------------------------------------------
    //------------//
    // GlyphEvent //
    //------------//
    /**
     * Creates a new GlyphEvent object.
     *
     * @param source   the entity that created this event
     * @param hint     hint about event origin (or null)
     * @param movement the mouse movement
     * @param glyph    the selected glyph (or null)
     */
    public GlyphEvent (Object source,
                       SelectionHint hint,
                       MouseMovement movement,
                       Glyph glyph)
    {
        super(source, hint, movement);
        this.glyph = glyph;
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // getEntity //
    //-----------//
    @Override
    public Glyph getData ()
    {
        return glyph;
    }
}
