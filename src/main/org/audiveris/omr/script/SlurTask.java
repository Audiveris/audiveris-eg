//----------------------------------------------------------------------------//
//                                                                            //
//                              S l u r T a s k                               //
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Class {@code SlurTask} attempts to fix a slur glyph (by extracting
 * a new glyph out of some sections of the old glyph).
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
public class SlurTask
        extends GlyphUpdateTask
{
    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SlurTask object.
     *
     * @param sheet  the sheet impacted
     * @param glyphs the collection of glyphs to process
     */
    public SlurTask (Sheet sheet,
                     Collection<Glyph> glyphs)
    {
        super(sheet, glyphs);
    }

    //----------//
    // SlurTask //
    //----------//
    /** No-arg constructor for JAXB only */
    private SlurTask ()
    {
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
                .trimSlurs(getInitialGlyphs());
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder(super.internalsString());
        sb.append(" slur");

        return sb.toString();
    }
}
