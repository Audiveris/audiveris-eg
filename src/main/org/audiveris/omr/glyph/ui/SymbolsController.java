//----------------------------------------------------------------------------//
//                                                                            //
//                     S y m b o l s C o n t r o l l e r                      //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur and others 2000-2013. All rights reserved.      //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.glyph.ui;

import org.audiveris.omr.glyph.SymbolsModel;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.score.entity.Note;
import org.audiveris.omr.score.entity.TimeRational;

import org.audiveris.omr.script.BoundaryTask;
import org.audiveris.omr.script.RationalTask;
import org.audiveris.omr.script.SegmentTask;
import org.audiveris.omr.script.SlurTask;
import org.audiveris.omr.script.TextTask;

import org.audiveris.omr.sheet.BrokenLineContext;
import org.audiveris.omr.sheet.SystemBoundary;
import org.audiveris.omr.sheet.SystemInfo;

import org.audiveris.omr.text.TextRoleInfo;

import org.audiveris.omr.util.BrokenLine;
import org.audiveris.omr.util.VerticalSide;

import org.jdesktop.application.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Class {@code SymbolsController} is a GlyphsController specifically
 * meant for symbol glyphs, adding handling for assigning Texts, for fixing
 * Slurs and for segmenting on Stems.
 *
 * @author Hervé Bitteur
 */
public class SymbolsController
        extends GlyphsController
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            SymbolsController.class);

    /** Color for hiding unknown glyphs when filter is ON */
    public static final Color hiddenColor = Color.white;

    //~ Constructors -----------------------------------------------------------
    //-------------------//
    // SymbolsController //
    //-------------------//
    /**
     * Create a handler dedicated to symbol glyphs
     *
     * @param model the related glyphs model
     */
    public SymbolsController (SymbolsModel model)
    {
        super(model);
    }

    //~ Methods ----------------------------------------------------------------
    //----------------------//
    // asyncAssignRationals //
    //----------------------//
    /**
     * Asynchronously assign a rational value to a collection of glyphs with
     * CUSTOM_TIME_SIGNATURE shape
     *
     * @param glyphs       the impacted glyphs
     * @param timeRational the time sig rational value
     * @return the task that carries out the processing
     */
    public Task<Void, Void> asyncAssignRationals (Collection<Glyph> glyphs,
                                                  TimeRational timeRational)
    {
        return new RationalTask(sheet, timeRational, glyphs).launch(sheet);
    }

    //------------------//
    // asyncAssignTexts //
    //------------------//
    /**
     * Asynchronously assign text characteristics to a collection of textual
     * glyphs
     *
     * @param glyphs      the impacted glyphs
     * @param roleInfo    the role of this textual element
     * @param textContent the content as a string (if not empty)
     * @return the task that carries out the processing
     */
    public Task<Void, Void> asyncAssignTexts (Collection<Glyph> glyphs,
                                              TextRoleInfo roleInfo,
                                              String textContent)
    {
        return new TextTask(sheet, roleInfo, textContent, glyphs).
                launch(sheet);
    }

    //-----------------------//
    // asyncModifyBoundaries //
    //-----------------------//
    /**
     * Asynchronously perform a modification in systems boundaries
     *
     * @param modifiedLines the set of modified lines
     * @return the task that carries out the processing
     */
    public Task<Void, Void> asyncModifyBoundaries (Set<BrokenLine> modifiedLines)
    {
        List<BrokenLineContext> contexts = new ArrayList<>();

        // Retrieve impacted systems
        for (BrokenLine line : modifiedLines) {
            int above = 0;
            int below = 0;

            for (SystemInfo system : sheet.getSystems()) {
                SystemBoundary boundary = system.getBoundary();

                if (boundary.getLimit(VerticalSide.BOTTOM) == line) {
                    above = system.getId();
                } else if (boundary.getLimit(VerticalSide.TOP) == line) {
                    below = system.getId();
                }
            }

            contexts.add(new BrokenLineContext(above, below, line));
        }

        return new BoundaryTask(sheet, contexts).launch(sheet);
    }

    //--------------//
    // asyncSegment //
    //--------------//
    /**
     * Asynchronously segment a set of glyphs on their stems
     *
     * @param glyphs  glyphs to segment in order to retrieve stems
     * @param isShort looking for short (or standard) stems
     * @return the task that carries out the processing
     */
    public Task<Void, Void> asyncSegment (Collection<Glyph> glyphs,
                                          boolean isShort)
    {
        return new SegmentTask(sheet, isShort, glyphs).launch(sheet);
    }

    //----------------//
    // asyncTrimSlurs //
    //----------------//
    /**
     * Asynchronously fix a collection of glyphs as large slurs
     *
     * @param glyphs the slur glyphs to fix
     * @return the task that carries out the processing
     */
    public Task<Void, Void> asyncTrimSlurs (Collection<Glyph> glyphs)
    {
        return new SlurTask(sheet, glyphs).launch(sheet);
    }

    //----------//
    // getModel //
    //----------//
    /**
     * Report the underlying model
     *
     * @return the underlying glyphs model
     */
    @Override
    public SymbolsModel getModel ()
    {
        return (SymbolsModel) model;
    }

    //------------------//
    // showTranslations //
    //------------------//
    public void showTranslations (Collection<Glyph> glyphs)
    {
        for (Glyph glyph : glyphs) {
            for (Object entity : glyph.getTranslations()) {
                if (entity instanceof Note) {
                    Note note = (Note) entity;
                    logger.info("{}->{}", note, note.getChord());
                } else {
                    logger.info(entity.toString());
                }
            }
        }
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        return getClass().getSimpleName();
    }
}
