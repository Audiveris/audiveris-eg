//----------------------------------------------------------------------------//
//                                                                            //
//                          S t a f f M a n a g e r                           //
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
package org.audiveris.omr.grid;

import org.audiveris.omr.constant.ConstantSet;

import org.audiveris.omr.math.GeoPath;

import org.audiveris.omr.sheet.Scale;
import org.audiveris.omr.sheet.Sheet;

import org.audiveris.omr.util.Navigable;
import static org.audiveris.omr.util.VerticalSide.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class {@code StaffManager} handles physical information about all
 * the staves of a given sheet.
 *
 * @author Hervé Bitteur
 */
public class StaffManager
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(StaffManager.class);

    //~ Instance fields --------------------------------------------------------
    //
    /** The related sheet */
    @Navigable(false)
    private final Sheet sheet;

    /** The sequence of staves, from top to bottom */
    private final List<StaffInfo> staves = new ArrayList<>();

    /** The systems tops per staff */
    private Integer[] systemTops;

    /** The parts tops per staff */
    private Integer[] partTops;

    //~ Constructors -----------------------------------------------------------
    //
    //--------------//
    // StaffManager //
    //--------------//
    /**
     * Creates a new StaffManager object.
     *
     * @param sheet the related sheet
     */
    public StaffManager (Sheet sheet)
    {
        this.sheet = sheet;
    }

    //~ Methods ----------------------------------------------------------------
    //
    //----------//
    // addStaff //
    //----------//
    /**
     * Append one staff to the current collection
     *
     * @param staff the staff to add
     */
    public void addStaff (StaffInfo staff)
    {
        staves.add(staff);
    }

    //--------------------//
    // computeStaffLimits //
    //--------------------//
    public void computeStaffLimits ()
    {
        final int width = sheet.getWidth();
        final int height = sheet.getHeight();
        StaffInfo prevStaff = null;
        double samplingDx = sheet.getScale()
                .toPixelsDouble(constants.samplingDx);
        final int sampleCount = (int) Math.rint(width / samplingDx);
        samplingDx = width / sampleCount;

        for (StaffInfo staff : staves) {
            if (prevStaff == null) {
                // Very first staff
                staff.setLimit(
                        TOP,
                        new GeoPath(new Line2D.Double(0, 0, width, 0)));
            } else {
                // Define a middle line between last line of previous staff 
                // and first line of current staff
                LineInfo prevLine = prevStaff.getLastLine();
                LineInfo nextLine = staff.getFirstLine();
                GeoPath middle = new GeoPath();

                for (int i = 0; i <= sampleCount; i++) {
                    int x = (int) Math.rint(i * samplingDx);
                    double y = (prevLine.yAt(x) + nextLine.yAt(x)) / 2;

                    if (i == 0) {
                        middle.moveTo(x, y);
                    } else {
                        middle.lineTo(x, y);
                    }
                }

                // Point on right side
                middle.lineTo(width, (prevLine.yAt(width) + nextLine.yAt(width)) / 2);

                prevStaff.setLimit(BOTTOM, middle);
                staff.setLimit(TOP, middle);
            }

            // Remember this staff for next one
            prevStaff = staff;
        }

        // Bottom of last staff
        prevStaff.setLimit(
                BOTTOM,
                new GeoPath(new Line2D.Double(0, height, width, height)));
    }

    //------------//
    // getIndexOf //
    //------------//
    ///TODO @Deprecated
    public int getIndexOf (StaffInfo staff)
    {
        return staves.indexOf(staff);
    }

    //----------//
    // getRange //
    //----------//
    /**
     * Report a view on the range of staves from first to last
     * (both inclusive).
     *
     * @param first the first staff of the range
     * @param last  the last staff of the range
     * @return a view on this range
     */
    public List<StaffInfo> getRange (StaffInfo first,
                                     StaffInfo last)
    {
        return staves.subList(getIndexOf(first), getIndexOf(last) + 1);
    }

    //----------//
    // getStaff //
    //----------//
    ///TODO @Deprecated
    public StaffInfo getStaff (int index)
    {
        return staves.get(index);
    }

    //------------//
    // getStaffAt //
    //------------//
    /**
     * Report the staff, among the sequence provided, whose area
     * contains the provided point.
     *
     * @param point     the provided point
     * @param theStaves the staves sequence to search
     * @return the containing staff, or null if none found
     */
    public static StaffInfo getStaffAt (Point2D point,
                                        List<StaffInfo> theStaves)
    {
        for (StaffInfo staff : theStaves) {
            Rectangle2D box = staff.getAreaBounds();

            if (point.getY() > box.getMaxY()) {
                continue;
            }

            if (point.getY() < box.getMinY()) {
                // Point above first staff, use first staff
                // TODO: this decision is questionable
                return null; //staff;
            }

            // If the point is ON the area boundary, it is NOT contained.
            // So we use a rectangle of 1x1 pixels
            if (staff.getArea()
                    .intersects(point.getX(), point.getY(), 1, 1)) {
                return staff;
            }
        }

        // Point below last staff, use last staff
        // TODO: this decision is questionable
        return null; //theStaves.get(theStaves.size() - 1);
    }

    //------------//
    // getStaffAt //
    //------------//
    /**
     * Report the staff whose area contains the provided point
     *
     * @param point the provided point
     * @return the nearest staff, or null if none found
     */
    public StaffInfo getStaffAt (Point2D point)
    {
        return getStaffAt(point, staves);
    }

    //---------------//
    // getStaffCount //
    //---------------//
    /**
     * Report the total number of staves, whatever their containing
     * systems.
     *
     * @return the count of staves
     */
    public int getStaffCount ()
    {
        return staves.size();
    }

    //-----------//
    // getStaves //
    //-----------//
    /**
     * Report an unmodifiable view (perhaps empty) of list of current
     * staves.
     *
     * @return a view on staves
     */
    public List<StaffInfo> getStaves ()
    {
        return Collections.unmodifiableList(staves);
    }

    //--------//
    // render //
    //--------//
    /**
     * Paint all the staff lines
     *
     * @param g the graphics context (including current color and stroke)
     */
    public void render (Graphics2D g)
    {
        for (StaffInfo staff : staves) {
            staff.renderAttachments(g);
        }
    }
    //-------------//
    // getPartTops //
    //-------------//

    /**
     * @return the partTops
     */
    public Integer[] getPartTops ()
    {
        return partTops;
    }

    //-------------//
    // setPartTops //
    //-------------//
    /**
     * @param partTops the partTops to set
     */
    public void setPartTops (Integer[] partTops)
    {
        this.partTops = partTops;
    }

    //---------------//
    // getSystemTops //
    //---------------//
    /**
     * @return the systemTops
     */
    public Integer[] getSystemTops ()
    {
        return systemTops;
    }

    //---------------//
    // setSystemTops //
    //---------------//
    /**
     * @param systemTops the systemTops to set
     */
    public void setSystemTops (Integer[] systemTops)
    {
        this.systemTops = systemTops;
    }

    //-------//
    // reset //
    //-------//
    /**
     * Empty the whole collection of staves.
     */
    public void reset ()
    {
        staves.clear();
    }

    //~ Inner Classes ----------------------------------------------------------
    //
    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
            extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        Scale.Fraction samplingDx = new Scale.Fraction(
                4d,
                "Abscissa sampling to compute top & bottom limits of staff areas");

    }
}
