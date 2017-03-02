//----------------------------------------------------------------------------//
//                                                                            //
//                           C h e c k R e s u l t                            //
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
package org.audiveris.omr.check;


/**
 * Class {@code CheckResult} encapsulates the <b>result</b> of a check,
 * composed of a value (double) and a flag which can be RED, YELLOW or GREEN.
 *
 * @author Hervé Bitteur
 */
public class CheckResult
{
    //~ Instance fields --------------------------------------------------------

    /** Numerical result value */
    public double value;

    /** Flag the result (RED, YELLOW, GREEN) */
    public int flag;
}
