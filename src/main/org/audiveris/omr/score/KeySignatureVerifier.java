//----------------------------------------------------------------------------//
//                                                                            //
//                  K e y S i g n a t u r e V e r i f i e r                   //
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
package org.audiveris.omr.score;

import org.audiveris.omr.constant.ConstantSet;

import org.audiveris.omr.glyph.Evaluation;
import org.audiveris.omr.glyph.GlyphNetwork;
import org.audiveris.omr.glyph.Glyphs;
import org.audiveris.omr.glyph.Grades;
import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.grid.StaffInfo;

import org.audiveris.omr.math.GeoUtil;

import org.audiveris.omr.score.entity.Barline;
import org.audiveris.omr.score.entity.KeySignature;
import org.audiveris.omr.score.entity.Measure;
import org.audiveris.omr.score.entity.ScoreSystem;
import org.audiveris.omr.score.entity.Staff;
import org.audiveris.omr.score.entity.SystemPart;

import org.audiveris.omr.sheet.Scale;
import org.audiveris.omr.sheet.SystemInfo;

import org.audiveris.omr.util.Predicate;
import org.audiveris.omr.util.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Rectangle;
import java.util.Collection;

/**
 * Class {@code KeySignatureVerifier} verifies, at system level, that all
 * vertical measures exhibit the same key signature, and correct them if
 * necessary.
 *
 * @author Herv� Bitteur
 */
public class KeySignatureVerifier
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            KeySignatureVerifier.class);

    //~ Instance fields --------------------------------------------------------
    // The system concerned
    private final ScoreSystem system;

    // Total number of staves in the system
    private final int staffNb;

    //~ Constructors -----------------------------------------------------------
    //----------------------//
    // KeySignatureVerifier //
    //----------------------//
    /**
     * Creates a new KeySignatureVerifier object.
     *
     * @param system the system at hand
     */
    public KeySignatureVerifier (ScoreSystem system)
    {
        this.system = system;
        staffNb = system.getInfo()
                .getStaves()
                .size();
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // verifyKeys //
    //------------//
    /**
     * Perform verifications (and corrections when possible) when all keysigs
     * have been generated for the system: The key signature must be the same
     * (in terms of fifths) for the same measure index in all parts and staves.
     */
    public void verifyKeys ()
    {
        logger.debug(
                "\n------------------------------------------------------");
        logger.debug("verifySystemKeys for {}", system);

        // Number of measures in the system
        final int measureNb = system.getFirstPart()
                .getMeasures()
                .size();

        // Verify each measure index on turn
        for (int im = 0; im < measureNb; im++) {
            logger.debug("measure index ={}", im);
            verifyVerticalMeasure(im);
        }

        logger.debug(
                "\n======================================================");
    }

    //-------------//
    // checkKeySig //
    //-------------//
    private Glyph checkKeySig (Collection<Glyph> glyphs,
                               final KeySignature bestKey)
    {
        logger.debug(
                "Merging {} for shape {}",
                Glyphs.toString(glyphs),
                bestKey.getShape());

        SystemInfo systemInfo = system.getInfo();
        Glyphs.purgeManuals(glyphs);

        if (glyphs.isEmpty()) {
            return null;
        }

        Glyph compound = systemInfo.buildTransientCompound(glyphs);

        // Check if a proper key sig appears in the top evaluations
        Evaluation vote = GlyphNetwork.getInstance()
                .rawVote(
                compound,
                Grades.keySigMinGrade,
                new Predicate<Shape>()
        {
            @Override
            public boolean check (Shape shape)
            {
                return shape == bestKey.getShape();
            }
        });

        if (vote != null) {
            // We now have a key sig!
            logger.debug(
                    "{} built from {}",
                    vote.shape,
                    Glyphs.toString(glyphs));
            compound = systemInfo.addGlyph(compound);
            compound.setShape(vote.shape, Evaluation.ALGORITHM);

            return compound;
        } else {
            logger.debug(
                    "{}Could not find {} in {}",
                    systemInfo.getLogPrefix(),
                    bestKey.getShape(),
                    Glyphs.toString(glyphs));

            return null;
        }
    }

    //------------------//
    // getContextString //
    //------------------//
    private String getContextString (int measureIndex,
                                     int systemStaffIndex)
    {
        return system.getContextString() + "M" + (measureIndex + 1) + "F"
               + staffOf(systemStaffIndex)
                .getId();
    }

    //--------------//
    // getMeasureOf //
    //--------------//
    private Measure getMeasureOf (int staffIndex,
                                  int measureIndex)
    {
        int staffOffset = 0;

        for (TreeNode node : system.getParts()) {
            SystemPart part = (SystemPart) node;
            staffOffset += part.getStaves()
                    .size();

            if (staffIndex < staffOffset) {
                return (Measure) part.getMeasures()
                        .get(measureIndex);
            }
        }

        logger.error("Illegal systemStaffIndex: {}", staffIndex);

        return null;
    }

    //-------------//
    // harmonizeTo //
    //-------------//
    private void harmonizeTo (KeySignature bestKey,
                              KeySignature[] keyVector,
                              int iMeasure)
    {
        Rectangle bestBox = bestKey.getBox();

        for (int iStaff = 0; iStaff < staffNb; iStaff++) {
            KeySignature ks = keyVector[iStaff];

            // Is this staff OK?
            if ((ks != null) && ks.getKey()
                    .equals(bestKey.getKey())) {
                continue;
            }

            Staff staff = staffOf(iStaff);
            StaffInfo staffInfo = staff.getInfo();

            logger.debug(
                    "{} Forcing key signature to {}",
                    getContextString(iMeasure, iStaff),
                    bestKey.getKey());

            try {
                // Define the box to intersect keysig glyph(s)
                int xCenter = bestBox.x + (bestBox.width / 2);
                Rectangle inner = new Rectangle(
                        xCenter,
                        staffInfo.getFirstLine().yAt(xCenter)
                        + (staffInfo.getHeight() / 2),
                        0,
                        0);
                inner.grow((bestBox.width / 2), (staffInfo.getHeight() / 2));

                // Draw the box, for visual debug
                SystemPart part = system.getPartAt(GeoUtil.centerOf(inner));
                Barline barline = part.getStartingBarline();

                if (barline != null) {
                    Glyph line = Glyphs.firstOf(
                            barline.getGlyphs(),
                            Barline.linePredicate);

                    if (line != null) {
                        line.addAttachment("k" + staff.getId(), inner);
                    }
                }

                // We now must find a key sig out of these glyphs
                Collection<Glyph> glyphs = system.getInfo()
                        .lookupIntersectedGlyphs(
                        inner);

                Glyph compound = checkKeySig(glyphs, bestKey);

                if (compound != null) {
                    if (ks != null) {
                        ks.getParent()
                                .getChildren()
                                .remove(ks);
                    }

                    Measure measure = getMeasureOf(iStaff, iMeasure);
                    ks = new KeySignature(measure, staff);
                    ks.addGlyph(compound);
                }
            } catch (Exception ex) {
                logger.warn("Cannot copy key", ex);
                ks.addError("Cannot copy key");
            }

            // TODO deassign glyphs that do not contribute to the key ?
        }
    }

    //---------//
    // staffOf //
    //---------//
    private Staff staffOf (int systemStaffIndex)
    {
        int staffOffset = 0;

        for (TreeNode node : system.getParts()) {
            SystemPart part = (SystemPart) node;
            int partStaffNb = part.getStaves()
                    .size();
            staffOffset += partStaffNb;

            if (systemStaffIndex < staffOffset) {
                return (Staff) part.getStaves()
                        .get(
                        (partStaffNb + systemStaffIndex) - staffOffset);
            }
        }

        logger.error("Illegal systemStaffIndex: {}", systemStaffIndex);

        return null;
    }

    //-----------------------//
    // verifyVerticalMeasure //
    //-----------------------//
    private void verifyVerticalMeasure (int im)
    {
        // Retrieve a key, if any, for this measure in each staff
        KeySignature[] keyVector = new KeySignature[staffNb];
        int staffOffset = 0;
        boolean keyFound = false;

        for (TreeNode node : system.getParts()) {
            SystemPart part = (SystemPart) node;
            Measure measure = (Measure) part.getMeasures()
                    .get(im);

            for (TreeNode ksnode : measure.getKeySignatures()) {
                KeySignature ks = (KeySignature) ksnode;
                keyFound = true;

                keyVector[ks.getStaff()
                        .getId() - 1 + staffOffset] = ks;
            }

            staffOffset += part.getStaves()
                    .size();
        }

        // Some keys found in this vertical measure?
        if (keyFound) {
            logger.debug(
                    "{} key(s) found in M{}",
                    system.getContextString(),
                    im);

            // Browse all staves for sharp/flat compatibility
            // If compatible, adjust all keysigs to the longest
            boolean compatible = true;
            boolean adjustment = false;
            KeySignature bestKey = null;

            for (int iStaff = 0; iStaff < staffNb; iStaff++) {
                KeySignature ks = keyVector[iStaff];

                if (ks == null) {
                    logger.debug("Key signatures will need to be created");
                    adjustment = true;
                } else if (bestKey == null) {
                    bestKey = ks;
                } else if (!bestKey.getKey()
                        .equals(ks.getKey())) {
                    logger.debug("Key signatures will need adjustment");
                    adjustment = true;

                    if ((ks.getKey() * bestKey.getKey()) < 0) {
                        logger.debug("Non compatible key signatures");

                        compatible = false;

                        break;
                    } else if (Math.abs(bestKey.getKey()) < Math.abs(
                            ks.getKey())) {
                        // Keep longest key
                        bestKey = ks;
                    }
                }
            }

            // Force key signatures to this value, if compatible
            if (compatible && adjustment) {
                harmonizeTo(bestKey, keyVector, im);
            }
        }
    }

    //~ Inner Classes ----------------------------------------------------------
    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
            extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        Scale.Fraction yOffset = new Scale.Fraction(
                0.5d,
                "Key signature vertical offset since staff line");

    }
}
