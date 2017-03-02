//----------------------------------------------------------------------------//
//                                                                            //
//                     B i n a r i z a t i o n B o a r d                      //
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

import org.audiveris.omr.run.AdaptiveFilter;
import org.audiveris.omr.run.AdaptiveFilter.AdaptiveContext;
import org.audiveris.omr.run.FilterDescriptor;
import org.audiveris.omr.run.PixelFilter;

import org.audiveris.omr.selection.LocationEvent;
import org.audiveris.omr.selection.MouseMovement;
import org.audiveris.omr.selection.UserEvent;

import org.audiveris.omr.sheet.Sheet;

import org.audiveris.omr.ui.Board;
import org.audiveris.omr.ui.field.LDoubleField;
import org.audiveris.omr.ui.util.Panel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Rectangle;

/**
 * Class {@code BinarizationBoard} is a board meant to display the
 * context of binarization for a given pixel location.
 *
 * @author Hervé Bitteur
 */
public class BinarizationBoard
        extends Board
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            BinarizationBoard.class);

    /** Events this entity is interested in */
    private static final Class<?>[] eventClasses = new Class<?>[]{
        LocationEvent.class
    };

    /** Format used for every double field. */
    private static final String format = "%.2f";

    //~ Instance fields --------------------------------------------------------
    //
    /** The related sheet. */
    private final Sheet sheet;

    /** Mean level in neighborhood. */
    private final LDoubleField mean = new LDoubleField(
            false,
            "Mean",
            "Mean value",
            format);

    /** Standard deviation in neighborhood. */
    private final LDoubleField stdDev = new LDoubleField(
            false,
            "StdDev",
            "Standard deviation value",
            format);

    /** Computed threshold. */
    private final LDoubleField threshold = new LDoubleField(
            false,
            "Thres.",
            "Threshold",
            format);

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new BinarizationBoard object.
     *
     * @param sheet DOCUMENT ME!
     */
    public BinarizationBoard (Sheet sheet)
    {
        super(
                "Binarization",
                150,
                sheet.getLocationService(),
                eventClasses,
                false,
                false);

        this.sheet = sheet;

        defineLayout();
    }

    //~ Methods ----------------------------------------------------------------
    //
    //---------//
    // onEvent //
    //---------//
    @Override
    public void onEvent (UserEvent event)
    {
        try {
            // Ignore RELEASING
            if (event.movement == MouseMovement.RELEASING) {
                return;
            }

            logger.debug("BinarizationBoard: {}", event);

            if (event instanceof LocationEvent) {
                // Display rectangle attributes
                LocationEvent sheetLocation = (LocationEvent) event;
                Rectangle rect = sheetLocation.getData();

                if (rect != null) {
                    FilterDescriptor desc = sheet.getPage()
                            .getFilterParam()
                            .getTarget();
                    PixelFilter source = desc.getFilter(
                            sheet.getPicture());

                    if (source == null) {
                        source = new AdaptiveFilter(
                                sheet.getPicture(),
                                AdaptiveFilter.getDefaultMeanCoeff(),
                                AdaptiveFilter.getDefaultStdDevCoeff());
                    }

                    PixelFilter.Context context = source.getContext(
                            rect.x,
                            rect.y);

                    if (context != null) {
                        if (context instanceof AdaptiveContext) {
                            AdaptiveContext ctx = (AdaptiveContext) context;
                            mean.setValue(ctx.mean);
                            stdDev.setValue(ctx.standardDeviation);
                        } else {
                            mean.setText("");
                            stdDev.setText("");
                        }

                        threshold.setValue(context.threshold);

                        return;
                    }
                }

                mean.setText("");
                stdDev.setText("");
                threshold.setText("");
            }
        } catch (Exception ex) {
            logger.warn(getClass().getName() + " onEvent error", ex);
        }
    }

    //--------------//
    // defineLayout //
    //--------------//
    private void defineLayout ()
    {
        FormLayout layout = Panel.makeFormLayout(1, 3);
        PanelBuilder builder = new PanelBuilder(layout, getBody());

        CellConstraints cst = new CellConstraints();

        int r = 1; // --------------------------------
        builder.add(mean.getLabel(), cst.xy(1, r));
        builder.add(mean.getField(), cst.xy(3, r));

        builder.add(stdDev.getLabel(), cst.xy(5, r));
        builder.add(stdDev.getField(), cst.xy(7, r));

        builder.add(threshold.getLabel(), cst.xy(9, r));
        builder.add(threshold.getField(), cst.xy(11, r));
    }
}
