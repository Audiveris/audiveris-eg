//----------------------------------------------------------------------------//
//                                                                            //
//                            R e m o v e T a s k                             //
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
package org.audiveris.omr.script;

import org.audiveris.omr.sheet.Sheet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Class {@code RemoveTask} removes a page from its containing score
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
public class RemoveTask
        extends SheetTask
{
    //~ Constructors -----------------------------------------------------------

    //------------//
    // RemoveTask //
    //------------//
    /**
     * Create a task to remove a page from its score
     *
     * @param sheet the sheet/page to remove
     */
    public RemoveTask (Sheet sheet)
    {
        super(sheet);
    }

    //------------//
    // RemoveTask //
    //------------//
    /** No-arg constructor for JAXB only */
    private RemoveTask ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //------//
    // core //
    //------//
    @Override
    public void core (Sheet sheet)
    {
        sheet.remove(false);
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        return super.internalsString() + " remove";
    }
}
