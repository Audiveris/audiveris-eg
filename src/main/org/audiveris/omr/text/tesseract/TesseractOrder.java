//----------------------------------------------------------------------------//
//                                                                            //
//                        T e s s e r a c t O r d e r                         //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur and others 2000-2013. All rights reserved.      //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.text.tesseract;

import org.audiveris.omr.WellKnowns;

import org.audiveris.omr.sheet.SystemInfo;

import org.audiveris.omr.text.FontInfo;
import org.audiveris.omr.text.TextChar;
import org.audiveris.omr.text.TextLine;
import org.audiveris.omr.text.TextWord;

import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageOutputStream;

/**
 * Class {@code TesseractOrder} carries a processing order submitted
 * to Tesseract OCR program.
 *
 * @author Hervé Bitteur
 */
public class TesseractOrder
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(TesseractOrder.class);
    
    /** To specify UTF-8 encoding. */
    private static final String UTF8 = "UTF-8";

    /** To avoid repetitive warnings if OCR binding failed */
    private static boolean userWarned;

    /** Needed (for OpenJDK) to register TIFF support. */
    static {
        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi());
        registry.registerServiceProvider(new com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi());
    }

    //~ Instance fields --------------------------------------------------------
    //
    /** Containing system. */
    private final SystemInfo system;

    /** Serial number for this order. */
    private final int serial;

    /** Image label. */
    private final String label;

    /** Should we keep a disk copy of the image?. */
    private final boolean keepImage;

    /** Language specification. */
    private final String lang;

    /** Desired handling of layout. */
    private final int segMode;

    /** The dedicated API. */
    private TessBaseAPI api;

    /** The image being processed. */
    private PIX image;

    //~ Constructors -----------------------------------------------------------
    //
    //----------------//
    // TesseractOrder //
    //----------------//
    /**
     * Creates a new TesseractOrder object.
     *
     * @param system        The containing system
     * @param label         A debugging label (such as glyph id)
     * @param serial        A unique id for this order instance
     * @param keepImage     True to keep a disk copy of the image
     * @param lang          The language specification
     * @param segMode       The desired page segmentation mode
     * @param bufferedImage The image to process
     *
     * @throws UnsatisfiedLinkError When bridge to C++ could not be loaded
     * @throws IOException          When temporary Tiff buffer failed
     * @throws RuntimeException     When PIX image failed
     */
    public TesseractOrder (SystemInfo system,
                           String label,
                           int serial,
                           boolean keepImage,
                           String lang,
                           int segMode,
                           BufferedImage bufferedImage)
            throws UnsatisfiedLinkError, IOException
    {
        this.system = system;
        this.label = label;
        this.serial = serial;
        this.keepImage = keepImage;
        this.lang = lang;
        this.segMode = segMode;

        // Build a PIX from the image provided
        ByteBuffer buf = toTiffBuffer(bufferedImage);
        buf.position(0);
        image = pixReadMemTiff(buf, buf.capacity(), 0);

        if (image == null) {
            logger.warn("Invalid image {}", label);
            throw new RuntimeException("Invalid image");
        }
    }

    //~ Methods ----------------------------------------------------------------
    //
    //---------//
    // process //
    //---------//
    /**
     * Actually allocate a Tesseract API and recognize the image.
     *
     * @return the sequence of lines found
     */
    public List<TextLine> process ()
    {
        try {
            api = new TessBaseAPI();

            // Init API with proper language
            if (api.Init(WellKnowns.OCR_FOLDER.toString(), lang) != 0) {
                logger.warn("Could not initialize Tesseract with lang {}", lang);

                return finish(null);
            }

            // Set API image
            api.SetImage(image);

            // Perform layout analysis according to segmentation mode
            api.SetPageSegMode(segMode);
            api.AnalyseLayout();

            // Perform image recognition
            if (api.Recognize(null) != 0) {
                logger.warn("Error in Tesseract recognize");

                return finish(null);
            }

            // Extract lines
            return finish(getLines());
        } catch (UnsatisfiedLinkError ex) {
            if (!userWarned) {
                logger.warn("Could not link Tesseract engine", ex);
                logger.warn(
                        "java.library.path="
                        + System.getProperty("java.library.path"));
                userWarned = true;
            }

            throw new RuntimeException(ex);
        }
    }

    //--------//
    // finish //
    //--------//
    /**
     * A convenient way to cleanup Tesseract resources while ending
     * the current processing
     *
     * @param lines the lines found, if any
     * @return the lines found, if nay
     */
    private List<TextLine> finish (List<TextLine> lines)
    {
        if (image != null) {
            pixDestroy(image);
        }

        if (api != null) {
            api.End();
        }

        return lines;
    }

    //---------//
    // getFont //
    //---------//
    /**
     * Map Tesseract3 font attributes to our own FontInfo class.
     *
     * @param rit ResultIterator to query for font attributes out of OCR
     * @return our FontInfo structure, or null
     */
    private FontInfo getFont (ResultIterator rit)
    {
        BoolPointer is_bold = new BoolPointer(0);
        BoolPointer is_italic = new BoolPointer(0);
        BoolPointer is_underlined = new BoolPointer(0);
        BoolPointer is_monospace = new BoolPointer(0);
        BoolPointer is_serif = new BoolPointer(0);
        BoolPointer is_smallcaps = new BoolPointer(0);
        IntPointer pointSize = new IntPointer(0);
        IntPointer font_id = new IntPointer(0);

        String fontName = null;

        BytePointer bp = rit.WordFontAttributes(
                is_bold,
                is_italic,
                is_underlined,
                is_monospace,
                is_serif,
                is_smallcaps,
                pointSize,
                font_id);

        // don't try to decode fontName from null bytepointer!
        if (bp != null) {
            fontName = bp.getString();
        }

        if (fontName != null) {
            return new FontInfo(
                is_bold.get(),
                is_italic.get(),
                is_underlined.get(),
                is_monospace.get(),
                is_serif.get(),
                is_smallcaps.get(),
                pointSize.get(),
                fontName);
        } else {
            return null;
        }
    }

    private Rectangle BoundingBox (PageIterator it,
                                   int level)
    {
        IntPointer left = new IntPointer(0);
        IntPointer top = new IntPointer(0);
        IntPointer right = new IntPointer(0);
        IntPointer bottom = new IntPointer(0);

        if (it.BoundingBox(level, left, top, right, bottom)) {
            return new Rectangle(
                    left.get(),
                    top.get(),
                    right.get() - left.get(),
                    bottom.get() - top.get());
        } else {
            return null;
        }
    }

    private Line2D Baseline (ResultIterator rit,
                             int level)
    {
        IntPointer x1 = new IntPointer(0);
        IntPointer y1 = new IntPointer(0);
        IntPointer x2 = new IntPointer(0);
        IntPointer y2 = new IntPointer(0);

        if (rit.Baseline(level, x1, y1, x2, y2)) {
            return new Line2D.Double(x1.get(), y1.get(), x2.get(), y2.get());
        } else {
            return null;
        }
    }

    //----------//
    // getLines //
    //----------//
    /**
     * Build the hierarchy of TextLine / TextWord / TextChar instances
     * out of the results of OCR recognition
     *
     * @return the sequence of lines
     */
    private List<TextLine> getLines ()
    {
        final int maxDashWidth = system.getScoreSystem().getScale().getInterline();

        final ResultIterator it = api.GetIterator();

        //List<TextLine> lines = new ArrayList<>(); // Lines built so far
        final List<TextLine> lines = new ArrayList<TextLine>(); // All lines built so far
        TextLine line = null; // Line being built
        TextWord word = null; // Word being built
        int nextLevel;

        try {
            do {
                nextLevel = RIL_SYMBOL;
                
                // SKip empty stuff
                if (it.Empty(RIL_SYMBOL)) {
                    continue;
                }

                // Start of line?
                if (it.IsAtBeginningOf(RIL_TEXTLINE)) {
                    line = new TextLine(system);
                    logger.debug("{} {}", label, line);
                    lines.add(line);
                }

                // Start of word?
                if (it.IsAtBeginningOf(RIL_WORD)) {
                    FontInfo fontInfo = getFont(it);

                    if (fontInfo == null) {
                        logger.debug("No font info on {}", label);
                        nextLevel = RIL_WORD; // skip words without font info
                        continue;
                    }
                    
                    word = new TextWord(
                            BoundingBox(it, RIL_WORD),
                            it.GetUTF8Text(RIL_WORD).getString(UTF8),
                            Baseline(it, RIL_WORD),
                            (int) Math.rint(it.Confidence(RIL_WORD)),
                            fontInfo,
                            line);
                    logger.debug("    {}", word);
                    line.appendWord(word);

                    // // Heuristic... (just to test)
                    // boolean isDict = it.WordIsFromDictionary();
                    // boolean isNumeric = it.WordIsNumeric();
                    // boolean isLatin = encoder.canEncode(wordContent);
                    // int conf = (int) Math.rint(it.Confidence(WORD));
                    // int len = wordContent.length();
                    // boolean isValid = isLatin
                    //         && (conf >= 80
                    //   || (conf >= 50 && ((isDict && len > 1) || isNumeric)));
                }

                // Char/symbol to be processed

                // Fix long "—" vs short "-"
                String charValue = it.GetUTF8Text(RIL_SYMBOL).getString(UTF8);
                Rectangle charBox = BoundingBox(it, RIL_SYMBOL);
                if (charValue.equals("—") && charBox.width <= maxDashWidth) {
                    charValue = "-";
                    // Containing word value will be updated later
                }

                word.addChar(new TextChar(charBox, charValue));
            } while (it.Next(nextLevel));

            return lines;
        } catch (Exception ex) {
            logger.warn("Error decoding tesseract output", ex);

            return null;
        } finally {
            it.deallocate();
        }
    }

    //--------------//
    // toTiffBuffer //
    //--------------//
    /**
     * Convert the given image into a TIFF-formatted ByteBuffer for
     * passing it directly to Tesseract.
     * A copy of the tiff buffer can be saved on disk, if so desired.
     *
     * @param image the input image
     * @return a buffer in TIFF format
     */
    private ByteBuffer toTiffBuffer (BufferedImage image)
            throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (final ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("tiff").
                    next();
            writer.setOutput(ios);
            writer.write(image);
        }

        ByteBuffer buf = ByteBuffer.allocate(baos.size());
        byte[] bytes = baos.toByteArray();
        buf.put(bytes);

        // Should we keep a local copy of this buffer on disk?
        if (keepImage) {
            String name = String.format("%03d-", serial) + ((label != null) ? label : "");
            File file = new File(WellKnowns.TEMP_FOLDER, name + ".tif");

            // Make sure the TEMP directory exists
            if (!WellKnowns.TEMP_FOLDER.exists()) {
                WellKnowns.TEMP_FOLDER.mkdir();
            }
            try (final FileOutputStream fos = new FileOutputStream(
                    file.getAbsolutePath())) {
                fos.write(bytes);
            }
        }

        return buf;
    }
}
