//----------------------------------------------------------------------------//
//                                                                            //
//                       R e g r e s s i o n P a n e l                        //
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
package org.audiveris.omr.glyph.ui.panel;

import org.audiveris.omr.glyph.GlyphRegression;
import org.audiveris.omr.glyph.ui.panel.TrainingPanel.DumpAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;

/**
 * Class {@code RegressionPanel} is the user interface that handles the
 * training of the linear engine. It is a dedicated companion of class
 * {@link GlyphTrainer}.
 *
 * @author Hervé Bitteur
 */
class RegressionPanel
        extends TrainingPanel
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            RegressionPanel.class);

    //~ Constructors -----------------------------------------------------------
    //-----------------//
    // RegressionPanel //
    //-----------------//
    /**
     * Creates a new RegressionPanel object.
     *
     * @param task           the current training activity
     * @param standardWidth  standard width for fields & buttons
     * @param selectionPanel the panel for glyph repository
     */
    public RegressionPanel (GlyphTrainer.Task task,
                            String standardWidth,
                            SelectionPanel selectionPanel)
    {
        super(
                task,
                standardWidth,
                GlyphRegression.getInstance(),
                selectionPanel,
                4);
        task.addObserver(this);

        trainAction = new TrainAction("Train");

        defineSpecificLayout();
    }

    //~ Methods ----------------------------------------------------------------
    //----------------------//
    // defineSpecificLayout //
    //----------------------//
    private void defineSpecificLayout ()
    {
        int r = 7;

        // Training entities
        JButton dumpButton = new JButton(new DumpAction());
        dumpButton.setToolTipText("Dump the evaluator internals");

        JButton trainButton = new JButton(trainAction);
        trainButton.setToolTipText("Train the evaluator from scratch");

        builder.add(dumpButton, cst.xy(3, r));
        builder.add(trainButton, cst.xy(5, r));
    }
}
