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
package org.audiveris.omr.jai;

import org.audiveris.omr.util.StopWatch;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 * Class {@code TiffSplit}
 *
 * @author Hervé Bitteur
 */
public class TiffSplit
{
    //~ Methods ----------------------------------------------------------------

    public static void main (String... args)
        throws Exception
    {
        String                ext = "tif";
        StopWatch             watch = new StopWatch(
            "Reading tif / Writing " + ext);

        // Input file
        String                fileName = "D0394228.tif";
        File                  inputFile = new File(fileName);
        ImageInputStream      iis = ImageIO.createImageInputStream(inputFile);

        // Reader
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        ImageReader           reader = readers.next();
        reader.setInput(iis);

        // Number of images
        int                   number = reader.getNumImages(true);

        // Writer
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(
            ext);
        ImageWriter           writer = writers.next();

        ImageOutputStream     ios = null;
        BufferedImage         img = null;
        File                  outputFile = null;

        for (int i = 0; i < number; i++) {
            watch.start("Reading " + i);
            img = reader.read(i);
            watch.stop();
            outputFile = new File("myimage" + i + "." + ext);
            ios = ImageIO.createImageOutputStream(outputFile);
            writer.setOutput(ios);
            watch.start("Writing " + i);
            writer.write(img);
            watch.stop();
            ios.flush();
            img.flush();
        }

        watch.print();
    }

    private TiffSplit ()
    {
    }
}
