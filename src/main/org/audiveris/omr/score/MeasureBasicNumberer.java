//----------------------------------------------------------------------------//
//                                                                            //
//                  M e a s u r e B a s i c N u m b e r e r                   //
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
package org.audiveris.omr.score;

import org.audiveris.omr.score.entity.Measure;
import org.audiveris.omr.score.entity.Page;
import org.audiveris.omr.score.visitor.AbstractScoreVisitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class {@code MeasureBasicNumberer} visits a page hierarchy to
 * assign very basic measures ids.
 * These Ids are very basic (and temporary), ranging from 1 for the first
 * measure in the page.
 *
 * @author Hervé Bitteur
 */
public class MeasureBasicNumberer
        extends AbstractScoreVisitor
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            MeasureBasicNumberer.class);

    //~ Constructors -----------------------------------------------------------
    //----------------------//
    // MeasureBasicNumberer //
    //----------------------//
    /**
     * Creates a new MeasureBasicNumberer object.
     */
    public MeasureBasicNumberer ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //---------------//
    // visit Measure //
    //---------------//
    @Override
    public boolean visit (Measure measure)
    {
        try {
            // Set measure id, based on a preceding measure, whatever the part
            Measure precedingMeasure = measure.getPrecedingInPage();

            if (precedingMeasure != null) {
                int precedingId = precedingMeasure.getIdValue();
                measure.setPageId(precedingId + 1, false);
            } else {
                // Very first measure (in this page)
                measure.setPageId(1, false);
            }
        } catch (Exception ex) {
            logger.warn(
                    getClass().getSimpleName() + " Error visiting " + measure,
                    ex);
        }

        return true;
    }

    //------------//
    // visit Page //
    //------------//
    @Override
    public boolean visit (Page page)
    {
        page.acceptChildren(this);

        // Temporary value
        page.setDeltaMeasureId(0);

        return false;
    }

    //-------------//
    // visit Score //
    //-------------//
    @Override
    public boolean visit (Score score)
    {
        score.acceptChildren(this);

        return false;
    }
}
