//----------------------------------------------------------------------------//
//                                                                            //
//                     A b s t r a c t D i r e c t i o n                      //
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
 * Class {@code Direction} is the basis for all variants of direction
 * indications: pedal, words, dynamics, wedge, dashes, etc...
 *
 * <p>For some directions (such as wedge, dashes, pedal), we may have two
 * "events": the starting event and the stopping event. Both will trigger the
 * creation of a Direction instance, the difference being made by the "start"
 * boolean.
 *
 * @author Hervé Bitteur
 */
public abstract class AbstractDirection
        extends MeasureElement
        implements Direction
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            AbstractDirection.class);

    //~ Constructors -----------------------------------------------------------
    /** Creates a new instance of Direction
     *
     * @param measure        the containing measure
     * @param referencePoint the reference point for this direction
     * @param chord          the related chord if any
     * @param glyph          the underlying glyph
     */
    public AbstractDirection (Measure measure,
                              Point referencePoint,
                              Chord chord,
                              Glyph glyph)
    {
        this(measure, true, referencePoint, chord, glyph);
    }

    /** Creates a new instance of Direction
     *
     * @param measure        the containing measure
     * @param isStart        true or false, to flag a start or a stop
     * @param referencePoint the reference point for this direction
     * @param chord          the related chord if any
     * @param glyph          the underlying glyph
     */
    public AbstractDirection (Measure measure,
                              boolean isStart,
                              Point referencePoint,
                              Chord chord,
                              Glyph glyph)
    {
        super(measure, isStart, referencePoint, chord, glyph);

        // Register at its related chord
        if (chord != null) {
            chord.addDirection(this);
        } else {
            // We have a direction item without any related chord/note
            // This is legal, however where do we store this item? TODO
            addError(glyph, "Direction with no related note");
        }
    }
}
