//----------------------------------------------------------------------------//
//                                                                            //
//                              P a g e N o d e                               //
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

import org.audiveris.omr.sheet.Scale;

import org.audiveris.omr.util.TreeNode;

/**
 * Class {@code PageNode} represents a score entity in a page, so its
 * direct children are {@link ScoreSystem} instances.
 *
 * @author Hervé Bitteur
 */
public abstract class PageNode
        extends ScoreNode
{
    //~ Constructors -----------------------------------------------------------

    //----------//
    // PageNode //
    //----------//
    /**
     * Create a node in the tree, given its container
     *
     * @param container the containing node, or null otherwise
     */
    public PageNode (ScoreNode container)
    {
        super(container);
    }

    //~ Methods ----------------------------------------------------------------
    //------------------//
    // getContextString //
    //------------------//
    @Override
    public String getContextString ()
    {
        StringBuilder sb = new StringBuilder(super.getContextString());

        if (getScore()
                .isMultiPage()) {
            sb.append("[#")
                    .append(getPage().getIndex())
                    .append("] ");
        }

        return sb.toString();
    }

    //---------//
    // getPage //
    //---------//
    /**
     * Report the containing page
     *
     * @return the containing page
     */
    public Page getPage ()
    {
        for (TreeNode c = this; c != null; c = c.getParent()) {
            if (c instanceof Page) {
                return (Page) c;
            }
        }

        return null;
    }

    //----------//
    // getScale //
    //----------//
    /**
     * Report the global scale of this page (and sheet)
     *
     * @return the global page scale
     */
    public Scale getScale ()
    {
        return getPage()
                .getScale();
    }
}
