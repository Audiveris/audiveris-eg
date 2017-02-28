//----------------------------------------------------------------------------//
//                                                                            //
//                      S y m b o l s B l a c k L i s t                       //
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
package org.audiveris.omr.glyph.ui;

import org.audiveris.omr.WellKnowns;

import org.audiveris.omr.util.BlackList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class {@code SymbolsBlackList} is a special {@link BlackList} meant
 * to handle the collection of symbols as artificial glyphs.
 *
 * @author Hervé Bitteur
 */
public class SymbolsBlackList
        extends BlackList
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            SymbolsBlackList.class);

    //~ Constructors -----------------------------------------------------------
    //------------------//
    // SymbolsBlackList //
    //------------------//
    /**
     * Creates a new SymbolsBlackList object.
     */
    public SymbolsBlackList ()
    {
        super(WellKnowns.SYMBOLS_FOLDER);
    }
}
