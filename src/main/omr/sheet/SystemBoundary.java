//----------------------------------------------------------------------------//
//                                                                            //
//                        S y s t e m B o u n d a r y                         //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur 2000-2012. All rights reserved.                 //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.sheet;

import omr.log.Logger;

import omr.util.BrokenLine;
import omr.util.VerticalSide;

import net.jcip.annotations.NotThreadSafe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

/**
 * Class {@code SystemBoundary} handles the closed boundary of a system
 * as a 2D area, defined by two broken lines, on north and south sides.
 *
 * @author Hervé Bitteur
 */
@NotThreadSafe
public class SystemBoundary
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(SystemBoundary.class);

    //~ Instance fields --------------------------------------------------------

    /** Related system */
    private final SystemInfo system;

    /** The north and south limits */
    private final EnumMap<VerticalSide, BrokenLine> limits = new EnumMap<>(
        VerticalSide.class);

    /** Handling of the SystemBoundary is delegated to a Polygon */
    private final Polygon polygon = new Polygon();

    //~ Constructors -----------------------------------------------------------

    //----------------//
    // SystemBoundary //
    //----------------//
    /**
     * Creates a new SystemBoundary object with north and south borders
     * @param system the related system
     * @param north the northern limit
     * @param south the southern limit
     */
    public SystemBoundary (SystemInfo system,
                           BrokenLine north,
                           BrokenLine south)
    {
        if ((north == null) || (south == null)) {
            throw new IllegalArgumentException(
                "SystemBoundary needs non-null limits");
        }

        this.system = system;
        limits.put(VerticalSide.TOP, north);
        limits.put(VerticalSide.BOTTOM, south);

        buildPolygon();
    }

    //~ Methods ----------------------------------------------------------------

    //----------//
    // contains //
    //----------//
    /**
     * Check whether the provided point lies within the SystemBoundary
     * @param point the provided point
     * @return true if the provided point lies within the SystemBoundary
     */
    public boolean contains (Point point)
    {
        return polygon.contains(point);
    }

    //-----------//
    // getBounds //
    //-----------//
    /**
     * Report the rectangular bounds that enclose this boundary
     * @return the rectangular bounds
     */
    public Rectangle getBounds ()
    {
        return polygon.getBounds();
    }

    //----------//
    // getLimit //
    //----------//
    /**
     * Report the broken line on provided side
     * @param side the desired side (TOP or BOTTOM)
     * @return the desired limit
     */
    public BrokenLine getLimit (VerticalSide side)
    {
        return limits.get(side);
    }

    //-----------//
    // getLimits //
    //-----------//
    /**
     * Report the limits as a collection
     * @return the north and south limits
     */
    public Collection<BrokenLine> getLimits ()
    {
        return limits.values();
    }

    //--------//
    // render //
    //--------//
    /**
     * Paint the SystemBoundary in the provided graphic context.
     * @param g     the Graphics context
     * @param editable flag to indicate that boundary is editable
     */
    public void render (Graphics g,
                        boolean  editable)
    {
        Graphics2D g2 = (Graphics2D) g;
        int        radius = limits.get(VerticalSide.TOP)
                                  .getStickyDistance();

        Color      oldColor = g.getColor();

        if (editable) {
            g.setColor(Color.RED);
        }

        // Draw the polygon
        g2.drawPolygon(polygon);

        // Mark the points
        if (editable) {
            for (int i = 0; i < polygon.npoints; i++) {
                g2.drawRect(
                    polygon.xpoints[i] - radius,
                    polygon.ypoints[i] - radius,
                    2 * radius,
                    2 * radius);
            }
        }

        g.setColor(oldColor);
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        return "{Boundary" + " system#" + system.getId() + " north:" +
               limits.get(VerticalSide.TOP) + " south:" +
               limits.get(VerticalSide.BOTTOM) + "}";
    }

    //--------//
    // update //
    //--------//
    /**
     * Update the system boundary, using the (updated) limits lines
     */
    public void update ()
    {
        // Simply rebuild polygon, 
        buildPolygon();
    }

    //--------------//
    // buildPolygon //
    //--------------//
    private void buildPolygon ()
    {
        polygon.reset();

        // North
        for (Point point : limits.get(VerticalSide.TOP)
                                 .getPoints()) {
            polygon.addPoint(point.x, point.y);
        }

        // South (in reverse order)
        List<Point> reverse = new ArrayList<>(
            limits.get(VerticalSide.BOTTOM).getPoints());
        Collections.reverse(reverse);

        for (Point point : reverse) {
            polygon.addPoint(point.x, point.y);
        }
    }
}
