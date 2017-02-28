//----------------------------------------------------------------------------//
//                                                                            //
//                    S h a p e D e s c r i p t o r A R T                     //
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

import org.audiveris.omr.moments.ARTMoments;

import org.audiveris.omr.run.Orientation;

import java.util.HashMap;
import java.util.Map;

/**
 * Class {@code ShapeDescriptorART} defines shape descriptions based
 * on ART moments.
 *
 * @author Hervé Bitteur
 */
public class ShapeDescriptorART
        implements ShapeDescription.Descriptor
{
    //~ Static fields/initializers ---------------------------------------------

    /** Number of orthogonal moments used */
    private static final int momentCount = -1
                                           + (ARTMoments.ANGULAR * ARTMoments.RADIAL);

    /** Use the orthogonal moments + weight + stems + aspect */
    private static final int length = momentCount + 3;

    //~ Methods ----------------------------------------------------------------
    //----------//
    // features //
    //----------//
    @Override
    public double[] features (Glyph glyph)
    {
        ARTMoments moments = glyph.getARTMoments();
        double[] ins = new double[length];
        int i = 0;

        // We take the orthogonal moments
        for (int p = 0; p < ARTMoments.ANGULAR; p++) {
            for (int r = 0; r < ARTMoments.RADIAL; r++) {
                if ((p != 0) || (r != 0)) {
                    ins[i++] = moments.getMoment(p, r);
                }
            }
        }

        // We append weight, stem count, aspect
        ins[i++] = glyph.getNormalizedWeight();
        ins[i++] = glyph.getStemNumber();
        ins[i++] = glyph.getAspect(Orientation.VERTICAL);

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
            int i = 0;

            // We take all the ART moments
            for (int p = 0; p < ARTMoments.ANGULAR; p++) {
                for (int r = 0; r < ARTMoments.RADIAL; r++) {
                    if ((p != 0) || (r != 0)) {
                        labels[i++] = String.format("F%02d%1d", p, r);
                    }
                }
            }

            // We append weight, stem count and aspect
            labels[i++] = "weight";
            labels[i++] = "stemNb";
            labels[i++] = "aspect";

            for (int j = 0; j < labels.length; j++) {
                indices.put(labels[j], j);
            }
        }

        private LabelsHolder ()
        {
        }
    }
}
