//----------------------------------------------------------------------------//
//                                                                            //
//                           S e c t i o n V i e w                            //
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
package org.audiveris.omr.lag.ui;

import org.audiveris.omr.graph.VertexView;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Class {@code SectionView} defines one view meant for display of a
 * given section.
 *
 * @author Hervé Bitteur
 */
public interface SectionView
        extends VertexView
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Report the default color. This is the permanent default, which is used
     * when the color is reset by {@link #resetColor}
     *
     * @return the section default color
     */
    public Color getDefaultColor ();

    /**
     * Report whether a default color has been assigned
     *
     * @return true if defaultColor is no longer null
     */
    public boolean isColorized ();

    /**
     * Render the section using the provided graphics object, while
     * showing that the section has been selected.
     *
     * @param g the graphics environment (which may be applying transformation
     *          such as scale)
     * @return true if the section is concerned by the clipping rectangle, which
     *         means if (part of) the section has been drawn
     */
    public boolean renderSelected (Graphics g);

    /**
     * Allow to reset to default the display color of a given section
     */
    public void resetColor ();

    /**
     * Allow to modify the display color of a given section.
     *
     * @param color the new color
     */
    public void setColor (Color color);

    /**
     * Set the default color.
     *
     * @param color the default color for this section
     */
    public void setDefaultColor (Color color);
}
