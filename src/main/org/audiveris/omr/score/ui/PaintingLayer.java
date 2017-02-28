//----------------------------------------------------------------------------//
//                                                                            //
//                         P a i n t i n g L a y e r                          //
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
package org.audiveris.omr.score.ui;

/**
 * Enum {@code PaintingLayer} defines layers to be painted
 */
public enum PaintingLayer
{

    /** Input data: image or glyphs */
    INPUT("layer-input.png"),
    /** Both input and output */
    INPUT_OUTPUT("layer-input-output.png"),
    /** Output data: score entities */
    OUTPUT("layer-output.png");

    /**
     * Creates a new PaintingLayer object.
     *
     * @param imageName name of the related image
     */
    PaintingLayer (String imageName)
    {
        this.imageName = imageName;
    }

    private final String imageName;

    public String getImageName ()
    {
        return imageName;
    }
}
