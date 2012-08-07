//----------------------------------------------------------------------------//
//                                                                            //
//                   P a g e P h y s i c a l P a i n t e r                    //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur 2000-2012. All rights reserved.                 //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.score.ui;

import omr.glyph.Shape;
import static omr.glyph.Shape.*;
import omr.glyph.facets.Glyph;

import omr.grid.LineInfo;
import omr.grid.StaffInfo;

import omr.log.Logger;

import omr.math.BasicLine;
import omr.math.Rational;

import omr.sheet.Ending;
import omr.sheet.Ledger;
import omr.sheet.Sheet;
import omr.sheet.Skew;
import omr.sheet.SystemInfo;

import omr.score.common.PixelPoint;
import omr.score.common.PixelRectangle;
import omr.score.entity.Barline;
import omr.score.entity.Chord;
import omr.score.entity.Measure;
import omr.score.entity.Note;
import omr.score.entity.Page;
import omr.score.entity.ScoreSystem;
import omr.score.entity.Slot;
import omr.score.entity.Staff;
import omr.score.entity.SystemPart;

import omr.ui.Colors;
import static omr.ui.symbol.Alignment.*;
import omr.ui.symbol.MusicFont;
import omr.ui.util.UIUtilities;

import omr.util.VerticalSide;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;

/**
 * Class {@code PagePhysicalPainter} paints the recognized page
 * entities at the location of their image counterpart, so that
 * discrepancies between them can be easily seen.
 *
 * <p>TODO:
 * - Paint breath marks
 *
 * @author Hervé Bitteur
 */
public class PagePhysicalPainter
        extends PagePainter
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(
            PagePhysicalPainter.class);

    //~ Constructors -----------------------------------------------------------
    //---------------------//
    // PagePhysicalPainter //
    //---------------------//
    /**
     * Creates a new PagePhysicalPainter object.
     *
     * @param graphics      Graphic context
     * @param color         the color to be used for foreground
     * @param coloredVoices true for voices with different colors
     * @param linePainting  true for painting staff lines
     * @param annotated     true if annotations are to be drawn
     */
    public PagePhysicalPainter (Graphics graphics,
                                Color color,
                                boolean coloredVoices,
                                boolean linePainting,
                                boolean annotated)
    {
        super(graphics, color, coloredVoices, linePainting, annotated);
    }

    //~ Methods ----------------------------------------------------------------
    //----------//
    // drawSlot //
    //----------//
    /**
     * Draw a time slot in the score display.
     *
     * @param wholeSystem if true, the slot will embrace the whole system,
     *                    otherwise only the part is embraced
     * @param measure     the containing measure
     * @param slot        the slot to draw
     * @param color       the color to use in drawing
     */
    public void drawSlot (boolean wholeSystem,
                          Measure measure,
                          Slot slot,
                          Color color)
    {
        final Color oldColor = g.getColor();
        g.setColor(color);

        final Stroke oldStroke = UIUtilities.setAbsoluteStroke(g, 1);

        try {
            final int x = slot.getX();

            if (wholeSystem) {
                // Draw for the whole system height
                system = measure.getSystem();

                int top = system.getFirstPart()
                        .getFirstStaff()
                        .getInfo()
                        .getFirstLine()
                        .yAt(x);
                int bottom = system.getLastPart()
                        .getLastStaff()
                        .getInfo()
                        .getLastLine()
                        .yAt(x);

                g.drawLine(x, top, x, bottom);
            } else {
                // Draw for just the part height
                SystemPart part = measure.getPart();
                int top = part.getFirstStaff()
                        .getInfo()
                        .getFirstLine()
                        .yAt(x);
                int bottom = part.getLastStaff()
                        .getInfo()
                        .getLastLine()
                        .yAt(x);
                g.drawLine(x, top, x, bottom);

                // Draw slot start time
                Rational slotStartTime = slot.getStartTime();

                if (slotStartTime != null) {
                    paint(
                            basicLayout(slotStartTime.toString(), halfAT),
                            new PixelPoint(x, top - annotationDy),
                            BOTTOM_CENTER);
                }
            }
        } catch (Exception ex) {
            logger.warning(
                    getClass().getSimpleName() + " Error drawing " + slot,
                    ex);
        }

        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    //---------------//
    // visit Barline //
    //---------------//
    @Override
    public boolean visit (Barline barline)
    {
        if (!barline.getBox().intersects(oldClip)
                || systemInfo.getSheet().getStaffManager().getStaves().isEmpty()) {
            return false;
        }

        g.setColor(defaultColor);

        try {
            Stroke oldStroke = g.getStroke();

            // This drawing is driven by the barline shape
            Shape shape = barline.getShape();
            PixelRectangle box = barline.getBox();
            PixelPoint center = barline.getCenter();
            SystemPart part = barline.getPart();

            // Top and bottom limits of the barline, using staff lines
            StaffInfo topStaff = systemInfo.getStaffAt(box.getLocation());
            LineInfo topLine = topStaff.getFirstLine();
            StaffInfo botStaff = systemInfo.getStaffAt(
                    new Point(box.x, box.y + box.height));
            LineInfo botLine = botStaff.getLastLine();

            Skew skew = systemInfo.getSkew();
            if (skew == null) { // Safer
                return false;
            }
            double slope = skew.getSlope();
            BasicLine bar = new BasicLine();
            bar.includePoint(center.x, center.y);
            bar.includePoint(center.x - (100 * slope), center.y + 100);
            Point2D topCenter = topLine.verticalIntersection(bar);
            Point2D botCenter = botLine.verticalIntersection(bar);

            if (shape != null) {
                BarPainter barPainter = BarPainter.getBarPainter(shape);
                barPainter.draw(g, topCenter, botCenter, part);
            } else {
                barline.addError("Barline with no recognized shape");
            }

            // This drawing is driven by the underlying glyphs
//            for (Glyph glyph : barline.getGlyphs()) {
//                Shape shape = glyph.getShape();
//
//                if (glyph.isBar()) {
//                    float thickness = (float) glyph.getWeight() / glyph.
//                            getLength(
//                            Orientation.VERTICAL);
//                    g.setStroke(new BasicStroke(thickness));
//
//                    // Stroke is now OK for thickness but will draw beyond start
//                    // and stop points of the bar. So use clipping to fix this.
//                    final PixelRectangle box = glyph.getBounds();
//                    box.y = (int) Math.floor(
//                            glyph.getStartPoint(Orientation.VERTICAL).getY());
//                    box.height = (int) Math.ceil(
//                            glyph.getStopPoint(Orientation.VERTICAL).getY())
//                            - box.y;
//                    g.setClip(oldClip.intersection(box));
//
//                    glyph.renderLine(g);
//
//                    g.setClip(oldClip);
//                } else if ((shape == REPEAT_DOT) || (shape == DOT_set)) {
//                    paint(DOT_set, glyph.getCentroid());
//                }
//            }

            g.setStroke(oldStroke);
        } catch (ConcurrentModificationException ignored) {
            return false;
        } catch (Exception ex) {
            logger.warning(
                    getClass().getSimpleName() + " Error visiting " + barline,
                    ex);
        }

        return true;
    }

    //-------------//
    // visit Chord //
    //-------------//
    @Override
    public boolean visit (Chord chord)
    {
        try {
            // Super: check, voice color, flags
            if (!super.visit(chord)) {
                return false;
            }

            // Draw the stem (physical)
            if (chord.getStem() != null) {
                final PixelPoint tail = chord.getTailLocation();
                final PixelPoint head = chord.getHeadLocation();
                if (tail == null || head == null) {
                    chord.addError("Missing head or tail for " + chord);
                    return false;
                }
                final PixelPoint headCopy = new PixelPoint(head);

                // Slightly correct the ordinate on head side
                final int dyFix = scale.getInterline() / 4;

                if (tail.y < headCopy.y) {
                    // Stem up
                    headCopy.y -= dyFix;
                } else {
                    // Stem down
                    headCopy.y += dyFix;
                }

                g.drawLine(headCopy.x, headCopy.y, tail.x, tail.y);
            }
        } catch (ConcurrentModificationException ignored) {
        } catch (Exception ex) {
            logger.warning(
                    getClass().getSimpleName() + " Error visiting " + chord,
                    ex);
        }

        return true;
    }

    //---------------//
    // visit Measure //
    //---------------//
    @Override
    public boolean visit (Measure measure)
    {
        if (annotated) {
            if (!measure.isDummy()) {
                final SystemPart part = measure.getPart();
                final Color oldColor = g.getColor();

                // Write the score-based measure id, on first real part only
                if (part == measure.getSystem()
                        .getFirstRealPart()) {
                    String mid = measure.getScoreId();
                    if (mid != null) {
                        g.setColor(Colors.ANNOTATION);
                        StaffInfo staff = measure.getPart().getFirstStaff().
                                getInfo();
                        PixelPoint loc = new PixelPoint(
                                measure.getLeftX(),
                                staff.getFirstLine().yAt(
                                measure.getLeftX()) - annotationDy);
                        paint(basicLayout(mid, null), loc, BOTTOM_CENTER);
                    }
                }

                // Draw slot vertical lines ?
                if (parameters.isSlotPainting()
                        && (measure.getSlots() != null)) {
                    for (Slot slot : measure.getSlots()) {
                        drawSlot(false, measure, slot, Colors.SLOT);
                    }
                }

                //            // Flag for measure excess duration?
                //            if (measure.getExcess() != null) {
                //                g.setColor(Color.red);
                //                g.drawString(
                //                    "Excess " + Note.quarterValueOf(measure.getExcess()),
                //                    measure.getLeftX() + 10,
                //                    measure.getPart().getFirstStaff().getTopLeft().y - 15);
                //            }
                g.setColor(oldColor);
            }
        }

        // WholeChords are not in the children hierarchy
        // Thus, we must explicitly visit them
        for (Chord chord : measure.getWholeChords()) {
            if (chord.accept(this)) {
                chord.acceptChildren(this);
            }
        }

        return true;
    }

    //------------//
    // visit Note //
    //------------//
    @Override
    public boolean visit (Note note)
    {
        try {
            // Paint note head and accidentals
            super.visit(note);

            // Augmentation dots ?
            if (note.getFirstDot() != null) {
                paint(DOT_set, note.getFirstDot().getAreaCenter());
            }

            if (note.getSecondDot() != null) {
                paint(DOT_set, note.getSecondDot().getAreaCenter());
            }
        } catch (ConcurrentModificationException ignored) {
        } catch (Exception ex) {
            logger.warning(
                    getClass().getSimpleName() + " Error visiting " + note,
                    ex);
        }

        return true;
    }

    //------------//
    // visit Page //
    //------------//
    @Override
    public boolean visit (Page page)
    {
        try {
            score = page.getScore();
            scale = page.getScale();

            final Sheet sheet = page.getSheet();

            if ((sheet == null) || (scale == null)) {
                return false;
            }

            // Set all painting parameters
            initParameters();

            // Determine beams parameters
            if (scale.getMainBeam() != null) {
                beamThickness = scale.getMainBeam();
                beamHalfThickness = beamThickness / 2;
            }

            if (!page.getSystems()
                    .isEmpty()) {
                // Normal (full) rendering of the score
                page.acceptChildren(this);
            } else {
                // Render only what we have got so far...
                g.setColor(defaultColor);

                // Staff lines
                sheet.getStaffManager()
                        .render(g);

                if (sheet.getHorizontals() != null) {
                    // Horizontals

                    // Ledgers
                    for (Ledger ledger : sheet.getHorizontals()
                            .getLedgers()) {
                        ledger.render(g);
                    }

                    // Endings
                    for (Ending ending : sheet.getHorizontals()
                            .getEndings()) {
                        ending.render(g);
                    }
                }
            }
        } catch (ConcurrentModificationException ignored) {
        } catch (Exception ex) {
            logger.warning(
                    getClass().getSimpleName() + " Error visiting " + page,
                    ex);
        }

        return false;
    }

    //-------------------//
    // visit ScoreSystem //
    //-------------------//
    @Override
    public boolean visit (ScoreSystem system)
    {
        this.system = system;
        this.systemInfo = system.getInfo();

        if (!visit(systemInfo)) {
            return false;
        }

        // System id annotation
        if (annotated) {
            Color oldColor = g.getColor();
            g.setColor(Colors.ANNOTATION);

            Point ul = systemInfo.getBoundary()
                    .getLimit(VerticalSide.TOP)
                    .getPoint(0);
            paint(
                    basicLayout("S" + system.getId(), null),
                    new PixelPoint(ul.x + annotationDx, ul.y + annotationDy),
                    TOP_LEFT);
            g.setColor(oldColor);
        }

        return true;
    }

    //-------------//
    // visit Staff //
    //-------------//
    /**
     * This specific version paints the staff lines as closely as
     * possible to the physical sheet lines.
     *
     * @param staff the staff to handle
     * @return true if actually painted
     */
    @Override
    public boolean visit (Staff staff)
    {
        try {
            if (staff.isDummy()) {
                return false;
            }

            staff.getInfo()
                    .render(g);
        } catch (ConcurrentModificationException ignored) {
        } catch (Exception ex) {
            logger.warning(
                    getClass().getSimpleName() + " Error visiting " + staff,
                    ex);
        }

        return true;
    }

    //------------------//
    // visit SystemInfo //
    //------------------//
    public boolean visit (SystemInfo systemInfo)
    {
        try {
            // Check that this system is visible
            PixelRectangle bounds = systemInfo.getBounds();

            if ((bounds == null) || !bounds.intersects(oldClip)) {
                return false;
            }

            // Determine proper font size for the system
            musicFont = MusicFont.getFont(scale.getInterline());

            g.setColor(defaultColor);

            // Ledgers
            for (Glyph ledger : systemInfo.getLedgers()) {
                ledger.renderLine(g);
            }

            // Endings
            for (Glyph ending : systemInfo.getEndings()) {
                ending.renderLine(g);
            }
        } catch (ConcurrentModificationException ignored) {
        } catch (Exception ex) {
            logger.
                    warning(
                    getClass().getSimpleName() + " Error visiting " + systemInfo,
                    ex);
        }

        return true;
    }

    //--------------------//
    // accidentalLocation //
    //--------------------//
    @Override
    protected PixelPoint accidentalLocation (Note note,
                                             Glyph accidental)
    {
        return new PixelPoint(accidental.getAreaCenter().x, note.getCenter().y);
    }

    //----------//
    // braceBox //
    //----------//
    @Override
    protected PixelRectangle braceBox (SystemPart part)
    {
        PixelRectangle braceBox = part.getBrace()
                .getBounds();

        // Cheat a little, so that top and bottom are aligned with part extrema
        int leftX = braceBox.x + braceBox.width;
        int top = part.getFirstStaff()
                .getInfo()
                .getFirstLine()
                .yAt(leftX);
        int bot = part.getLastStaff()
                .getInfo()
                .getLastLine()
                .yAt(leftX);
        braceBox.y = top;
        braceBox.height = bot - top + 1;

        return braceBox;
    }

    //--------------//
    // noteLocation //
    //--------------//
    @Override
    protected PixelPoint noteLocation (Note note)
    {
        final PixelPoint center = note.getCenter();
        final Chord chord = note.getChord();
        final Glyph stem = chord.getStem();

        if (stem != null) {
            return location(center, chord);
        } else {
            return center;
        }
    }
}
