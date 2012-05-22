//----------------------------------------------------------------------------//
//                                                                            //
//                                 B e n c h                                  //
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

import omr.score.ScoreBench;

import java.util.Properties;

/**
 * Class {@code Bench} defines the general features of a bench, used by
 * each individual {@link SheetBench} and the containing {@link
 * ScoreBench}.
 *
 * @author Hervé Bitteur
 */
public abstract class Bench
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(Bench.class);

    //~ Instance fields --------------------------------------------------------
    /** The internal set of properties */
    protected final Properties props = new Properties();

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new Bench object.
     */
    public Bench ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //------------//
    // flushBench //
    //------------//
    /**
     * Flush the current content of bench to disk
     */
    protected abstract void flushBench ();

    //---------//
    // addProp //
    //---------//
    /**
     * This is a specific setProperty functionality, that creates unique
     * keys by appending numbered suffixes
     *
     * @param radix the provided radix (to which proper suffix will be appended)
     * @param value the property value
     */
    protected void addProp (String radix,
                            String value)
    {
        if ((value == null) || (value.length() == 0)) {
            return;
        }

        String key = null;
        int index = 0;

        do {
            key = keyOf(radix, ++index);
        } while (props.containsKey(key));

        props.setProperty(key, value);

        logger.fine("addProp key:{0} value:{1}", new Object[]{key, value});
    }

    //-------//
    // keyOf //
    //-------//
    protected String keyOf (String radix,
                            int index)
    {
        return String.format("%s.%02d", radix, index);
    }
}
