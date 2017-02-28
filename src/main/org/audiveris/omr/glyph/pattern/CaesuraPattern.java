//----------------------------------------------------------------------------//
//                                                                            //
//                        C a e s u r a P a t t e r n                         //
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
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.score.entity.Measure;
import org.audiveris.omr.score.entity.ScoreSystem;
import org.audiveris.omr.score.entity.SystemPart;

import org.audiveris.omr.sheet.SystemInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;

/**
 * Class {@code CaesuraPattern} checks that a caesura in a measure
 * is not surrounded with chords.
 *
 * @author Hervé Bitteur
 */
public class CaesuraPattern
        extends GlyphPattern
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            CaesuraPattern.class);

    //~ Constructors -----------------------------------------------------------
    //----------------//
    // CaesuraPattern //
    //----------------//
    /**
     * Creates a new CaesuraPattern object.
     *
     * @param system the system to process
     */
    public CaesuraPattern (SystemInfo system)
    {
        super("Caesura", system);
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // runPattern //
    //------------//
    @Override
    public int runPattern ()
    {
        int nb = 0;
        ScoreSystem scoreSystem = system.getScoreSystem();

        for (Glyph glyph : system.getGlyphs()) {
            if ((glyph.getShape() != Shape.CAESURA) || glyph.isManualShape()) {
                continue;
            }

            Point center = glyph.getAreaCenter();
            SystemPart part = scoreSystem.getPartAt(center);
            Measure measure = part.getMeasureAt(center);

            if (!measure.getChords()
                    .isEmpty()) {
                if (glyph.isVip() || logger.isDebugEnabled()) {
                    logger.info("Cancelled caesura #{}", glyph.getId());
                }

                glyph.setShape(null);
                nb++;
            }
        }

        return nb;
    }
}
