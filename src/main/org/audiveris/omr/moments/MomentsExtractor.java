//----------------------------------------------------------------------------//
//                                                                            //
//                      M o m e n t s E x t r a c t o r                       //
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
package org.audiveris.omr.moments;

import java.awt.image.WritableRaster;

/**
 * Interface {@code MomentsExtractor} is a general definition for an
 * extractor of {@link OrthogonalMoments}.
 *
 * @param <D> the descriptor type
 *
 * @author Hervé Bitteur
 */
public interface MomentsExtractor<D extends OrthogonalMoments<D>>
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Extract information from provided foreground points and save
     * the results in the target descriptor.
     *
     * @param xx   the array of abscissa values
     * @param yy   the array of ordinate values
     * @param mass the number of points
     */
    void extract (int[] xx,
                  int[] yy,
                  int mass);

    /**
     * Reconstruct an image from the shape features.
     *
     * @param raster the (square) raster to populate
     */
    void reconstruct (WritableRaster raster);

    /**
     * Assign a target descriptor of this extractor, to receive
     * extraction results.
     *
     * @param descriptor the target descriptor
     */
    void setDescriptor (D descriptor);
}
