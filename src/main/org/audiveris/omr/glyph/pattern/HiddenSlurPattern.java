//----------------------------------------------------------------------------//
//                                                                            //
//                     H i d d e n S l u r P a t t e r n                      //
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
package org.audiveris.omr.glyph.pattern;

import org.audiveris.omr.constant.ConstantSet;

import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.sheet.Scale;
import org.audiveris.omr.sheet.SystemInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class {@code HiddenSlurPattern} processes the significant glyphs
 * which have not been assigned a shape, looking for a slur inside.
 *
 * @author Hervé Bitteur
 */
public class HiddenSlurPattern
        extends GlyphPattern
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            HiddenSlurPattern.class);

    //~ Constructors -----------------------------------------------------------
    //-------------------//
    // HiddenSlurPattern //
    //-------------------//
    /**
     * Creates a new HiddenSlurPattern object.
     *
     * @param system the containing system
     */
    public HiddenSlurPattern (SystemInfo system)
    {
        super("HiddenSlur", system);
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // runPattern //
    //------------//
    @Override
    public int runPattern ()
    {
        SlurInspector inspector = system.getSlurInspector();
        int successNb = 0;
        final double minGlyphWeight = constants.minGlyphWeight.getValue();

        for (Glyph glyph : system.getGlyphs()) {
            if (glyph.isKnown()
                || glyph.isManualShape()
                || (glyph.getNormalizedWeight() < minGlyphWeight)) {
                continue;
            }

            if (glyph.isVip()) {
                logger.info("Running HiddenSlur on {}", glyph.idString());
            }

            // Pickup a long thin section as seed
            // Aggregate others progressively
            Glyph newSlur = inspector.trimSlur(glyph);

            if ((newSlur != null) && (newSlur != glyph)) {
                successNb++;
            }
        }

        return successNb;
    }

    //~ Inner Classes ----------------------------------------------------------
    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
            extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        Scale.AreaFraction minGlyphWeight = new Scale.AreaFraction(
                0.5,
                "Minimum normalized glyph weight to lookup a slur section");

    }
}
