//----------------------------------------------------------------------------//
//                                                                            //
//                       S c o r e C o n t r o l l e r                        //
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

import org.audiveris.omr.score.Score;

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.ui.SheetsController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class {@code ScoreController} is only a convenient way to retrieve
 * the current score (which contains the sheet currently selected by
 * the user).
 *
 * @author Hervé Bitteur
 */
public class ScoreController
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            ScoreController.class);

    //~ Constructors -----------------------------------------------------------
    //-----------------//
    // ScoreController //
    //-----------------//
    /**
     * No meant to be instantiated
     */
    private ScoreController ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //-----------------//
    // getCurrentScore //
    //-----------------//
    /**
     * Convenient method to get the current score instance
     *
     * @return the current score instance, or null
     */
    public static Score getCurrentScore ()
    {
        Sheet sheet = SheetsController.getCurrentSheet();

        if (sheet != null) {
            return sheet.getScore();
        }

        return null;
    }
}
