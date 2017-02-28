//----------------------------------------------------------------------------//
//                                                                            //
//                      E v a l u a t i o n E n g i n e                       //
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
package org.audiveris.omr.glyph;

import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.math.NeuralNetwork;

import java.util.Collection;

/**
 * Interface {@code EvaluationEngine} describes the life-cycle of an
 * evaluation engine.
 *
 * @author Hervé Bitteur
 */
public interface EvaluationEngine
        extends ShapeEvaluator
{
    //~ Enumerations -----------------------------------------------------------

    /** The various modes for starting the training of an evaluator. */
    public static enum StartingMode
    {
        //~ Enumeration constant initializers ----------------------------------

        /** Start with the current values. */
        INCREMENTAL,
        /** Start from
         * scratch, with new initial values. */
        SCRATCH;

    }

    //~ Methods ----------------------------------------------------------------
    /**
     * Dump the internals of the engine.
     */
    void dump ();

    /**
     * Store the engine in XML format.
     */
    void marshal ();

    /**
     * Stop the on-going training.
     */
    void stop ();

    /**
     * Train the evaluator on the provided base of sample glyphs.
     *
     * @param base    the collection of glyphs to train the evaluator
     * @param monitor a monitoring interface
     * @param mode    specify the starting mode of the training session
     */
    void train (Collection<Glyph> base,
                Monitor monitor,
                StartingMode mode);

    //~ Inner Interfaces -------------------------------------------------------
    /**
     * General monitoring interface to pass information about the
     * training of an evaluator when processing a sample glyph.
     */
    public static interface Monitor
            extends NeuralNetwork.Monitor
    {
        //~ Methods ------------------------------------------------------------

        /**
         * Entry called when a glyph is being processed.
         *
         * @param glyph the sample glyph being processed
         */
        void glyphProcessed (Glyph glyph);
    }
}
