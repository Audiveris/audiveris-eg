//----------------------------------------------------------------------------//
//                                                                            //
//                                 G l y p h                                  //
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
package org.audiveris.omr.glyph.facets;

import org.audiveris.omr.check.Checkable;

import java.awt.Point;
import java.util.Comparator;

/**
 * Interface {@code Glyph} represents any glyph found, such as stem,
 * ledger, accidental, note head, word, text line, etc...
 *
 * <p>A Glyph is basically a collection of sections. It can be split into
 * smaller glyphs, which may later be re-assembled into another instance of
 * glyph. There is a means, based on a simple signature (weight and moments)
 * to detect if the glyph at hand is identical to a previous one, which is
 * then reused.
 *
 * <p>A Glyph can be stored on disk and reloaded in order to train a glyph
 * evaluator.
 *
 * @author Hervé Bitteur
 */
public interface Glyph
        extends
        /** For handling check results */
        Checkable,
        /** For id and related lag */
        GlyphAdministration,
        /** For member sections */
        GlyphComposition,
        /** For display color */
        GlyphDisplay,
        /** For items nearby */
        GlyphEnvironment,
        /** For physical appearance */
        GlyphGeometry,
        /** For shape assignment */
        GlyphRecognition,
        /** For translation to score items */
        GlyphTranslation,
        /** For mean line */
        GlyphAlignment,
        /** For textual content */
        GlyphContent
{
    //~ Static fields/initializers ---------------------------------------------

    /** For comparing glyphs according to their height. */
    public static final Comparator<Glyph> byHeight = new Comparator<Glyph>()
    {
        @Override
        public int compare (Glyph o1,
                            Glyph o2)
        {
            return o1.getBounds().height - o2.getBounds().height;
        }
    };

    /** For comparing glyphs according to their decreasing weight. */
    public static final Comparator<Glyph> byReverseWeight = new Comparator<Glyph>()
    {
        @Override
        public int compare (Glyph o1,
                            Glyph o2)
        {
            return o2.getWeight() - o1.getWeight();
        }
    };

    /** For comparing glyphs according to their id. */
    public static final Comparator<Glyph> byId = new Comparator<Glyph>()
    {
        @Override
        public int compare (Glyph o1,
                            Glyph o2)
        {
            return o1.getId() - o2.getId();
        }
    };

    /** For comparing glyphs according to their abscissa,
     * then ordinate, then id. */
    public static final Comparator<Glyph> byAbscissa = new Comparator<Glyph>()
    {
        @Override
        public int compare (Glyph o1,
                            Glyph o2)
        {
            if (o1 == o2) {
                return 0;
            }

            Point ref = o1.getBounds()
                    .getLocation();
            Point otherRef = o2.getBounds()
                    .getLocation();

            // Are x values different?
            int dx = ref.x - otherRef.x;

            if (dx != 0) {
                return dx;
            }

            // Vertically aligned, so use ordinates
            int dy = ref.y - otherRef.y;

            if (dy != 0) {
                return dy;
            }

            // Finally, use id ...
            return o1.getId() - o2.getId();
        }
    };

    /** For comparing glyphs according to their ordinate,
     * then abscissa, then id. */
    public static final Comparator<Glyph> ordinateComparator = new Comparator<Glyph>()
    {
        @Override
        public int compare (Glyph o1,
                            Glyph o2)
        {
            if (o1 == o2) {
                return 0;
            }

            Point ref = o1.getBounds()
                    .getLocation();
            Point otherRef = o2.getBounds()
                    .getLocation();

            // Are y values different?
            int dy = ref.y - otherRef.y;

            if (dy != 0) {
                return dy;
            }

            // Horizontally aligned, so use abscissae
            int dx = ref.x - otherRef.x;

            if (dx != 0) {
                return dx;
            }

            // Finally, use id ...
            return o1.getId() - o2.getId();
        }
    };

}
