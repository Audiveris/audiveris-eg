//----------------------------------------------------------------------------//
//                                                                            //
//                        S h e e t D e p e n d e n t                         //
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
package org.audiveris.omr.sheet.ui;

import org.audiveris.omr.selection.MouseMovement;
import org.audiveris.omr.selection.SheetEvent;

import org.audiveris.omr.sheet.Sheet;

import org.bushe.swing.event.EventSubscriber;

import org.jdesktop.application.AbstractBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class {@code SheetDependent} handles the dependency on sheet
 * availability.
 *
 * @author Hervé Bitteur
 */
public abstract class SheetDependent
        extends AbstractBean
        implements EventSubscriber<SheetEvent>
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            SheetDependent.class);

    /** Name of property linked to sheet availability */
    public static final String SHEET_AVAILABLE = "sheetAvailable";

    //~ Instance fields --------------------------------------------------------
    /** Indicates whether there is a current sheet */
    protected boolean sheetAvailable = false;

    //~ Constructors -----------------------------------------------------------
    //----------------//
    // SheetDependent //
    //----------------//
    /**
     * Creates a new SheetDependent object.
     */
    protected SheetDependent ()
    {
        // Stay informed on sheet status, in order to enable or disable all
        // sheet-dependent actions accordingly
        SheetsController.getInstance()
                .subscribe(this);
    }

    //~ Methods ----------------------------------------------------------------
    //------------------//
    // isSheetAvailable //
    //------------------//
    /**
     * Getter for sheetAvailable property
     *
     * @return the current property value
     */
    public boolean isSheetAvailable ()
    {
        return sheetAvailable;
    }

    //---------//
    // onEvent //
    //---------//
    /**
     * Notification of sheet selection.
     *
     * @param sheetEvent the notified sheet event
     */
    @Override
    public void onEvent (SheetEvent sheetEvent)
    {
        try {
            // Ignore RELEASING
            if (sheetEvent.movement == MouseMovement.RELEASING) {
                return;
            }

            Sheet sheet = sheetEvent.getData();
            setSheetAvailable(sheet != null);
        } catch (Exception ex) {
            logger.warn(getClass().getName() + " onEvent error", ex);
        }
    }

    //-------------------//
    // setSheetAvailable //
    //-------------------//
    /**
     * Setter for sheetAvailable property.
     *
     * @param sheetAvailable the new property value
     */
    public void setSheetAvailable (boolean sheetAvailable)
    {
        boolean oldValue = this.sheetAvailable;
        this.sheetAvailable = sheetAvailable;
        firePropertyChange(SHEET_AVAILABLE, oldValue, this.sheetAvailable);
    }
}
