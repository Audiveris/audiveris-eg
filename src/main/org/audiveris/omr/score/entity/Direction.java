//----------------------------------------------------------------------------//
//                                                                            //
//                             D i r e c t i o n                              //
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
 * Interface {@code Direction} is the basis for all variants of
 * direction indications.
 * This should apply to:
 * <pre>
 * rehearsal                    nyi
 * segno                        standard
 * words                        nyi
 * coda                         standard
 * wedge                        standard
 * dynamics                     standard (a dynamic can also be a notation)
 * dashes                       nyi
 * bracket                      nyi
 * pedal                        standard
 * metronome                    nyi
 * octave-shift                 nyi
 * harp-pedals                  nyi
 * damp                         nyi
 * damp-all                     nyi
 * eyeglasses                   nyi
 * scordatura                   nyi
 * image                        nyi
 * accordion-registration       nyi
 * other-direction              nyi
 * </pre>
 *
 * <p>For some directions (such as wedge, dashes, pedal), we may have two
 * "events": the starting event and the stopping event. Both will trigger the
 * creation of a Direction instance, the difference being made by the "start"
 * boolean.
 *
 * @author Hervé Bitteur
 */
public interface Direction
        extends Visitable
{
}
