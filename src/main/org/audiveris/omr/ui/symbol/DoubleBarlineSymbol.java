//----------------------------------------------------------------------------//
//                                                                            //
//                   D o u b l e B a r l i n e S y m b o l                    //
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
import java.awt.geom.Rectangle2D;

/**
 * Class {@code DoubleBarlineSymbol} displays two thin barlines
 *
 * @author Hervé Bitteur
 */
public class DoubleBarlineSymbol
        extends ShapeSymbol
{
    //~ Static fields/initializers ---------------------------------------------

    // Total width, computed from width of thin barline
    private static final double WIDTH_RATIO = 4.5;

    //~ Instance fields --------------------------------------------------------
    // The thin barline symbol
    private final ShapeSymbol thinSymbol = Symbols.getSymbol(
            Shape.THIN_BARLINE);

    //~ Constructors -----------------------------------------------------------
    //---------------------//
    // DoubleBarlineSymbol //
    //---------------------//
    /**
     * Create a DoubleBarlineSymbol
     *
     * @param isIcon true for an icon
     */
    public DoubleBarlineSymbol (boolean isIcon)
    {
        super(isIcon, Shape.DOUBLE_BARLINE, false);
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // createIcon //
    //------------//
    @Override
    protected ShapeSymbol createIcon ()
    {
        return new DoubleBarlineSymbol(true);
    }

    //-----------//
    // getParams //
    //-----------//
    @Override
    protected Params getParams (MusicFont font)
    {
        Params p = new Params();

        p.layout = font.layout(thinSymbol);

        Rectangle2D thinRect = p.layout.getBounds();
        p.rect = new Rectangle(
                (int) Math.ceil(thinRect.getWidth() * WIDTH_RATIO),
                (int) Math.ceil(thinRect.getHeight()));

        return p;
    }

    //-------//
    // paint //
    //-------//
    @Override
    protected void paint (Graphics2D g,
                          Params p,
                          Point location,
                          Alignment alignment)
    {
        Point loc = alignment.translatedPoint(TOP_LEFT, p.rect, location);
        MusicFont.paint(g, p.layout, loc, TOP_LEFT);
        loc.x += p.rect.width;
        MusicFont.paint(g, p.layout, loc, TOP_RIGHT);
    }
}
