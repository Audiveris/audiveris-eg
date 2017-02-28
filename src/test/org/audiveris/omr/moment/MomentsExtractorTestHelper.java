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
package org.audiveris.omr.moment;

import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.ShapeSet;
import org.audiveris.omr.glyph.SymbolGlyph;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.math.PointsCollector;

import org.audiveris.omr.moments.MomentsExtractor;
import org.audiveris.omr.moments.OrthogonalMoments;

import org.audiveris.omr.ui.symbol.MusicFont;
import org.audiveris.omr.ui.symbol.ShapeSymbol;
import org.audiveris.omr.ui.symbol.Symbols;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;
import org.junit.Ignore;

/**
 * Class {@code MomentsExtractorTestHelper}
 *
 * @author Hervé Bitteur
 */
public class MomentsExtractorTestHelper <D extends OrthogonalMoments<D>>
{
    //~ Instance fields --------------------------------------------------------

    Map<Shape, D> descriptors = new EnumMap<>(Shape.class);

    File temp = new File("data/temp");

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new MomentsExtractorTest object.
     */
    public MomentsExtractorTestHelper ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //---------------//
    // testAllShapes //
    //---------------//
    /**
     * Use a symbol glyph as input for each shape
     */
    public void testAllShapes (MomentsExtractor<D> extractor,
                               Class<? extends D> classe)
            throws InstantiationException, IllegalAccessException
    {
        temp.mkdirs();

        // Retrieve descriptor for each physical shape
        for (Shape shape : ShapeSet.allPhysicalShapes) {
            ShapeSymbol symbol = Symbols.getSymbol(shape);

            // If no plain symbol, use the decorated symbol as plan B
            if (symbol == null) {
                symbol = Symbols.getSymbol(shape, true);
            }

            if (symbol != null) {
                System.out.println("shape:" + shape);

                Glyph glyph = new SymbolGlyph(
                        shape,
                        symbol,
                        MusicFont.DEFAULT_INTERLINE,
                        null);
                PointsCollector collector = glyph.getPointsCollector();
                D descriptor = classe.newInstance();
                extractor.setDescriptor(descriptor);
                extractor.extract(
                        collector.getXValues(),
                        collector.getYValues(),
                        collector.getSize());
                descriptors.put(shape, descriptor);

                // Reconstruct
                ///reconstruct(shape, extractor);
            } else {
                System.out.println(shape + " no symbol");
            }
        }

        // Print moments per shape
        printMoments();

        // Print inter-shape distances
        printRelations();
    }

    //--------------//
    // printMoments //
    //--------------//
    private void printMoments ()
    {
        // Print moments per shape
        for (Map.Entry<Shape, D> entry : descriptors.entrySet()) {
            System.out.println(
                    String.format(
                    "%-30s %s",
                    entry.getKey().toString(),
                    entry.getValue().toString()));
        }

        System.out.println();
    }

    //----------------//
    // printRelations //
    //----------------//
    private void printRelations ()
    {
        List<ShapeRelations> allRelations = new ArrayList<>();

        for (Map.Entry<Shape, D> entry : descriptors.entrySet()) {
            Shape shape = entry.getKey();
            List<Relation> relations = new ArrayList<>();

            for (Map.Entry<Shape, D> e : descriptors.entrySet()) {
                Shape s = e.getKey();

                if (s == shape) {
                    continue;
                }

                OrthogonalMoments d = e.getValue();
                relations.add(new Relation(shape, s));
            }

            Collections.sort(relations);
            allRelations.add(new ShapeRelations(shape, relations));
        }

        Collections.sort(allRelations);

        for (ShapeRelations shapeRelations : allRelations) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 5; i++) {
                Relation rel = shapeRelations.relations.get(i);
                sb.append(" ").append(rel);
            }

            System.out.println(
                    String.format("%30s =>%s", shapeRelations.shape.toString(),
                                  sb));
        }
    }

    //-------------//
    // reconstruct //
    //-------------//
    private void reconstruct (Shape shape,
                              MomentsExtractor<D> extractor)
    {
        int size = 200;
        BufferedImage img = new BufferedImage(
                size,
                size,
                BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = img.getRaster();

        extractor.reconstruct(raster);

        try {
            ImageIO.write(img, "png", new File(temp, shape + ".png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //~ Inner Classes ----------------------------------------------------------
    //----------//
    // Relation //
    //----------//
    private class Relation
            implements Comparable<Relation>
    {
        //~ Instance fields ----------------------------------------------------

        final Shape from;

        final Shape to;

        final double distance;

        //~ Constructors -------------------------------------------------------
        Relation (Shape from,
                  Shape to)
        {
            this.from = from;
            this.to = to;
            distance = descriptors.get(from).distanceTo(descriptors.get(to));
        }

        //~ Methods ------------------------------------------------------------
        @Override
        public int compareTo (Relation other)
        {
            return Double.compare(distance, other.distance);
        }

        @Override
        public String toString ()
        {
            return String.format(
                    Locale.US,
                    "%30s %5.3f ",
                    to.toString(),
                    distance);
        }
    }

    //----------------//
    // ShapeRelations //
    //----------------//
    private class ShapeRelations
            implements Comparable<ShapeRelations>
    {
        //~ Instance fields ----------------------------------------------------

        final Shape shape;

        final List<Relation> relations; // Sorted

        //~ Constructors -------------------------------------------------------
        ShapeRelations (Shape shape,
                        List<Relation> relations)
        {
            this.shape = shape;
            this.relations = relations;
        }

        //~ Methods ------------------------------------------------------------
        @Override
        public int compareTo (ShapeRelations that)
        {
            return Double.compare(
                    this.relations.get(0).distance,
                    that.relations.get(0).distance);
        }
    }
}
