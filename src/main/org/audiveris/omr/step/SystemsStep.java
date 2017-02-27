//----------------------------------------------------------------------------//
//                                                                            //
//                             S p l i t S t e p                              //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur and others 2000-2013. All rights reserved.      //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.step;

import org.audiveris.omr.grid.LagWeaver;

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.SystemInfo;

import java.util.Collection;

/**
 * Class {@code SystemsStep} splits entities among retrieved systems
 * according to precise systems boundaries.
 *
 * @author Hervé Bitteur
 */
public class SystemsStep
        extends AbstractStep
{
    //~ Constructors -----------------------------------------------------------

    //-------------//
    // SystemsStep //
    //-------------//
    /**
     * Creates a new SystemsStep object.
     */
    public SystemsStep ()
    {
        super(
                Steps.SYSTEMS,
                Level.SHEET_LEVEL,
                Mandatory.MANDATORY,
                DATA_TAB,
                "Split all data per system");
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // displayUI //
    //-----------//
    @Override
    public void displayUI (Sheet sheet)
    {
        sheet.getAssembly()
                .addBoard(Step.DATA_TAB, sheet.getBarsChecker().getCheckBoard());
    }

    //------//
    // doit //
    //------//
    @Override
    public void doit (Collection<SystemInfo> systems,
                      Sheet sheet)
            throws StepException
    {
        // Purge sections & runs of staff lines from hLag
        // Cross-connect vertical & remaining horizontal sections
        // Build glyphs out of connected sections
        new LagWeaver(sheet).buildInfo();

        // Create systems & parts
        sheet.createSystemsBuilder();
        sheet.getSystemsBuilder()
                .buildSystems();
    }
}
