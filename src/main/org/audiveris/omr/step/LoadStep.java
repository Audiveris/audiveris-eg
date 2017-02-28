//----------------------------------------------------------------------------//
//                                                                            //
//                              L o a d S t e p                               //
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
package org.audiveris.omr.step;

import org.audiveris.omr.score.Score;

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.SystemInfo;
import org.audiveris.omr.sheet.picture.PictureLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Collection;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class {@code LoadStep} reloads the image for a sheet,
 * from a provided image file.
 * <p>This is simply a RE-loading, triggered by the user. The initial loading
 * is always done in {@link Score#createPages(SortedSet)}.</p>
 *
 * @author Hervé Bitteur
 */
public class LoadStep
        extends AbstractStep
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(LoadStep.class);

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new LoadStep object.
     */
    public LoadStep ()
    {
        super(
                Steps.LOAD,
                Level.SHEET_LEVEL,
                Mandatory.MANDATORY,
                Step.PICTURE_TAB,
                "Reload the sheet picture");
    }

    //~ Methods ----------------------------------------------------------------
    //------//
    // doit //
    //------//
    @Override
    public void doit (Collection<SystemInfo> unused,
                      Sheet sheet)
            throws StepException
    {
        final Score score = sheet.getScore();
        final File imageFile = score.getImageFile();
        final int index = sheet.getPage()
                .getIndex();
        final SortedSet<Integer> set = new TreeSet<>();
        set.add(index);

        SortedMap<Integer, RenderedImage> images =
                PictureLoader.loadImages(imageFile, set);
        if (images != null) {
            sheet.setImage(images.get(index));
        }
    }
}
