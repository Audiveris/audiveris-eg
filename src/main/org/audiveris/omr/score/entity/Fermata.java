//----------------------------------------------------------------------------//
//                                                                            //
//                               F e r m a t a                                //
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

import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.score.visitor.ScoreVisitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;

/**
 * Class {@code Fermata} represents a fermata event (upright or
 * inverted)
 *
 * @author Hervé Bitteur
 */
public class Fermata
        extends AbstractNotation
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(Fermata.class);

    //~ Constructors -----------------------------------------------------------
    //---------//
    // Fermata //
    //---------//
    /**
     * Creates a new instance of Fermata event.
     *
     * @param measure measure that contains this mark
     * @param point   location of mark
     * @param chord   the chord related to the mark
     * @param glyph   the underlying glyph
     */
    public Fermata (Measure measure,
                    Point point,
                    Chord chord,
                    Glyph glyph)
    {
        super(measure, point, chord, glyph);
    }

    //~ Methods ----------------------------------------------------------------
    //----------//
    // populate //
    //----------//
    /**
     * Used by SystemTranslator to allocate the fermata marks.
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
            logger.info("Fermata. populate {}", glyph.idString());
        }

        // A Fermata relates to the note on the same time slot
        // With placement depending on fermata upright / inverted.
        // Beware of whole rests which are handled separately.
        //
        // TODO: Fermata is said to apply to barline as well, but this feature 
        // is not yet implemented.
        Chord chord;

        if (glyph.getShape() == Shape.FERMATA) {
            // Look for a chord below
            chord = measure.getClosestChordBelow(point);

            if (chord == null) {
                chord = measure.getClosestWholeChordBelow(point);
            }
        } else {
            // Look for a chord above
            chord = measure.getClosestChordAbove(point);

            if (chord == null) {
                chord = measure.getClosestWholeChordAbove(point);
            }
        }

        if (chord != null) {
            glyph.setTranslation(new Fermata(measure, point, chord, glyph));
        }
    }

    //--------//
    // accept //
    //--------//
    @Override
    public boolean accept (ScoreVisitor visitor)
    {
        return visitor.visit(this);
    }
}
