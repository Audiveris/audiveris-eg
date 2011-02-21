//----------------------------------------------------------------------------//
//                                                                            //
//                            B o a r d s P a n e                             //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright (C) Herve Bitteur 2000-2010. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.ui;

import omr.log.Logger;

import omr.sheet.Sheet;

import omr.ui.util.Panel;
import omr.ui.view.RubberPanel;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.layout.*;

import javax.swing.*;

/**
 * Class {@code BoardsPane} defines a comprehensive user board, where data
 * related to current point, run, section and glyph can be displayed in
 * dedicated boards, as well as a general-purpose Filter board and a custom
 * board.
 *
 * <p>There is one BoardsPane instance for each view of the same sheet.
 *
 * @author Hervé Bitteur
 */
public class BoardsPane
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(BoardsPane.class);

    //~ Instance fields --------------------------------------------------------

    /** The concrete UI component */
    private final Panel component;

    /** Collection of boards */
    private final Board[] boards;

    /** Unique (application-wide) name for this pane. */
    private String name;

    //~ Constructors -----------------------------------------------------------

    //------------//
    // BoardsPane //
    //------------//
    /**
     * Create a BoardsPane, with selected boards
     *
     * @param boards a varying number of boards
     */
    public BoardsPane (Board... boards)
    {
        this.boards = boards;

        component = new Panel();
        component.setNoInsets();
        component.add(defineLayout());
    }

    //~ Methods ----------------------------------------------------------------

    //--------------//
    // getComponent //
    //--------------//
    /**
     * Report the UI component
     *
     * @return the concrete component
     */
    public JComponent getComponent ()
    {
        return component;
    }

    //---------//
    // setName //
    //---------//
    /**
     * Assign the unique name for this boards pane
     *
     * @param name the assigned name
     */
    public void setName (String name)
    {
        this.name = name;
        component.setName(name);
    }

    //---------//
    // getName //
    //---------//
    /**
     * Report the unique name for this boards pane
     *
     * @return the declared name
     */
    public String getName ()
    {
        return name;
    }

    //---------//
    // connect //
    //---------//
    /**
     * Invoked when the boardsPane has been selected
     */
    public void connect ()
    {
        ///logger.info("+BoardPane " + name + " connect");
        for (Board board : boards) {
            board.connect();
        }
    }

    //------------//
    // disconnect //
    //------------//
    /**
     * Invoked when the boardsPane has been deselected
     */
    public void disconnect ()
    {
        ///logger.info("-BoardPane " + name + " disconnect");
        for (Board board : boards) {
            board.disconnect();
        }
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        return "{BoardsPane " + name + "}";
    }

    //--------------//
    // defineLayout //
    //--------------//
    private JPanel defineLayout ()
    {
        // Prepare layout elements
        final String  panelInterline = Panel.getPanelInterline();
        StringBuilder sbr = new StringBuilder();

        for (int n = 0; n <= boards.length; n++) {
            if (n != 0) {
                sbr.append(", ")
                   .append(panelInterline)
                   .append(", ");
            }

            sbr.append("pref");
        }

        FormLayout layout = new FormLayout("pref", sbr.toString());

        Panel      panel = new Panel();
        panel.setNoInsets();

        PanelBuilder builder = new PanelBuilder(layout, panel);
        builder.setDefaultDialogBorder();

        CellConstraints cst = new CellConstraints();

        // Now add the desired components, using provided order
        int r = 1;

        for (Board board : boards) {
            builder.add(board.getComponent(), cst.xy(1, r));
            r += 2;
        }

        return builder.getPanel();
    }
}
