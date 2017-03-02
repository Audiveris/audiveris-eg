//----------------------------------------------------------------------------//
//                                                                            //
//                             U s e r E v e n t                              //
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
package org.audiveris.omr.selection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface {@code UserEvent} defines the common behavior of user
 * events that are stored as selections, and handled by the EventBus.
 *
 * <p>All events are subclasses of this abstract class, and managed by proper
 * EventService instances: <ul>
 *
 * <li>SheetManager event service: a singleton which handles THE currently
 * selected sheet as {@link SheetEvent}
 *
 * <li>Sheet event service: each Sheet instance handles events of types
 * {@link LocationEvent} and {@link PixelLevelEvent}
 *
 * <li>Run event service: each RunsTable instance handles {@link RunEvent}
 *
 * <li>Lag event service: each Lag instance handles {@link SectionEvent},
 * {@link SectionIdEvent} and {@link SectionSetEvent} (all are subclasses of
 * {@link LagEvent})
 *
 * <li>Nest event service: each glyph Nest instance handles
 * {@link GlyphEvent}, {@link GlyphIdEvent} and {@link
 * GlyphSetEvent} (all are subclasses of {@link NestEvent})
 *
 * </ul>
 *
 * @author Hervé Bitteur
 */
public abstract class UserEvent
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            UserEvent.class);

    //~ Instance fields --------------------------------------------------------
    /** The entity which created this event */
    public final Object source;

    /** Hint about the event origin (can be null) */
    public final SelectionHint hint;

    /** Precise user mouse action (can be null) */
    public MouseMovement movement;

    //~ Constructors -----------------------------------------------------------
    //-----------//
    // UserEvent //
    //-----------//
    /**
     * Creates a new UserEvent object.
     *
     * @param source   the (non null) entity that created this event
     * @param hint     hint about the origin
     * @param movement the originating mouse movement
     */
    public UserEvent (Object source,
                      SelectionHint hint,
                      MouseMovement movement)
    {
        if (source == null) {
            throw new IllegalArgumentException("event source cannot be null");
        }

        this.source = source;
        this.hint = hint;
        this.movement = movement;

        //        logger.warn(
        //            ClassUtil.nameOf(this) + " created by:" + source);
    }

    //~ Methods ----------------------------------------------------------------
    //---------//
    // getData //
    //---------//
    /**
     * Report the data conveyed by this event.
     *
     * @return the conveyed data (which may be null)
     */
    public abstract Object getData ();

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder("{");
        sb.append(getClass().getSimpleName());
        sb.append(" src:")
                .append(source);

        if (hint != null) {
            sb.append(" ")
                    .append(hint);
        }

        if (movement != null) {
            sb.append(" ")
                    .append(movement);
        }

        sb.append(" ")
                .append(internalString());
        sb.append("}");

        return sb.toString();
    }

    //----------------//
    // internalString //
    //----------------//
    /**
     * Report a string about the internals of the specific subclass.
     *
     * @return the (sub)class internals as a string
     */
    protected String internalString ()
    {
        if (getData() != null) {
            return getData()
                    .toString();
        } else {
            return "";
        }
    }
}
