//----------------------------------------------------------------------------//
//                                                                            //
//                            P i x e l C o u n t                             //
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
package org.audiveris.omr.ui;

import org.audiveris.omr.constant.Constant;

/**
 * A subclass of Constant.Integer, meant to store a number of pixels.
 */
public class PixelCount
        extends Constant.Integer
{
    //~ Constructors -----------------------------------------------------------

    //------------//
    // PixelCount //
    //------------//
    /**
     * Specific constructor, where 'unit' and 'name' are assigned later.
     *
     * @param defaultValue the (int) default value
     * @param description  the semantic of the constant
     */
    public PixelCount (int defaultValue,
                       java.lang.String description)
    {
        super("Pixels", defaultValue, description);
    }
}
