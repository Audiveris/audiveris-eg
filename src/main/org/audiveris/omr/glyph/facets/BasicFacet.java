//----------------------------------------------------------------------------//
//                                                                            //
//                            B a s i c F a c e t                             //
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
package org.audiveris.omr.glyph.facets;

import org.audiveris.omr.util.Navigable;

/**
 * Class {@code BasicFacet} is the root for implementation on any
 * glyph facet.
 *
 * @author Hervé Bitteur
 */
public class BasicFacet
        implements GlyphFacet
{
    //~ Instance fields --------------------------------------------------------

    /** Our glyph */
    @Navigable(false)
    protected final Glyph glyph;

    //~ Constructors -----------------------------------------------------------
    //------------//
    // BasicFacet //
    //------------//
    /**
     * Create a new BasicFacet object
     *
     * @param glyph the glyph this facet describes
     */
    public BasicFacet (Glyph glyph)
    {
        this.glyph = glyph;
    }

    //~ Methods ----------------------------------------------------------------
    //--------//
    // dumpOf //
    //--------//
    @Override
    public String dumpOf ()
    {
        // Empty by default
        return "";
    }

    //-----------------//
    // invalidateCache //
    //-----------------//
    @Override
    public void invalidateCache ()
    {
        // void by default
    }
}
