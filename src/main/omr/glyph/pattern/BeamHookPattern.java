//----------------------------------------------------------------------------//
//                                                                            //
//                       B e a m H o o k P a t t e r n                        //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright (C) Herve Bitteur 2000-2011. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.glyph.pattern;

import omr.glyph.Shape;
import omr.glyph.ShapeRange;
import omr.glyph.facets.Glyph;

import omr.log.Logger;

import omr.score.common.PixelRectangle;

import omr.sheet.SystemInfo;

import omr.util.HorizontalSide;

/**
 * Class {@code BeamHookPattern} removes beam hooks for which the
 * related stem has no beam on the same side.
 *
 * @author Hervé Bitteur
 */
public class BeamHookPattern
    extends GlyphPattern
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(
        BeamHookPattern.class);

    //~ Constructors -----------------------------------------------------------

    //-----------------//
    // BeamHookPattern //
    //-----------------//
    /**
     * Creates a new BeamHookPattern object.
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
            if (hook.getShape() == Shape.BEAM_HOOK) {
                if (hook.getStemNumber() != 1) {
                    if (hook.isVip() || logger.isFineEnabled()) {
                        logger.info(
                            hook.getStemNumber() + " stem(s) for beam hook #" +
                            hook.getId());
                    }

                    hook.setShape(null);
                    nb++;
                } else {
                    if (hook.isVip() || logger.isFineEnabled()) {
                        logger.info("Checking hook #" + hook.getId());
                    }

                    Glyph          stem = null;
                    HorizontalSide side = null;

                    for (HorizontalSide s : HorizontalSide.values()) {
                        side = s;
                        stem = hook.getStem(s);

                        if (stem != null) {
                            break;
                        }
                    }

                    // Look for other stuff on the stem
                    PixelRectangle stemBox = system.stemBoxOf(stem);
                    boolean        found = false;

                    for (Glyph g : system.lookupIntersectedGlyphs(
                        stemBox,
                        stem)) {
                        if (g == hook) {
                            continue;
                        }

                        // We look for  beam on same stem side
                        if ((g.getStem(side) == stem)) {
                            Shape shape = g.getShape();

                            if (ShapeRange.Beams.contains(shape) &&
                                (shape != Shape.BEAM_HOOK)) {
                                if (hook.isVip() || logger.isFineEnabled()) {
                                    logger.info(
                                        "Confirmed beam hook #" + hook.getId());
                                }

                                found = true;

                                break;
                            }
                        }
                    }

                    if (!found) {
                        // Deassign this hook w/ no beam neighbor
                        if (hook.isVip() || logger.isFineEnabled()) {
                            logger.info("Cancelled beam hook #" + hook.getId());
                        }

                        hook.setShape(null);
                        nb++;
                    }
                }
            }
        }

        return nb;
    }
}
