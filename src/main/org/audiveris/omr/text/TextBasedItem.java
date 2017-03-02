//----------------------------------------------------------------------------//
//                                                                            //
//                          O c r B a s e d I t e m                           //
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
package org.audiveris.omr.text;

import org.audiveris.omr.util.Vip;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;

/**
 * Class {@code TextBasedItem} is an abstract TextItem with
 * baseline and confidence informations.
 *
 * @author Hervé Bitteur
 */
public abstract class TextBasedItem
        extends TextItem
        implements Vip
{
    //~ Instance fields --------------------------------------------------------

    /** Baseline. */
    private Line2D baseline;

    /** OCR confidence. */
    private Integer confidence;

    /** (Debug) flag this object as VIP. */
    private boolean vip;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new TextBasedItem object.
     *
     * @param bounds     Bounding box
     * @param value      UTF-8 content for this item
     * @param baseline   item baseline
     * @param confidence OCR confidence in this item content
     */
    public TextBasedItem (Rectangle bounds,
                          String value,
                          Line2D baseline,
                          Integer confidence)
    {
        super(bounds, value);

        this.baseline = baseline;
        this.confidence = confidence;
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // baselineOf //
    //------------//
    public static Line2D baselineOf (List<? extends TextBasedItem> items)
    {
        Point2D first = items.get(0)
                .getBaseline()
                .getP1();
        Point2D last = items.get(items.size() - 1)
                .getBaseline()
                .getP2();

        return new Line2D.Double(first, last);
    }

    //--------------//
    // confidenceOf //
    //--------------//
    public static int confidenceOf (Collection<? extends TextBasedItem> items)
    {
        // Use average confidence
        double total = 0;

        for (TextBasedItem item : items) {
            total += item.getConfidence();
        }

        return (int) Math.rint(total / items.size());
    }

    //-------------//
    // getBaseline //
    //-------------//
    /**
     * Report the word baseline
     *
     * @return the item baseline
     */
    public Line2D getBaseline ()
    {
        return baseline;
    }

    //---------------//
    // getConfidence //
    //---------------//
    /**
     * Report the item confidence level
     *
     * @return the confidence or null
     */
    public Integer getConfidence ()
    {
        return confidence;
    }

    //-------------//
    // getLocation //
    //-------------//
    public Point getLocation ()
    {
        Line2D bl = getBaseline();

        if (bl == null) {
            return null;
        }

        return new Point(
                (int) Math.rint(bl.getX1()),
                (int) Math.rint(bl.getY1()));
    }

    //-------//
    // isVip //
    //-------//
    @Override
    public boolean isVip ()
    {
        return vip;
    }

    //
    //-------------//
    // setBaseline //
    //-------------//
    /**
     * Assign the word baseline
     *
     * @param baseline the new item baseline
     */
    public void setBaseline (Line2D baseline)
    {
        this.baseline = baseline;
    }

    //---------------//
    // setConfidence //
    //---------------//
    /**
     * Assign the item confidence level
     *
     * @param confidence the confidence or null
     */
    public void setConfidence (Integer confidence)
    {
        this.confidence = confidence;
    }

    //--------//
    // setVip //
    //--------//
    @Override
    public void setVip ()
    {
        vip = true;
    }

    //-----------//
    // translate //
    //-----------//
    /**
     * Apply a translation to this based item.
     *
     * @param dx abscissa translation
     * @param dy ordinate translation
     */
    @Override
    public void translate (int dx,
                           int dy)
    {
        // Translate bounds
        super.translate(dx, dy);

        // Translate baseline
        if (getBaseline() != null) {
            baseline.setLine(
                    baseline.getX1() + dx,
                    baseline.getY1() + dy,
                    baseline.getX2() + dx,
                    baseline.getY2() + dy);
        }
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder(super.internalsString());

        if (getConfidence() != null) {
            sb.append(" conf:")
                    .append(getConfidence());
        }

        if (getBaseline() != null) {
            sb.append(
                    String.format(
                    " base[%.0f,%.0f]-[%.0f,%.0f]",
                    baseline.getX1(),
                    baseline.getY1(),
                    baseline.getX2(),
                    baseline.getY2()));
        }

        return sb.toString();
    }
}
