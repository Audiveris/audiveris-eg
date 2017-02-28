//----------------------------------------------------------------------------//
//                                                                            //
//                              N o t a t i o n                               //
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
package org.audiveris.omr.score.entity;

import org.audiveris.omr.score.visitor.Visitable;

/**
 * Interface {@code Notation} is used to flag a measure element used
 * as a (note related) notation.
 * This should apply to:
 * <pre>
 *  tied                specific
 *  slur                specific
 *  tuplet              nyi, to be done soon
 *  glissando           nyi
 *  slide               nyi
 *  ornaments           standard
 *  technical           nyi
 *  articulations       nyi
 *  dynamics            nyi
 *  fermata             nyi
 *  arpeggiate          standard
 *  non-arpeggiate      nyi
 *  accidental-mark     nyi
 *  other-notation      nyi
 * </pre>
 *
 * @author Hervé Bitteur
 */
public interface Notation
        extends Visitable
{
}
