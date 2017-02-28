//----------------------------------------------------------------------------//
//                                                                            //
//                      A b s t r a c t N o t a t i o n                       //
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

import org.audiveris.omr.glyph.facets.Glyph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;

/**
 * Class {@code Notation} is the basis for all variants of notations:
 * tied, slur, ...
 *
 * @author Hervé Bitteur
 */
public abstract class AbstractNotation
        extends MeasureElement
        implements Notation
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            AbstractNotation.class);

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new instance of a simple Notation (assumed to be both the
     * start and the stop)
     *
     * @param measure the containing measure
     * @param point
     * @param chord   the related chord
     * @param glyph   the underlying glyph
     */
    public AbstractNotation (Measure measure,
                             Point point,
                             Chord chord,
                             Glyph glyph)
    {
        super(measure, true, point, chord, glyph);

        // Register at its related chord
        if (chord != null) {
            chord.addNotation(this);
        } else {
            // We have a notation item without any related chord/note
            addError(glyph, "Notation " + this + " without related note");
        }
    }
}
