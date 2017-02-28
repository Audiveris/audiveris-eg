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
package org.audiveris.omr.lag;

import org.audiveris.omr.lag.BasicLag;
import org.audiveris.omr.lag.Section;
import org.audiveris.omr.lag.Lag;
import org.audiveris.omr.Main;

import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.facets.BasicGlyph;
import org.audiveris.omr.glyph.facets.Glyph;
import org.audiveris.omr.glyph.facets.GlyphValue;

import org.audiveris.omr.run.Orientation;
import org.audiveris.omr.run.Run;
import org.audiveris.omr.run.RunsTable;

import org.audiveris.omr.util.BaseTestCase;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.junit.BeforeClass;

/**
 * Class {@code SectionBindingTest} tests the marshalling / unmarshalling of
 * a {@link Section}.
 *
 * @author Hervé Bitteur
 */
public class SectionBindingTest
        extends BaseTestCase
{
    //~ Instance fields --------------------------------------------------------

    private static final File dir = new File("data/temp");

    private final File fileNameVertical = new File(dir, "section.vertical.xml");

    private final File fileNameHorizontal = new File(dir, "section.horizontal.xml");

    private final File fileNameValue = new File(dir, "glyph.value.xml");

    private JAXBContext jaxbContext;

    // Lags and RunsTable instances
    Lag vLag;

    RunsTable vTable;

    Lag hLag;

    RunsTable hTable;

    //~ Methods ----------------------------------------------------------------
    //
    @BeforeClass
    public static void createTempFolder ()
    {
        dir.mkdirs();
    }
    //-----------//
    // testGlyph //
    //-----------//

    public void testGlyph ()
            throws JAXBException, FileNotFoundException
    {
        Section sv = vLag.createSection(180, new Run(100, 10, 127));
        sv.append(new Run(101, 20, 127));

        int p = 180;
        Section sh = hLag.createSection(180, createRun(hTable, p++, 100, 10));
        sh.append(createRun(hTable, p++, 102, 20));
        sh.append(createRun(hTable, p++, 102, 20));
        sh.append(createRun(hTable, p++, 102, 20));

        SortedSet<Section> sections = new TreeSet<>();
        sections.add(sv);
        sections.add(sh);

        GlyphValue value = new GlyphValue(
                Shape.BREVE,
                20,
                1,
                0,
                false,
                0.5,
                sections);

        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(value, new FileOutputStream(fileNameValue));
        System.out.println("Marshalled to " + fileNameValue);
        
        
        Unmarshaller um = jaxbContext.createUnmarshaller();
        InputStream is = new FileInputStream(fileNameValue);
        GlyphValue newValue = (GlyphValue) um.unmarshal(is);
        System.out.println("Unmarshalled from " + fileNameValue);
        Main.dumping.dump(newValue);

        Glyph glyph = new BasicGlyph(newValue);
        System.out.println("Glyph: " + glyph);
        glyph.dumpOf();

        for (Section s : glyph.getMembers()) {
            System.out.println("member: " + s);
        }
        
    }

    //----------------//
    // testHorizontal //
    //----------------//
    public void testHorizontal ()
            throws JAXBException, FileNotFoundException
    {
        int p = 180;
        Section section = hLag.createSection(
                180,
                createRun(hTable, p++, 100, 10));
        section.append(createRun(hTable, p++, 102, 20));
        section.append(createRun(hTable, p++, 102, 20));
        section.append(createRun(hTable, p++, 102, 20));

        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(section, new FileOutputStream(fileNameHorizontal));
        System.out.println("Marshalled to " + fileNameHorizontal);
        
        Unmarshaller um = jaxbContext.createUnmarshaller();
        InputStream is = new FileInputStream(fileNameHorizontal);
        Section newSection = (Section) um.unmarshal(is);
        System.out.println("Unmarshalled from " + fileNameHorizontal);
        System.out.println("Section: " + newSection);
        
    }

    //--------------//
    // testVertical //
    //--------------//
    public void testVertical ()
            throws JAXBException, FileNotFoundException
    {
        Section section = vLag.createSection(180, new Run(100, 10, 127));
        section.append(new Run(101, 20, 127));

        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(section, new FileOutputStream(fileNameVertical));
        System.out.println("Marshalled to " + fileNameVertical);
        
        Unmarshaller um = jaxbContext.createUnmarshaller();
        InputStream is = new FileInputStream(fileNameVertical);
        Section newSection = (Section) um.unmarshal(is);
        System.out.println("Unmarshalled from " + fileNameVertical);
        System.out.println("Section: " + newSection);
        
    }

    //-------//
    // setUp //
    //-------//
    @Override
    protected void setUp ()
            throws JAXBException
    {
        jaxbContext = JAXBContext.newInstance(GlyphValue.class);

        vLag = new BasicLag("My Vertical Lag", Orientation.VERTICAL);
        vTable = new RunsTable(
                "Vert Runs",
                Orientation.VERTICAL,
                new Dimension(100, 200)); // Absolute
        vLag.setRuns(vTable);

        hLag = new BasicLag("My Horizontal Lag", Orientation.HORIZONTAL);
        hTable = new RunsTable(
                "Hori Runs",
                Orientation.HORIZONTAL,
                new Dimension(100, 200)); // Absolute
        hLag.setRuns(hTable);
    }

    //-----------//
    // createRun //
    //-----------//
    private Run createRun (RunsTable table,
                           int alignment,
                           int start,
                           int length)
    {
        Run run = new Run(start, length, 127);

        table.getSequence(alignment)
                .add(run);

        return run;
    }
}
