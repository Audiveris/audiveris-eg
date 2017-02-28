//----------------------------------------------------------------------------//
//                                                                            //
//                             S c o r e N o d e                              //
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

import org.audiveris.omr.score.Score;

import org.audiveris.omr.util.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class {@code ScoreNode} handles a node in the tree hierarchy of a score
 * entity.
 *
 * @author Hervé Bitteur
 */
public abstract class ScoreNode
        extends VisitableNode
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            ScoreNode.class);

    //~ Constructors -----------------------------------------------------------
    //-----------//
    // ScoreNode //
    //-----------//
    /**
     * Create a node in the tree, given its container
     *
     * @param container the containing node, or null otherwise
     */
    public ScoreNode (VisitableNode container)
    {
        super(container);
    }

    //~ Methods ----------------------------------------------------------------
    //------------------//
    // getContextString //
    //------------------//
    /**
     * Report a string that describes the context (containment chain, score
     * excluded) of this entity.
     *
     * @return the properly filled context string
     */
    public String getContextString ()
    {
        return ""; // Empty by default
    }

    //----------//
    // getScore //
    //----------//
    /**
     * Report the containing score
     *
     * @return the containing score
     */
    public Score getScore ()
    {
        for (TreeNode c = this; c != null; c = c.getParent()) {
            if (c instanceof Score) {
                return (Score) c;
            }
        }

        return null;
    }
}
