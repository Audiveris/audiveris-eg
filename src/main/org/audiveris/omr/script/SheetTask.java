//----------------------------------------------------------------------------//
//                                                                            //
//                             S h e e t T a s k                              //
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

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Class {@code SheetTask} is a {@link ScriptTask} that applies to a
 * given sheet.
 *
 * @author Hervé Bitteur
 */
public abstract class SheetTask
    extends ScriptTask
{
    //~ Instance fields --------------------------------------------------------

    /** Page index */
    @XmlAttribute(name = "page")
    protected Integer page;

    /** The related sheet */
    protected Sheet sheet;

    //~ Constructors -----------------------------------------------------------

    //-----------//
    // SheetTask //
    //-----------//
    /**
     * Creates a new SheetTask object.
     */
    protected SheetTask (Sheet sheet)
    {
        page = sheet.getPage()
                    .getIndex();
    }

    //-----------//
    // SheetTask //
    //-----------//
    /** No-arg constructor for JAXB only */
    protected SheetTask ()
    {
    }

    //~ Methods ----------------------------------------------------------------

    //--------------//
    // getPageIndex //
    //--------------//
    /**
     * Report the id of the page/sheet if any
     * @return the sheet index (counted from 1) or null if none
     */
    public Integer getPageIndex ()
    {
        return page;
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder(super.internalsString());

        if (page != null) {
            sb.append(" page#")
              .append(page);
        }

        return sb.toString();
    }
}
