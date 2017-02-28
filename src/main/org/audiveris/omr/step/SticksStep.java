//----------------------------------------------------------------------------//
//                                                                            //
//                            S t i c k s S t e p                             //
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

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.SystemInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Class {@code SticksStep} retrieves the vertical sticks such as
 * stems and horizontal sticks such as ledgers and endings.
 *
 * @author Hervé Bitteur
 */
public class SticksStep
        extends AbstractSystemStep
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            SticksStep.class);

    //~ Constructors -----------------------------------------------------------
    //------------//
    // SticksStep //
    //------------//
    /**
     * Creates a new SticksStep object.
     */
    public SticksStep ()
    {
        super(
                Steps.STICKS,
                Level.SHEET_LEVEL,
                Mandatory.MANDATORY,
                DATA_TAB,
                "Extract vertical & horizontal sticks");
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // displayUI //
    //-----------//
    @Override
    public void displayUI (Sheet sheet)
    {
        // Create verticals display
        sheet.getVerticalsController()
                .refresh();

        // Add ledger checkboard
        sheet.getSystems()
                .get(0)
                .getHorizontalsBuilder()
                .addCheckBoard();
    }

    //----------//
    // doSystem //
    //----------//
    @Override
    public void doSystem (SystemInfo system)
            throws StepException
    {
        clearSystemErrors(system);
        system.retrieveVerticals();
        system.retrieveHorizontals();
    }

    //----------//
    // doEpilog //
    //----------//
    @Override
    protected void doEpilog (Collection<SystemInfo> systems,
                             Sheet sheet)
            throws StepException
    {
        sheet.createVerticalsController();
    }
}
