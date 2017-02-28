//----------------------------------------------------------------------------//
//                                                                            //
//                           S y m b o l s S t e p                            //
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
package org.audiveris.omr.step;

import org.audiveris.omr.constant.Constant;
import org.audiveris.omr.constant.ConstantSet;

import org.audiveris.omr.glyph.ui.SymbolsEditor;

import org.audiveris.omr.score.entity.ScoreSystem;
import org.audiveris.omr.score.entity.SystemPart;

import org.audiveris.omr.selection.GlyphEvent;
import org.audiveris.omr.selection.SelectionService;

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.SystemInfo;

import org.audiveris.omr.util.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * Class {@code SymbolsStep} builds symbols glyphs and
 * performs specific patterns at symbol level
 * (clefs, sharps, naturals, stems, slurs, etc).
 *
 * @author Hervé Bitteur
 */
public class SymbolsStep
        extends AbstractSystemStep
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            SymbolsStep.class);

    //~ Constructors -----------------------------------------------------------
    //-------------//
    // SymbolsStep //
    //-------------//
    /**
     * Creates a new SymbolsStep object.
     */
    public SymbolsStep ()
    {
        super(
                Steps.SYMBOLS,
                Level.SHEET_LEVEL,
                Mandatory.MANDATORY,
                DATA_TAB,
                "Apply specific glyph patterns");
    }

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // displayUI //
    //-----------//
    @Override
    public void displayUI (Sheet sheet)
    {
        SymbolsEditor editor = sheet.getSymbolsEditor();

        if (editor != null) {
            editor.refresh();
        }

        // Update glyph board if needed (to see OCR'ed data)
        SelectionService service = sheet.getNest()
                .getGlyphService();
        GlyphEvent glyphEvent = (GlyphEvent) service.getLastEvent(
                GlyphEvent.class);

        if (glyphEvent != null) {
            service.publish(glyphEvent);
        }
    }

    //----------//
    // doSystem //
    //----------//
    @Override
    public void doSystem (SystemInfo system)
            throws StepException
    {
        //        // Cleanup system sentences
        //        system.getSentences().clear();

        // Cleanup system dummy parts
        ScoreSystem scoreSystem = system.getScoreSystem();

        for (Iterator<TreeNode> it = scoreSystem.getParts()
                .iterator(); it.hasNext();) {
            SystemPart part = (SystemPart) it.next();

            if (part.isDummy()) {
                it.remove();
            }
        }

        // Iterate
        for (int iter = 1; iter <= constants.MaxPatternsIterations.getValue();
                iter++) {
            logger.debug("System#{} patterns iter #{}", system.getId(), iter);
            clearSystemErrors(system);

            if (!system.runPatterns()) {
                return; // No more progress made
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

        private final Constant.Integer MaxPatternsIterations = new Constant.Integer(
                "count",
                1,
                "Maximum number of iterations for PATTERNS task");

    }
}
