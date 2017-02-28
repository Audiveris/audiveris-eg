//----------------------------------------------------------------------------//
//                                                                            //
//                                 P e d a l                                  //
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

/**
 * Class {@code Pedal} represents a pedal (start) or pedal up (stop) event
 *
 * @author Hervé Bitteur
 */
public class Pedal
        extends AbstractDirection
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(Pedal.class);

    //~ Constructors -----------------------------------------------------------
    //-------//
    // Pedal //
    //-------//
    /**
     * Creates a new instance of Pedal event
     *
     * @param measure measure that contains this mark
     * @param point   location of mark
     * @param chord   the chord related to the mark, if any
     * @param glyph   the underlying glyph
     */
    public Pedal (Measure measure,
                  Point point,
                  Chord chord,
                  Glyph glyph)
    {
        super(
                measure,
                glyph.getShape() == Shape.PEDAL_MARK,
                point,
                chord,
                glyph);
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

    //----------//
    // populate //
    //----------//
    /**
     * Used by SystemTranslator to allocate the pedal marks
     *
     * @param glyph   underlying glyph
     * @param measure measure where the mark is located
     * @param point   location for the mark
     */
    public static void populate (Glyph glyph,
                                 Measure measure,
                                 Point point)
    {
        if (glyph.isVip()) {
            logger.info("Pedal. populate {}", glyph.idString());
        }

        glyph.setTranslation(
                new Pedal(measure, point, findChord(measure, point), glyph));
    }
}
