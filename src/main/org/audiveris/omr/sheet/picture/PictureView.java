//----------------------------------------------------------------------------//
//                                                                            //
//                           P i c t u r e V i e w                            //
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
package org.audiveris.omr.sheet.picture;

import org.audiveris.omr.score.ui.PagePhysicalPainter;
import org.audiveris.omr.score.ui.PaintingParameters;

import org.audiveris.omr.sheet.Sheet;

import org.audiveris.omr.ui.Colors;
import org.audiveris.omr.ui.view.RubberPanel;
import org.audiveris.omr.ui.view.ScrollView;

import org.audiveris.omr.util.WeakPropertyChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Class {@code PictureView} defines the view dedicated to the display of
 * the picture image of a music sheet.
 *
 * @author Hervé Bitteur
 */
public class PictureView
        extends ScrollView
        implements PropertyChangeListener
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            PictureView.class);

    //~ Instance fields --------------------------------------------------------
    /** Link with sheet */
    private final Sheet sheet;

    //~ Constructors -----------------------------------------------------------
    //-------------//
    // PictureView //
    //-------------//
    /**
     * Create a new {@code PictureView} instance, dedicated to a sheet.
     *
     * @param sheet the related sheet
     */
    public PictureView (Sheet sheet)
    {
        this.sheet = sheet;

        view = new MyView();
        view.setName("Picture-View");
        view.setPreferredSize(sheet.getDimension());

        // Inject dependency of pixel location
        view.setLocationService(sheet.getLocationService());

        // Listen to all painting parameters
        PaintingParameters.getInstance()
                .addPropertyChangeListener(
                new WeakPropertyChangeListener(this));

        // Insert view
        setView(view);
    }

    //~ Methods ----------------------------------------------------------------
    //----------------//
    // propertyChange //
    //----------------//
    @Override
    public void propertyChange (PropertyChangeEvent evt)
    {
        view.repaint();
    }

    //~ Inner Classes ----------------------------------------------------------
    //--------//
    // MyView //
    //--------//
    private class MyView
            extends RubberPanel
    {
        //~ Methods ------------------------------------------------------------

        //--------//
        // render //
        //--------//
        @Override
        public void render (Graphics2D g)
        {
            PaintingParameters painting = PaintingParameters.getInstance();

            // Render the picture image
            if (painting.isInputPainting()) {
                sheet.getPicture()
                        .render(g);
            } else {
                // Use a white background
                Color oldColor = g.getColor();
                g.setColor(Color.WHITE);

                Rectangle rect = g.getClipBounds();

                g.fill(rect);
                g.setColor(oldColor);
            }

            // Render the recognized score entities?
            if (painting.isOutputPainting()) {
                if (sheet.getTargetBuilder() != null) {
                    sheet.getTargetBuilder()
                            .renderSystems(g); // TODO: Temporary 
                }

                boolean mixed = painting.isInputPainting();
                sheet.getPage()
                        .accept(
                        new PagePhysicalPainter(
                        g,
                        mixed ? Colors.MUSIC_PICTURE : Colors.MUSIC_ALONE,
                        mixed ? false : painting.isVoicePainting(),
                        true,
                        false));
            } else {
                if (sheet.getTargetBuilder() != null) {
                    sheet.getTargetBuilder()
                            .renderWarpGrid(g, true);
                }
            }
        }
    }
}
