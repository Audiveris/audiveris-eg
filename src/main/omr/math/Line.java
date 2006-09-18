//----------------------------------------------------------------------------//
//                                                                            //
//                                  L i n e                                   //
//                                                                            //
//  Copyright (C) Herve Bitteur 2000-2006. All rights reserved.               //
//  This software is released under the terms of the GNU General Public       //
//  License. Please contact the author at herve.bitteur@laposte.net           //
//  to report bugs & suggestions.                                             //
//----------------------------------------------------------------------------//
//
package omr.math;

import omr.util.Logger;

/**
 * Interface <code>Line</code> handles the equation of a line, whatever its
 * orientation, using the form : a*x + b*y + c = 0. The equation is normalized,
 * in other words, we always have a**2 + b**2 = 1.
 *
 * @author Herv&eacute; Bitteur
 * @version $Id$
 */
public interface Line
    extends java.io.Serializable
{
    //~ Static fields/initializers ---------------------------------------------

    /** The same logger, usable by all subclasses */
    static final Logger logger = Logger.getLogger(Line.class);

    //~ Methods ----------------------------------------------------------------

    //------//
    // getA //
    //------//
    /**
     * Return a, the coefficient of x in a*x + b*y +c
     *
     * @return x coefficient
     */
    double getA ();

    //------//
    // getB //
    //------//
    /**
     * Return b, the coefficient of y in a*x + b*y +c
     *
     * @return y coefficient
     */
    double getB ();

    //------//
    // getC //
    //------//
    /**
     * Return c, the coefficient of 1 in a*x + b*y +c
     *
     * @return 1 coefficient
     */
    double getC ();

    //------------------//
    // getInvertedSlope //
    //------------------//
    /**
     * Return -b/a, from a*x + b*y +c
     *
     * @return the x/y coefficient
     */
    double getInvertedSlope ();

    //-----------------//
    // getMeanDistance //
    //-----------------//
    /**
     * Return the mean quadratic distance of the defining population of points
     * to the resulting line. This can be used to measure how well the line fits
     * the points.
     *
     * @return the absolute value of the mean distance
     */
    double getMeanDistance ();

    //-------------------//
    // getNumberOfPoints //
    //-------------------//
    /**
     * Return the cardinality of the population of defining points.
     *
     * @return the number of defining points so far
     */
    int getNumberOfPoints ();

    //----------//
    // getSlope //
    //----------//
    /**
     * Return -a/b, from a*x + b*y +c
     *
     * @return the y/x coefficient
     */
    double getSlope ();

    //------------//
    // distanceOf //
    //------------//
    /**
     * Compute the orthogonal distance between the line and the provided
     * point. Note that the distance may be negative.
     *
     * @param x the point abscissa
     * @param y the point ordinate
     *
     * @return the algebraic orthogonal distance
     */
    double distanceOf (double x,
                       double y);

    //-------------//
    // includeLine //
    //-------------//
    /**
     * Add the whole population of another line, which results in merging this
     * other line with the line at hand.
     *
     * @param other the other line
     *
     * @return this augmented line, which permits to chain the additions.
     */
    Line includeLine (Line other);

    //--------------//
    // includePoint //
    //--------------//
    /**
     * Add the coordinates of a point in the defining population of points.
     *
     * @param x abscissa of the new point
     * @param y ordinate of the new point
     */
    void includePoint (double x,
                       double y);

    //-------//
    // reset //
    //-------//
    /**
     * Remove the whole population of points. The line is not immediately
     * usable, it needs now to include defining points.
     */
    void reset ();

    //-----//
    // xAt //
    //-----//
    /**
     * Retrieve the abscissa where the line crosses the given ordinate y.
     * Beware of horizontal lines !!!
     *
     * @param y the imposed ordinate
     *
     * @return the corresponding x value
     */
    double xAt (double y);

    //-----//
    // xAt //
    //-----//
    /**
     * Retrieve the abscissa where the line crosses the given ordinate y,
     * rounded to the nearest integer value.  Beware of horizontal lines !!!
     *
     * @param y the imposed ordinate
     *
     * @return the corresponding x value
     */
    int xAt (int y);

    //-----//
    // yAt //
    //-----//
    /**
     * Retrieve the ordinate where the line crosses the given abscissa x.
     * Beware of vertical lines !!!
     *
     * @param x the imposed abscissa
     *
     * @return the corresponding y value
     */
    double yAt (double x);

    //-----//
    // yAt //
    //-----//
    /**
     * Retrieve the ordinate where the line crosses the given abscissa x,
     * rounded to the nearest integer value.  Beware of vertical lines !!!
     *
     * @param x the imposed abscissa
     *
     * @return the corresponding y value
     */
    int yAt (int x);

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Specific exception raised when trying to use a line with undefined
     * parameters
     */
    static class UndefinedLineException
        extends RuntimeException
    {
        UndefinedLineException (String message)
        {
            super(message);
        }
    }
}
