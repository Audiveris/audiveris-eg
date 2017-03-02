//----------------------------------------------------------------------------//
//                                                                            //
//                   J u n c t i o n D e l t a P o l i c y                    //
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
package org.audiveris.omr.lag;

import org.audiveris.omr.run.Run;

/**
 * Class {@code JunctionDeltaPolicy} defines a junction policy based
 * on the delta between the length of the candidate run and the length
 * of the last run of the section.
 *
 * @author Hervé Bitteur
 */
public class JunctionDeltaPolicy
        implements JunctionPolicy
{
    //~ Instance fields --------------------------------------------------------

    /**
     * Maximum value acceptable for delta length, for a delta criteria
     */
    private final int maxDeltaLength;

    //~ Constructors -----------------------------------------------------------
    //---------------------//
    // JunctionDeltaPolicy //
    //---------------------//
    /**
     * Creates an instance of policy based on delta run length.
     *
     * @param maxDeltaLength the maximum possible length gap between two
     *                       consecutive rows
     */
    public JunctionDeltaPolicy (int maxDeltaLength)
    {
        this.maxDeltaLength = maxDeltaLength;
    }

    //~ Methods ----------------------------------------------------------------
    //---------------//
    // consistentRun //
    //---------------//
    /**
     * Check whether the Run is consistent with the provided Section,
     * according to this junction policy, based on run length and last
     * section run length.
     *
     * @param run     the Run candidate
     * @param section the potentially hosting Section
     * @return true if consistent, false otherwise
     */
    @Override
    public boolean consistentRun (Run run,
                                  Section section)
    {
        // Check based on absolute differences between the two runs
        Run last = section.getLastRun();

        return Math.abs(run.getLength() - last.getLength()) <= maxDeltaLength;
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        return "{JunctionDeltaPolicy" + " maxDeltaLength=" + maxDeltaLength
               + "}";
    }
}
