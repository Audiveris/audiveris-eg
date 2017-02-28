//----------------------------------------------------------------------------//
//                                                                            //
//                             P r i n t T a s k                              //
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

import org.audiveris.omr.score.ScoresManager;

import org.audiveris.omr.sheet.Sheet;

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Class {@code PrintTask} prints a score to a PDFfile
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
public class PrintTask
        extends ScriptTask
{
    //~ Instance fields --------------------------------------------------------

    /** The file used for print */
    @XmlAttribute
    private String path;

    //~ Constructors -----------------------------------------------------------
    //------------//
    // PrintTask //
    //------------//
    /**
     * Create a task to print the score to a PDF file
     *
     * @param path the full path of the PDF file
     */
    public PrintTask (String path)
    {
        this.path = path;
    }

    //------------//
    // PrintTask //
    //------------//
    /** No-arg constructor needed by JAXB */
    private PrintTask ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //------//
    // core //
    //------//
    @Override
    public void core (Sheet sheet)
    {
        ScoresManager.getInstance()
                .writePhysicalPdf(
                sheet.getScore(),
                (path != null) ? new File(path) : null);
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        return " print " + path + super.internalsString();
    }
}
