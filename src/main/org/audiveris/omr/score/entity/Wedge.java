//----------------------------------------------------------------------------//
//                                                                            //
//                                 W e d g e                                  //
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
package org.audiveris.omr.score.entity;

import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.score.visitor.ScoreVisitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Class {@code Wedge} represents a crescendo (&lt;) or a
 * decrescendo (&gt;).
 *
 * @author Hervé Bitteur
 */
public class Wedge
        extends AbstractDirection
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(Wedge.class);

    //~ Instance fields --------------------------------------------------------
    /** Vertical spread in units */
    private final int spread;

    //~ Constructors -----------------------------------------------------------
    //-------//
    // Wedge //
    //-------//
    /**
     * Creates a new instance of Wedge edge.
     * (there must be one for the wedge start, and one for the wedge stop).
     *
     * @param measure measure that contains this wedge edge
     * @param start   indicate a wedge start
     * @param point   middle point on wedge edge
     * @param chord   a related chord if any
     * @param glyph   the underlying glyph
     */
    public Wedge (Measure measure,
                  boolean start,
                  Point point,
                  Chord chord,
                  Glyph glyph)
    {
        super(measure, start, point, chord, glyph);

        // Spread
        if ((start && (getShape() == Shape.DECRESCENDO))
            || (!start && (getShape() == Shape.CRESCENDO))) {
            spread = glyph.getBounds().height;
        } else {
            spread = 0;
        }
    }

    //~ Methods ----------------------------------------------------------------
    //--------//
    // accept //
    //--------//
    @Override
    public boolean accept (ScoreVisitor visitor)
    {
        return visitor.visit(this);
    }

    //-----------//
    // getSpread //
    //-----------//
    /**
     * Report the vertical spread of the wedge
     *
     * @return vertical spread in units
     */
    public int getSpread ()
    {
        return spread;
    }

    //----------//
    // populate //
    //----------//
    /**
     * Used by SystemTranslator to allocate the wedges
     *
     * @param glyph           underlying glyph
     * @param startingMeasure measure where left side is located
     * @param startingPoint   location for left point
     */
    public static void populate (Glyph glyph,
                                 Measure startingMeasure,
                                 Point startingPoint)
    {
        if (glyph.isVip()) {
            logger.info("Wedge. populate {}", glyph.idString());
        }

        SystemPart part = startingMeasure.getPart();
        Rectangle box = glyph.getBounds();

        // Start
        glyph.setTranslation(
                new Wedge(
                startingMeasure,
                true,
                startingPoint,
                findChord(startingMeasure, startingPoint),
                glyph));

        // Stop
        Point endingPoint = new Point(
                box.x + box.width,
                box.y + (box.height / 2));
        Measure endingMeasure = part.getMeasureAt(endingPoint);
        glyph.addTranslation(
                new Wedge(
                endingMeasure,
                false,
                endingPoint,
                findChord(endingMeasure, endingPoint),
                glyph));
    }
}
