//-----------------------------------------------------------------------//
//                                                                       //
//                  H o r i z o n t a l s B u i l d e r                  //
//                                                                       //
//  Copyright (C) Herve Bitteur 2000-2005. All rights reserved.          //
//  This software is released under the terms of the GNU General Public  //
//  License. Please contact the author at herve.bitteur@laposte.net      //
//  to report bugs & suggestions.                                        //
//-----------------------------------------------------------------------//

package omr.sheet;

import omr.Main;
import omr.ProcessingException;
import omr.check.Check;
import omr.check.CheckSuite;
import omr.check.FailureResult;
import omr.check.SuccessResult;
import omr.constant.Constant;
import omr.constant.ConstantSet;
import omr.glyph.Glyph;
import omr.glyph.GlyphDirectory;
import omr.glyph.GlyphLag;
import omr.glyph.GlyphSection;
import omr.glyph.Shape;
import omr.stick.Stick;
import omr.stick.StickSection;
import omr.stick.StickUtil;
import omr.stick.StickView;
import omr.ui.BoardsPane;
import omr.ui.FilterBoard;
import omr.glyph.ui.GlyphBoard;
import omr.ui.PixelBoard;
import omr.ui.ScrollLagView;
import omr.ui.SectionBoard;
import omr.ui.ToggleHandler;
import omr.ui.Zoom;
import omr.util.Logger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

/**
 * Class <code>HorizontalsBuilder</code> is in charge of retrieving
 * horizontal dashes in the given sheet.
 *
 * @author Herv&eacute Bitteur
 * @version $Id$
 */
public class HorizontalsBuilder
    implements GlyphDirectory
{
    //~ Static variables/initializers -------------------------------------

    private static final Constants constants = new Constants();
    private static final Logger logger = Logger.getLogger(HorizontalsBuilder.class);

    // Success codes
    private static final SuccessResult LEDGER = new SuccessResult("Ledger");
    private static final SuccessResult ENDING = new SuccessResult("Ending");

    // Failure codes
    private static final FailureResult TOO_SHORT = new FailureResult("Hori-TooShort");
    private static final FailureResult TOO_LONG = new FailureResult("Hori-TooLong");
    private static final FailureResult TOO_THIN = new FailureResult("Hori-TooThin");
    private static final FailureResult TOO_THICK = new FailureResult("Hori-TooThick");
    private static final FailureResult TOO_FAT = new FailureResult("Hori-TooFat");
    private static final FailureResult TOO_HOLLOW = new FailureResult("Hori-TooHollow");
    private static final FailureResult IN_STAVE = new FailureResult("Hori-InStave");
    private static final FailureResult TOO_FAR = new FailureResult("Hori-TooFar");
    private static final FailureResult TOO_ADJA = new FailureResult("Hori-TooHighAdjacency");
    private static final FailureResult BI_CHUNK = new FailureResult("Hori-BiChunk");

    //~ Instance variables ---------------------------------------------------

    // The containing sheet
    private Sheet sheet;

    // Lag of horizontal runs
    private GlyphLag lag;

    // Horizontals area, with retrieved horizontal sticks
    private HorizontalArea horizontalsArea;

    // The related view if any
    private MyView lagView;

    // The various check suites
    private CheckSuite<Stick> ledgerSuite;
    private CheckSuite<Stick> endingSuite;

    // The whole list of horizontals (ledgers, legato signs, endings) found
    private final Horizontals info;

    // The collection of all horizontal items
    private final List<Dash> allDashes = new ArrayList<Dash>();

    // Specific model on section id
    private SpinnerModel idModel;

    //~ Constructors ------------------------------------------------------

    //--------------------//
    // HorizontalsBuilder //
    //--------------------//

    /**
     * @param sheet the related sheet
     */
    public HorizontalsBuilder (Sheet sheet)
    {
        this.sheet = sheet;
        info = new Horizontals();
    }

    //~ Methods -----------------------------------------------------------

    //-----------//
    // getEntity //
    //-----------//
    public Glyph getEntity (Integer id)
    {
        return lag.getGlyph(id);
    }

    //-----------//
    // buildInfo //
    //-----------//
    /**
     * Run the Horizontals step, searching all horizontal sticks for
     * typical things like ledgers, endings and legato signs.
     *
     * @return the built Horizontals info
     * @throws ProcessingException raised is process gets stopped
     */
    public Horizontals buildInfo ()
            throws ProcessingException
    {
        // Reuse the horizontal lag of runs (from staff lines)
        lag = sheet.getHorizontalLag();

        // Purge small sections
        Scale scale = sheet.getScale();
        ///lag.purgeTinySections(scale.fracToSquarePixels(constants.minForeWeight));

        // Retrieve (horizontal) sticks
        horizontalsArea = new HorizontalArea(sheet, lag,
                                             scale.fracToPixels(constants.maxThicknessHigh) - 1);

        // Recognize horizontals -> ledgers, endings
        retrieveHorizontals();

        // Cleanup the ledgers (and the endings)
        cleanup(info.getLedgers());
        cleanup(info.getEndings());

        // Display the resulting rubber is so asked for
        if (constants.displayFrame.getValue() &&
            Main.getJui() != null) {
            displayFrame();
        }

        logger.info(info.getLedgers().size() + " ledger(s), " +
                    info.getEndings().size() + " ending(s)");

        return info;
    }

    //---------//
    // cleanup //
    //---------//
    private void cleanup (List<? extends Dash> dashes)
    {
        for (Dash dash : dashes) {
            StickUtil.cleanup(dash.getStick(), lag,
                              constants.extensionMinPointNb.getValue(),
                              sheet.getPicture());
        }
    }

    //--------------//
    // displayFrame //
    //--------------//
    private void displayFrame ()
    {
        // Sections that, as members of horizontals, will be treated as
        // specific
        List<GlyphSection> members = new ArrayList<GlyphSection>();
        for (Dash dash : allDashes) {
            members.addAll(dash.getStick().getMembers());
        }

        // Specific rubber display
        lagView = new MyView(lag, members);

        // Ids of recognized glyphs
        List<Integer> knownIds = new ArrayList<Integer>(allDashes.size() +1);
        knownIds.add(GlyphBoard.NO_VALUE);
        for (Dash dash : allDashes) {
            knownIds.add(new Integer(dash.getStick().getId()));
        }

        BoardsPane boardsPane = new BoardsPane
            (lagView,
             new PixelBoard(),
             new SectionBoard(lag.getLastVertexId()),
             new GlyphBoard(lag.getLastGlyphId(), knownIds),
             new FilterBoard());

        // Create a hosting frame for the view
        ScrollLagView slv = new ScrollLagView(lagView);
        sheet.getAssembly().addViewTab("Horizontals",  slv, boardsPane);
        slv.addAncestorListener
            (new ToggleHandler
             ("Horizontals", lagView,
              "Toggle between before & after horizontal cleanup"));
    }

    //---------------------//
    // retrieveHorizontals //
    //---------------------//
    private void retrieveHorizontals ()
    {
        // Define the suites of Checks
        double minResult = constants.minCheckResult.getValue();

        createSuites();

        for (Stick stick : horizontalsArea.getSticks()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Checking " + stick);
            }

            // Run the Ledger Checks
            if (ledgerSuite.pass(stick) >= minResult) {
                stick.setResult(LEDGER);
                stick.setShape(Shape.LEDGER);
                info.getLedgers().add(new Ledger(stick));
            } else {
                // Then, if failed, the Ending Checks
                if (endingSuite.pass(stick) >= minResult) {
                    stick.setResult(ENDING);
                    stick.setShape(Shape.ENDING_HORIZONTAL);
                    info.getEndings().add(new Ending(stick));
                }
            }
        }

        // Update lists
        allDashes.addAll(info.getLedgers());
        allDashes.addAll(info.getEndings());

        if (logger.isDebugEnabled()) {
            logger.debug("Found " + info.getLedgers().size() +
                         " ledgers and " + info.getEndings().size() +
                         " endings");
        }
    }

    //--------------//
    // createSuites //
    //--------------//
    private void createSuites ()
    {
        // Common horizontal suite
        HorizontalSuite horizontalSuite = new HorizontalSuite();

        // ledgerSuite
        ledgerSuite = new CheckSuite<Stick>("Ledgers", constants.minCheckResult.getValue());
        ledgerSuite.addAll(horizontalSuite);
        ledgerSuite.addAll(new LedgerSuite());

        // endingSuite
        endingSuite = new CheckSuite<Stick>("Endings", constants.minCheckResult.getValue());
        endingSuite.addAll(horizontalSuite);
        endingSuite.addAll(new EndingSuite());

        if (logger.isDebugEnabled()) {
            ledgerSuite.dump();
            endingSuite.dump();
        }
    }

    //---------------//
    // staveDistance //
    //---------------//
    private static double staveDistance (Sheet sheet,
                                         Stick stick)
    {
        // Compute the (algebraic) distance from the stick to the nearest
        // stave. Distance is negative if the stick is within the stave,
        // positive outside.
        final int y = stick.getMidPos();
        final int x = (stick.getStart() + stick.getStop()) / 2;
        final int idx = sheet.getStaveIndexAtY(y);
        StaveInfo area = sheet.getStaves().get(idx);
        final int top = area.getFirstLine().getLine().yAt(x);
        final int bottom = area.getLastLine().getLine().yAt(x);
        final int dist = Math.max(top - y, y - bottom);

        return sheet.getScale().pixelsToFrac(dist);
    }

    //~ Classes -----------------------------------------------------------

    //--------//
    // MyView //
    //--------//
    private class MyView
        extends StickView
    {
        //~ Constructors --------------------------------------------------

        public MyView (GlyphLag lag,
                       List<GlyphSection> members)
        {
            super(lag, members, HorizontalsBuilder.this);
        }

        //~ Methods -------------------------------------------------------

        //----------//
        // colorize //
        //----------//
        @Override
            public void colorize ()
        {
            super.colorize();

            final int viewIndex = lag.getViews().indexOf(this);

            // All checked sticks. Perhaps, recognized sticks should no
            // longer be part of the list, since they'll be colorized a few
            // lines below... TBD
            for (Stick stick : horizontalsArea.getSticks()) {
                if (stick.getResult() != LEDGER &&
                    stick.getResult() != ENDING) {
                    stick.colorize(lag, viewIndex, Color.red);
                }
            }

            // Use light gray color for past successful entities
            // If relevant to the current lag...
            sheet.colorize(lag, viewIndex, Color.lightGray);
        }

        //-------------//
        // renderItems //
        //-------------//
        @Override
            public void renderItems (Graphics g)
        {
            Zoom z = getZoom();

            // Render all physical info known so far (staff lines)
            sheet.render(g, z);

            // Render the dashes found
            for (Dash dash : allDashes) {
                dash.render(g, z);
                dash.renderContour(g, z);
            }
        }

        //--------------//
        // glyphSelected //
        //--------------//
        @Override
            protected void glyphSelected (Glyph glyph,
                                          Point pt)
        {
            createSuites();           // Safer, to take modifs into account

            // Present table of ledger checks then table of ending checks
            Stick stick = (Stick) glyph;
            String str1 = ledgerSuite.passHtml(null, stick);
            filterMonitor.tellHtml(endingSuite.passHtml(str1, stick));
        }
    }

    //-------------------//
    // MinThicknessCheck //
    //-------------------//
    private class MinThicknessCheck
            extends Check<Stick>
    {
        //~ Constructors --------------------------------------------------

        protected MinThicknessCheck ()
        {
            super("MinThickness", constants.minThicknessLow.getValue(),
                  constants.minThicknessHigh.getValue(), true, TOO_THIN);
        }

        //~ Methods -------------------------------------------------------

        // Retrieve the thickness data
        protected double getValue (Stick stick)
        {
            return sheet.getScale().pixelsToFrac(stick.getThickness());
        }
    }

    //-------------------//
    // MaxThicknessCheck //
    //-------------------//
    private class MaxThicknessCheck
            extends Check<Stick>
    {
        //~ Constructors --------------------------------------------------

        protected MaxThicknessCheck ()
        {
            super("MaxThickness", constants.maxThicknessLow.getValue(),
                  constants.maxThicknessHigh.getValue(), false, TOO_THICK);
        }

        //~ Methods -------------------------------------------------------

        // Retrieve the thickness data
        protected double getValue (Stick stick)
        {
            return sheet.getScale().pixelsToFrac(stick.getThickness());
        }
    }

    //----------------//
    // MinLengthCheck //
    //----------------//
    private class MinLengthCheck
            extends Check<Stick>
    {
        //~ Constructors --------------------------------------------------

        protected MinLengthCheck (double low,
                                  double high)
        {
            super("MinLength", low, high, true, TOO_SHORT);
        }

        //~ Methods -------------------------------------------------------

        // Retrieve the length data
        protected double getValue (Stick stick)
        {
            return sheet.getScale().pixelsToFrac(stick.getLength());
        }
    }

    //----------------//
    // MaxLengthCheck //
    //----------------//
    private class MaxLengthCheck
            extends Check<Stick>
    {
        //~ Constructors --------------------------------------------------

        protected MaxLengthCheck ()
        {
            super("MaxLength", constants.maxLengthLow.getValue(),
                  constants.maxLengthHigh.getValue(), false, TOO_LONG);
        }

        //~ Methods -------------------------------------------------------

        // Retrieve the length data
        protected double getValue (Stick stick)
        {
            return sheet.getScale().pixelsToFrac(stick.getLength());
        }
    }

    //-----------------//
    // MinDensityCheck //
    //-----------------//
    private class MinDensityCheck
            extends Check<Stick>
    {
        //~ Constructors --------------------------------------------------

        protected MinDensityCheck ()
        {
            super("MinDensity", constants.minDensityLow.getValue(),
                  constants.minDensityHigh.getValue(), true, TOO_HOLLOW);
        }

        //~ Methods -------------------------------------------------------

        // Retrieve the density
        protected double getValue (Stick stick)
        {
            Rectangle rect = stick.getBounds();
            double area = rect.width * rect.height;

            return (double) stick.getWeight() / area;
        }
    }

    //--------------//
    // MinDistCheck //
    //--------------//
    private class MinDistCheck
            extends Check<Stick>
    {
        //~ Constructors --------------------------------------------------

        protected MinDistCheck ()
        {
            super("MinDist", 0, 0, true, IN_STAVE);
            setLowHigh(constants.minStaveDistanceLow.getValue(),
                       constants.minStaveDistanceHigh.getValue());
        }

        //~ Methods -------------------------------------------------------

        // Retrieve the position with respect to the various staves of the
        // system being checked.
        protected double getValue (Stick stick)
        {
            return staveDistance(sheet, stick);
        }
    }

    //--------------//
    // MaxDistCheck //
    //--------------//
    private class MaxDistCheck
            extends Check<Stick>
    {
        //~ Constructors --------------------------------------------------

        protected MaxDistCheck ()
        {
            super("MaxDist", 0, 0, false, TOO_FAR);
            setLowHigh(constants.maxStaveDistanceLow.getValue(),
                       constants.maxStaveDistanceHigh.getValue());
        }

        //~ Methods -------------------------------------------------------

        // Retrieve the position with respect to the various staves of the
        // system being checked.
        protected double getValue (Stick stick)
        {
            return staveDistance(sheet, stick);
        }
    }

    //------------//
    // ChunkCheck //
    //------------//

    /**
     * Class <code>ChunkCheck</code> checks for absence of a chunk either
     * at start or stop
     */
    private class ChunkCheck
            extends Check<Stick>
    {
        //~ Instance variables --------------------------------------------

        // Half-dimensions for window at top and bottom, checking for
        // chunks
        private final int nWidth;
        private final int nHeight;

        //~ Constructors -------------------------------------------------

        protected ChunkCheck ()
        {
            super("Chunk", 0, 0, false, BI_CHUNK);

            // Adjust chunk window according to system scale (problem, we
            // have sheet scale and stave scale, not system scale...)
            Scale scale = sheet.getScale();
            nWidth = scale.fracToPixels(constants.chunkWidth);
            nHeight = scale.fracToPixels(constants.chunkHeight);

            int area = 4 * nWidth * nHeight;
            setLowHigh(area * constants.chunkRatioLow.getValue(),
                       area * constants.chunkRatioHigh.getValue());

            if (logger.isDebugEnabled()) {
                logger.debug("MaxPixLow=" + getLow() + ", MaxPixHigh="
                             + getHigh());
            }
        }

        //~ Methods -------------------------------------------------------

        protected double getValue (Stick stick)
        {
            // Retrieve the smallest stick chunk either at top or bottom
            int res = Math.min(stick.getAliensAtStart(nHeight, nWidth),
                               stick.getAliensAtStop(nHeight, nWidth));

            if (logger.isDebugEnabled()) {
                logger.debug("MaxAliens= " + res + " for " + stick);
            }

            return res;
        }
    }

    //---------------------//
    // FirstAdjacencyCheck //
    //---------------------//
    private static class FirstAdjacencyCheck
            extends Check<Stick>
    {
        //~ Constructors --------------------------------------------------

        protected FirstAdjacencyCheck ()
        {
            super("TopAdj", constants.maxAdjacencyLow.getValue(),
                  constants.maxAdjacencyHigh.getValue(), false,
                  TOO_ADJA);
        }

        //~ Methods -------------------------------------------------------

        // Retrieve the adjacency value
        protected double getValue (Stick stick)
        {
            int length = stick.getLength();

            return (double) stick.getFirstStuck() / (double) length;
        }
    }

    //--------------------//
    // LastAdjacencyCheck //
    //--------------------//
    private static class LastAdjacencyCheck
            extends Check<Stick>
    {
        //~ Constructors -----------------------------------------------------

        protected LastAdjacencyCheck ()
        {
            super("BottomAdj", constants.maxAdjacencyLow.getValue(),
                  constants.maxAdjacencyHigh.getValue(), false,
                  TOO_ADJA);
        }

        //~ Methods -------------------------------------------------------

        // Retrieve the adjacency value
        protected double getValue (Stick stick)
        {
            int length = stick.getLength();

            return (double) stick.getLastStuck() / (double) length;
        }
    }

    //-----------------//
    // HorizontalSuite //
    //-----------------//
    private class HorizontalSuite
            extends CheckSuite<Stick>
    {
        //~ Constructors --------------------------------------------------

        public HorizontalSuite ()
        {
            super("Horizontals", constants.minCheckResult.getValue());
            add(1, new MinThicknessCheck()); // Minimum thickness
            add(1, new MinDistCheck()); // Not within staves
            add(1, new MaxDistCheck()); // Not too far from staves
        }
    }

    //-------------//
    // LedgerSuite //
    //-------------//
    private class LedgerSuite
            extends CheckSuite<Stick>
    {
        //~ Constructors --------------------------------------------------

        public LedgerSuite ()
        {
            super("Ledgers", constants.minCheckResult.getValue());
            add(1,
                new MinLengthCheck(constants.minLedgerLengthLow.getValue(),
                                   constants.minLedgerLengthHigh.getValue())); // Minimum length
            add(1, new MaxThicknessCheck());
            add(1, new MaxLengthCheck()); // Maximum length
            add(1, new MinDensityCheck());
            add(1, new ChunkCheck()); // At least one edge WITHOUT a chunk
        }
    }

    //-------------//
    // EndingSuite //
    //-------------//
    private class EndingSuite
            extends CheckSuite<Stick>
    {
        //~ Constructors --------------------------------------------------

        public EndingSuite ()
        {
            super("Endings", constants.minCheckResult.getValue());
            add(1,
                new MinLengthCheck(constants.minEndingLengthLow.getValue(),
                                   constants.minEndingLengthHigh.getValue())); // Minimum length
            add(1, new MaxThicknessCheck());
            add(1, new FirstAdjacencyCheck());
            add(1, new LastAdjacencyCheck());
        }
    }

    //-----------//
    // Constants //
    //-----------//
    private static class Constants
            extends ConstantSet
    {
        Constant.Boolean displayFrame = new Constant.Boolean
                (false,
                 "Should we display a frame on the horizontal sticks");

        Constant.Integer maxDeltaLength = new Constant.Integer
                (4,
                 "Maximum difference in run length to be part of the same section");

        Scale.Fraction minForeWeight = new Scale.Fraction
                (1.25,
                 "Minimum foreground weight for a section to be kept");

        Constant.Double minCheckResult = new Constant.Double
                (0.50,
                 "Minimum result for suite of check");

        Scale.Fraction minThicknessLow = new Scale.Fraction
                (0.3,
                 " Low Minimum thickness of an interesting stick");

        Scale.Fraction minThicknessHigh = new Scale.Fraction
                (0.3,
                 " High Minimum thickness of an interesting stick");

        Scale.Fraction maxThicknessLow = new Scale.Fraction
                (0.3,
                 " Low Maximum thickness of an interesting stick");

        Scale.Fraction maxThicknessHigh = new Scale.Fraction
                (0.3,
                 " High Maximum thickness of an interesting stick");

        Scale.Fraction maxLengthLow = new Scale.Fraction
                (2.5,
                 "Low Maximum length for a horizontal");

        Scale.Fraction maxLengthHigh = new Scale.Fraction
                (3.5,
                 "High Maximum length for a horizontal");

        Scale.Fraction minLedgerLengthLow = new Scale.Fraction
                (2.5,
                 "Low Minimum length for a ledger");

        Scale.Fraction minLedgerLengthHigh = new Scale.Fraction
                (3.5,
                 "High Minimum length for a ledger");

        Scale.Fraction minEndingLengthLow = new Scale.Fraction
                (5,
                 "Low Minimum length for an ending");

        Scale.Fraction minEndingLengthHigh = new Scale.Fraction
                (10,
                 "High Minimum length for an ending");

        Constant.Double minDensityLow = new Constant.Double
                (0.8,
                 "Low Minimum density for a horizontal");

        Constant.Double minDensityHigh = new Constant.Double
                (0.9,
                 "High Minimum density for a horizontal");

        Scale.Fraction maxStaveDistanceLow = new Scale.Fraction
                (5,
                 "Low Maximum stave distance for a horizontal");

        Scale.Fraction maxStaveDistanceHigh = new Scale.Fraction
                (7,
                 "High Maximum stave distance for a horizontal");

        Scale.Fraction minStaveDistanceLow = new Scale.Fraction
                (0.6,
                 "Low Minimum stave distance for a horizontal");

        Scale.Fraction minStaveDistanceHigh = new Scale.Fraction
                (0.8,
                 "High Minimum stave distance for a horizontal");

        Scale.Fraction chunkHeight = new Scale.Fraction
                (0.33,
                 "Height of half area to look for chunks");

        Scale.Fraction chunkWidth = new Scale.Fraction
                (0.33,
                 "Width of half area to look for chunks");

        Constant.Double chunkRatioLow = new Constant.Double
                (0.25,
                 "LowMaximum ratio of alien pixels to detect chunks");

        Constant.Double chunkRatioHigh = new Constant.Double
                (0.25,
                 "HighMaximum ratio of alien pixels to detect chunks");

        Constant.Double maxAdjacencyLow = new Constant.Double
                (0.60,
                 "Low Maximum adjacency ratio for an ending");

        Constant.Double maxAdjacencyHigh = new Constant.Double
                (0.70,
                 "High Maximum adjacency ratio for an ending");

        Constant.Integer extensionMinPointNb = new Constant.Integer
                (4,
                 "Minimum number of points to compute extension of crossing objects during cleanup");

        Constants ()
        {
            initialize();
        }
    }
}
