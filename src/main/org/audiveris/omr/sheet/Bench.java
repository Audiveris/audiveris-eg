//----------------------------------------------------------------------------//
//                                                                            //
//                                 B e n c h                                  //
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
package org.audiveris.omr.sheet;

import org.audiveris.omr.score.ScoreBench;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Class {@code Bench} defines the general features of a bench, used 
 * by each individual {@link SheetBench} and the containing {@link
 * ScoreBench}.
 *
 * @author Hervé Bitteur
 */
public abstract class Bench
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(Bench.class);

    //~ Instance fields --------------------------------------------------------
    /** The internal set of properties */
    protected final Properties props = new Properties();

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new Bench object.
     */
    public Bench ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //---------//
    // addProp //
    //---------//
    /**
     * This is a specific setProperty functionality, that creates unique
     * keys by appending numbered suffixes
     *
     * @param radix the provided radix (to which proper suffix will be appended)
     * @param value the property value
     */
    protected void addProp (String radix,
                            String value)
    {
        if ((value == null) || (value.length() == 0)) {
            return;
        }

        String key = null;
        int index = 0;

        do {
            key = keyOf(radix, ++index);
        } while (props.containsKey(key));

        props.setProperty(key, value);

        logger.debug("addProp key:{} value:{}", key, value);
    }

    //------------//
    // flushBench //
    //------------//
    /**
     * Flush the current content of bench to disk
     */
    protected abstract void flushBench ();

    //-------//
    // keyOf //
    //-------//
    protected String keyOf (String radix,
                            int index)
    {
        return String.format("%s.%02d", radix, index);
    }
}
