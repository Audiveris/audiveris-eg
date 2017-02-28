//----------------------------------------------------------------------------//
//                                                                            //
//                           F l a g P a t t e r n                            //
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

import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.ShapeSet;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.sheet.Scale;
import org.audiveris.omr.sheet.SystemInfo;

import org.audiveris.omr.util.HorizontalSide;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Rectangle;

/**
 * Class {@code FlagPattern} removes flags for which the related stem
 * has no attached head (or at least some significant no-shape stuff).
 *
 * @author Hervé Bitteur
 */
public class FlagPattern
        extends GlyphPattern
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            FlagPattern.class);

    //~ Constructors -----------------------------------------------------------
    //-------------//
    // FlagPattern //
    //-------------//
    /**
     * Creates a new FlagPattern object.
     *
     * @param system the system to process
     */
    public FlagPattern (SystemInfo system)
    {
        super("Flag", system);
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // runPattern //
    //------------//
    @Override
    public int runPattern ()
    {
        int nb = 0;

        for (Glyph flag : system.getGlyphs()) {
            if (!ShapeSet.Flags.contains(flag.getShape())
                || flag.isManualShape()) {
                continue;
            }

            if (flag.isVip() || logger.isDebugEnabled()) {
                logger.info("Checking flag #{}", flag.getId());
            }

            Glyph stem = flag.getStem(HorizontalSide.LEFT);

            if (stem == null) {
                if (flag.isVip() || logger.isDebugEnabled()) {
                    logger.info("No left stem for flag #{}", flag.getId());
                }

                flag.setShape(null);
                nb++;

                break;
            }

            // Look for other stuff on the stem, whatever the side
            Rectangle stemBox = system.stemBoxOf(stem);
            boolean found = false;

            for (Glyph g : system.lookupIntersectedGlyphs(stemBox, stem, flag)) {
                // We are looking for head (or some similar large stuff)
                Shape shape = g.getShape();

                if (ShapeSet.NoteHeads.contains(shape)
                    || ((shape == null)
                        && (g.getNormalizedWeight() >= constants.minStuffWeight.getValue()))) {
                    if (flag.isVip() || logger.isDebugEnabled()) {
                        logger.info("Confirmed flag #{}", flag.getId());
                    }

                    found = true;

                    break;
                }
            }

            if (!found) {
                // Deassign this flag w/ no head neighbor
                if (flag.isVip() || logger.isDebugEnabled()) {
                    logger.info("Cancelled flag #{}", flag.getId());
                }

                flag.setShape(null);
                nb++;
            }
        }

        return nb;
    }

    //~ Inner Classes ----------------------------------------------------------
    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
            extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        //
        Scale.AreaFraction minStuffWeight = new Scale.AreaFraction(
                0.5,
                "Minimum weight for a stem stuff");

    }
}
