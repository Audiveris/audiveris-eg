//----------------------------------------------------------------------------//
//                                                                            //
//                             S c o r e S t e p                              //
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
package org.audiveris.omr.step;

import org.audiveris.omr.score.DurationRetriever;
import org.audiveris.omr.score.MeasureFixer;
import org.audiveris.omr.score.Score;
import org.audiveris.omr.score.ScoreReduction;
import org.audiveris.omr.score.entity.Page;

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.SystemInfo;

import org.audiveris.omr.util.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Class {@code ScoreStep} merges all pages into one score.
 *
 * @author Hervé Bitteur
 */
public class ScoreStep
        extends AbstractStep
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            ScoreStep.class);

    //~ Constructors -----------------------------------------------------------
    //-----------//
    // ScoreStep //
    //-----------//
    /**
     * Creates a new ScoreStep object.
     */
    public ScoreStep ()
    {
        super(
                Steps.SCORE,
                Level.SCORE_LEVEL,
                Mandatory.MANDATORY,
                DATA_TAB,
                "Build the final score");
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // displayUI //
    //-----------//
    @Override
    public void displayUI (Sheet sheet)
    {
        Steps.valueOf(Steps.SYMBOLS)
                .displayUI(sheet);
    }

    //--------//
    // doStep //
    //--------//
    /**
     * Notify the completion to ALL sheets of the merge and not just
     * the first one.
     */
    @Override
    public void doStep (Collection<SystemInfo> systems,
                        Sheet sheet)
            throws StepException
    {
        super.doStep(systems, sheet);

        // Set completion for all sheets of the score
        for (TreeNode pn : sheet.getScore()
                .getPages()) {
            Page page = (Page) pn;
            done(page.getSheet());
        }
    }

    //------//
    // doit //
    //------//
    @Override
    protected void doit (Collection<SystemInfo> systems,
                         Sheet sheet)
            throws StepException
    {
        Score score = sheet.getScore();

        // Merge the pages (connecting the parts across pages)
        ScoreReduction reduction = new ScoreReduction(score);
        reduction.reduce();

        // This work needs to know which time sig governs any measure, and this
        // time sig may be inherited from a previous page, therefore it cannot
        // be performed on every page in isolation (except when the page starts
        // with an explicit time sig).
        for (TreeNode pn : score.getPages()) {
            Page page = (Page) pn;

            // - Retrieve the actual duration of every measure
            page.accept(new DurationRetriever());

            // - Check all voices timing, assign forward items if needed.
            // - Detect special measures and assign proper measure ids
            // If needed, we can trigger a reprocessing of this page
            page.accept(new MeasureFixer());

            // Connect slurs across pages
            page.getFirstSystem()
                    .connectPageInitialSlurs();
        }
    }
}
