//----------------------------------------------------------------------------//
//                                                                            //
//                            V e r t e x V i e w                             //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
///
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
package org.audiveris.omr.graph;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Interface {@code VertexView} defines the interface needed to handle
 * the rendering of a vertex.
 *
 * @author Hervé Bitteur
 */
public interface VertexView
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Return the display rectangle used by the rendering of the vertex
     *
     * @return the bounding rectangle in the display space
     */
    Rectangle getBounds ();

    /**
     * Render the vertex
     *
     * @param g           the graphics context
     * @param drawBorders should vertex borders be drawn
     * @return true if actually rendered, i.e. is displayed
     */
    boolean render (Graphics g,
                    boolean drawBorders);
}
