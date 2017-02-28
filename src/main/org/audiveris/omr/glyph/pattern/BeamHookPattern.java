//----------------------------------------------------------------------------//
//                                                                            //
//                       B e a m H o o k P a t t e r n                        //
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

import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.ShapeSet;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.sheet.SystemInfo;

import org.audiveris.omr.util.HorizontalSide;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Rectangle;

/**
 * Class {@code BeamHookPattern} removes beam hooks for which the
 * related stem has no beam on the same horizontal side of the stem
 * and on the same vertical end of the stem.
 *
 * @author Hervé Bitteur
 */
public class BeamHookPattern
        extends GlyphPattern
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            BeamHookPattern.class);

    //~ Constructors -----------------------------------------------------------
    //-----------------//
    // BeamHookPattern //
    //-----------------//
    /**
     * Creates a new BeamHookPattern object.
     *
     * @param system the system to process
     */
    public BeamHookPattern (SystemInfo system)
    {
        super("BeamHook", system);
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // runPattern //
    //------------//
    @Override
    public int runPattern ()
    {
        int nb = 0;

        for (Glyph hook : system.getGlyphs()) {
            if ((hook.getShape() != Shape.BEAM_HOOK) || hook.isManualShape()) {
                continue;
            }

            if (hook.getStemNumber() != 1) {
                if (hook.isVip() || logger.isDebugEnabled()) {
                    logger.info(
                            "{} stem(s) for beam hook #{}",
                            hook.getStemNumber(),
                            hook.getId());
                }

                hook.setShape(null);
                nb++;
            } else {
                if (hook.isVip() || logger.isDebugEnabled()) {
                    logger.info("Checking hook #{}", hook.getId());
                }

                Glyph stem = null;
                HorizontalSide side = null;

                for (HorizontalSide s : HorizontalSide.values()) {
                    side = s;
                    stem = hook.getStem(s);

                    if (stem != null) {
                        break;
                    }
                }

                int hookDy = hook.getCentroid().y - stem.getCentroid().y;

                // Look for other stuff on the stem
                Rectangle stemBox = system.stemBoxOf(stem);
                boolean beamFound = false;

                for (Glyph g : system.lookupIntersectedGlyphs(
                        stemBox,
                        stem,
                        hook)) {
                    // We look for a beam on the same stem side
                    if ((g.getStem(side) == stem)) {
                        Shape shape = g.getShape();

                        if (ShapeSet.Beams.contains(shape)
                            && (shape != Shape.BEAM_HOOK)) {
                            if (hook.isVip() || logger.isDebugEnabled()) {
                                logger.info(
                                        "Confirmed beam hook #{}",
                                        hook.getId());
                            }

                            beamFound = true;

                            break;
                        }
                    }
                }

                if (!beamFound) {
                    // Deassign this hook w/ no beam neighbor
                    if (hook.isVip() || logger.isDebugEnabled()) {
                        logger.info("Cancelled beam hook #{}", hook.getId());
                    }

                    hook.setShape(null);
                    nb++;
                }
            }
        }

        return nb;
    }
}
