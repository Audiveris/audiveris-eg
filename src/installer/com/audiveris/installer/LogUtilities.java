//----------------------------------------------------------------------------//
//                                                                            //
//                           L o g U t i l i t i e s                          //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur and others 2000-2013. All rights reserved.      //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package com.audiveris.installer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.util.StatusPrinter;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class {@code LogUtilities} handles default logging configuration
 * for the Installer.
 *
 * @author Hervé Bitteur
 */
public class LogUtilities
{
    //~ Static fields/initializers ---------------------------------------------

    /** System property for LogBack configuration. */
    private static final String LOGBACK_LOGGING_KEY = "logback.configurationFile";

    //~ Methods ----------------------------------------------------------------
    //------------//
    // initialize //
    //------------//
    /**
     * Check for (BackLog) logging configuration, and if not found,
     * define a minimal configuration.
     * This method should be called at the very beginning of the program before
     * any logging request is sent.
     */
    public static void initialize ()
    {
        // Check if system property is set and points to a real file
        final String loggingProp = System.getProperty(LOGBACK_LOGGING_KEY);

        if (loggingProp != null) {
            File loggingFile = new File(loggingProp);

            if (loggingFile.exists()) {
                // Everything seems OK, let LogBack use the config file
                System.out.println("Using " + loggingFile.getAbsolutePath());

                return;
            } else {
                System.out.println(
                        "File " + loggingFile.getAbsolutePath()
                        + " does not exist.");
            }
        } else {
            System.out.println(
                    "Property " + LOGBACK_LOGGING_KEY + " not defined.");
        }

        // Define a minimal logging configuration, programmatically
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(
                Logger.ROOT_LOGGER_NAME);

        // CONSOLE
        ConsoleAppender consoleAppender = new ConsoleAppender();
        PatternLayoutEncoder consoleEncoder = new PatternLayoutEncoder();
        consoleAppender.setName("CONSOLE");
        consoleAppender.setContext(loggerContext);
        consoleEncoder.setContext(loggerContext);
        consoleEncoder.setPattern("%-5level %caller{1} - %msg%ex%n");
        consoleEncoder.start();
        consoleAppender.setEncoder(consoleEncoder);
        consoleAppender.start();
        root.addAppender(consoleAppender);

        // FILE (located in default temp directory)
        File logFile;
        FileAppender fileAppender = new FileAppender();
        PatternLayoutEncoder fileEncoder = new PatternLayoutEncoder();
        fileAppender.setName("FILE");
        fileAppender.setContext(loggerContext);
        fileAppender.setAppend(false);

        String now = new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(
                new Date());
        logFile = Paths.get(
                System.getProperty("java.io.tmpdir"),
                "audiveris-installer-" + now + ".log")
                .toFile();
        fileAppender.setFile(logFile.getAbsolutePath());
        fileEncoder.setContext(loggerContext);
        fileEncoder.setPattern("%date %level \\(%file:%line\\) - %msg%ex%n");
        fileEncoder.start();
        fileAppender.setEncoder(fileEncoder);
        fileAppender.start();
        root.addAppender(fileAppender);

        // VIEW (filtered on INFO+)
        Appender guiAppender = new ViewAppender();
        guiAppender.setName("VIEW");
        guiAppender.setContext(loggerContext);

        Filter filter = new Filter()
        {
            @Override
            public FilterReply decide (Object obj)
            {
                if (!isStarted()) {
                    return FilterReply.NEUTRAL;
                }

                LoggingEvent event = (LoggingEvent) obj;

                if (event.getLevel()
                        .toInt() >= Level.INFO_INT) {
                    return FilterReply.NEUTRAL;
                } else {
                    return FilterReply.DENY;
                }
            }
        };

        filter.start();

        guiAppender.addFilter(filter);
        guiAppender.start();
        root.addAppender(guiAppender);

        // Levels
        root.setLevel(Level.DEBUG);

        Logger jarExpander = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(
                JarExpander.class);
        jarExpander.setLevel(Level.INFO);

        // OPTIONAL: print logback internal status messages
        StatusPrinter.print(loggerContext);

        root.info("Logging to file {}", logFile.getAbsolutePath());
    }
}
