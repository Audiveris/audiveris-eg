//----------------------------------------------------------------------------//
//                                                                            //
//                      G l y p h T r a n s l a t i o n                       //
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

/**
 * Interface {@code GlyphTranslation} defines a facet dealing with the
 * translation of a glyph into its score entity counter-part(s).
 *
 * @author Hervé Bitteur
 */
interface GlyphTranslation
        extends GlyphFacet
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Add a score entity as a translation for this glyph
     *
     * @param entity the counterpart of this glyph on the score side
     */
    void addTranslation (PartNode entity);

    /**
     * Remove all the links to score entities
     */
    void clearTranslations ();

    /**
     * Report the collection of score entities this glyph contributes to
     *
     * @return the collection of entities that are translations of this glyph
     */
    Collection<PartNode> getTranslations ();

    /**
     * Report whether this glyph is translated to a score entity
     *
     * @return true if this glyph is translated to score
     */
    boolean isTranslated ();

    /**
     * Assign a unique score translation for this glyph
     *
     * @param entity the score entity that is a translation of this glyph
     */
    void setTranslation (PartNode entity);
}
