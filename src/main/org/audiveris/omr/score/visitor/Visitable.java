//----------------------------------------------------------------------------//
//                                                                            //
//                             V i s i t a b l e                              //
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
package org.audiveris.omr.score.visitor;

/**
 * Interface {@code Visitable} must be implemented by any node to be
 * visited in the Score hierarchy
 *
 * @author Hervé Bitteur
 */
public interface Visitable
{
    //~ Methods ----------------------------------------------------------------

    /**
     * General entry for any visiting
     *
     * @param visitor concrete visitor object to define the actual processing
     * @return true if children must be visited also, false otherwise.
     * <b>Nota</b>: Unless there is a compelling reason, it's safer
     * to return true to let the visitor work normally.
     */
    boolean accept (ScoreVisitor visitor);
}
