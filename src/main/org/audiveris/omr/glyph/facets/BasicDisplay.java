//----------------------------------------------------------------------------//
//                                                                            //
//                          B a s i c D i s p l a y                           //
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
package org.audiveris.omr.glyph.facets;

import org.audiveris.omr.glyph.ui.AttachmentHolder;
import org.audiveris.omr.glyph.ui.BasicAttachmentHolder;

import org.audiveris.omr.lag.BasicSection;
import org.audiveris.omr.lag.Section;

import org.audiveris.omr.ui.Colors;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Class {@code BasicDisplay} is the basic implementation of a display
 * facet.
 *
 * @author Hervé Bitteur
 */
class BasicDisplay
        extends BasicFacet
        implements GlyphDisplay
{
    //~ Instance fields --------------------------------------------------------

    /** Potential attachments, lazily allocated */
    protected AttachmentHolder attachments;

    //~ Constructors -----------------------------------------------------------
    //--------------//
    // BasicDisplay //
    //--------------//
    /**
     * Create a new BasicDisplay object.
     *
     * @param glyph our glyph
     */
    public BasicDisplay (Glyph glyph)
    {
        super(glyph);
    }

    //~ Methods ----------------------------------------------------------------
    //---------------//
    // addAttachment //
    //---------------//
    @Override
    public void addAttachment (String id,
                               java.awt.Shape attachment)
    {
        if (attachment != null) {
            if (attachments == null) {
                attachments = new BasicAttachmentHolder();
            }

            attachments.addAttachment(id, attachment);
        }
    }

    //----------//
    // colorize //
    //----------//
    @Override
    public void colorize (Color color)
    {
        colorize(glyph.getMembers(), color);
    }

    //----------//
    // colorize //
    //----------//
    @Override
    public void colorize (Collection<Section> sections,
                          Color color)
    {
        for (Section section : sections) {
            section.setColor(color);
        }
    }

    //--------------//
    // asciiDrawing //
    //--------------//
    @Override
    public String asciiDrawing ()
    {        
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("%s%n", glyph));

        // Determine the bounding box
        Rectangle box = glyph.getBounds();

        if (box == null) {
            return sb.toString(); // Safer
        }

        // Allocate the drawing table
        char[][] table = BasicSection.allocateTable(box);

        // Register each section in turn
        for (Section section : glyph.getMembers()) {
            section.fillTable(table, box);
        }

        // Draw the result
        sb.append(BasicSection.drawingOfTable(table, box));
        
        return sb.toString();
    }

    //--------//
    // dumpOf //
    //--------//
    @Override
    public String dumpOf ()
    {
        StringBuilder sb = new StringBuilder();

        if (attachments != null) {
            sb.append(String.format("   attachments=%s%n", getAttachments()));
        }

        return sb.toString();
    }

    //----------------//
    // getAttachments //
    //----------------//
    @Override
    public Map<String, java.awt.Shape> getAttachments ()
    {
        if (attachments != null) {
            return attachments.getAttachments();
        } else {
            return Collections.emptyMap();
        }
    }

    //----------//
    // getColor //
    //----------//
    @Override
    public Color getColor ()
    {
        if (glyph.getShape() == null) {
            return Colors.SHAPE_UNKNOWN;
        } else {
            return glyph.getShape()
                    .getColor();
        }
    }

    //----------//
    // getImage //
    //----------//
    @Override
    public BufferedImage getImage ()
    {
        // Determine the bounding box
        Rectangle box = glyph.getBounds();
        BufferedImage image = new BufferedImage(
                box.width,
                box.height,
                BufferedImage.TYPE_BYTE_GRAY);

        for (Section section : glyph.getMembers()) {
            section.fillImage(image, box);
        }

        return image;
    }

    //------------//
    // recolorize //
    //------------//
    @Override
    public void recolorize ()
    {
        for (Section section : glyph.getMembers()) {
            section.resetColor();
        }
    }

    //-------------------//
    // removeAttachments //
    //-------------------//
    @Override
    public int removeAttachments (String prefix)
    {
        if (attachments != null) {
            return attachments.removeAttachments(prefix);
        } else {
            return 0;
        }
    }

    //-------------------//
    // renderAttachments //
    //-------------------//
    @Override
    public void renderAttachments (Graphics2D g)
    {
        if (attachments != null) {
            attachments.renderAttachments(g);
        }
    }
}
