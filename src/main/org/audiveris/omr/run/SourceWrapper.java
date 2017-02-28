//----------------------------------------------------------------------------//
//                                                                            //
//                          S o u r c e W r a p p e r                         //
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
package org.audiveris.omr.run;

/**
 * Class {@code SourceWrapper} wraps a PixelSource.
 *
 * @author Hervé Bitteur
 */
public class SourceWrapper
        implements PixelSource
{
    //~ Instance fields --------------------------------------------------------

    /** Underlying pixel source. */
    protected final PixelSource source;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new SourceWrapper object.
     *
     * @param source DOCUMENT ME!
     */
    public SourceWrapper (PixelSource source)
    {
        this.source = source;
    }

    //~ Methods ----------------------------------------------------------------
    //
    //-----------//
    // getHeight //
    //-----------//
    @Override
    public int getHeight ()
    {
        return source.getHeight();
    }

    //----------//
    // getPixel //
    //----------//
    @Override
    public int getPixel (int x,
                         int y)
    {
        return source.getPixel(x, y);
    }

    //----------//
    // getWidth //
    //----------//
    @Override
    public int getWidth ()
    {
        return source.getWidth();
    }
}
