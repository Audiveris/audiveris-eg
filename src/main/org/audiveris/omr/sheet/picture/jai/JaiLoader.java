//----------------------------------------------------------------------------//
//                                                                            //
//                              J a i L o a d e r                             //
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
package org.audiveris.omr.sheet.picture.jai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.RenderedImage;
import java.io.File;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.media.jai.JAI;

/**
 * Class {@code JaiLoader} is meant to keep JAI-based image features
 * separate from the rest of Audiveris application, and thus saving on
 * jar download.
 *
 * @author Brenton Partridge
 * @author Hervé Bitteur
 */
public class JaiLoader
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(JaiLoader.class);

//    /** A future which reflects whether JAI has been initialized * */
//    private static final Future<Void> loading = OmrExecutors.getCachedLowExecutor()
//            .submit(
//            new Callable<Void>()
//    {
//        @Override
//        public Void call ()
//                throws Exception
//        {
//            javax.media.jai.JAI.getBuildVersion();
//
//            return null;
//        }
//    });
    //~ Constructors -----------------------------------------------------------
    //-----------//
    // JaiLoader //
    //-----------//
    private JaiLoader ()
    {
    }

    //~ Methods ----------------------------------------------------------------
//    //--------------//
//    // ensureLoaded //
//    //--------------//
//    /**
//     * Blocks until the JAI class has been initialized.
//     * If initialization has not yet begun, begins initialization.
//     *
//     */
//    public static void ensureLoaded ()
//    {
//        try {
//            loading.get();
//        } catch (Exception ex) {
//            logger.error("Cannot load JAI", ex);
//        }
//    }
    //---------//
    // preload //
    //---------//
    /**
     * On the first call, starts the initialization.
     */
    public static void preload ()
    {
        // Empty body, the purpose is just to trigger class elaboration
    }

    //---------//
    // loadJAI //
    //---------//
    /**
     * Try to load an image, using JAI.
     * This seems limited to a single image, thus no id parameter is to be
     * provided.
     *
     * @param imgFile the input file
     * @return a map of one image, or null if failed to load
     */
    public static SortedMap<Integer, RenderedImage> loadJAI (File imgFile)
    {
        RenderedImage image = JAI.create("fileload", imgFile.getPath());

        try {
            if ((image.getWidth() > 0) && (image.getHeight() > 0)) {
                SortedMap<Integer, RenderedImage> images = new TreeMap<>();
                images.put(1, image);

                return images;
            }
        } catch (Exception ex) {
            logger.debug(ex.getMessage());
        }

        return null;
    }
}
