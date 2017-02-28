//----------------------------------------------------------------------------//
//                                                                            //
//                                   R o i                                    //
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
package org.audiveris.omr.lag;

import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.math.Histogram;

import org.audiveris.omr.run.Orientation;
import org.audiveris.omr.run.RunsTable;

import java.awt.Rectangle;
import java.util.Collection;

/**
 * Interface {@code Roi} defines aan absolute rectangular region of
 * interest, on which histograms can be computed vertically and
 * horizontally.
 *
 * @author Hervé Bitteur
 */
public interface Roi
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Report the rectangular contour, in absolute coordinates
     *
     * @return the absolute contour
     */
    Rectangle getAbsoluteContour ();

    /**
     * Report the histogram obtained in the provided projection orientation
     * of the runs contained in the provided glyphs
     *
     * @param projection the orientation of the projection
     * @param glyphs     the provided glyphs (which can contain sections of
     *                   various
     *                   orientations)
     * @return the computed histogram
     */
    Histogram<Integer> getGlyphHistogram (Orientation projection,
                                          Collection<Glyph> glyphs);

    /**
     * Report the histogram obtained in the provided projection orientation
     * of the runs contained in the provided runs table
     *
     * @param projection the orientation of the projection
     * @param table      the runs table
     * @return the computed histogram
     */
    Histogram<Integer> getRunHistogram (Orientation projection,
                                        RunsTable table);

    /**
     * Report the histogram obtained in the provided projection orientation
     * of the runs contained in the provided sections
     *
     * @param projection the orientation of the projection
     * @param sections   the provided sections (which can be of various
     *                   orientations)
     * @return the computed histogram
     */
    Histogram<Integer> getSectionHistogram (Orientation projection,
                                            Collection<Section> sections);
}
