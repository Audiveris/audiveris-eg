//----------------------------------------------------------------------------//
//                                                                            //
//                         S t e p E x c e p t i o n                          //
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
package org.audiveris.omr.step;

/**
 * Class {@code StepException} describes an exception occurring while
 * doing OMR processing, and which should immediately stop the current
 * Step.
 *
 * @author Hervé Bitteur
 */
public class StepException
        extends Exception
{
    //~ Constructors -----------------------------------------------------------

    /**
     * Construct an {@code StepException} with no detail message.
     */
    public StepException ()
    {
        super();
    }

    /**
     * Construct an {@code StepException} with detail message.
     *
     * @param message the related message
     */
    public StepException (String message)
    {
        super(message);
    }

    /**
     * Construct an {@code StepException} from an existing exception.
     *
     * @param ex the related exception
     */
    public StepException (Throwable ex)
    {
        super(ex);
    }
}
