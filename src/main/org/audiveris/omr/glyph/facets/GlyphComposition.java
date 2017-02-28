//----------------------------------------------------------------------------//
//                                                                            //
//                      G l y p h C o m p o s i t i o n                       //
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
package org.audiveris.omr.glyph.facets;

import org.audiveris.omr.check.Result;

import org.audiveris.omr.lag.Section;

import org.audiveris.omr.sheet.SystemInfo;

import java.util.SortedSet;

/**
 * Interface {@code GlyphComposition} defines the facet that handles
 * the way a glyph is composed of sections members, as well as the
 * relationships with sub-glyphs (parts) if any.
 *
 * @author Hervé Bitteur
 */
public interface GlyphComposition
        extends GlyphFacet
{
    //~ Enumerations -----------------------------------------------------------

    /** Specifies whether a section must point back to a containing glyph */
    enum Linking
    {
        //~ Enumeration constant initializers ----------------------------------

        /** Make the section point back to the containing glyph */
        LINK_BACK,
        /** Do
         * not make the section point back to the containing glyph */
        NO_LINK_BACK;

    }

    //~ Methods ----------------------------------------------------------------
    /**
     * Report the top ancestor of this glyph.
     * This is this glyph itself, when it has no parent (i.e. not been included
     * into another one)
     *
     * @return the glyph ancestor
     */
    public Glyph getAncestor ();

    /**
     * Report the containing compound, if any, which has "stolen" the
     * sections of this glyph.
     *
     * @return the containing compound if any
     */
    public Glyph getPartOf ();

    /**
     * Record the link to the compound which has "stolen" the sections
     * of this glyph.
     *
     * @param compound the containing compound, if any
     */
    public void setPartOf (Glyph compound);

    /**
     * Add a section as a member of this glyph.
     *
     * @param section The section to be included
     * @param link    While adding a section to this glyph members, should we
     *                also
     *                set the link from section back to the glyph?
     */
    void addSection (Section section,
                     Linking link);

    /**
     * Debug function that returns true if this glyph contains the
     * section whose ID is provided.
     *
     * @param id the ID of interesting section
     * @return true if such section exists among glyph sections
     */
    boolean containsSection (int id);

    /**
     * Cut the link to this glyph from its member sections, only if the
     * sections actually point to this glyph.
     */
    void cutSections ();

    /**
     * Check whether all the glyph sections belong to the same system.
     *
     * @param system the supposed containing system
     * @return the alien system found, or null if OK
     */
    SystemInfo getAlienSystem (SystemInfo system);

    /**
     * Report the first section in the ordered collection of members.
     *
     * @return the first section of the glyph
     */
    Section getFirstSection ();

    /**
     * Report the set of member sections.
     *
     * @return member sections
     */
    SortedSet<Section> getMembers ();

    /**
     * Report the result found during analysis of this glyph.
     *
     * @return the analysis result
     */
    Result getResult ();

    /**
     * Tests whether this glyph is active.
     * (all its member sections point to it)
     *
     * @return true if glyph is active, false otherwise
     */
    boolean isActive ();

    /**
     * Check whether the glyph is successfully recognized.
     *
     * @return true if the glyph is successfully recognized
     */
    boolean isSuccessful ();

    /**
     * Make all the glyph's sections point back to this glyph.
     */
    void linkAllSections ();

    /**
     * Remove a section from the glyph members
     *
     * @param section the section to remove
     * @param link    should we update the link from section to glyph?
     * @return true if the section was actually found and removed
     */
    boolean removeSection (Section section,
                           Linking link);

    /**
     * Record the analysis result in the glyph itself.
     *
     * @param result the assigned result
     */
    void setResult (Result result);

    /**
     * Include the sections from another glyph into this one, and make
     * its sections point into this one.
     * Doing so, the other glyph becomes inactive.
     *
     * @param that the glyph to swallow
     */
    void stealSections (Glyph that);
}
