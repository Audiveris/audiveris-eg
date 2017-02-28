//----------------------------------------------------------------------------//
//                                                                            //
//                             C o n t a i n e r                              //
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
package org.audiveris.omr.score.entity;

/**
 * Class {@code Container} is meant for intermediate containers with
 * no values by themselves. Thus, there are shown in the score tree, only if
 * have children.
 *
 * @author Hervé Bitteur
 */
public class Container
        extends VisitableNode
{
    //~ Instance fields --------------------------------------------------------

    /** Container name meant for debugging mainly */
    private final String name;

    //~ Constructors -----------------------------------------------------------
    //-----------//
    // Container //
    //-----------//
    /**
     * Creates a new Container object.
     *
     * @param container the parent in the score hierarchy
     * @param name      a name for this container
     */
    public Container (VisitableNode container,
                      String name)
    {
        super(container);
        this.name = name;
    }

    //~ Methods ----------------------------------------------------------------
    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        return name;
    }
}
