//----------------------------------------------------------------------------//
//                                                                            //
//                     T r a n s f o r m e d S y m b o l                      //
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
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Class {@code TransformedSymbol} displays a baseShape symbol with the
 * application
 * on an AffineTransform
 */
public class TransformedSymbol
        extends ShapeSymbol
{
    //~ Instance fields --------------------------------------------------------

    /** The baseShape shape */
    protected final Shape baseShape;

    /** The baseShape symbol */
    protected final ShapeSymbol baseSymbol;

    /** Proper transformation */
    private final AffineTransform at;

    //~ Constructors -----------------------------------------------------------
    //-------------------//
    // TransformedSymbol //
    //-------------------//
    /**
     * Creates a new TransformedSymbol object.
     *
     * @param isIcon    true for an icon
     * @param shape     the related shape
     * @param baseShape the baseShape shape which is reused
     * @param at        the AffineTransform to apply
     */
    public TransformedSymbol (boolean isIcon,
                              Shape shape,
                              Shape baseShape,
                              AffineTransform at)
    {
        super(isIcon, shape, false);
        this.baseShape = baseShape;
        this.baseSymbol = Symbols.getSymbol(baseShape);
        this.at = at;
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // createIcon //
    //------------//
    @Override
    protected ShapeSymbol createIcon ()
    {
        return new TransformedSymbol(true, shape, baseShape, at);
    }

    //-----------//
    // getParams //
    //-----------//
    @Override
    protected Params getParams (MusicFont font)
    {
        Params p = new Params();

        p.layout = font.layout(baseSymbol.getString(), at);

        Rectangle2D r = p.layout.getBounds();
        p.rect = new Rectangle(
                (int) Math.ceil(r.getWidth()),
                (int) Math.ceil(r.getHeight()));

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
    }
}
