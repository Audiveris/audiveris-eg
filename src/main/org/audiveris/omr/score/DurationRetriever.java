//----------------------------------------------------------------------------//
//                                                                            //
//                     D u r a t i o n R e t r i e v e r                      //
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

import org.audiveris.omr.math.Rational;

import org.audiveris.omr.score.entity.Chord;
import org.audiveris.omr.score.entity.Measure;
import org.audiveris.omr.score.entity.MeasureId.PageBased;
import org.audiveris.omr.score.entity.Page;
import org.audiveris.omr.score.entity.ScoreSystem;
import org.audiveris.omr.score.entity.Slot;
import org.audiveris.omr.score.entity.TimeSignature.InvalidTimeSignature;
import org.audiveris.omr.score.visitor.AbstractScoreVisitor;

import org.audiveris.omr.util.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Class {@code DurationRetriever} can visit a page hierarchy to compute
 * the actual duration of every measure
 *
 * @author Hervé Bitteur
 */
public class DurationRetriever
        extends AbstractScoreVisitor
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            DurationRetriever.class);

    //~ Instance fields --------------------------------------------------------
    //
    /** Map of Measure id -> Measure duration, whatever the containing part */
    private final Map<PageBased, Rational> measureDurations = new HashMap<>();

    /** Pass number, since we need 2 passes per system */
    private int pass = 1;

    //~ Constructors -----------------------------------------------------------
    //
    //-------------------//
    // DurationRetriever //
    //-------------------//
    /**
     * Creates a new DurationRetriever object.
     */
    public DurationRetriever ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //
    //---------------//
    // visit Measure //
    //---------------//
    @Override
    public boolean visit (Measure measure)
    {
        try {
            logger.debug("Visiting Part#{} {}",
                    measure.getPart().getId(), measure);

            Rational measureDur = Rational.ZERO;

            // Whole/multi rests are handled outside of slots
            for (Slot slot : measure.getSlots()) {
                if (slot.getStartTime() != null) {
                    for (Chord chord : slot.getChords()) {
                        Rational chordEnd = slot.getStartTime().plus(chord.
                                getDuration());

                        if (chordEnd.compareTo(measureDur) > 0) {
                            measureDur = chordEnd;
                        }
                    }
                }
            }

            if (!measureDur.equals(Rational.ZERO)) {
                // Make sure the measure duration is not bigger than limit
                if (measureDur.compareTo(measure.getExpectedDuration()) <= 0) {
                    measure.setActualDuration(measureDur);
                } else {
                    measure.setActualDuration(measure.getExpectedDuration());
                }

                measureDurations.put(measure.getPageId(), measureDur);
                logger.debug("{}: {}", measure.getPageId(), measureDur);
            } else if (!measure.getWholeChords().isEmpty()) {
                if (pass > 1) {
                    Rational dur = measureDurations.get(measure.getPageId());

                    if (dur != null) {
                        measure.setActualDuration(dur);
                    } else {
                        measure.setActualDuration(
                                measure.getExpectedDuration());
                    }
                }
            }
        } catch (InvalidTimeSignature ex) {
        } catch (Exception ex) {
            logger.warn(
                    getClass().getSimpleName() + " Error visiting " + measure,
                    ex);
        }

        return false; // Dead end, we don't go deeper than measure level
    }

    //------------//
    // visit Page //
    //------------//
    /**
     * Page hierarchy entry point
     *
     * @param page the page for which measure durations are to be computed
     * @return false, since no further processing is required after this node
     */
    @Override
    public boolean visit (Page page)
    {
        // Delegate to children
        page.acceptChildren(this);

        return false; // No default browsing this way
    }

    //-------------//
    // visit Score //
    //-------------//
    /**
     * Score hierarchy entry point, to delegate to all pages
     *
     * @param score the score to process
     * @return false, since no further processing is required after this node
     */
    @Override
    public boolean visit (Score score)
    {
        for (TreeNode pn : score.getPages()) {
            Page page = (Page) pn;
            page.accept(this);
        }

        return false; // No browsing
    }

    //--------------//
    // visit System //
    //--------------//
    /**
     * System processing. The rest of processing is directly delegated to the
     * measures
     *
     * @param system visit the system to export
     * @return false
     */
    @Override
    public boolean visit (ScoreSystem system)
    {
        logger.debug("Visiting {}", system);

        // 2 passes are needed, to get the actual duration of whole notes
        // Since the measure duration may be specified in another system part
        for (pass = 1; pass <= 2; pass++) {
            logger.debug("Pass #{}", pass);

            // Browse the (SystemParts and the) Measures
            system.acceptChildren(this);

            logger.debug("Durations:{}", measureDurations);
        }

        return false; // No default browsing this way
    }
}
