//----------------------------------------------------------------------------//
//                                                                            //
//                          S c o r e V i s i t o r                           //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur and others 2000-2013. All rights reserved.      //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.score.visitor;

import org.audiveris.omr.score.Score;
import org.audiveris.omr.score.entity.Arpeggiate;
import org.audiveris.omr.score.entity.Articulation;
import org.audiveris.omr.score.entity.Barline;
import org.audiveris.omr.score.entity.Beam;
import org.audiveris.omr.score.entity.Chord;
import org.audiveris.omr.score.entity.ChordSymbol;
import org.audiveris.omr.score.entity.Clef;
import org.audiveris.omr.score.entity.Coda;
import org.audiveris.omr.score.entity.DirectionStatement;
import org.audiveris.omr.score.entity.Dynamics;
import org.audiveris.omr.score.entity.Fermata;
import org.audiveris.omr.score.entity.KeySignature;
import org.audiveris.omr.score.entity.Measure;
import org.audiveris.omr.score.entity.MeasureElement;
import org.audiveris.omr.score.entity.MeasureNode;
import org.audiveris.omr.score.entity.Note;
import org.audiveris.omr.score.entity.Ornament;
import org.audiveris.omr.score.entity.Page;
import org.audiveris.omr.score.entity.PartNode;
import org.audiveris.omr.score.entity.Pedal;
import org.audiveris.omr.score.entity.ScoreNode;
import org.audiveris.omr.score.entity.ScoreSystem;
import org.audiveris.omr.score.entity.Segno;
import org.audiveris.omr.score.entity.Slur;
import org.audiveris.omr.score.entity.Staff;
import org.audiveris.omr.score.entity.SystemPart;
import org.audiveris.omr.score.entity.Text;
import org.audiveris.omr.score.entity.TimeSignature;
import org.audiveris.omr.score.entity.Tuplet;
import org.audiveris.omr.score.entity.VisitableNode;
import org.audiveris.omr.score.entity.Wedge;

/**
 * Interface {@code ScoreVisitor} is meant to visit any node of the
 * Score hierarchy.
 *
 * <p>The hierarchy is meant to be browsed "depth-first".</p>
 * <p>
 * All the polymorphic visit(node) methods return a boolean which
 * tells whether the visit shall continue to the children of this class.
 * <ul>
 * <li>It is true by default (the whole visitable hierarchy is meant to be
 * visited).</li>
 * <li>Returning false avoids the automatic visit of the children of the class
 * for the specific visitor, it is then up to the caller to potentially handle
 * the children by another way.</li>
 * </ul>
 *
 * @author Hervé Bitteur
 */
public interface ScoreVisitor
{
    //~ Methods ----------------------------------------------------------------

    boolean visit (Articulation node);

    boolean visit (Arpeggiate node);

    boolean visit (Barline node);

    boolean visit (Beam node);

    boolean visit (Chord node);

    boolean visit (ChordSymbol node);

    boolean visit (Clef node);

    boolean visit (Coda node);

    boolean visit (DirectionStatement node);

    boolean visit (Dynamics node);

    boolean visit (Fermata node);

    boolean visit (KeySignature node);

    boolean visit (Measure node);

    boolean visit (MeasureElement node);

    boolean visit (MeasureNode node);

    boolean visit (Note node);

    boolean visit (Ornament node);

    boolean visit (Page node);

    boolean visit (PartNode node);

    boolean visit (Pedal node);

    boolean visit (Score node);

    boolean visit (ScoreNode node);

    boolean visit (Segno node);

    boolean visit (Slur node);

    boolean visit (Staff node);

    boolean visit (ScoreSystem node);

    boolean visit (SystemPart node);

    boolean visit (Text node);

    boolean visit (TimeSignature node);

    boolean visit (Tuplet node);

    boolean visit (VisitableNode node);

    boolean visit (Wedge node);
}
