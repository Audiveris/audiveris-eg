//----------------------------------------------------------------------------//
//                                                                            //
//                                 V i p U t i l                              //
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
package org.audiveris.omr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code VipUtil} gathers convenient methods related to Vip
 * handling.
 *
 * @author Hervé Bitteur
 */
public class VipUtil
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(VipUtil.class);

    //~ Methods ----------------------------------------------------------------
    //-----------//
    // decodeIds //
    //-----------//
    /**
     * Parse a string of integer ids, separated by comma.
     *
     * @param str the string to parse
     * @return tha sequence of ids decoded
     */
    public static List<Integer> decodeIds (String str)
    {
        List<Integer> ids = new ArrayList<>();

        // Retrieve the list of ids
        String[] tokens = str.split("\\s*,\\s*");

        for (String token : tokens) {
            try {
                String trimmedToken = token.trim();
                if (!trimmedToken.isEmpty()) {
                    ids.add(Integer.decode(trimmedToken));
                }
            } catch (Exception ex) {
                logger.warn("Illegal glyph id", ex);
            }
        }

        return ids;
    }
}
