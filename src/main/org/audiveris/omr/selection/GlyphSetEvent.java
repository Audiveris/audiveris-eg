//----------------------------------------------------------------------------//
//                                                                            //
//                         G l y p h S e t E v e n t                          //
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

import org.audiveris.omr.glyph.Glyphs;
import org.audiveris.omr.glyph.facets.Glyph;

import java.util.Set;

/**
 * Class {@code GlyphSetEvent} represents a Glyph Set selection.
 *
 * @author Hervé Bitteur
 */
public class GlyphSetEvent
        extends NestEvent
{
    //~ Instance fields --------------------------------------------------------

    /** The selected glyph set, which may be null */
    private final Set<Glyph> glyphs;

    //~ Constructors -----------------------------------------------------------
    //------------//
    // GlyphEvent //
    //------------//
    /**
     * Creates a new GlyphEvent object.
     *
     * @param source   the entity that created this event
     * @param hint     hint about event origin (or null)
     * @param movement the user movement
     * @param glyphs   the selected collection of glyphs (or null)
     */
    public GlyphSetEvent (Object source,
                          SelectionHint hint,
                          MouseMovement movement,
                          Set<Glyph> glyphs)
    {
        super(source, hint, movement);
        this.glyphs = glyphs;
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // getEntity //
    //-----------//
    @Override
    public Set<Glyph> getData ()
    {
        return glyphs;
    }

    //----------------//
    // internalString //
    //----------------//
    @Override
    protected String internalString ()
    {
        if (glyphs != null) {
            return Glyphs.toString(glyphs);
        } else {
            return "";
        }
    }
}
