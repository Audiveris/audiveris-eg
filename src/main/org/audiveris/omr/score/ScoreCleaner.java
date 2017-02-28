//----------------------------------------------------------------------------//
//                                                                            //
//                          S c o r e C l e a n e r                           //
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
package org.audiveris.omr.score;

import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.score.entity.Measure;
import org.audiveris.omr.score.entity.ScoreSystem;
import org.audiveris.omr.score.entity.SystemPart;
import org.audiveris.omr.score.visitor.AbstractScoreVisitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class {@code ScoreCleaner} can visit the score hierarchy to get rid
 * of all measure items except barlines, ready for a new score
 * translation.
 *
 * @author Hervé Bitteur
 */
public class ScoreCleaner
        extends AbstractScoreVisitor
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            ScoreCleaner.class);

    //~ Constructors -----------------------------------------------------------
    //--------------//
    // ScoreCleaner //
    //--------------//
    /**
     * Creates a new ScoreCleaner object.
     */
    public ScoreCleaner ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //--------------//
    // visit System //
    //--------------//
    @Override
    public boolean visit (ScoreSystem system)
    {
        try {
            logger.debug("Cleaning up {}", system);

            // Remove recorded translations for all system glyphs
            for (Glyph glyph : system.getInfo()
                    .getGlyphs()) {
                if (glyph.getShape() != Shape.LEDGER) {
                    glyph.clearTranslations();
                }
            }

            system.acceptChildren(this);
        } catch (Exception ex) {
            logger.warn(
                    getClass().getSimpleName() + " Error visiting " + system,
                    ex);
        }

        return false;
    }

    //------------------//
    // visit SystemPart //
    //------------------//
    @Override
    public boolean visit (SystemPart systemPart)
    {
        try {
            if (systemPart.isDummy()) {
                systemPart.getParent()
                        .getChildren()
                        .remove(systemPart);

                return false;
            } else {
                // Remove slurs and wedges
                systemPart.cleanupNode();

                return true;
            }
        } catch (Exception ex) {
            logger.warn(
                    getClass().getSimpleName() + " Error visiting " + systemPart,
                    ex);
        }

        return false;
    }

    //---------------//
    // visit Measure //
    //---------------//
    @Override
    public boolean visit (Measure measure)
    {
        try {
            measure.cleanupNode();
        } catch (Exception ex) {
            logger.warn(
                    getClass().getSimpleName() + " Error visiting " + measure,
                    ex);
        }

        return false;
    }
}
