//----------------------------------------------------------------------------//
//                                                                            //
//                             C l a s s U t i l                              //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur and others 2000-2013. All rights reserved.      //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Class {@code ClassUtil} provides utilities related to Class handling.
 *
 * @author Hervé Bitteur
 */
public class ClassUtil
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    //~ Methods ----------------------------------------------------------------
    //-----------------//
    // getCallingFrame //
    //-----------------//
    /**
     * Infer the calling frame, skipping the given classes if so provided.
     * Code was derived from a private method found in the JDK Logger class
     *
     * @param skippedClasses the classes to skip
     * @return the frame found, just before the skipped classes (or just before
     *         the caller of this method)
     */
    public static StackTraceElement getCallingFrame (Class... skippedClasses)
    {
        // Get the current stack trace.
        StackTraceElement[] stack = (new Throwable()).getStackTrace();

        // Simple case, no classes to skip, just return the caller of the caller
        if (skippedClasses.length == 0) {
            return stack[2];
        }

        // More complex case, return the caller, just before the skipped classes

        // First, search back to a method in the skipped classes, if any
        int ix;
        searchingForSkipped:
        for (ix = 0; ix < stack.length; ix++) {
            StackTraceElement frame = stack[ix];
            String cname = frame.getClassName();

            for (Class<?> skipped : skippedClasses) {
                if (cname.equals(skipped.getName())) {
                    break searchingForSkipped;
                }
            }
        }

        // Now search for the first frame before the skipped classes
        searchingForNonSkipped:
        for (; ix < stack.length; ix++) {
            StackTraceElement frame = stack[ix];
            String cname = frame.getClassName();

            for (Class<?> skipped : skippedClasses) {
                if (cname.equals(skipped.getName())) {
                    continue searchingForNonSkipped;
                }
            }

            // We've found the relevant frame.
            return frame;
        }

        // We haven't found a suitable frame
        return null;
    }

    //-----------------//
    // getCallingFrame //
    //-----------------//
    /**
     * Infer the calling frame, skipping the given classes if so provided.
     * Code was derived from a private method found in the JDK Logger class
     *
     * @param skipped predicate to skip class(es)
     * @return the frame found, just before the skipped classes (or just before
     *         the caller of this method)
     */
    public static StackTraceElement getCallingFrame (Predicate<String> skipped)
    {
        // Get the current stack trace.
        StackTraceElement[] stack = (new Throwable()).getStackTrace();

        // Simple case, no classes to skip, just return the caller of the caller
        if (skipped == null) {
            return stack[2];
        } else {
            // More complex case, skip the unwanted classes
            int ix;
            searchingForSkipped:
            for (ix = 2; ix < stack.length; ix++) {
                StackTraceElement frame = stack[ix];
                String cname = frame.getClassName();

                if (!skipped.check(cname)) {
                    return frame;
                }
            }

            // We haven't found a suitable frame
            return null;
        }
    }

    //------//
    // load //
    //------//
    /**
     * Try to load a (library) file.
     *
     * @param file the file to load, which must point to the precise location
     * @throws Exception
     */
    public static void load (File file)
            throws Throwable
    {
        String path = file.getAbsolutePath();

        logger.debug("Loading file {} ...", path);

        try {
            System.load(path);

            logger.debug("Loaded  file {}", path);
        } catch (Throwable ex) {
            if (logger.isDebugEnabled()) {
                logger.warn("Error while loading file " + path, ex);
            }

            throw ex;
        }
    }

    //-------------//
    // loadLibrary //
    //-------------//
    /**
     * Try to load a library.
     *
     * @param library the library to load (without ".dll" suffix for Windows,
     *                without "lib" prefix and ".so" suffix for Linux
     * @return true if succeeded, false otherwise (no exception is thrown)
     */
    public static boolean loadLibrary (String library)
    {
        logger.debug("loadLibrary for {} ...", library);

        try {
            System.loadLibrary(library);

            logger.debug("Loaded  library {}", library);
            return true;
        } catch (Throwable ex) {
            logger.warn("Error while loading library " + library, ex);

            return false;
        }
    }

    //--------//
    // nameOf //
    //--------//
    /**
     * Report the full name of the object class, without the package
     * information.
     *
     * @param obj the object to name
     * @return the concatenation of (enclosing) simple names
     */
    public static String nameOf (Object obj)
    {
        StringBuilder sb = new StringBuilder();

        for (Class<?> cl = obj.getClass(); cl != null;
                cl = cl.getEnclosingClass()) {
            if (sb.length() > 0) {
                sb.insert(0, "-");
            }

            sb.insert(0, cl.getSimpleName());
        }

        return sb.toString();
    }

    private ClassUtil ()
    {
    }
}
