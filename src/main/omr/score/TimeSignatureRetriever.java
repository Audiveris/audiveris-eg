//----------------------------------------------------------------------------//
//                                                                            //
//                T i m e S i g n a t u r e R e t r i e v e r                 //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright (C) Hervé Bitteur 2000-2011. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.score;

import omr.constant.ConstantSet;

import omr.glyph.CompoundBuilder;
import omr.glyph.Evaluation;
import omr.glyph.Glyphs;
import omr.glyph.Grades;
import omr.glyph.Shape;
import omr.glyph.ShapeRange;
import omr.glyph.facets.Glyph;

import omr.grid.StaffInfo;

import omr.log.Logger;

import omr.score.common.PixelPoint;
import omr.score.common.PixelRectangle;
import omr.score.entity.Barline;
import omr.score.entity.Chord;
import omr.score.entity.Clef;
import omr.score.entity.KeySignature;
import omr.score.entity.Measure;
import omr.score.entity.Note;
import omr.score.entity.Page;
import omr.score.entity.ScoreSystem;
import omr.score.entity.Staff;
import omr.score.entity.SystemPart;
import omr.score.entity.TimeSignature;
import omr.score.visitor.AbstractScoreVisitor;

import omr.sheet.Scale;
import omr.sheet.SystemInfo;

import omr.util.TreeNode;

import java.util.EnumSet;

/**
 * Class {@code TimeSignatureRetriever} checks carefully the first
 * measure of each staff for a time signature.
 *
 * @author Hervé Bitteur
 */
public class TimeSignatureRetriever
    extends AbstractScoreVisitor
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(
        TimeSignatureRetriever.class);

    //~ Instance fields --------------------------------------------------------

    // Scale-dependent constants
    private int timeSigWidth;
    private int yOffset;

    //~ Constructors -----------------------------------------------------------

    //------------------------//
    // TimeSignatureRetriever //
    //------------------------//
    /**
     * Creates a new TimeSignatureRetriever object.
     */
    public TimeSignatureRetriever ()
    {
    }

    //~ Methods ----------------------------------------------------------------

    //------------//
    // visit Page //
    //------------//
    /**
     * Page hierarchy (sole) entry point.
     * @param page the page to check
     * @return false
     */
    @Override
    public boolean visit (Page page)
    {
        Scale scale = page.getScale();
        timeSigWidth = scale.toPixels(constants.timeSigWidth);
        yOffset = scale.toPixels(constants.yOffset);

        try {
            // We simply consider the very first measure of every staff
            ScoreSystem system = page.getFirstSystem();
            Measure     firstMeasure = system.getFirstRealPart()
                                             .getFirstMeasure();

            // If we have some TS, then it's OK
            if (hasTimeSig(firstMeasure)) {
                return false;
            }

            // No TS found. Let's look where it should be, if there was one
            PixelRectangle roi = getRoi(firstMeasure);

            if (roi.width < timeSigWidth) {
                if (logger.isFineEnabled()) {
                    logger.fine("No room for time sig: " + roi.width);
                }

                return false;
            }

            for (Staff.SystemIterator sit = new Staff.SystemIterator(
                firstMeasure); sit.hasNext();) {
                Staff staff = sit.next();

                if (staff.isDummy()) {
                    continue;
                }

                int   center = roi.x + (roi.width / 2);
                Glyph compound = system.getInfo()
                                       .buildCompound(
                    null,
                    false,
                    system.getInfo().getGlyphs(),
                    new TimeSigAdapter(
                        system.getInfo(),
                        Grades.timeMinGrade,
                        ShapeRange.FullTimes,
                        staff,
                        center));

                if (compound != null) {
                    // Insert time sig in proper measure
                    TimeSignature.populateFullTime(
                        compound,
                        firstMeasure,
                        staff);
                }
            }
        } catch (Exception ex) {
            logger.warning(
                getClass().getSimpleName() + " Error visiting " + page,
                ex);
        }

        return false; // No navigation
    }

    //--------//
    // getRoi //
    //--------//
    /**
     * Retrieve the free space where a time signature could be within the first
     * measure
     * @param firstMeasure the containing measure (whatever the part)
     * @return a degenerated rectangle, just to provide left and right bounds
     */
    private PixelRectangle getRoi (Measure firstMeasure)
    {
        ScoreSystem system = firstMeasure.getSystem();
        int         left = 0; // Min
        int         right = system.getTopLeft().x +
                            system.getDimension().width; // Max

        for (Staff.SystemIterator sit = new Staff.SystemIterator(firstMeasure);
             sit.hasNext();) {
            Staff staff = sit.next();

            if (staff.isDummy()) {
                continue;
            }

            int          staffId = staff.getId();
            Measure      measure = sit.getMeasure();

            // Before: clef? + key signature?
            KeySignature keySig = measure.getFirstMeasureKey(staffId);

            if (keySig != null) {
                PixelRectangle kBox = keySig.getBox();
                left = Math.max(left, kBox.x + kBox.width);
            } else {
                Clef clef = measure.getFirstMeasureClef(staffId);

                if (clef != null) {
                    PixelRectangle cBox = clef.getBox();
                    left = Math.max(left, cBox.x + cBox.width);
                }
            }

            // After: alteration? + chord?
            Chord chord = measure.getClosestChord(new PixelPoint(0, 0));

            if (chord != null) {
                for (TreeNode tn : chord.getNotes()) {
                    Note  note = (Note) tn;
                    Glyph accid = note.getAccidental();

                    if (accid != null) {
                        right = Math.min(right, accid.getContourBox().x);
                    } else {
                        right = Math.min(right, note.getBox().x);
                    }
                }
            }

            if (logger.isFineEnabled()) {
                logger.fine(
                    "Staff:" + staffId + " left:" + left + " right:" + right);
            }
        }

        return new PixelRectangle(left, 0, right - left, 0);
    }

    //------------//
    // hasTimeSig //
    //------------//
    /**
     * Check whether the provided measure contains at least one explicit time
     * signature
     *
     * @param measure the provided measure (in fact we care only about the
     * measure id, regardless of the part)
     * @return true if a time sig exists in some staff of the measure
     */
    private boolean hasTimeSig (Measure measure)
    {
        for (Staff.SystemIterator sit = new Staff.SystemIterator(measure);
             sit.hasNext();) {
            Staff staff = sit.next();

            if (sit.getMeasure()
                   .getTimeSignature(staff) != null) {
                return true;
            }
        }

        return false;
    }

    //~ Inner Classes ----------------------------------------------------------

    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
        extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        Scale.Fraction timeSigWidth = new Scale.Fraction(
            2d,
            "Width of a time signature");
        Scale.Fraction yOffset = new Scale.Fraction(
            0.5d,
            "Time signature vertical offset since staff line");
    }

    //----------------//
    // TimeSigAdapter //
    //----------------//
    /**
     * Compound adapter to search for a time sig shape
     */
    private class TimeSigAdapter
        extends CompoundBuilder.TopShapeAdapter
    {
        //~ Instance fields ----------------------------------------------------

        final Staff staff;
        final int   center;

        //~ Constructors -------------------------------------------------------

        public TimeSigAdapter (SystemInfo     system,
                               double         minGrade,
                               EnumSet<Shape> desiredShapes,
                               Staff          staff,
                               int            center)
        {
            super(system, minGrade, desiredShapes);
            this.staff = staff;
            this.center = center;
        }

        //~ Methods ------------------------------------------------------------

        public boolean isCandidateSuitable (Glyph glyph)
        {
            return !glyph.isManualShape();
        }

        @Override
        public Evaluation getChosenEvaluation ()
        {
            return new Evaluation(chosenEvaluation.shape, Evaluation.ALGORITHM);
        }

        public PixelRectangle getReferenceBox ()
        {
            StaffInfo      staffInfo = staff.getInfo();
            PixelRectangle box = new PixelRectangle(
                center,
                staffInfo.getFirstLine().yAt(center) +
                (staffInfo.getHeight() / 2),
                0,
                0);
            box.grow((timeSigWidth / 2), (staffInfo.getHeight() / 2) - yOffset);

            // Draw the box, for visual debug
            SystemPart part = system.getScoreSystem()
                                    .getPartAt(box.getCenter());
            Barline    barline = part.getStartingBarline();
            Glyph      line = null;

            if (barline != null) {
                line = Glyphs.firstOf(
                    barline.getGlyphs(),
                    Barline.linePredicate);

                if (line != null) {
                    line.addAttachment("ti" + staff.getId(), box);
                }
            }

            return box;
        }
    }
}
