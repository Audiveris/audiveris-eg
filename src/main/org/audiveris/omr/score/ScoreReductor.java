//----------------------------------------------------------------------------//
//                                                                            //
//                         S c o r e R e d u c t o r                          //
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

import org.audiveris.omr.math.Rational;

import org.audiveris.omr.score.entity.Chord;
import org.audiveris.omr.score.entity.TimeSignature.InvalidTimeSignature;
import org.audiveris.omr.score.visitor.AbstractScoreVisitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class {@code ScoreReductor} can visit the score hierarchy to simplify
 * all duration values.
 *
 * @author Hervé Bitteur
 */
public class ScoreReductor
        extends AbstractScoreVisitor
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(ScoreReductor.class);

    //~ Instance fields --------------------------------------------------------
    /** Set of all different duration values */
    private final SortedSet<Rational> durations = new TreeSet<>();

    //~ Constructors -----------------------------------------------------------
    //---------------//
    // ScoreReductor //
    //---------------//
    /**
     * Creates a new ScoreReductor object.
     */
    public ScoreReductor ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //-------------//
    // visit Chord //
    //-------------//
    @Override
    public boolean visit (Chord chord)
    {
        Rational duration;

        try {
            // Special case for whole chords
            if (chord.isWholeDuration()) {
                duration = chord.getMeasure().getExpectedDuration();
            } else {
                duration = chord.getDuration();
            }

            if (duration != null) {
                durations.add(duration);
            }
        } catch (InvalidTimeSignature ex) {
            // Ignored here (TBC)
        } catch (Exception ex) {
            logger.warn(
                    getClass().getSimpleName() + " Error visiting " + chord,
                    ex);
        }

        return false;
    }

    //-------------//
    // visit Score //
    //-------------//
    @Override
    public boolean visit (Score score)
    {
        try {
            // Collect duration values for each part
            score.acceptChildren(this);

            // Compute and remember greatest duration divisor for the score
            score.setDurationDivisor(computeDurationDivisor());
        } catch (Exception ex) {
            logger.warn(
                    getClass().getSimpleName() + " Error visiting " + score,
                    ex);
        }

        return false;
    }

    //------------------------//
    // computeDurationDivisor //
    //------------------------//
    private int computeDurationDivisor ()
    {
        Rational[] durationArray = durations.toArray(
                new Rational[durations.size()]);
        Rational divisor = Rational.gcd(durationArray);
        logger.debug("durations={} divisor={}",
                Arrays.deepToString(durationArray), divisor);

        return divisor.den;
    }
}
