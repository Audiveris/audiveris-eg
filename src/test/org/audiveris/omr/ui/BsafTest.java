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
package org.audiveris.omr.ui;

import com.jgoodies.looks.LookUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.audiveris.omr.util.BaseTestCase;

import org.jdesktop.application.SingleFrameApplication;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import org.audiveris.omr.ui.util.SeparableToolBar;
import org.jdesktop.application.Application;
import org.junit.Test;

/**
 *
 * @author herve
 */
public class BsafTest
        extends BaseTestCase
{
    //~ Methods ----------------------------------------------------------------

    public static final void main (String... args)
    {
        Application.launch(Appl.class, null);
    }

    @Test
    public void testAppl ()
    {
        Application.launch(Appl.class, null);

    }

    //~ Inner Classes ----------------------------------------------------------
    private static class Appl
            extends SingleFrameApplication
    {
        //~ Methods ------------------------------------------------------------

        @Override
        protected void startup ()
        {
            String lafName =
                    LookUtils.IS_OS_WINDOWS_XP
                    ? com.jgoodies.looks.Options.getCrossPlatformLookAndFeelClassName()
                    : com.jgoodies.looks.Options.getSystemLookAndFeelClassName();

            try {
                UIManager.setLookAndFeel(lafName);
            } catch (Exception e) {
                System.err.println("Can't set look & feel:" + e);
            }


            JFrame frame = getMainFrame();
            frame.setJMenuBar(buildMenuBar());
            frame.setContentPane(buildContentPane());
            frame.setSize(600, 400);
            frame.setTitle("BSAF Test");
            show(frame);
        }

        /**
         * Builds and answers the menu bar.
         */
        private JMenuBar buildMenuBar ()
        {
            JMenu menu;
            JMenuBar innerBar = new JMenuBar();
            innerBar.putClientProperty(com.jgoodies.looks.Options.HEADER_STYLE_KEY, Boolean.TRUE);

            menu = new JMenu("File");
            menu.add(new JMenuItem("New..."));
            menu.add(new JMenuItem("Open..."));
            menu.add(new JMenuItem("Save"));
            menu.addSeparator();
            menu.add(new JMenuItem("Print..."));
            innerBar.add(menu);

            menu = new JMenu("Edit");
            menu.add(new JMenuItem("Cut"));
            menu.add(new JMenuItem("Copy"));
            menu.add(new JMenuItem("Paste"));
            innerBar.add(menu);

            JProgressBar bar;

            JMenuBar outerBar = new JMenuBar();
            outerBar.setLayout(new GridLayout(1, 0));

            outerBar.add(innerBar);
            outerBar.add(new JLabel("I'm the banner!", JLabel.CENTER));

            return outerBar;
        }

        /**
         * Builds and answers the content pane.
         */
        private JComponent buildContentPane ()
        {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.orange);
            panel.add(buildToolBar(), BorderLayout.NORTH);
            panel.add(createCenteredLabel("Content"), BorderLayout.CENTER);
            panel.add(buildStatusBar(), BorderLayout.SOUTH);
            return panel;
        }

        /**
         * Builds and answers the tool bar.
         */
        private Component buildStatusBar ()
        {
            JPanel statusBar = new JPanel(new BorderLayout());
            statusBar.setBackground(Color.LIGHT_GRAY);
            statusBar.add(createCenteredLabel("Status Bar"));
            return statusBar;
        }

        /**
         * Builds and answers the tool bar.
         */
        private Component buildToolBar ()
        {
            JToolBar toolBar = new SeparableToolBar();
            toolBar.setBackground(Color.PINK);
            ///toolBar.putClientProperty(Options.HEADER_STYLE_KEY, Boolean.TRUE);

            toolBar.add(createCenteredLabel("Tool Bar"));

            JButton button1 = new JButton("Bouton #1");
            toolBar.add(button1);

            toolBar.addSeparator();

            JButton button2 = new JButton("Bouton #2");
            toolBar.add(button2);

            return toolBar;
        }

        /**
         * Creates and answers a
         * <code>JLabel</code> that has the text
         * centered and that is wrapped with an empty border.
         */
        private Component createCenteredLabel (String text)
        {
            JLabel label = new JLabel(text);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(new EmptyBorder(3, 3, 3, 3));
            return label;
        }
    }
}
