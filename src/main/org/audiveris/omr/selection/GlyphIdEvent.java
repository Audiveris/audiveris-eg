//----------------------------------------------------------------------------//
//                                                                            //
//                          G l y p h I d E v e n t                           //
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

/**
 * Class {@code GlyphIdEvent} represents a Glyph Id selection.
 *
 * @author Hervé Bitteur
 */
public class GlyphIdEvent
        extends NestEvent
{
    //~ Instance fields --------------------------------------------------------

    /** The selected glyph id, which may be null */
    private final Integer id;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new GlyphIdEvent object.
     *
     * @param source   the entity that created this event
     * @param hint     hint about event origin (or null)
     * @param movement the precise mouse movement
     * @param id       the glyph id
     */
    public GlyphIdEvent (Object source,
                         SelectionHint hint,
                         MouseMovement movement,
                         Integer id)
    {
        super(source, hint, null);
        this.id = id;
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // getEntity //
    //-----------//
    @Override
    public Integer getData ()
    {
        return id;
    }
}
