//----------------------------------------------------------------------------//
//                                                                            //
//                                V e r t e x                                 //
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
package org.audiveris.omr.graph;

import java.util.List;

/**
 * Interface {@code Vertex} encapsulates a Vertex (or Node) in a
 * directed graph.
 * Any vertex can have incoming edges from other vertices and outgoing
 * edges to other vertices.
 *
 * <p>The Vertex can have a list of related {@code VertexView}'s.
 * All the vertices in the graph have parallel lists of {@code VertexView}'s as
 * the Digraph itself which has a parallel list of {@code DigraphView}'s.
 *
 * @param <D> type for enclosing digraph precise subtype
 * @param <V> type for Vertex precise subtype
 *
 * @author Hervé Bitteur
 */
public interface Vertex<D extends Digraph, V extends Vertex<D, V>>
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Create an edge between this vertex and the target vertax
     *
     * @param target arrival vertex
     */
    public void addTarget (V target);

    /**
     * Add a related view of this vertex
     *
     * @param view the view to be linked
     */
    public void addView (VertexView view);

    /**
     * Get rid of all views for this vertex
     */
    public void clearViews ();

    /**
     * Delete this vertex.
     * This implies also the removal of all its incoming and outgoing edges.
     */
    public void delete ();

    /**
     * Prints on standard output a detailed information about this
     * vertex.
     */
    public void dump ();

    /**
     * Report the containing graph of this vertex
     *
     * @return the containing graph
     */
    public D getGraph ();

    /**
     * Report the unique Id (within the containing graph) of this
     * vertex.
     *
     * @return the id
     */
    public int getId ();

    /**
     * Return how many incoming edges we have
     *
     * @return the number of incomings
     */
    public int getInDegree ();

    /**
     * Return the number of edges outgoing from this vertex
     *
     * @return the number of outgoings
     */
    public int getOutDegree ();

    /**
     * An access to incoming vertices
     *
     * @return the incoming vertices
     */
    public List<V> getSources ();

    /**
     * Return an access to the outgoing vertices of this vertex
     *
     * @return the outgoing vertices
     */
    public List<V> getTargets ();

    /**
     * Report the view at given index
     *
     * @param index index of the desired view
     * @return the desired view
     */
    public VertexView getView (int index);

    /**
     * Report the current number of views on this Vertex
     *
     * @return the current number of views
     */
    public int getViewsCount ();

    /**
     * Remove an edge between this vertex and a target vertex
     *
     * @param target arrival vertex
     * @param strict throw RuntimeException if the edge does not exist
     */
    public void removeTarget (V target,
                              boolean strict);

    /**
     * Assign the containing graph of this vertex
     *
     * @param graph The hosting graph
     */
    public void setGraph (D graph);

    /**
     * Assign a new Id (for expert use only)
     *
     * @param id The assigned id
     */
    public void setId (int id);
}
