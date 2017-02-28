//----------------------------------------------------------------------------//
//                                                                            //
//                          B o u n d a r y T a s k                           //
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

import org.audiveris.omr.sheet.BrokenLineContext;
import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.SystemBoundary;
import org.audiveris.omr.sheet.SystemInfo;

import org.audiveris.omr.step.Stepping;
import org.audiveris.omr.step.Steps;

import org.audiveris.omr.util.BrokenLine;
import org.audiveris.omr.util.VerticalSide;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Class {@code BoundaryTask} modifies systems boundaries
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
public class BoundaryTask
        extends SheetTask
{
    //~ Instance fields --------------------------------------------------------

    /** The collection of line contexts */
    @XmlElement(name = "context")
    private final List<BrokenLineContext> contexts;

    //~ Constructors -----------------------------------------------------------
    //--------------//
    // BoundaryTask //
    //--------------//
    /**
     * Creates a new BoundaryTask object.
     *
     * @param sheet    the related sheet
     * @param contexts the collections of lines contexts
     */
    public BoundaryTask (Sheet sheet,
                         List<BrokenLineContext> contexts)
    {
        super(sheet);

        this.contexts = contexts;
    }

    //--------------//
    // BoundaryTask // No-arg constructor for JAXB only
    //--------------//
    private BoundaryTask ()
    {
        contexts = null;
    }

    //~ Methods ----------------------------------------------------------------
    //------//
    // core //
    //------//
    @Override
    public void core (Sheet sheet)
            throws Exception
    {
        for (BrokenLineContext context : contexts) {
            // Use a deep copy of the points sequence
            List<Point> copy = new ArrayList<>();

            for (Point p : context.line.getPoints()) {
                copy.add(new Point(p));
            }

            if (context.systemAbove != 0) {
                SystemInfo system = sheet.getSystems()
                        .get(context.systemAbove - 1);
                SystemBoundary boundary = system.getBoundary();
                BrokenLine brokenLine = boundary.getLimit(VerticalSide.BOTTOM);
                brokenLine.resetPoints(copy);
                system.updateBoundary();
            }

            if (context.systemBelow != 0) {
                SystemInfo system = sheet.getSystems()
                        .get(context.systemBelow - 1);
                SystemBoundary boundary = system.getBoundary();
                BrokenLine brokenLine = boundary.getLimit(VerticalSide.TOP);
                brokenLine.resetPoints(copy);
                system.updateBoundary();
            }
        }
    }

    //--------//
    // epilog //
    //--------//
    @Override
    public void epilog (Sheet sheet)
    {
        // Resplit systems content
        sheet.getSystemsBuilder().splitSystemEntities();

        // Update the following steps if any
        Stepping.reprocessSheet(
                Steps.valueOf(Steps.TEXTS),
                sheet,
                sheet.getSystems(),
                false);
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder(" boundary");
        sb.append(" [");

        for (BrokenLineContext context : contexts) {
            sb.append(" ")
                    .append(context);
        }

        sb.append("]");

        return sb.toString() + super.internalsString();
    }
}
