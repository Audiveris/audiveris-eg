//----------------------------------------------------------------------------//
//                                                                            //
//                          G l o b a l F i l t e r                           //
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

import org.audiveris.omr.constant.Constant;
import org.audiveris.omr.constant.ConstantSet;

import net.jcip.annotations.ThreadSafe;

/**
 * Class {@code GlobalFilter} implements Interface
 * {@code PixelFilter} by using a global threshold on pixel value.
 *
 * @author Hervé Bitteur
 */
@ThreadSafe
public class GlobalFilter
        extends SourceWrapper
        implements PixelFilter
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    //~ Instance fields --------------------------------------------------------
    //
    /** Global threshold. */
    private final int threshold;

    //~ Constructors -----------------------------------------------------------
    //
    //--------------//
    // GlobalFilter //
    //--------------//
    /**
     * Create a binary wrapper on a raw pixel source.
     *
     * @param source    the underlying source of raw pixels
     * @param threshold maximum gray level of foreground pixel
     */
    public GlobalFilter (PixelSource source,
                         int threshold)
    {
        super(source);
        this.threshold = threshold;
    }

    //~ Methods ----------------------------------------------------------------
    //----------------------//
    // getDefaultDescriptor //
    //----------------------//
    public static FilterDescriptor getDefaultDescriptor ()
    {
        return GlobalDescriptor.getDefault();
    }

    //---------------------//
    // getDefaultThreshold //
    //---------------------//
    public static int getDefaultThreshold ()
    {
        return constants.defaultThreshold.getValue();
    }

    //---------------------//
    // setDefaultThreshold //
    //---------------------//
    public static void setDefaultThreshold (int threshold)
    {
        constants.defaultThreshold.setValue(threshold);
    }

    //
    //------------//
    // getContext //
    //------------//
    @Override
    public Context getContext (int x,
                               int y)
    {
        return new Context(threshold);
    }

    //
    // -------//
    // isFore //
    // -------//
    @Override
    public boolean isFore (int x,
                           int y)
    {
        return source.getPixel(x, y) <= threshold;
    }

    //~ Inner Classes ----------------------------------------------------------
    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
            extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        Constant.Integer defaultThreshold = new Constant.Integer(
                "GrayLevel",
                140,
                "Default threshold value (in 0..255)");

    }
}
