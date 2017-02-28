//----------------------------------------------------------------------------//
//                                                                            //
//                           D y n a m i c M e n u                            //
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
package org.audiveris.omr.ui.util;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * Class {@code DynamicMenu} simplifies the definition of a menu,
 * whose content needs to be updated on-the-fly when the menu is being
 * selected.
 *
 * @author Hervé Bitteur
 */
public abstract class DynamicMenu
{
    //~ Instance fields --------------------------------------------------------

    /** The concrete UI menu */
    private final JMenu menu;

    /** Specific menu listener */
    private MenuListener listener = new MenuListener()
    {
        @Override
        public void menuCanceled (MenuEvent e)
        {
        }

        @Override
        public void menuDeselected (MenuEvent e)
        {
        }

        @Override
        public void menuSelected (MenuEvent e)
        {
            // Clean up the whole menu
            menu.removeAll();

            // Rebuild the whole list of menu items on the fly
            buildItems();
        }
    };

    //~ Constructors -----------------------------------------------------------
    //-------------//
    // DynamicMenu //
    //-------------//
    /**
     * Create the dynamic menu.
     *
     * @param menuLabel the label to be used for the menu
     */
    public DynamicMenu (String menuLabel)
    {
        menu = new JMenu(menuLabel);

        // Listener to menu selection, to modify content on-the-fly
        menu.addMenuListener(listener);
    }

    /**
     * Creates a new DynamicMenu object.
     *
     * @param action related action
     */
    public DynamicMenu (Action action)
    {
        menu = new JMenu(action);

        // Listener to menu selection, to modify content on-the-fly
        menu.addMenuListener(listener);
    }

    //~ Methods ----------------------------------------------------------------
    //---------//
    // getMenu //
    //---------//
    /**
     * Report the concrete menu.
     *
     * @return the usable menu
     */
    public JMenu getMenu ()
    {
        return menu;
    }

    //------------//
    // buildItems //
    //------------//
    /**
     * This is the method that is called whenever the menu is selected.
     * To be implemented in a subclass.
     */
    protected abstract void buildItems ();
}
