//----------------------------------------------------------------------------//
//                                                                            //
//                               B a r I n f o                                //
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
package org.audiveris.omr.grid;

import org.audiveris.omr.glyph.facets.Glyph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Class {@code BarInfo} records the physical information about a bar
 * line, used especially as a vertical limit for a staff or system.
 *
 * @author Hervé Bitteur
 */
public class BarInfo
{
    //~ Instance fields --------------------------------------------------------

    /** Composing sticks, ordered by their relative abscissa. */
    private List<Glyph> sticks;

    //~ Constructors -----------------------------------------------------------
    //---------//
    // BarInfo //
    //---------//
    /**
     * Creates a new BarInfo object.
     *
     * @param sticks one or several physical bars, from left to right
     */
    public BarInfo (Glyph... sticks)
    {
        this(Arrays.asList(sticks));
    }

    //---------//
    // BarInfo //
    //---------//
    /**
     * Creates a new BarInfo object.
     *
     * @param sticks one or several physical bars, from left to right
     */
    public BarInfo (Collection<? extends Glyph> sticks)
    {
        setSticks(sticks);
    }

    //~ Methods ----------------------------------------------------------------
    //--------------------//
    // getSticksAncestors //
    //--------------------//
    public List<Glyph> getSticksAncestors ()
    {
        List<Glyph> list = new ArrayList<>(sticks.size());

        for (Glyph stick : sticks) {
            list.add(stick.getAncestor());
        }

        return list;
    }

    //-----------//
    // setSticks //
    //-----------//
    public final void setSticks (Collection<? extends Glyph> sticks)
    {
        this.sticks = new ArrayList<>(sticks); // Copy
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder("{BarInfo");

        for (Glyph stick : sticks) {
            sb.append(" #")
                    .append(stick.getAncestor().getId());
        }

        sb.append("}");

        return sb.toString();
    }
}
