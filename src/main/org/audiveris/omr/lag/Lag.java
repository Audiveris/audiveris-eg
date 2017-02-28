//----------------------------------------------------------------------------//
//                                                                            //
//                                   L a g                                    //
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

import org.audiveris.omr.graph.Digraph;

import org.audiveris.omr.run.Oriented;
import org.audiveris.omr.run.Run;
import org.audiveris.omr.run.RunsTable;

import org.audiveris.omr.selection.SectionEvent;
import org.audiveris.omr.selection.SectionIdEvent;
import org.audiveris.omr.selection.SectionSetEvent;
import org.audiveris.omr.selection.SelectionService;

import org.audiveris.omr.util.Predicate;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Interface {@code Lag} defines a graph of {@link Section} instances
 * (sets of contiguous runs with compatible lengths), linked by
 * Junctions when there is no more contiguous run or when the
 * compatibility is no longer met.
 *
 * Sections are thus vertices of the graph, while junctions are directed edges
 * between sections. All the sections (and runs) have the same orientation
 * shared by the lag.
 *
 * <p>A lag may have a related UI selection service accessible through {@link
 * #getSectionService}. This selection service handles Section, SectionId and
 * SectionSet events. The {@link #getSelectedSection} and
 * {@link #getSelectedSectionSet} methods are just convenient ways to retrieve
 * the last selected section, sectionId or sectionSet from the lag selection
 * service.</p>
 *
 * <p>Run selection is provided by a separate selection service hosted by the
 * underlying RunsTable instance. For convenience, one can use the method
 * {@link #getRunService()} to get access to this run service.</p>
 *
 * @author Hervé Bitteur
 */
public interface Lag
        extends Digraph<Lag, Section>, Oriented
{
    //~ Static fields/initializers ---------------------------------------------

    /** Events that can be published on lag section service */
    static final Class<?>[] eventsWritten = new Class<?>[]{
        SectionIdEvent.class,
        SectionEvent.class,
        SectionSetEvent.class
    };

    //~ Methods ----------------------------------------------------------------
    /**
     * Include the content of runs table to the lag.
     *
     * @param runsTable the populated runs
     */
    void addRuns (RunsTable runsTable);

    /**
     * Create a section in the lag (using the defined vertexClass).
     *
     * @param firstPos the starting position of the section
     * @param firstRun the very first run of the section
     * @return the created section
     */
    Section createSection (int firstPos,
                           Run firstRun);

    /**
     * Cut dependency about other services for lag.
     */
    void cutServices ();

    /**
     * Report the run found at given coordinates, if any.
     *
     * @param x absolute abscissa
     * @param y absolute ordinate
     * @return the run found, or null otherwise
     */
    Run getRunAt (int x,
                  int y);

    /**
     * Report the selection service for runs.
     *
     * @return the run selection service
     */
    SelectionService getRunService ();

    /**
     * Report the underlying runs table.
     *
     * @return the table of runs
     */
    RunsTable getRuns ();

    /**
     * Report the section selection service.
     *
     * @return the section selection service
     */
    SelectionService getSectionService ();

    /**
     * Return a view of the collection of sections that are currently
     * part of this lag.
     *
     * @return the sections collection
     */
    Collection<Section> getSections ();

    /**
     * Convenient method to report the UI currently selected Section,
     * if any, in this lag.
     *
     * @return the UI selected section, or null if none
     */
    Section getSelectedSection ();

    /**
     * Convenient method to report the UI currently selected set of
     * Sections, if any, in this lag.
     *
     * @return the UI selected section set, or null if none
     */
    Set<Section> getSelectedSectionSet ();

    /**
     * Lookup for lag sections that are <b>intersected</b> by the
     * provided rectangle.
     * Specific sections are not considered.
     *
     * @param rect the given rectangle
     * @return the set of lag sections intersected, which may be empty
     */
    Set<Section> lookupIntersectedSections (Rectangle rect);

    /**
     * Lookup for lag sections that are <b>contained</b> in the
     * provided rectangle.
     * Specific sections are not considered.
     *
     * @param rect the given rectangle
     * @return the set of lag sections contained, which may be empty
     */
    Set<Section> lookupSections (Rectangle rect);

    /**
     * Purge the lag of all sections for which provided predicate holds.
     *
     * @param predicate means to specify whether a section applies for purge
     * @return the list of sections purged in this call
     */
    List<Section> purgeSections (Predicate<Section> predicate);

    /**
     * Use the provided runs table as the lag underlying table.
     *
     * @param runsTable the populated runs
     */
    void setRuns (RunsTable runsTable);

    /**
     * Inject dependency about other services for lag.
     *
     * @param locationService the location service to read & write
     * @param sceneService    the glyphservice to write
     */
    void setServices (SelectionService locationService,
                      SelectionService sceneService);
}
