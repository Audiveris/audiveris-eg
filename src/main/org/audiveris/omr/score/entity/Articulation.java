//----------------------------------------------------------------------------//
//                                                                            //
//                          A r t i c u l a t i o n                           //
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

import org.audiveris.omr.constant.ConstantSet;

import org.audiveris.omr.glyph.Evaluation;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.score.visitor.ScoreVisitor;

import org.audiveris.omr.sheet.Scale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;

/**
 * Class {@code Articulation} represents an articulation event, a
 * special notation.
 * This should apply to:
 * <pre>
 * accent               standard
 * strong-accent        standard
 * staccato             standard
 * tenuto               standard
 * detached-legato      nyi
 * staccatissimo        standard
 * spiccato             nyi
 * scoop                nyi
 * plop                 nyi
 * doit                 nyi
 * falloff              nyi
 * breath-mark          nyi ???
 * caesura              nyi ???
 * stress               nyi
 * unstress             nyi
 * other-articulation   nyi
 * </pre>
 *
 * @author Hervé Bitteur
 */
public class Articulation
        extends AbstractNotation
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            Articulation.class);

    //~ Constructors -----------------------------------------------------------
    //--------------//
    // Articulation //
    //--------------//
    /**
     * Creates a new instance of Articulation event
     *
     * @param measure measure that contains this mark
     * @param point   location of mark
     * @param chord   the chord related to the mark
     * @param glyph   the underlying glyph
     */
    public Articulation (Measure measure,
                         Point point,
                         Chord chord,
                         Glyph glyph)
    {
        super(measure, point, chord, glyph);
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
     * Used by SystemTranslator
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
            logger.info("Articulation. populate {}", glyph.idString());
        }

        // An Articulation relates to the note below or above on the same time slot
        Chord chord = measure.getEventChord(point);

        if (chord != null) {
            // Check vertical distance between chord and articulation
            Point head = chord.getHeadLocation();
            Point tail = chord.getTailLocation();
            int dy = Math.min(
                    Math.abs(head.y - point.y),
                    Math.abs(tail.y - point.y));
            double normedDy = measure.getScale()
                    .pixelsToFrac(dy);

            if (normedDy <= constants.maxArticulationDy.getValue()) {
                glyph.setTranslation(
                        new Articulation(measure, point, chord, glyph));

                if (glyph.isVip() || logger.isDebugEnabled()) {
                    logger.info("Translated articulation {}", glyph.idString());
                }

                return;
            }
        }

        // Incorrect articulation
        if (glyph.isVip() || logger.isDebugEnabled()) {
            logger.info(
                    "No chord close enough to articulation {}",
                    glyph.idString());
        }

        glyph.setShape(null, Evaluation.ALGORITHM);
    }

    //~ Inner Classes ----------------------------------------------------------
    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
            extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        /**
         * Maximum dy between articulation and chord
         */
        Scale.Fraction maxArticulationDy = new Scale.Fraction(
                4,
                "Maximum dy between articulation and chord");

    }
}
