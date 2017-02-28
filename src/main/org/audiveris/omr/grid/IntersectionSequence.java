//----------------------------------------------------------------------------//
//                                                                            //
//                  I n t e r s e c t i o n S e q u e n c e                   //
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
package org.audiveris.omr.grid;

import org.audiveris.omr.glyph.facets.Glyph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * Class {@code IntersectionSequence} handles a sorted sequence of
 * sticks intersections.
 *
 * @author Hervé Bitteur
 */
class IntersectionSequence
        extends TreeSet<StickIntersection>
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            IntersectionSequence.class);

    //~ Constructors -----------------------------------------------------------
    //----------------------//
    // IntersectionSequence //
    //----------------------//
    /**
     * Creates a new IntersectionSequence object.
     *
     * @param comparator the comparator (hori or vert) to use for the sequence
     */
    public IntersectionSequence (
            Comparator<? super StickIntersection> comparator)
    {
        super(comparator);
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // getSticks //
    //-----------//
    public List<Glyph> getSticks ()
    {
        return StickIntersection.sticksOf(this);
    }

    //--------//
    // reduce //
    //--------//
    public void reduce (double maxDeltaPos)
    {
        // If 2 sticks are close in position, simply merge them
        for (Iterator<StickIntersection> headIt = iterator(); headIt.hasNext();) {
            StickIntersection head = headIt.next();

            for (StickIntersection tail : tailSet(head, false)) {
                if (tail.getStickAncestor() == head.getStickAncestor()) {
                    continue;
                }

                if ((tail.x - head.x) <= maxDeltaPos) {
                    if (logger.isDebugEnabled()
                        || head.getStickAncestor()
                            .isVip()
                        || tail.getStickAncestor()
                            .isVip()) {
                        logger.info("Merging verticals {} & {}", head, tail);
                    }

                    Glyph tailAncestor = tail.getStickAncestor();
                    tailAncestor.stealSections(head.getStickAncestor());
                    headIt.remove();

                    break;
                }
            }
        }
    }
}
