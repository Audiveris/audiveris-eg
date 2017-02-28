//----------------------------------------------------------------------------//
//                                                                            //
//                            R u n s V i e w e r                             //
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
package org.audiveris.omr.grid;

import org.audiveris.omr.run.RunBoard;
import org.audiveris.omr.run.RunsTable;
import org.audiveris.omr.run.RunsTableView;

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.ui.BinarizationBoard;
import org.audiveris.omr.sheet.ui.PixelBoard;

import org.audiveris.omr.ui.BoardsPane;
import org.audiveris.omr.ui.view.RubberPanel;
import org.audiveris.omr.ui.view.ScrollView;

import java.awt.Graphics2D;

/**
 * Class {@code RunsViewer} handles the display of instance(s) of
 * {@link RunsTable} in the assembly of the related sheet.
 *
 * @author Hervé Bitteur
 */
public class RunsViewer
{
    //~ Instance fields --------------------------------------------------------

    /** The related sheet */
    private final Sheet sheet;

    /** The related lines retriever */
    private final LinesRetriever linesRetriever;

    /** The related bars retriever */
    private final BarsRetriever barsRetriever;

    //~ Constructors -----------------------------------------------------------
    //------------//
    // RunsViewer //
    //------------//
    /**
     * Creates a new RunsViewer object.
     *
     * @param sheet          the related sheet
     * @param linesRetriever the related lines retriever
     * @param barsRetriever  the related bars retriever
     */
    public RunsViewer (Sheet sheet,
                       LinesRetriever linesRetriever,
                       BarsRetriever barsRetriever)
    {
        this.sheet = sheet;
        this.linesRetriever = linesRetriever;
        this.barsRetriever = barsRetriever;
    }

    //~ Methods ----------------------------------------------------------------
    //---------//
    // display //
    //---------//
    /**
     * Display a view on provided runs table
     *
     * @param table the runs to display
     */
    public void display (RunsTable table)
    {
        RubberPanel view = new MyRunsTableView(table);
        view.setName(table.getName());
        view.setPreferredSize(table.getDimension());

        BoardsPane boards = new BoardsPane(
                new PixelBoard(sheet),
                new BinarizationBoard(sheet),
                new RunBoard(table, true));

        sheet.getAssembly()
                .addViewTab(table.getName(), new ScrollView(view), boards);
    }

    //~ Inner Classes ----------------------------------------------------------
    //-----------------//
    // MyRunsTableView //
    //-----------------//
    /**
     * A specific runs view, which displays retrieved lines and bars
     * on top of the runs.
     */
    private class MyRunsTableView
            extends RunsTableView
    {
        //~ Constructors -------------------------------------------------------

        public MyRunsTableView (RunsTable table)
        {
            super(table, sheet.getLocationService());
        }

        //~ Methods ------------------------------------------------------------
        @Override
        protected void renderItems (Graphics2D g)
        {
            linesRetriever.renderItems(g);
            barsRetriever.renderItems(g);
        }
    }
}
