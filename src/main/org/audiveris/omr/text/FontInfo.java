//----------------------------------------------------------------------------//
//                                                                            //
//                              F o n t I n f o                               //
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

/**
 * Non-mutable font attributes (generally for a word).
 */
public class FontInfo
{
    //~ Static fields/initializers ---------------------------------------------

    /** A default FontInfo instance when we absolutely need one. */
    public static final FontInfo DEFAULT = createDefault(36);

    //~ Instance fields --------------------------------------------------------
    /** True if bold. */
    public final boolean isBold;

    /** True if italic. */
    public final boolean isItalic;

    /** True if underlined. */
    public final boolean isUnderlined;

    /** True if monospace. */
    public final boolean isMonospace;

    /** True if serif. */
    public final boolean isSerif;

    /** True if small capitals. */
    public final boolean isSmallcaps;

    /** Font size, specified in printer points (1/72 inch). */
    public final int pointsize;

    /** Font name. */
    public final String fontName;

    //~ Constructors -----------------------------------------------------------
    //
    //----------//
    // FontInfo //
    //----------//
    /**
     * Creates a new FontInfo object.
     *
     * @param isBold
     * @param isItalic
     * @param isUnderlined
     * @param isMonospace
     * @param isSerif
     * @param isSmallcaps
     * @param pointsize
     * @param fontName
     */
    public FontInfo (boolean isBold,
                     boolean isItalic,
                     boolean isUnderlined,
                     boolean isMonospace,
                     boolean isSerif,
                     boolean isSmallcaps,
                     int pointsize,
                     String fontName)
    {
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isUnderlined = isUnderlined;
        this.isMonospace = isMonospace;
        this.isSerif = isSerif;
        this.isSmallcaps = isSmallcaps;
        this.pointsize = pointsize;
        this.fontName = fontName;
    }

    //~ Methods ----------------------------------------------------------------
    //
    //---------------//
    // createDefault //
    //---------------//
    /**
     * Create a default font using provided fontSize.
     *
     * @param fontSize the size to use
     * @return the FontInfo instance
     */
    public static FontInfo createDefault (int fontSize)
    {
        return new FontInfo(
                false,
                false,
                false,
                false,
                true,
                false,
                fontSize,
                "Serif");
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder("{");
        sb.append(fontName)
                .append(' ');

        if (isBold) {
            sb.append('B');
        }

        if (isItalic) {
            sb.append('I');
        }

        if (isUnderlined) {
            sb.append('U');
        }

        if (isMonospace) {
            sb.append('M');
        }

        if (isSerif) {
            sb.append('S');
        }

        if (isSmallcaps) {
            sb.append('C');
        }

        sb.append('-')
                .append(pointsize);
        sb.append("}");

        return sb.toString();
    }
}
