//----------------------------------------------------------------------------//
//                                                                            //
//                             P r i n t S t e p                              //
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

import org.audiveris.omr.score.ScoresManager;

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.SystemInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Class {@code PrintStep} prints the whole score (using physical layout)
 *
 * @author Hervé Bitteur
 */
public class PrintStep
        extends AbstractStep
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            PrintStep.class);

    //~ Constructors -----------------------------------------------------------
    //-----------//
    // PrintStep //
    //-----------//
    /**
     * Creates a new PrintStep object.
     */
    public PrintStep ()
    {
        super(
                Steps.PRINT,
                Level.SCORE_LEVEL,
                Mandatory.OPTIONAL,
                DATA_TAB,
                "Write the output PDF file");
    }

    //~ Methods ----------------------------------------------------------------
    //------//
    // doit //
    //------//
    @Override
    public void doit (Collection<SystemInfo> systems,
                      Sheet sheet)
            throws StepException
    {
        ScoresManager.getInstance()
                .writePhysicalPdf(sheet.getScore(), null);
    }
}
