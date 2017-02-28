//----------------------------------------------------------------------------//
//                                                                            //
//                          G l y p h P a t t e r n                           //
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
package org.audiveris.omr.glyph.pattern;

import org.audiveris.omr.sheet.Scale;
import org.audiveris.omr.sheet.SystemInfo;

/**
 * Class {@code GlyphPattern} describes a specific pattern applied on
 * glyphs of a given system.
 *
 * @author Hervé Bitteur
 */
public abstract class GlyphPattern
{
    //~ Instance fields --------------------------------------------------------

    /** Name for debugging */
    public final String name;

    /** Related system */
    protected final SystemInfo system;

    /** System scale */
    protected final Scale scale;

    //~ Constructors -----------------------------------------------------------
    //--------------//
    // GlyphPattern //
    //--------------//
    /**
     * Creates a new GlyphPattern object.
     *
     * @param name   the unique name for this pattern
     * @param system the related system
     */
    public GlyphPattern (String name,
                         SystemInfo system)
    {
        this.name = name;
        this.system = system;

        scale = system.getSheet()
                .getScale();
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // runPattern //
    //------------//
    /**
     * This method runs the pattern and reports the number of modified
     * glyphs.
     *
     * @return the number of modified glyphs
     */
    public abstract int runPattern ();

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        return "S" + system.getId() + " pattern:" + name;
    }
}
