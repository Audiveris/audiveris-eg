//----------------------------------------------------------------------------//
//                                                                            //
//                           B r a c e S y m b o l                            //
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
package org.audiveris.omr.ui.symbol;

import org.audiveris.omr.glyph.Shape;
import static org.audiveris.omr.ui.symbol.Alignment.*;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Class {@code BraceSymbol} displays a BRACE symbol: {
 *
 * @author Hervé Bitteur
 */
public class BraceSymbol
        extends ShapeSymbol
{
    //~ Static fields/initializers ---------------------------------------------

    // The upper part symbol
    private static final BasicSymbol upperSymbol = Symbols.SYMBOL_BRACE_UPPER_HALF;

    // The lower part symbol
    private static final BasicSymbol lowerSymbol = Symbols.SYMBOL_BRACE_LOWER_HALF;

    //~ Constructors -----------------------------------------------------------
    //-------------//
    // BraceSymbol //
    //-------------//
    /**
     * Create a BraceSymbol (which is mad of upper and lower parts)
     *
     * @param isIcon true for an icon
     */
    public BraceSymbol (boolean isIcon)
    {
        super(isIcon, Shape.BRACE, false);
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // createIcon //
    //------------//
    @Override
    protected ShapeSymbol createIcon ()
    {
        return new BraceSymbol(true);
    }

    //-----------//
    // getParams //
    //-----------//
    @Override
    protected Params getParams (MusicFont font)
    {
        MyParams p = new MyParams();

        AffineTransform at = isIcon ? tiny : null;
        p.upperLayout = font.layout(upperSymbol.getString(), at);
        p.lowerLayout = font.layout(lowerSymbol.getString(), at);

        Rectangle2D r = p.upperLayout.getBounds();
        p.rect = new Rectangle(
                (int) Math.ceil(r.getWidth()),
                (int) Math.ceil(2 * r.getHeight()));

        return p;
    }

    //-------//
    // paint //
    //-------//
    @Override
    protected void paint (Graphics2D g,
                          Params params,
                          Point location,
                          Alignment alignment)
    {
        MyParams p = (MyParams) params;
        Point loc = alignment.translatedPoint(MIDDLE_LEFT, p.rect, location);
        MusicFont.paint(g, p.upperLayout, loc, BOTTOM_LEFT);
        MusicFont.paint(g, p.lowerLayout, loc, TOP_LEFT);
    }

    //~ Inner Classes ----------------------------------------------------------
    //--------//
    // Params //
    //--------//
    protected class MyParams
            extends Params
    {
        //~ Instance fields ----------------------------------------------------

        TextLayout upperLayout;

        TextLayout lowerLayout;

    }
}
