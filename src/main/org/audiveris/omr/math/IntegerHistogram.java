//----------------------------------------------------------------------------//
//                                                                            //
//                      I n t e g e r H i s t o g r a m                       //
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
package org.audiveris.omr.math;

import java.io.PrintStream;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Class {@code IntegerHistogram} is an histogram where buckets are
 * integers.
 *
 * @author Hervé Bitteur
 */
public class IntegerHistogram
        extends Histogram<Integer>
{
    //~ Instance fields --------------------------------------------------------

    /** The derivatives values */
    private SortedMap<Integer, Double> derivatives;

    //~ Methods ----------------------------------------------------------------
    //-------//
    // clear //
    //-------//
    @Override
    public void clear ()
    {
        super.clear();
        derivatives = null;
    }

    //----------------//
    // getDerivatives //
    //----------------//
    public SortedMap<Integer, Double> getDerivatives ()
    {
        if (derivatives == null) {
            derivatives = new TreeMap<>();

            Integer prevKey = null;
            Integer prevValue = null;
            Integer key = null;
            Integer value = null;

            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                Integer nextKey = entry.getKey();
                Integer nextValue = entry.getValue();

                if (key != null) {
                    if (prevKey != null) {
                        // We can compute a derivative 
                        derivatives.put(
                                key,
                                (double) (nextValue - prevValue) / (nextKey
                                                                    - prevKey));
                    }

                    prevKey = key;
                    prevValue = value;
                }

                key = nextKey;
                value = nextValue;
            }
        }

        return derivatives;
    }

    //---------------//
    // increaseCount //
    //---------------//
    @Override
    public void increaseCount (Integer bucket,
                               int delta)
    {
        super.increaseCount(bucket, delta);
        derivatives = null;
    }

    //-------//
    // print //
    //-------//
    @Override
    public void print (PrintStream stream)
    {
        stream.print("[\n");

        getDerivatives();

        for (Map.Entry<Integer, Integer> entry : entrySet()) {
            Integer key = entry.getKey();
            Double der = derivatives.get(key);
            stream.format(
                    " %s: v:%d d:%s\n",
                    key.toString(),
                    entry.getValue(),
                    (der != null) ? der.toString() : "");
        }

        stream.println("]");
    }
}
