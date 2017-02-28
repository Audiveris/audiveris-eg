//----------------------------------------------------------------------------//
//                                                                            //
//                           S e g m e n t T a s k                            //
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
package org.audiveris.omr.script;

import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.sheet.Sheet;

import java.util.Collection;

/**
 * Class {@code SegmentTask} attempts to segment a collection of
 * glyphs (along verticals for the time being).
 *
 * @author Hervé Bitteur
 */
public class SegmentTask
        extends GlyphUpdateTask
{
    //~ Instance fields --------------------------------------------------------

    /** Are we looking for short verticals? */
    private final boolean isShort;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new SegmentTask object.
     *
     * @param sheet   the sheet impacted
     * @param isShort true if we are looking for short verticals
     * @param glyphs  the collection of glyphs to look up
     */
    public SegmentTask (Sheet sheet,
                        boolean isShort,
                        Collection<Glyph> glyphs)
    {
        super(sheet, glyphs);
        this.isShort = isShort;
    }

    //-------------//
    // SegmentTask //
    //-------------//
    /** No-arg constructor for JAXB only */
    private SegmentTask ()
    {
        isShort = false; // Dummy value
    }

    //~ Methods ----------------------------------------------------------------
    //------//
    // core //
    //------//
    @Override
    public void core (Sheet sheet)
            throws Exception
    {
        sheet.getSymbolsController()
                .getModel()
                .segmentGlyphs(getInitialGlyphs(), isShort);
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder(super.internalsString());
        sb.append(" segment");

        if (isShort) {
            sb.append(" ")
                    .append("short");
        }

        return sb.toString();
    }
}
