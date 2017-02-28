//----------------------------------------------------------------------------//
//                                                                            //
//                    A b s t r a c t C e l l E d i t o r                     //
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
package org.audiveris.omr.ui.treetable;

import java.util.EventObject;

import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

/**
 * DOCUMENT ME!
 *
 * @author Hervé Bitteur
 */
public class AbstractCellEditor
        implements CellEditor
{
    //~ Instance fields --------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected EventListenerList listenerList = new EventListenerList();

    //~ Methods ----------------------------------------------------------------
    //-----------------------//
    // addCellEditorListener //
    //-----------------------//
    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    @Override
    public void addCellEditorListener (CellEditorListener l)
    {
        listenerList.add(CellEditorListener.class, l);
    }

    //-------------------//
    // cancelCellEditing //
    //-------------------//
    /**
     * DOCUMENT ME!
     */
    @Override
    public void cancelCellEditing ()
    {
    }

    //--------------------//
    // getCellEditorValue //
    //--------------------//
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public Object getCellEditorValue ()
    {
        return null;
    }

    //----------------//
    // isCellEditable //
    //----------------//
    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public boolean isCellEditable (EventObject e)
    {
        return true;
    }

    //--------------------------//
    // removeCellEditorListener //
    //--------------------------//
    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    @Override
    public void removeCellEditorListener (CellEditorListener l)
    {
        listenerList.remove(CellEditorListener.class, l);
    }

    //------------------//
    // shouldSelectCell //
    //------------------//
    /**
     * DOCUMENT ME!
     *
     * @param anEvent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public boolean shouldSelectCell (EventObject anEvent)
    {
        return false;
    }

    //-----------------//
    // stopCellEditing //
    //-----------------//
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public boolean stopCellEditing ()
    {
        return true;
    }

    /*
     * Notify all listeners that have registered interest for notification on
     * this event type.
     * @see EventListenerList
     */
    protected void fireEditingCanceled ()
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying those that are
        // interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                ((CellEditorListener) listeners[i + 1]).editingCanceled(
                        new ChangeEvent(this));
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.
     * @see EventListenerList
     */
    protected void fireEditingStopped ()
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                ((CellEditorListener) listeners[i + 1]).editingStopped(
                        new ChangeEvent(this));
            }
        }
    }
}
