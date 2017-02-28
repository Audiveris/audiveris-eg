//----------------------------------------------------------------------------//
//                                                                            //
//                         P a g e R e d u c t i o n                          //
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

import org.audiveris.omr.score.PartConnection.Candidate;
import org.audiveris.omr.score.PartConnection.Result;
import org.audiveris.omr.score.entity.Page;
import org.audiveris.omr.score.entity.ScorePart;
import org.audiveris.omr.score.entity.SystemPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class {@code PageReduction} reduces the parts of each system to a list of
 * parts defined at page level.
 *
 * @author Hervé Bitteur
 */
public class PageReduction
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(PageReduction.class);

    //~ Instance fields --------------------------------------------------------
    /** Related page */
    private final Page page;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new PageReduction object.
     *
     * @param page the page to process
     */
    public PageReduction (Page page)
    {
        this.page = page;
    }

    //~ Methods ----------------------------------------------------------------
    //--------//
    // reduce //
    //--------//
    /**
     * Process a page by merging information from the page systems
     */
    public void reduce ()
    {
        if (page.getSystems().isEmpty()) {
            return;
        }

        /* Connect parts across the systems */
        PartConnection connection = PartConnection.connectPageSystems(page);

        // Build part list
        List<ScorePart> scoreParts = new ArrayList<>();

        for (Result result : connection.getResultMap().keySet()) {
            scoreParts.add((ScorePart) result.getUnderlyingObject());
        }

        page.setPartList(scoreParts);

        // Make the connections: (system) SystemPart -> (page) ScorePart
        Map<Candidate, Result> candidateMap = connection.getCandidateMap();
        logger.debug("Candidates:{}", candidateMap.size());

        for (Map.Entry<Candidate, Result> entry : candidateMap.entrySet()) {
            Candidate candidate = entry.getKey();
            SystemPart systemPart = (SystemPart) candidate.getUnderlyingObject();

            Result result = entry.getValue();
            ScorePart scorePart = (ScorePart) result.getUnderlyingObject();

            // Connect (system) part -> (page) part
            systemPart.setScorePart(scorePart);

            // Use same ID
            systemPart.setId(scorePart.getId());
        }
    }
}
