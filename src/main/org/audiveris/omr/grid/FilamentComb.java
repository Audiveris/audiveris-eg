//----------------------------------------------------------------------------//
//                                                                            //
//                          F i l a m e n t C o m b                           //
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

import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code FilamentComb} describe a series of y values
 * corresponding to horizontal filaments rather regularly separated.
 *
 * @author Hervé Bitteur
 */
public class FilamentComb
{
    //~ Instance fields --------------------------------------------------------

    /** Column index where sample was taken */
    private final int col;

    /** Series of filaments involved */
    private final List<LineFilament> filaments;

    /** Ordinate value for each filament */
    private final List<Double> ys;

    /** To save processing */
    private boolean processed = false;

    //~ Constructors -----------------------------------------------------------
    //-----------------//
    // FilamentComb //
    //-----------------//
    /**
     * Creates a new FilamentComb object.
     *
     * @param col the column index
     */
    public FilamentComb (int col)
    {
        this.col = col;

        filaments = new ArrayList<>();
        ys = new ArrayList<>();
    }

    //~ Methods ----------------------------------------------------------------
    //--------//
    // append //
    //--------//
    /**
     * Append a filament to the series.
     *
     * @param filament the filament to append
     * @param y        the filament ordinate at x abscissa
     */
    public void append (LineFilament filament,
                        double y)
    {
        filaments.add(filament);
        ys.add(y);
        filament.addComb(col, this); // Link back Fil -> Comb
    }

    //----------//
    // getCount //
    //----------//
    /**
     * Report the number of filaments in this series.
     *
     * @return the count
     */
    public int getCount ()
    {
        return filaments.size();
    }

    //-------------//
    // getFilament //
    //-------------//
    public LineFilament getFilament (int index)
    {
        return filaments.get(index);
    }

    //--------------//
    // getFilaments //
    //--------------//
    public List<LineFilament> getFilaments ()
    {
        return filaments;
    }

    //----------//
    // getIndex //
    //----------//
    public int getIndex (LineFilament filament)
    {
        LineFilament ancestor = (LineFilament) filament.getAncestor();

        for (int index = 0; index < filaments.size(); index++) {
            LineFilament fil = filaments.get(index);

            if (fil.getAncestor() == ancestor) {
                return index;
            }
        }

        return -1;
    }

    //------//
    // getY //
    //------//
    public double getY (int index)
    {
        return ys.get(index);
    }

    //-------------//
    // isProcessed //
    //-------------//
    /**
     * @return the processed
     */
    public boolean isProcessed ()
    {
        return processed;
    }

    //--------------//
    // setProcessed //
    //--------------//
    /**
     * @param processed the processed to set
     */
    public void setProcessed (boolean processed)
    {
        this.processed = processed;
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder("{");
        sb.append("Pattern");

        sb.append(" col:")
                .append(col);

        sb.append(" ")
                .append(filaments.size());

        for (int i = 0; i < filaments.size(); i++) {
            LineFilament fil = (LineFilament) filaments.get(i)
                    .getAncestor();
            double y = ys.get(i);
            sb.append(" F#")
                    .append(fil.getId())
                    .append("@")
                    .append((float) y);
        }

        sb.append("}");

        return sb.toString();
    }
}
