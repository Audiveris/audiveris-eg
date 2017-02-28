//----------------------------------------------------------------------------//
//                                                                            //
//                    S h a p e D e s c r i p t o r G e o                     //
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
package org.audiveris.omr.glyph;

import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.moments.GeometricMoments;

import org.audiveris.omr.run.Orientation;

import java.util.HashMap;
import java.util.Map;

/**
 * Class {@code ShapeDescriptorGeo} defines shape description based
 * on Geometric moments.
 *
 * @author Hervé Bitteur
 */
public class ShapeDescriptorGeo
        implements ShapeDescription.Descriptor
{
    //~ Static fields/initializers ---------------------------------------------

    /** Number of geometric moments used */
    private static final int momentCount = 10;

    /** Use the 10 first geometric moments + legder + stems + aspect */
    private static final int length = momentCount + 3;

    //~ Methods ----------------------------------------------------------------
    //----------//
    // features //
    //----------//
    @Override
    public double[] features (Glyph glyph)
    {
        double[] ins = new double[length];

        // We take all the first moments
        Double[] k = glyph.getGeometricMoments()
                .getValues();

        for (int i = 0; i < momentCount; i++) {
            ins[i] = k[i];
        }

        // We append ledger presence, stem count and aspect
        int i = momentCount;
        /* 10 */ ins[i++] = ShapeDescription.boolAsDouble(glyph.isWithLedger());
        /* 11 */ ins[i++] = glyph.getStemNumber();
        /* 12 */ ins[i++] = glyph.getAspect(Orientation.VERTICAL);

        return ins;
    }

    //-----------------//
    // getFeatureIndex //
    //-----------------//
    @Override
    public int getFeatureIndex (String label)
    {
        return LabelsHolder.indices.get(label);
    }

    //------------------//
    // getFeatureLabels //
    //------------------//
    @Override
    public String[] getFeatureLabels ()
    {
        return LabelsHolder.labels;
    }

    //--------//
    // length //
    //--------//
    @Override
    public int length ()
    {
        return length;
    }

    //~ Inner Classes ----------------------------------------------------------
    //--------------//
    // LabelsHolder //
    //--------------//
    /**
     * Descriptive strings for glyph characteristics.
     *
     * NOTA: Keep in sync method {@link #features}
     */
    private static class LabelsHolder
    {
        //~ Static fields/initializers -----------------------------------------

        /** Label -> Index */
        public static final Map<String, Integer> indices = new HashMap<>();

        /** Index -> Label */
        public static final String[] labels = new String[length];

        static {
            // We take all the first moments
            for (int i = 0; i < momentCount; i++) {
                labels[i] = GeometricMoments.getLabel(i);
            }

            // We append flags and step position
            int i = momentCount;
            /* 10 */ labels[i++] = "ledger";
            /* 11 */ labels[i++] = "stemNb";
            /* 12 */ labels[i++] = "aspect";

            ////* 13 */ labels[i++] = "pitch";

            //
            for (int j = 0; j < labels.length; j++) {
                indices.put(labels[j], j);
            }
        }

        private LabelsHolder ()
        {
        }
    }
}
