//----------------------------------------------------------------------------//
//                                                                            //
//                       G l y p h U p d a t e T a s k                        //
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
package org.audiveris.omr.script;

import org.audiveris.omr.glyph.SectionSets;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.lag.Section;

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.SystemInfo;

import java.util.Collection;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Class {@code GlyphUpdateTask} is applied to a collection of
 * existing glyphs.
 *
 * <p>The glyphs are designated either by their member sections, or (for the
 * special case of virtual glyphs) simply by their location.
 *
 * <p>Since sections are stable (they are assigned once and for all, the
 * relationship between a section and its containing system can be modified only
 * when system boundaries change in SystemsBuilder) they are used for the
 * underlying persistency of any GlyphUpdateTask. The XML file will thus contain
 * the ids of the member sections of the related glyphs.</p>
 *
 * <h4>Glyphs and sections in a script:<br/>
 * <img src="doc-files/script.jpg"/>
 * </h4>
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class GlyphUpdateTask
        extends GlyphTask
{
    //~ Instance fields --------------------------------------------------------

    /** The collection of underlying section sets (representing glyphs) */
    @XmlElement(name = "glyphs")
    protected final SectionSets sectionSets;

    //~ Constructors -----------------------------------------------------------
    //-----------------//
    // GlyphUpdateTask //
    //-----------------//
    /**
     * Creates a new GlyphUpdateTask object.
     *
     * @param sheet  the sheet impacted
     * @param glyphs the collection of glyphs concerned by this task
     */
    public GlyphUpdateTask (Sheet sheet,
                            Collection<Glyph> glyphs)
    {
        super(sheet, glyphs);
        sectionSets = SectionSets.createFromGlyphs(glyphs);
    }

    //-----------------//
    // GlyphUpdateTask //
    //-----------------//
    /** No-arg constructor for JAXB only */
    protected GlyphUpdateTask ()
    {
        sectionSets = null; // Dummy value
    }

    //~ Methods ----------------------------------------------------------------
    //----------------//
    // retrieveGlyphs //
    //----------------//
    @Override
    protected void retrieveGlyphs ()
    {
        glyphs = new TreeSet<>(Glyph.byAbscissa);

        for (Collection<Section> set : sectionSets.getSets(sheet)) {
            Glyph glyph = null;

            //            if (orientation == Orientation.VERTICAL) {
            SystemInfo system = set.iterator()
                    .next()
                    .getSystem();
            glyph = system.addGlyph(system.buildGlyph(set));
            //            } else {
            //                glyph = GlyphsBuilder.buildGlyph(sheet.getScale(), set);
            //                glyph = sheet.getScene()
            //                             .addGlyph(glyph);
            //                logger.info("Recreated " + glyph);
            //            }
            glyphs.add(glyph);
        }
    }
}
