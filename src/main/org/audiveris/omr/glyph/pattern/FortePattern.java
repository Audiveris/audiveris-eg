//----------------------------------------------------------------------------//
//                                                                            //
//                          F o r t e P a t t e r n                           //
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

import org.audiveris.omr.glyph.CompoundBuilder;
import org.audiveris.omr.glyph.Evaluation;
import org.audiveris.omr.glyph.Grades;
import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.sheet.SystemInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Rectangle;
import java.util.EnumSet;

/**
 * Class {@code FortePattern} uses easily recognized Forte signs ("f") to
 * check the glyph next to them of the left, which is harder to recognize.
 * It can only be "m" (mezzo), "r" (rinforzando) or "s" (sforzando).
 *
 * @author Hervé Bitteur
 */
public class FortePattern
        extends GlyphPattern
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            FortePattern.class);

    /** Pre-forte shapes */
    public static final EnumSet<Shape> forteNeighbors = EnumSet.of(
            Shape.DYNAMICS_CHAR_M,
            Shape.DYNAMICS_CHAR_R,
            Shape.DYNAMICS_CHAR_S);

    //~ Constructors -----------------------------------------------------------
    //--------------//
    // FortePattern //
    //--------------//
    /**
     * Creates a new FortePattern object.
     *
     * @param system the system to process
     */
    public FortePattern (SystemInfo system)
    {
        super("Forte", system);
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // runPattern //
    //------------//
    @Override
    public int runPattern ()
    {
        int nb = 0;

        for (Glyph forte : system.getGlyphs()) {
            // Focus on forte shaped glyphs
            if (forte.getShape() == Shape.DYNAMICS_F) {
                Glyph compound = system.buildCompound(
                        forte,
                        false,
                        system.getGlyphs(),
                        new ForteAdapter(
                        system,
                        Grades.forteMinGrade,
                        forteNeighbors));

                if (compound != null) {
                    nb++;
                }
            }
        }

        return nb;
    }

    //~ Inner Classes ----------------------------------------------------------
    //---------------//
    // ForteAdapter //
    //---------------//
    /**
     * Adapter to actively search a Forte-compatible entity near the Forte glyph
     */
    private final class ForteAdapter
            extends CompoundBuilder.TopShapeAdapter
    {
        //~ Constructors -------------------------------------------------------

        public ForteAdapter (SystemInfo system,
                             double minGrade,
                             EnumSet<Shape> desiredShapes)
        {
            super(system, minGrade, desiredShapes);
        }

        //~ Methods ------------------------------------------------------------
        @Override
        public Rectangle computeReferenceBox ()
        {
            Rectangle rect = seed.getBounds();
            Rectangle leftBox = new Rectangle(
                    rect.x,
                    rect.y + (rect.height / 3),
                    rect.width / 3,
                    rect.height / 3);
            seed.addAttachment("fl", leftBox);

            return rect;
        }

        @Override
        public Evaluation getChosenEvaluation ()
        {
            return new Evaluation(chosenEvaluation.shape, Evaluation.ALGORITHM);
        }

        @Override
        public boolean isCandidateSuitable (Glyph glyph)
        {
            return true;
        }
    }
}
