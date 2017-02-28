//----------------------------------------------------------------------------//
//                                                                            //
//                   V e r t i c a l s C o n t r o l l e r                    //
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
package org.audiveris.omr.sheet;

import org.audiveris.omr.check.CheckBoard;

import org.audiveris.omr.constant.Constant;
import org.audiveris.omr.constant.ConstantSet;

import org.audiveris.omr.glyph.GlyphsModel;
import org.audiveris.omr.glyph.Nest;
import org.audiveris.omr.glyph.facets.Glyph;
import org.audiveris.omr.glyph.ui.GlyphsController;
import org.audiveris.omr.glyph.ui.NestView;

import org.audiveris.omr.selection.GlyphEvent;
import org.audiveris.omr.selection.MouseMovement;
import org.audiveris.omr.selection.SelectionService;
import org.audiveris.omr.selection.UserEvent;

import org.audiveris.omr.sheet.ui.SheetPainter;

import org.audiveris.omr.step.Step;
import org.audiveris.omr.step.Steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Graphics2D;
import java.util.Arrays;

/**
 * Class {@code VerticalsController} is in charge of handling assignment
 * and deassignment of vertical entities (stems) at sheet level
 *
 * @author Hervé Bitteur
 */
public class VerticalsController
    extends GlyphsController
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
        VerticalsController.class);

    /** Events this entity is interested in */
    private static final Class<?>[] eventClasses = new Class<?>[] {
                                                       GlyphEvent.class
                                                   };

    //~ Instance fields --------------------------------------------------------

    /** Related user display if any */
    private MyView view;

    //~ Constructors -----------------------------------------------------------

    //---------------------//
    // VerticalsController //
    //---------------------//
    /**
     * Creates a new VerticalsController object.
     *
     * @param sheet the related sheet
     */
    public VerticalsController (Sheet sheet)
    {
        // We work with the sheet vertical lag
        super(
            new GlyphsModel(
                sheet,
                sheet.getNest(),
                Steps.valueOf(Steps.STICKS)));
    }

    //~ Methods ----------------------------------------------------------------

    //---------//
    // refresh //
    //---------//
    /**
     * Refresh the display if any, with proper colors for sections
     */
    public void refresh ()
    {
        if ((view == null) && constants.displayFrame.getValue()) {
            displayFrame();
        } else if (view != null) {
            view.refresh();
        }
    }

    //--------------//
    // displayFrame //
    //--------------//
    private void displayFrame ()
    {
        //                Lag vLag = sheet.getVerticalLag();

        // Specific rubber display
        view = new MyView(sheet.getNest());

        //                // Create a hosting frame for the view
        //                sheet.getAssembly()
        //                     .addViewTab(
        //                    Step.VERTICALS_TAB,
        //                    new ScrollView(view),
        //                    new BoardsPane(
        //                        sheet.getAssembly(),
        //                        new PixelBoard(sheet),
        //                        new RunBoard(vLag, false),
        //                        new SectionBoard(vLag, false),
        //                        new SymbolGlyphBoard(this, true),
        //                        new StemCheckBoard(
        //                            sheet.getNest().getGlyphService(),
        //                            eventClasses)));
        sheet.getAssembly()
             .addBoard(
            Step.DATA_TAB,
            new StemCheckBoard(sheet.getNest().getGlyphService(), eventClasses));
    }

    //~ Inner Classes ----------------------------------------------------------

    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
        extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        Constant.Boolean displayFrame = new Constant.Boolean(
            true,
            "Should we display a frame on the stem sticks");

        //
        Constant.Double maxCoTangentForCheck = new Constant.Double(
            "cotangent",
            0.1,
            "Maximum cotangent for checking a stem candidate");
    }

    //--------//
    // MyView //
    //--------//
    private final class MyView
        extends NestView
    {
        //~ Constructors -------------------------------------------------------

        public MyView (Nest nest)
        {
            super(
                nest,
                VerticalsController.this,
                Arrays.asList(sheet.getHorizontalLag(), sheet.getVerticalLag()));

            setLocationService(sheet.getLocationService());

            setName("VerticalsBuilder-MyView");
        }

        //~ Methods ------------------------------------------------------------

        //-------------//
        // renderItems //
        //-------------//
        @Override
        public void renderItems (Graphics2D g)
        {
            // Render all physical info known so far
            sheet.getPage()
                 .accept(new SheetPainter(g, false));

            super.renderItems(g);
        }
    }

    //----------------//
    // StemCheckBoard //
    //----------------//
    private class StemCheckBoard
        extends CheckBoard<Glyph>
    {
        //~ Constructors -------------------------------------------------------

        public StemCheckBoard (SelectionService eventService,
                               Class[]          eventList)
        {
            super("StemCheck", null, eventService, eventList);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void onEvent (UserEvent event)
        {
            try {
                // Ignore RELEASING
                if (event.movement == MouseMovement.RELEASING) {
                    return;
                }

                if (event instanceof GlyphEvent) {
                    GlyphEvent glyphEvent = (GlyphEvent) event;
                    Glyph      glyph = glyphEvent.getData();

                    if (glyph instanceof Glyph) {
                        SystemInfo system = sheet.getSystemOf(glyph);

                        // Make sure this is a rather vertical stick
                        if (Math.abs(glyph.getInvertedSlope()) <= constants.maxCoTangentForCheck.getValue()) {
                            // Get a fresh suite
                            applySuite(
                                system.createStemCheckSuite(true),
                                glyph);

                            return;
                        }
                    }

                    tellObject(null);
                }
            } catch (Exception ex) {
                logger.warn(getClass().getName() + " onEvent error", ex);
            }
        }
    }
}
