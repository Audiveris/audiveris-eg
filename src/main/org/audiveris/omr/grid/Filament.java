//----------------------------------------------------------------------------//
//                                                                            //
//                              F i l a m e n t                               //
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

import org.audiveris.omr.Main;

import org.audiveris.omr.glyph.facets.BasicGlyph;
import org.audiveris.omr.glyph.facets.GlyphComposition.Linking;

import org.audiveris.omr.lag.Section;

import org.audiveris.omr.run.Orientation;

import org.audiveris.omr.sheet.Scale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

/**
 * Class {@code Filament} represents a long glyph that can be far from
 * being a straight line.
 * It is used to handle candidate staff lines and bar lines.
 */
public class Filament
        extends BasicGlyph
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            Filament.class);

    /**
     * For comparing Filament instances on their top ordinate
     */
    public static final Comparator<Filament> topComparator = new Comparator<Filament>()
    {
        @Override
        public int compare (Filament f1,
                            Filament f2)
        {
            // Sort on top ordinate
            return Integer.signum(f1.getBounds().y - f2.getBounds().y);
        }
    };

    //~ Instance fields --------------------------------------------------------
    /** Related scale */
    private final Scale scale;

    //~ Constructors -----------------------------------------------------------
    //----------//
    // Filament //
    //----------//
    /**
     * Creates a new Filament object.
     *
     * @param scale scaling data
     */
    public Filament (Scale scale)
    {
        this(scale, FilamentAlignment.class);
    }

    //----------//
    // Filament //
    //----------//
    /**
     * Creates a new Filament object.
     *
     * @param scale scaling data
     */
    public Filament (Scale scale,
                     Class<? extends FilamentAlignment> alignmentClass)
    {
        super(scale.getInterline(), alignmentClass);
        this.scale = scale;
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // addSection //
    //------------//
    public void addSection (Section section)
    {
        addSection(section, Linking.LINK_BACK);
    }

    //------------//
    // addSection //
    //------------//
    @Override
    public void addSection (Section section,
                            Linking link)
    {
        getComposition()
                .addSection(section, link);
    }

    //----------//
    // deepDump //
    //----------//
    public void deepDump ()
    {
        Main.dumping.dump(this);
        Main.dumping.dump(getAlignment());
    }

    //------------------//
    // getMeanCurvature //
    //------------------//
    public double getMeanCurvature ()
    {
        return getAlignment()
                .getMeanCurvature();
    }

    //----------//
    // getScale //
    //----------//
    /**
     * Report the scale that governs this filament.
     *
     * @return the related scale
     */
    public Scale getScale ()
    {
        return scale;
    }

    //-----------------//
    // polishCurvature //
    //-----------------//
    /**
     * Polish the filament by looking at local curvatures and removing
     * sections when necessary.
     */
    public void polishCurvature ()
    {
        getAlignment()
                .polishCurvature();
    }

    //---------------//
    // getPositionAt //
    //---------------//
    /**
     * Report the precise filament position for the provided coordinate .
     *
     * @param coord       the coord value (x for horizontal, y for vertical)
     * @param orientation the reference orientation
     * @return the pos value (y for horizontal, x for vertical)
     */
    public double positionAt (double coord,
                              Orientation orientation)
    {
        return getAlignment()
                .getPositionAt(coord, orientation);
    }

    //---------//
    // slopeAt //
    //---------//
    public double slopeAt (double coord,
                           Orientation orientation)
    {
        return getAlignment()
                .slopeAt(coord, orientation);
    }

    //------------//
    // trueLength //
    //------------//
    /**
     * Report an evaluation of how this filament is filled by sections
     *
     * @return how solid this filament is
     */
    public int trueLength ()
    {
        return (int) Math.rint((double) getWeight() / scale.getMainFore());
    }

    //--------------//
    // getAlignment //
    //--------------//
    @Override
    protected FilamentAlignment getAlignment ()
    {
        return (FilamentAlignment) super.getAlignment();
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder();

        if (getPartOf() != null) {
            sb.append(" anc:")
                    .append(getAncestor());
        }

        if (getShape() != null) {
            sb.append(" ")
                    .append(getShape());
        }

        return sb.toString();
    }
}
