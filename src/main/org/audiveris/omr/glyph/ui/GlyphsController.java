//----------------------------------------------------------------------------//
//                                                                            //
//                      G l y p h s C o n t r o l l e r                       //
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
package org.audiveris.omr.glyph.ui;

import org.audiveris.omr.glyph.Evaluation;
import org.audiveris.omr.glyph.Glyphs;
import org.audiveris.omr.glyph.GlyphsModel;
import org.audiveris.omr.glyph.Nest;
import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.ShapeSet;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.script.AssignTask;
import org.audiveris.omr.script.BarlineTask;
import org.audiveris.omr.script.DeleteTask;

import org.audiveris.omr.selection.GlyphEvent;
import org.audiveris.omr.selection.SelectionHint;
import org.audiveris.omr.selection.SelectionService;

import org.audiveris.omr.sheet.Sheet;

import org.jdesktop.application.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

/**
 * Class {@code GlyphsController} is a common basis for glyph handling,
 * used by any user interface which needs to act on the actual glyph
 * data.
 *
 * <p>There are two main methods in this class ({@link #asyncAssignGlyphs} and
 * {@link #asyncDeassignGlyphs}). They share common characteristics:
 * <ul>
 * <li>They are processed asynchronously</li>
 * <li>Their action is recorded in the sheet script</li>
 * <li>They update the following steps, if any</li>
 * </ul>
 *
 * <p>Since the bus of user selections is used, the methods of this class are
 * meant to be used from within a user action, otherwise you must use a direct
 * access to similar synchronous actions in the underlying {@link GlyphsModel}.
 *
 * @author Hervé Bitteur
 */
public class GlyphsController
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            GlyphsController.class);

    //~ Instance fields --------------------------------------------------------
    /** Related model */
    protected final GlyphsModel model;

    /** Cached sheet */
    protected final Sheet sheet;

    //~ Constructors -----------------------------------------------------------
    //------------------//
    // GlyphsController //
    //------------------//
    /**
     * Create an instance of GlyphsController, with its underlying
     * GlyphsModel instance.
     *
     * @param model the related glyphs model
     */
    public GlyphsController (GlyphsModel model)
    {
        this.model = model;

        sheet = model.getSheet();
    }

    //~ Methods ----------------------------------------------------------------
    //-------------------//
    // asyncAssignGlyphs //
    //-------------------//
    /**
     * Asynchronouly assign a shape to the selected collection of glyphs
     * and record this action in the script.
     *
     * @param glyphs   the collection of glyphs to be assigned
     * @param shape    the shape to be assigned
     * @param compound flag to build one compound, rather than assign each
     *                 individual glyph
     * @return the task that carries out the processing
     */
    public Task<Void, Void> asyncAssignGlyphs (Collection<Glyph> glyphs,
                                               Shape shape,
                                               boolean compound)
    {
        // Safety check: we cannot alter virtual glyphs
        for (Glyph glyph : glyphs) {
            if (glyph.isVirtual()) {
                logger.warn("Cannot alter VirtualGlyph#{}", glyph.getId());

                return null;
            }
        }

        if (ShapeSet.Barlines.contains(shape)
            || Glyphs.containsBarline(glyphs)) {
            // Special case for barlines assignment or deassignment
            return new BarlineTask(sheet, shape, compound, glyphs).launch(
                    sheet);
        } else {
            // Normal symbol processing
            return new AssignTask(sheet, shape, compound, glyphs).launch(sheet);
        }
    }

    //---------------------//
    // asyncDeassignGlyphs //
    //---------------------//
    /**
     * Asynchronously de-Assign a collection of glyphs and record this
     * action in the script.
     *
     * @param glyphs the collection of glyphs to be de-assigned
     * @return the task that carries out the processing
     */
    public Task<Void, Void> asyncDeassignGlyphs (Collection<Glyph> glyphs)
    {
        return asyncAssignGlyphs(glyphs, null, false);
    }

    //--------------------------//
    // asyncDeleteVirtualGlyphs //
    //--------------------------//
    public Task<Void, Void> asyncDeleteVirtualGlyphs (Collection<Glyph> glyphs)
    {
        return new DeleteTask(sheet, glyphs).launch(sheet);
    }

    //--------------//
    // getGlyphById //
    //--------------//
    /**
     * Retrieve a glyph, knowing its id.
     *
     * @param id the glyph id
     * @return the glyph found, or null if not
     */
    public Glyph getGlyphById (int id)
    {
        return model.getGlyphById(id);
    }

    //----------------//
    // getLatestShape //
    //----------------//
    /**
     * Report the latest non null shape that was assigned, or null
     * if none.
     *
     * @return latest shape assigned, or null if none
     */
    public Shape getLatestShapeAssigned ()
    {
        return model.getLatestShape();
    }

    //--------------------//
    // getLocationService //
    //--------------------//
    /**
     * Report the event service to use for LocationEvent.
     * When no sheet is available, override this method to point to another
     * service
     *
     * @return the event service to use for LocationEvent
     */
    public SelectionService getLocationService ()
    {
        return model.getSheet()
                .getLocationService();
    }

    //----------//
    // getModel //
    //----------//
    /**
     * Report the underlying model.
     *
     * @return the underlying glpyhs model
     */
    public GlyphsModel getModel ()
    {
        return model;
    }

    //---------//
    // getNest //
    //---------//
    /**
     * Report the underlying glyph nest.
     *
     * @return the related glyph nest
     */
    public Nest getNest ()
    {
        return model.getNest();
    }

    //----------------//
    // setLatestShape //
    //----------------//
    /**
     * Assign the latest shape.
     *
     * @param shape the latest shape
     */
    public void setLatestShapeAssigned (Shape shape)
    {
        model.setLatestShape(shape);
    }

    //------------//
    // syncAssign //
    //------------//
    /**
     * Process synchronously the assignment defined in the provided
     * context.
     *
     * @param context the context of the assignment
     */
    public void syncAssign (AssignTask context)
    {
        final boolean compound = context.isCompound();
        final Shape shape = context.getAssignedShape();
        logger.debug("syncAssign {} compound:{}", context, compound);

        Set<Glyph> glyphs = context.getInitialGlyphs();

        if (shape != null) { // Assignment
            // Persistent?
            model.assignGlyphs(
                    glyphs,
                    context.getAssignedShape(),
                    compound,
                    Evaluation.MANUAL);

            // Publish modifications (about new glyph)
            Glyph firstGlyph = glyphs.iterator()
                    .next();

            if (firstGlyph != null) {
                publish(firstGlyph.getMembers().first().getGlyph());
            }
        } else { // Deassignment
            model.deassignGlyphs(glyphs);

            // Publish modifications (about current glyph)
            publish(glyphs.iterator().next());
        }
    }

    //------------//
    // syncDelete //
    //------------//
    /**
     * Process synchronously the deletion defined in the provided
     * context.
     *
     * @param context the context of the deletion
     */
    public void syncDelete (DeleteTask context)
    {
        logger.debug("syncDelete{}", context);

        model.deleteGlyphs(context.getInitialGlyphs());

        publish((Glyph) null);
    }

    //---------//
    // publish //
    //---------//
    protected void publish (Glyph glyph)
    {
        // Update immediately the glyph info as displayed
        if (model.getSheet() != null) {
            getNest()
                    .getGlyphService()
                    .publish(
                    new GlyphEvent(this, SelectionHint.GLYPH_MODIFIED, null, glyph));
        }
    }
}
