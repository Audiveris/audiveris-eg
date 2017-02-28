//----------------------------------------------------------------------------//
//                                                                            //
//                        S h e e t P d f O u t p u t                         //
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
package org.audiveris.omr.score.ui;

import org.audiveris.omr.score.Score;
import org.audiveris.omr.score.ScoresManager;
import org.audiveris.omr.score.entity.Page;

import org.audiveris.omr.util.TreeNode;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Class {@code SheetPdfOutput} produces a physical PDF output of a
 * score.
 *
 * <p>TODO: Implement a PDF with multiple output pages
 *
 * @author Hervé Bitteur
 */
public class SheetPdfOutput
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            ScoresManager.class);

    //~ Instance fields --------------------------------------------------------
    /** The related score */
    private final Score score;

    /** The file to print to */
    private final File file;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new SheetPdfOutput object.
     *
     * @param score the score to print
     * @param file  the target PDF file
     */
    public SheetPdfOutput (Score score,
                           File file)
    {
        this.score = score;
        this.file = file;
    }

    //~ Methods ----------------------------------------------------------------
    public void write ()
            throws Exception
    {
        FileOutputStream fos = new FileOutputStream(file);
        Document document = null;
        PdfWriter writer = null;

        try {
            for (TreeNode pn : score.getPages()) {
                Page page = (Page) pn;
                Dimension dim = page.getDimension();

                if (document == null) {
                    document = new Document(
                            new Rectangle(dim.width, dim.height));
                    writer = PdfWriter.getInstance(document, fos);
                    document.open();
                } else {
                    document.setPageSize(new Rectangle(dim.width, dim.height));
                    document.newPage();
                }

                PdfContentByte cb = writer.getDirectContent();
                Graphics2D g2 = cb.createGraphics(dim.width, dim.height);
                g2.scale(1, 1);

                // Painting
                PagePhysicalPainter painter = new PagePhysicalPainter(
                        g2,
                        Color.BLACK, // Foreground color
                        false, // No voice painting
                        true, // Paint staff lines
                        false); // No annotations
                page.accept(painter);

                // This is the end...
                g2.dispose();
            }
        } catch (Exception ex) {
            logger.warn("Error printing " + score.getRadix(), ex);
            throw ex;
        } finally {
            if (document != null) {
                document.close();
            }
        }

        fos.close();
    }
}
