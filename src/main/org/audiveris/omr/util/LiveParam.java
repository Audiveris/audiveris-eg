//----------------------------------------------------------------------------//
//                                                                            //
//                              L i v e P a r a m                             //
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
package org.audiveris.omr.util;

/**
 * Class {@code LiveParam} is a {@link Param} for which we handle the
 * value actually used, to detect when reprocessing must be done.
 *
 * @param <E> type of parameter handled
 *
 * @author Hervé Bitteur
 */
public class LiveParam<E>
        extends Param<E>
{
    //~ Instance fields --------------------------------------------------------

    /** Actually used parameter, if any. */
    protected E actual;

    //~ Constructors -----------------------------------------------------------
    //
    //-----------//
    // LiveParam //
    //-----------//
    /**
     * Creates a LiveParam object.
     *
     * @param parent parent context, or null
     */
    public LiveParam (Param<E> parent)
    {
        super(parent);
    }

    //~ Methods ----------------------------------------------------------------
    //
    //-------------//
    // needsUpdate //
    //-------------//
    public boolean needsUpdate ()
    {
        return !getTarget()
                .equals(actual);
    }

    //-----------//
    // setActual //
    //-----------//
    public void setActual (E actual)
    {
        this.actual = actual;
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder(super.internalsString());

        if (actual != null) {
            sb.append(" actual:")
                    .append(actual);
        }

        return sb.toString();
    }
}
