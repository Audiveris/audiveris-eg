//----------------------------------------------------------------------------//
//                                                                            //
//                              P a r t I n f o                               //
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

import org.audiveris.omr.grid.StaffInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code PartInfo} is the physical gathering of StaffInfo
 * instances in a part.
 *
 * @author Hervé Bitteur
 */
public class PartInfo
{
    //~ Instance fields --------------------------------------------------------

    /** Staves in this part */
    private List<StaffInfo> staves = new ArrayList<>();

    //~ Constructors -----------------------------------------------------------
    //----------//
    // PartInfo //
    //----------//
    /** Creates a new instance of PartInfo */
    public PartInfo ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //----------//
    // addStaff //
    //----------//
    public void addStaff (StaffInfo staffInfo)
    {
        staves.add(staffInfo);
    }

    //-----------//
    // getStaves //
    //-----------//
    public List<StaffInfo> getStaves ()
    {
        return staves;
    }
}
