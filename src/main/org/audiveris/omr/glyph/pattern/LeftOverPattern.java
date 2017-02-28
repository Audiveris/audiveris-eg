//----------------------------------------------------------------------------//
//                                                                            //
//                       L e f t O v e r P a t t e r n                        //
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

import org.audiveris.omr.constant.ConstantSet;

import org.audiveris.omr.glyph.Evaluation;
import org.audiveris.omr.glyph.GlyphNetwork;
import org.audiveris.omr.glyph.Grades;
import org.audiveris.omr.glyph.ShapeEvaluator;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.sheet.Scale;
import org.audiveris.omr.sheet.SystemInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class {@code LeftOverPattern} processes the significant glyphs
 * which have been left over.
 * It addresses glyphs of non-assigned shape with significant weight, and
 * assigns them the top 1 shape.
 *
 * @author Hervé Bitteur
 */
public class LeftOverPattern
        extends GlyphPattern
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            LeftOverPattern.class);

    //~ Constructors -----------------------------------------------------------
    //-----------------//
    // LeftOverPattern //
    //-----------------//
    /**
     * Creates a new LeftOverPattern object.
     *
     * @param system the containing system
     */
    public LeftOverPattern (SystemInfo system)
    {
        super("LeftOver", system);
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // runPattern //
    //------------//
    @Override
    public int runPattern ()
    {
        int successNb = 0;
        final double minWeight = constants.minWeight.getValue();
        final ShapeEvaluator evaluator = GlyphNetwork.getInstance();

        for (Glyph glyph : system.getGlyphs()) {
            if (glyph.isKnown()
                || glyph.isManualShape()
                || (glyph.getNormalizedWeight() < minWeight)) {
                continue;
            }

            Evaluation vote = evaluator.vote(
                    glyph,
                    system,
                    Grades.leftOverMinGrade);

            if (vote != null) {
                glyph = system.addGlyph(glyph);
                glyph.setEvaluation(vote);

                if (logger.isDebugEnabled() || glyph.isVip()) {
                    logger.info("LeftOver {} vote: {}", glyph.idString(), vote);
                }

                successNb++;
            }
        }

        return successNb;
    }

    //~ Inner Classes ----------------------------------------------------------
    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
            extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        Scale.AreaFraction minWeight = new Scale.AreaFraction(
                0.3,
                "Minimum normalized weight to be a left over glyph");

    }
}
