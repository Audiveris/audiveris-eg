//----------------------------------------------------------------------------//
//                                                                            //
//                      B a s i c T r a n s l a t i o n                       //
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

import org.audiveris.omr.score.entity.PartNode;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Class {@code BasicTranslation} is the basic implementation of the
 * translation facet
 *
 * @author Hervé Bitteur
 */
class BasicTranslation
        extends BasicFacet
        implements GlyphTranslation
{
    //~ Instance fields --------------------------------------------------------

    /** Set of translation(s) of this glyph on the score side */
    private Set<PartNode> translations = new HashSet<>();

    //~ Constructors -----------------------------------------------------------
    //------------------//
    // BasicTranslation //
    //------------------//
    /**
     * Create a new BasicTranslation object
     *
     * @param glyph our glyph
     */
    public BasicTranslation (Glyph glyph)
    {
        super(glyph);
    }

    //~ Methods ----------------------------------------------------------------
    //----------------//
    // addTranslation //
    //----------------//
    @Override
    public void addTranslation (PartNode entity)
    {
        translations.add(entity);
    }

    //-------------------//
    // clearTranslations //
    //-------------------//
    @Override
    public void clearTranslations ()
    {
        translations.clear();
    }

    //--------//
    // dumpOf //
    //--------//
    @Override
    public String dumpOf ()
    {
        StringBuilder sb = new StringBuilder();

        if (!translations.isEmpty()) {
            sb.append(String.format("   translations=%s%n", translations));
        }

        return sb.toString();
    }

    //-----------------//
    // getTranslations //
    //-----------------//
    @Override
    public Collection<PartNode> getTranslations ()
    {
        return translations;
    }

    //-----------------//
    // invalidateCache //
    //-----------------//
    @Override
    public void invalidateCache ()
    {
        clearTranslations();
    }

    //--------------//
    // isTranslated //
    //--------------//
    @Override
    public boolean isTranslated ()
    {
        return !translations.isEmpty();
    }

    //----------------//
    // setTranslation //
    //----------------//
    @Override
    public void setTranslation (PartNode entity)
    {
        translations.clear();
        addTranslation(entity);
    }
}
