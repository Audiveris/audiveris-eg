//----------------------------------------------------------------------------//
//                                                                            //
//                            E x p o r t T a s k                             //
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
 * Class {@code ExportTask} exports score entities to a MusicXML file
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ExportTask
        extends ScriptTask
{
    //~ Instance fields --------------------------------------------------------

    /** The file used for export */
    @XmlAttribute
    private String path;

    /** Should we add our signature? */
    @XmlAttribute(name = "inject-signature")
    private Boolean injectSignature;

    //~ Constructors -----------------------------------------------------------
    //------------//
    // ExportTask //
    //------------//
    /**
     * Create a task to export the related score entities of a sheet
     *
     * @param path the full path of the export file
     */
    public ExportTask (String path)
    {
        this.path = path;
    }

    //------------//
    // ExportTask //
    //------------//
    /** No-arg constructor needed by JAXB */
    private ExportTask ()
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
                .export(
                sheet.getScore(),
                (path != null) ? new File(path) : null,
                injectSignature);
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        return " export " + path + super.internalsString();
    }
}
