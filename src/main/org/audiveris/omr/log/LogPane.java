//----------------------------------------------------------------------------//
//                                                                            //
//                               L o g P a n e                                //
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
package org.audiveris.omr.log;

import org.audiveris.omr.constant.Constant;
import org.audiveris.omr.constant.ConstantSet;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Class {@code LogPane} defines the pane dedicated to application-level
 * messages, those that are logged using the {@code Logger} class.
 *
 * @author Hervé Bitteur
 */
public class LogPane
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(LogPane.class);

    //~ Instance fields --------------------------------------------------------
    /** The scrolling text area */
    private JScrollPane component;

    /** Status/log area */
    private final JTextPane logArea;

    private final AbstractDocument document;

    private final SimpleAttributeSet attributes = new SimpleAttributeSet();

    //~ Constructors -----------------------------------------------------------
    //---------//
    // LogPane //
    //---------//
    /**
     * Create the log pane, with a standard mailbox.
     */
    public LogPane ()
    {
        // Build the scroll pane
        component = new JScrollPane();
        component.setBorder(null);

        // log/status area
        logArea = new JTextPane();
        logArea.setEditable(false);
        logArea.setMargin(new Insets(5, 5, 5, 5));
        document = (AbstractDocument) logArea.getStyledDocument();

        // Let the scroll pane display the log area
        component.setViewportView(logArea);
    }

    //~ Methods ----------------------------------------------------------------
    //----------//
    // clearLog //
    //----------//
    /**
     * Clear the current content of the log
     */
    public void clearLog ()
    {
        logArea.setText("");
        logArea.setCaretPosition(0);
        component.repaint();
    }

    //--------------//
    // getComponent //
    //--------------//
    /**
     * Give access to the real component
     *
     * @return the concrete component
     */
    public JComponent getComponent ()
    {
        return component;
    }

    //-----------//
    // notifyLog //
    //-----------//
    /**
     * Notify that there is one or more log records in the Logger mailbox.
     */
    public void notifyLog ()
    {
        SwingUtilities.invokeLater(
                new Runnable()
        {
            @Override
            public void run ()
            {
                while (LogGuiAppender.getEventCount() > 0) {
                    ILoggingEvent event = LogGuiAppender.pollEvent();

                    if (event != null) {
                        // Color
                        StyleConstants.setForeground(
                                attributes,
                                getLevelColor(event.getLevel()));

                        // Font name
                        StyleConstants.setFontFamily(
                                attributes,
                                constants.fontName.getValue());

                        // Font size
                        StyleConstants.setFontSize(
                                attributes,
                                constants.fontSize.getValue());

                        try {
                            document.insertString(
                                    document.getLength(),
                                    event.getFormattedMessage() + "\n",
                                    attributes);
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    //---------------//
    // getLevelColor //
    //---------------//
    private Color getLevelColor (Level level)
    {
        if (level.isGreaterOrEqual(Level.ERROR)) {
            return Color.RED;
        } else if (level.isGreaterOrEqual(Level.WARN)) {
            return Color.BLUE;
        } else if (level.isGreaterOrEqual(Level.INFO)) {
            return Color.BLACK;
        } else {
            return Color.GRAY;
        }
    }

    //~ Inner Classes ----------------------------------------------------------
    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
            extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        Constant.Integer fontSize = new Constant.Integer(
                "Points",
                10,
                "Font size for log pane");

        Constant.String fontName = new Constant.String(
                "Lucida Console",
                "Font name for log pane");

    }
}
