//----------------------------------------------------------------------------//
//                                                                            //
//                        S e c t i o n s S o u r c e                         //
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
package org.audiveris.omr.stick;

import org.audiveris.omr.lag.Section;

import org.audiveris.omr.util.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

/**
 * Class {@code Source} allows to formalize the way relevant sections
 * are made available to the area to be scanned.
 *
 * @author Hervé Bitteur
 */
public class SectionsSource
{
    //~ Instance fields --------------------------------------------------------

    /** the predicate to check whether section is to be processed */
    protected final Predicate<Section> predicate;

    /** Underlying list */
    protected ArrayList<Section> list;

    /** the section iterator for the source */
    protected ListIterator<Section> vi;

    /** the section currently visited */
    protected Section section;

    //~ Constructors -----------------------------------------------------------
    //----------------//
    // SectionsSource //
    //----------------//
    /**
     * Create a source on a given collection of glyph sections,
     * with default predicate
     *
     * @param collection the provided sections
     */
    public SectionsSource (Collection<Section> collection)
    {
        this(collection, new UnknownSectionPredicate());
    }

    //----------------//
    // SectionsSource //
    //----------------//
    /**
     * Create a source on a given collection of glyph sections,
     * with a specific predicate for section
     *
     * @param collection the provided sections
     * @param predicate  the predicate to check for candidate sections
     */
    public SectionsSource (Collection<Section> collection,
                           Predicate<Section> predicate)
    {
        this.predicate = predicate;

        if (collection != null) {
            list = new ArrayList<>(collection);
            reset();
        }
    }

    //~ Methods ----------------------------------------------------------------
    //--------//
    // backup //
    //--------//
    public void backup ()
    {
        // void
    }

    //---------//
    // hasNext //
    //---------//
    /**
     * Check whether we have more sections to scan.
     *
     * @return the boolean result of the test
     */
    public boolean hasNext ()
    {
        while (vi.hasNext()) {
            // Update cached data
            section = vi.next();

            if (predicate.check(section)) {
                StickRelation relation = section.getRelation();

                if (relation != null) {
                    relation.role = null; // Safer ?
                }

                section.setGlyph(null);

                return true;
            }
        }

        return false;
    }

    //----------//
    // isInArea //
    //----------//
    /**
     * Check whether a given section lies entirely within the scanned area
     *
     * @param section The section to be checked
     *
     * @return The boolean result of the test
     */
    public boolean isInArea (Section section)
    {
        // Default behavior : no filtering
        return true;
    }

    //------//
    // next //
    //------//
    /**
     * Return the next relevant section in Area, if any
     *
     * @return the next section
     */
    public Section next ()
    {
        return section;
    }

    //-------//
    // reset //
    //-------//
    public void reset ()
    {
        vi = list.listIterator();
    }
}
