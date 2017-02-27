//----------------------------------------------------------------------------//
//                                                                            //
//                           S y m b o l G l y p h                            //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur and others 2000-2013. All rights reserved.      //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.glyph;

import org.audiveris.omr.glyph.facets.BasicGlyph;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.lag.BasicLag;
import org.audiveris.omr.lag.JunctionAllPolicy;
import org.audiveris.omr.lag.Lag;
import org.audiveris.omr.lag.Section;
import org.audiveris.omr.lag.SectionsBuilder;

import org.audiveris.omr.run.Orientation;

import org.audiveris.omr.ui.symbol.MusicFont;
import org.audiveris.omr.ui.symbol.ShapeSymbol;
import org.audiveris.omr.ui.symbol.SymbolPicture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * Class {@code SymbolGlyph} is an articial glyph, built from a symbol.
 * It is used to generate glyphs for training, when no real glyph (glyph
 * retrieved from scanned sheet) is available.
 *
 * @author Hervé Bitteur
 */
public class SymbolGlyph
        extends BasicGlyph
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            SymbolGlyph.class);

    //~ Instance fields --------------------------------------------------------
    /** The underlying symbol, with generic size */
    private final ShapeSymbol symbol;

    /** The underlying image, properly sized */
    private final BufferedImage image;

    //~ Constructors -----------------------------------------------------------
    //-------------//
    // SymbolGlyph //
    //-------------//
    /**
     * Build an (artificial) glyph out of a symbol icon.
     * This construction is meant to populate and train on glyph shapes for
     * which we have no real instance yet.
     *
     * @param shape      the corresponding shape
     * @param symbol     the related drawing
     * @param interline  the related interline scaling value
     * @param descriptor additional features, if any
     */
    public SymbolGlyph (Shape shape,
                        ShapeSymbol symbol,
                        int interline,
                        SymbolGlyphDescriptor descriptor)
    {
        super(interline);
        this.symbol = symbol;
        image = symbol.buildImage(MusicFont.getFont(interline));

        /** Build a dedicated SymbolPicture */
        SymbolPicture symbolPicture = new SymbolPicture(image);

        /** Build related vertical lag */
        Lag iLag = new BasicLag("iLag", Orientation.VERTICAL);

        new SectionsBuilder(iLag, new JunctionAllPolicy()) // catch all
                .createSections("symbol", symbolPicture, /* minRunLength
                 * => */ 0);

        // Retrieve the whole glyph made of all sections
        for (Section section : iLag.getSections()) {
            addSection(section, Glyph.Linking.LINK_BACK);
        }

        // Glyph features
        setShape(shape, Evaluation.MANUAL);

        // Use descriptor if any is provided
        if (descriptor != null) {
            // Number of connected stems
            if (descriptor.getStemNumber() != null) {
                setStemNumber(descriptor.getStemNumber());
            }

            // Has a related ledger ?
            if (descriptor.isWithLedger() != null) {
                setWithLedger(descriptor.isWithLedger());
            }

            // Vertical position wrt staff
            if (descriptor.getPitchPosition() != null) {
                setPitchPosition(descriptor.getPitchPosition());
            }
        }
    }
}
