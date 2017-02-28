//----------------------------------------------------------------------------//
//                                                                            //
//                         V i s i t a b l e N o d e                          //
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

import org.audiveris.omr.score.visitor.ScoreVisitor;
import org.audiveris.omr.score.visitor.Visitable;

import org.audiveris.omr.util.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class {@code VisitableNode} is a node which can accept a score
 * visitor for itself and for its children.
 *
 * <img src="doc-files/Visitable-Hierarchy.jpg" />
 *
 * @see ScoreVisitor
 *
 * @author Hervé Bitteur
 */
public abstract class VisitableNode
        extends TreeNode
        implements Visitable
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            VisitableNode.class);

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new VisitableNode object.
     *
     * @param container the parent in the hierarchy
     */
    public VisitableNode (VisitableNode container)
    {
        super(container);
    }

    //~ Methods ----------------------------------------------------------------
    //--------//
    // accept //
    //--------//
    /**
     * This class is visitable by definition, and calls the proper
     * polymorphic visit() method in the provided visitor.
     * The returned boolean is used to tell whether the visit shall continue to
     * the children of this class
     *
     * @param visitor the specific visitor which browses this class
     * @return false if children should not be (automatically) visited,
     *         true otherwise (which should be the default).
     */
    @Override
    public boolean accept (ScoreVisitor visitor)
    {
        return visitor.visit(this);
    }

    //----------------//
    // acceptChildren //
    //----------------//
    /**
     * Pattern to traverse the children of this node, and recursively
     * the grand-children, etc, in a "depth-first" mode.
     *
     * @param visitor concrete visitor object to define the actual processing
     */
    public void acceptChildren (ScoreVisitor visitor)
    {
        ///logger.info(children.size() + " children for " + this + " parent=" + parent);
        for (TreeNode node : getChildrenCopy()) {
            VisitableNode child = (VisitableNode) node;

            if (child.accept(visitor)) {
                child.acceptChildren(visitor);
            }
        }
    }
}
