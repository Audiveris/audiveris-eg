//----------------------------------------------------------------------------//
//                                                                            //
//                          T e x t R o l e I n f o                           //
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
package org.audiveris.omr.text;

import org.audiveris.omr.score.entity.Text;
import org.audiveris.omr.score.entity.Text.CreatorText.CreatorType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class {@code TextRoleInfo} gathers information about the role of a
 * piece of text (typically a sentence).
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "text-role-info")
public class TextRoleInfo
{
    //~ Instance fields --------------------------------------------------------

    /** Role of this piece of text. */
    @XmlAttribute(name = "role")
    public final TextRole role;

    /** Specific type for creator role. */
    @XmlAttribute(name = "creator-type")
    public final Text.CreatorText.CreatorType creatorType;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new TextRoleInfo object.
     *
     * @param role        DOCUMENT ME!
     * @param creatorType DOCUMENT ME!
     */
    public TextRoleInfo (TextRole role,
                         CreatorType creatorType)
    {
        this.role = role;
        this.creatorType = creatorType;
    }

    //--------------//
    /**
     * Creates a new TextRoleInfo object.
     *
     * @param role DOCUMENT ME!
     */
    public TextRoleInfo (TextRole role)
    {
        this(role, null);
    }

    //--------------//
    // TextRoleInfo // For JAXB only.
    //--------------//
    private TextRoleInfo ()
    {
        this(null, null);
    }

    //~ Methods ----------------------------------------------------------------
    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(" role:")
                .append(role);

        if (creatorType != null) {
            sb.append("/")
                    .append(creatorType);
        }

        return sb.toString();
    }
}
