//----------------------------------------------------------------------------//
//                                                                            //
//                          G l y p h D i s p l a y                           //
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

import org.audiveris.omr.lag.Section;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Interface {@code GlyphDisplay} defines the facet which handles the
 * way a glyph is displayed (its color, its image).
 *
 * @author Hervé Bitteur
 */
interface GlyphDisplay
        extends GlyphFacet, AttachmentHolder
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Report a basic representation of the glyph, using ascii chars.
     *
     * @return an ascii representation
     */
    String asciiDrawing ();

    /**
     * Set the display color of all sections that compose this glyph.
     *
     * @param color color for the whole glyph
     */
    void colorize (Color color);

    /**
     * Set the display color of all sections in provided collection.
     *
     * @param sections the collection of sections
     * @param color    the display color
     */
    void colorize (Collection<Section> sections,
                   Color color);

    /**
     * Report the color to be used to colorize the provided glyph,
     * according to the color policy which is based on the glyph shape.
     *
     * @return the related shape color of the glyph, or the predefined {@link
     * org.audiveris.omr.ui.Colors#SHAPE_UNKNOWN} if the glyph has no related shape
     */
    Color getColor ();

    /**
     * Report an image of the glyph (which can be handed to the OCR)
     *
     * @return a black & white image (contour box size )
     */
    BufferedImage getImage ();

    /**
     * Reset the display color of all sections that compose this glyph.
     */
    void recolorize ();
}
