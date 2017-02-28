//----------------------------------------------------------------------------//
//                                                                            //
//                          L i n e F i l a m e n t                           //
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

import org.audiveris.omr.sheet.Scale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Class {@code LineFilament} is a {@link Filament}, used as (part of)
 * a candidate staff line.
 */
public class LineFilament
        extends Filament
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(LineFilament.class);

    //~ Instance fields --------------------------------------------------------
    //
    /** Combs where this filament appears. map (column index -> comb) */
    private SortedMap<Integer, FilamentComb> combs;

    /** The line cluster this filament is part of, if any. */
    private LineCluster cluster;

    /**
     * Relative position in cluster.
     * (relevant only if cluster is not null)
     */
    private int clusterPos;

    //~ Constructors -----------------------------------------------------------
    //
    //--------------//
    // LineFilament //
    //--------------//
    /**
     * Creates a new LineFilament object.
     *
     * @param scale scaling data
     */
    public LineFilament (Scale scale)
    {
        super(scale, LineFilamentAlignment.class);
    }

    //~ Methods ----------------------------------------------------------------
    //
    //---------//
    // addComb //
    //---------//
    /**
     * Add a comb where this filament appears
     *
     * @param column the sheet column index of the comb
     * @param comb   the comb which contains this filament
     */
    public void addComb (int column,
                         FilamentComb comb)
    {
        if (combs == null) {
            combs = new TreeMap<>();
        }

        combs.put(column, comb);
    }

    //--------//
    // dumpOf //
    //--------//
    @Override
    public String dumpOf ()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(super.dumpOf());
        sb.append(String.format("   cluster=%s%n", cluster));
        sb.append(String.format("   clusterPos=%s%n", clusterPos));
        sb.append(String.format("   combs=%s%n", combs));

        return sb.toString();
    }

    //-----------//
    // fillHoles //
    //-----------//
    /**
     * Fill large holes (due to missing intermediate points) in this
     * filament, by interpolating (or extrapolating) from the
     * collection of rather parallel fils, this filament is part of
     * (at provided clusterPos).
     *
     * @param fils the provided collection of parallel filaments
     */
    public void fillHoles (List<LineFilament> fils)
    {
        getAlignment().fillHoles(clusterPos, fils);
    }

    //------------//
    // getCluster //
    //------------//
    /**
     * Report the line cluster, if any, this filament is part of
     *
     * @return the containing cluster, or null
     */
    public LineCluster getCluster ()
    {
        return cluster;
    }

    //---------------//
    // getClusterPos //
    //---------------//
    /**
     * @return the clusterPos
     */
    public int getClusterPos ()
    {
        return clusterPos;
    }

    //----------//
    // getCombs //
    //----------//
    /**
     * @return the combs
     */
    public SortedMap<Integer, FilamentComb> getCombs ()
    {
        if (combs != null) {
            return combs;
        } else {
            return new TreeMap<>();
        }
    }

    //---------//
    // include //
    //---------//
    /**
     * Include a whole other filament into this one
     *
     * @param that the filament to swallow
     */
    public void include (LineFilament that)
    {
        super.stealSections(that);

        that.cluster = this.cluster;
        that.clusterPos = this.clusterPos;
    }

    //------------//
    // setCluster //
    //------------//
    /**
     * Assign this filament to a line cluster
     *
     * @param cluster the containing cluster
     * @param pos     the relative line position within the cluster
     */
    public void setCluster (LineCluster cluster,
                            int pos)
    {
        this.cluster = cluster;
        clusterPos = pos;
    }

    //--------------//
    // getAlignment //
    //--------------//
    @Override
    protected LineFilamentAlignment getAlignment ()
    {
        return (LineFilamentAlignment) super.getAlignment();
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder(super.internalsString());

        if (cluster != null) {
            sb.append(" cluster:")
                    .append(cluster.getId())
                    .append("p")
                    .append(clusterPos);
        }

        return sb.toString();
    }
}
