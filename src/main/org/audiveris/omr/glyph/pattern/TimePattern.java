//----------------------------------------------------------------------------//
//                                                                            //
//                           T i m e P a t t e r n                            //
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

import org.audiveris.omr.glyph.CompoundBuilder;
import org.audiveris.omr.glyph.Evaluation;
import org.audiveris.omr.glyph.Grades;
import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.ShapeSet;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.grid.StaffInfo;

import org.audiveris.omr.sheet.Scale;
import org.audiveris.omr.sheet.SystemInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.EnumSet;

/**
 * Class {@code TimePattern} verifies the time signature glyphs.
 *
 * @author Hervé Bitteur
 */
public class TimePattern
        extends GlyphPattern
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            TimePattern.class);

    //~ Instance fields --------------------------------------------------------
    /** Specific compound adapter. */
    private final TimeSigAdapter adapter;

    /** Scale-dependent parameters. */
    private final Parameters params;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new TimePattern object.
     *
     * @param system the containing system
     */
    public TimePattern (SystemInfo system)
    {
        super("Time", system);

        params = new Parameters(system.getSheet().getScale());

        adapter = new TimeSigAdapter(
                system,
                Grades.timeMinGrade,
                ShapeSet.FullTimes);
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // runPattern //
    //------------//
    /**
     * Check that each staff begins with a time
     *
     * @return the number of times rebuilt
     */
    @Override
    public int runPattern ()
    {
        int successNb = 0;

        for (Glyph glyph : system.getGlyphs()) {
            if (!ShapeSet.Times.contains(glyph.getShape())
                || glyph.isManualShape()) {
                continue;
            }

            // We must find a time out of these glyphs
            Glyph compound = system.buildCompound(
                    glyph,
                    true,
                    system.getGlyphs(),
                    adapter);

            if (compound != null) {
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

        Scale.Fraction xOffset = new Scale.Fraction(
                0.25d,
                "Core Time horizontal offset");

        Scale.Fraction yOffset = new Scale.Fraction(
                0.25d,
                "Core Time vertical offset");

        Scale.Fraction timeWidth = new Scale.Fraction(
                1.6,
                "Typical width of a time signature");

        Scale.Fraction maxTimeWidth = new Scale.Fraction(
                4,
                "Maximum width of a time signature");

        Scale.Fraction maxTimeHeight = new Scale.Fraction(
                8,
                "Maximum height of a time signature");

    }

    //------------//
    // Parameters //
    //------------//
    private static class Parameters
    {
        //~ Instance fields ----------------------------------------------------

        final int xOffset;

        final int yOffset;

        final int timeWidth;

        final int maxTimeWidth;

        final int maxTimeHeight;

        //~ Constructors -------------------------------------------------------
        public Parameters (Scale scale)
        {
            xOffset = scale.toPixels(constants.xOffset);
            yOffset = scale.toPixels(constants.yOffset);
            timeWidth = scale.toPixels(constants.timeWidth);
            maxTimeWidth = scale.toPixels(constants.maxTimeWidth);
            maxTimeHeight = scale.toPixels(constants.maxTimeHeight);
        }
    }

    //----------------//
    // TimeSigAdapter //
    //----------------//
    /**
     * Compound adapter to search for a time sig shape
     */
    private class TimeSigAdapter
            extends CompoundBuilder.TopRawAdapter
    {
        //~ Constructors -------------------------------------------------------

        public TimeSigAdapter (SystemInfo system,
                               double minGrade,
                               EnumSet<Shape> desiredShapes)
        {
            super(system, minGrade, desiredShapes);
        }

        //~ Methods ------------------------------------------------------------
        @Override
        public Rectangle computeReferenceBox ()
        {
            // Define the core box to intersect time glyph(s)
            Point center = seed.getAreaCenter();
            StaffInfo staff = system.getStaffAt(center);

            Rectangle rect = seed.getBounds();

            if (rect.width < params.timeWidth) {
                rect.grow(params.timeWidth - rect.width, 0);
            }

            rect.grow(-params.xOffset, 0);
            rect.y = staff.getFirstLine()
                    .yAt(center.x) + params.yOffset;
            rect.height = staff.getLastLine()
                    .yAt(center.x) - params.yOffset - rect.y;

            // Draw the time core box, for visual debug
            seed.addAttachment("t", rect);

            return rect;
        }

        @Override
        public Evaluation getChosenEvaluation ()
        {
            return new Evaluation(chosenEvaluation.shape, Evaluation.ALGORITHM);
        }

        @Override
        public boolean isCandidateClose (Glyph glyph)
        {
            if (super.isCandidateClose(glyph)) {
                // Check dimension of resulting bounds
                Rectangle result = glyph.getBounds()
                        .union(box);

                if ((result.width > params.maxTimeWidth)
                    || (result.height > params.maxTimeHeight)) {
                    logger.debug("Excluding too large {}", glyph);

                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean isCandidateSuitable (Glyph glyph)
        {
            return !glyph.isManualShape() && glyph.isActive();
        }
    }
}
