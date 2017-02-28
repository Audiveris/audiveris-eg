//----------------------------------------------------------------------------//
//                                                                            //
//                          V i r t u a l G l y p h                           //
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
package org.audiveris.omr.glyph;

import org.audiveris.omr.lag.Section;

import org.audiveris.omr.math.GeoUtil;

import org.audiveris.omr.ui.symbol.Symbols;

import java.awt.Color;
import java.awt.Point;
import java.util.Collection;

/**
 * Class {@code VirtualGlyph} is an artificial glyph specifically
 * build from a MusicFont-based symbol, to carry a shape and features
 * just like a standard glyph would.
 *
 * @author Hervé Bitteur
 */
public class VirtualGlyph
        extends SymbolGlyph
{
    //~ Constructors -----------------------------------------------------------

    //--------------//
    // VirtualGlyph //
    //--------------//
    /**
     * Create a new VirtualGlyph object
     *
     * @param shape     the assigned shape
     * @param interline the related interline scaling value
     * @param center    where the glyph area center will be located
     */
    public VirtualGlyph (Shape shape,
                         int interline,
                         Point center)
    {
        // Build a glyph of proper size
        super(shape, Symbols.getSymbol(shape), interline, null);

        // Translation from generic center to actual center
        translate(GeoUtil.vectorOf(getAreaCenter(), center));
    }

    //~ Methods ----------------------------------------------------------------
    //----------//
    // colorize //
    //----------//
    @Override
    public void colorize (Color color)
    {
        // Nothing to colorize
    }

    //----------//
    // colorize //
    //----------//
    @Override
    public void colorize (Collection<Section> sections,
                          Color color)
    {
    }

    //----------//
    // isActive //
    //----------//
    /**
     * By definition a virtual glyph is always active
     *
     * @return true
     */
    @Override
    public boolean isActive ()
    {
        return true;
    }

    //-----------//
    // isVirtual //
    //-----------//
    @Override
    public boolean isVirtual ()
    {
        return true;
    }
}
