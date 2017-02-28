//----------------------------------------------------------------------------//
//                                                                            //
//                        S c o r e D e p e n d e n t                         //
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
package org.audiveris.omr.score.ui;

import org.audiveris.omr.selection.MouseMovement;
import org.audiveris.omr.selection.SheetEvent;

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.ui.SheetDependent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class {@code ScoreDependent} handles the dependency on score
 * availability
 *
 * @author Hervé Bitteur
 */
public abstract class ScoreDependent
        extends SheetDependent
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            ScoreDependent.class);

    /** Is a Score available. */
    protected static final String SCORE_AVAILABLE = "scoreAvailable";

    /** Is the Score idle. (available, but not being processed by a step) */
    protected static final String SCORE_IDLE = "scoreIdle";

    //~ Instance fields --------------------------------------------------------
    //
    /** Indicates whether there is a current score. */
    protected boolean scoreAvailable = false;

    /** Indicates whether there the current score is non busy. */
    protected boolean scoreIdle = false;

    //~ Constructors -----------------------------------------------------------
    //
    //----------------//
    // ScoreDependent //
    //----------------//
    /**
     * Creates a new ScoreDependent object.
     */
    protected ScoreDependent ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //
    //------------------//
    // isScoreAvailable //
    //------------------//
    /**
     * Getter for scoreAvailable property
     *
     * @return the current property value
     */
    public boolean isScoreAvailable ()
    {
        return scoreAvailable;
    }

    //-------------//
    // isScoreIdle //
    //-------------//
    /**
     * Getter for scoreIdle property
     *
     * @return the current property value
     */
    public boolean isScoreIdle ()
    {
        return scoreIdle;
    }

    //---------//
    // onEvent //
    //---------//
    /**
     * Notification of sheet selection (and thus related score if any).
     *
     * @param event the notified sheet event
     */
    @Override
    public void onEvent (SheetEvent event)
    {
        try {
            // Ignore RELEASING
            if (event.movement == MouseMovement.RELEASING) {
                return;
            }

            // This updates sheetAvailable
            super.onEvent(event);

            Sheet sheet = event.getData();

            // Update scoreAvailable
            setScoreAvailable((sheet != null) && (sheet.getScore() != null));

            // Update scoreIdle
            if (isScoreAvailable()) {
                setScoreIdle(sheet.getScore().isIdle());
            } else {
                setScoreIdle(false);
            }
        } catch (Exception ex) {
            logger.warn(getClass().getName() + " onEvent error", ex);
        }
    }

    //-------------------//
    // setScoreAvailable //
    //-------------------//
    /**
     * Setter for scoreAvailable property
     *
     * @param scoreAvailable the new property value
     */
    public void setScoreAvailable (boolean scoreAvailable)
    {
        boolean oldValue = this.scoreAvailable;
        this.scoreAvailable = scoreAvailable;
        firePropertyChange(SCORE_AVAILABLE, oldValue, this.scoreAvailable);
    }

    //--------------//
    // setScoreIdle //
    //--------------//
    /**
     * Setter for scoreIdle property
     *
     * @param scoreIdle the new property value
     */
    public void setScoreIdle (boolean scoreIdle)
    {
        boolean oldValue = this.scoreIdle;
        this.scoreIdle = scoreIdle;
        firePropertyChange(SCORE_IDLE, oldValue, this.scoreIdle);
    }
}
