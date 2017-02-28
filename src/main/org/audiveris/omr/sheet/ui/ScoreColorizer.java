//----------------------------------------------------------------------------//
//                                                                            //
//                        S c o r e C o l o r i z e r                         //
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
package org.audiveris.omr.sheet.ui;

import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.score.Score;
import org.audiveris.omr.score.entity.Barline;
import org.audiveris.omr.score.entity.SystemPart;
import org.audiveris.omr.score.visitor.AbstractScoreVisitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;

/**
 * Class {@code ScoreColorizer} can visit the score hierarchy for
 * colorization (assigning colors) of related sections in the Sheet display.
 *
 * @author Hervé Bitteur
 */
public class ScoreColorizer
        extends AbstractScoreVisitor
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            ScoreColorizer.class);

    //~ Instance fields --------------------------------------------------------
    /** The color to use */
    private final Color color;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new ScoreColorizer object.
     *
     * @param color the color to use
     */
    public ScoreColorizer (Color color)
    {
        this.color = color;
    }

    //~ Methods ----------------------------------------------------------------
    //---------------//
    // visit Barline //
    //---------------//
    @Override
    public boolean visit (Barline barline)
    {
        try {
            logger.debug("Colorizing {}", barline);

            for (Glyph glyph : barline.getGlyphs()) {
                glyph.colorize(color);
            }
        } catch (Exception ex) {
            logger.warn(
                    getClass().getSimpleName() + " Error visiting " + barline,
                    ex);
        }

        return true;
    }

    //-------------//
    // visit Score //
    //-------------//
    @Override
    public boolean visit (Score score)
    {
        try {
            logger.debug("Colorizing score ...");
            score.acceptChildren(this);
        } catch (Exception ex) {
            logger.warn(
                    getClass().getSimpleName() + " Error visiting " + score,
                    ex);
        }

        return false;
    }

    //------------------//
    // visit SystemPart //
    //------------------//
    @Override
    public boolean visit (SystemPart part)
    {
        try {
            // Set color for the starting bar line, if any
            Barline startingBarline = part.getStartingBarline();

            if (startingBarline != null) {
                startingBarline.accept(this);
            }
        } catch (Exception ex) {
            logger.warn(
                    getClass().getSimpleName() + " Error visiting " + part,
                    ex);
        }

        return true;
    }
}
