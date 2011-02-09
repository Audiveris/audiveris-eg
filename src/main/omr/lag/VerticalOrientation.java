//----------------------------------------------------------------------------//
//                                                                            //
//                   V e r t i c a l O r i e n t a t i o n                    //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright (C) Herve Bitteur 2000-2010. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.lag;

import omr.math.Line;

import omr.score.common.PixelPoint;
import omr.score.common.PixelRectangle;

import omr.util.Implement;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Class <code>VerticalOrientation</code> defines an orientation where sections
 * are vertical (coord is y, pos is x)
 *
 * @author Hervé Bitteur
 */
public class VerticalOrientation
    implements Oriented
{
    //~ Static fields/initializers ---------------------------------------------

    /** Constant orientation */
    public static final LagOrientation orientation = LagOrientation.VERTICAL;

    //~ Methods ----------------------------------------------------------------

    //----------------//
    // getOrientation //
    //----------------//
    @Implement(Oriented.class)
    public LagOrientation getOrientation ()
    {
        return orientation;
    }

    //------------//
    // isVertical //
    //------------//
    /**
     * A vertical lag IS indeed vertical !
     *
     * @return true
     */
    @Implement(Oriented.class)
    public boolean isVertical ()
    {
        return true;
    }

    //-----------//
    // switchRef //
    //-----------//
    /**
     * Retrieve absolute coordinates of a point relative to the (horizontal)
     * lag. Based on current lag implementation, this method implies to switch
     * coordinates values
     *
     * @param cp the relative coordinates
     * @param xy variable for the absolute coordinates, or null
     *
     * @return the absolute coordinates
     */
    @Implement(Oriented.class)
    public PixelPoint switchRef (Point      cp,
                                 PixelPoint xy)
    {
        if (cp == null) {
            return null;
        }

        if (xy == null) {
            xy = new PixelPoint();
        }

        // Vertical swap: coord->y, pos->x
        xy.x = cp.y;
        xy.y = cp.x;

        return xy;
    }

    //-----------//
    // switchRef //
    //-----------//
    /**
     * Retrieve absolute coordinates of a rectangle relative to the (horizontal)
     * lag. Based on current lag implementation, this method implies to switch
     * coordinates values
     *
     * @param cplt the relative coordinates
     * @param xywh variable for the absolute coordinates, or null
     *
     * @return the absolute coordinates
     */
    @Implement(Oriented.class)
    public PixelRectangle switchRef (Rectangle      cplt,
                                     PixelRectangle xywh)
    {
        if (cplt == null) {
            return null;
        }

        if (xywh == null) {
            xywh = new PixelRectangle();
        }

        // Vertical swap: coord->y, pos->x, length->height, thickness->width
        xywh.x = cplt.y;
        xywh.y = cplt.x;
        xywh.width = cplt.height;
        xywh.height = cplt.width;

        return xywh;
    }

    //-----------//
    // switchRef //
    //-----------//
    /**
     * Given an oriented line, return the corresponding absolute line, or vice
     * versa. Based on current lag implementation, this method implies to switch
     * coordinates values
     *
     * @param relLine the oriented line
     *
     * @return the corresponding absolute line.
     */
    @Implement(Oriented.class)
    public Line switchRef (Line relLine)
    {
        if (relLine == null) {
            return null;
        } else {
            return relLine.swappedCoordinates();
        }
    }
}
