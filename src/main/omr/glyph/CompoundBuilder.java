//----------------------------------------------------------------------------//
//                                                                            //
//                       C o m p o u n d B u i l d e r                        //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright (C) Hervé Bitteur 2000-2011. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.glyph;

import omr.glyph.facets.Glyph;

import omr.log.Logger;

import omr.score.common.PixelRectangle;

import omr.sheet.SystemInfo;

import omr.util.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class {@code CompoundBuilder} defines a generic way to smartly
 * build glyph compounds, and provides derived variants.
 *
 * @author Hervé Bitteur
 */
public class CompoundBuilder
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(
        CompoundBuilder.class);

    //~ Instance fields --------------------------------------------------------

    /** Dedicated system */
    protected final SystemInfo system;

    //~ Constructors -----------------------------------------------------------

    //-----------------//
    // CompoundBuilder //
    //-----------------//
    /**
     * Creates a new CompoundBuilder object.
     * @param system the containing system
     */
    public CompoundBuilder (SystemInfo system)
    {
        this.system = system;
    }

    //~ Methods ----------------------------------------------------------------

    //---------------//
    // buildCompound //
    //---------------//
    /**
     * Try to build a compound, starting from given seed and looking
     * into the collection of suitable glyphs.
     *
     * <p>If successful, this method assigns the proper shape to the compound,
     * and inserts it in the system environment.
     *
     * @param seed the initial glyph around which the compound is built
     * @param includeSeed true if seed must be included in compound
     * @param suitables collection of potential glyphs
     * @param adapter the specific behavior of the compound tests
     * @return the compound built if successful, null otherwise
     */
    public Glyph buildCompound (Glyph             seed,
                                boolean           includeSeed,
                                Collection<Glyph> suitables,
                                CompoundAdapter   adapter)
    {
        // Set seed (and reference box)
        adapter.setSeed(seed);

        // Retrieve good neighbors among the suitable glyphs
        Set<Glyph> neighbors = new HashSet<Glyph>();

        // Include the seed in the compound glyphs?
        int minCount = 1;

        if (includeSeed) {
            neighbors.add(seed);
            minCount++;
        }

        for (Glyph g : suitables) {
            if (includeSeed || (g != seed)) {
                if (adapter.isCandidateSuitable(g) &&
                    adapter.isCandidateClose(g)) {
                    neighbors.add(g);
                }
            }
        }

        if (neighbors.size() >= minCount) {
            if (logger.isFineEnabled()) {
                logger.finest(
                    "neighbors=" + Glyphs.toString(neighbors) + " seed=" +
                    seed);
            }

            Glyph compound = system.buildTransientCompound(neighbors);

            if (adapter.isCompoundValid(compound)) {
                // Assign and insert into system & nest environments
                compound = system.addGlyph(compound);
                compound.setEvaluation(adapter.getChosenEvaluation());

                if (logger.isFineEnabled()) {
                    logger.fine(
                        "Compound #" + compound.getId() + " built as " +
                        compound.getShape());
                }

                return compound;
            }
        }

        return null;
    }

    //---------------//
    // buildCompound //
    //---------------//
    /**
     * A basic building, which simply takes all the provided glyphs
     * and build a persistent compound out of them.
     * @param parts the glyphs to merge
     * @return the compound built
     */
    public Glyph buildCompound (Collection<Glyph> parts)
    {
        if (parts.isEmpty()) {
            return null;
        }

        List<Glyph> list = new ArrayList<Glyph>(parts);

        return buildCompound(
            list.get(0),
            true,
            list.subList(1, list.size()),
            new NoAdapter(system));
    }

    //~ Inner Interfaces -------------------------------------------------------

    //-----------------//
    // CompoundAdapter //
    //-----------------//
    /**
     * Interface {@code CompoundAdapter} provides the needed features
     * for building compounds out of glyphs.
     */
    public static interface CompoundAdapter
    {
        //~ Methods ------------------------------------------------------------

        /**
         * Report the evaluation chosen for the compound.
         * @return the evaluation (shape + grade) chosen
         */
        Evaluation getChosenEvaluation ();

        /**
         * Predicate to check whether a given candidate glyph is close
         * enough to the reference box.
         * @param glyph the glyph to check for proximity
         * @return true if glyph is close enough
         */
        boolean isCandidateClose (Glyph glyph);

        /**
         * Predicate for a glyph to be a potential part of the building
         * (the location criteria is handled by {@link #isCandidateClose}).
         * @param glyph the glyph to check
         * @return true if the glyph is suitable for inclusion
         */
        boolean isCandidateSuitable (Glyph glyph);

        /**
         * Predicate to check the validity of the newly built compound.
         * If valid, the chosenEvaluation is assigned accordingly.
         * @param compound the resulting compound glyph to check
         * @return true if the compound is found OK. The compound shape is not
         * assigned by this method, but can be later retrieved through
         * getChosenEvaluation() method.
         */
        boolean isCompoundValid (Glyph compound);

        /**
         * Define the seed glyph around which the compound will be built.
         * @param seed the seed glyph
         * @return the computed reference box
         */
        PixelRectangle setSeed (Glyph seed);

        /**
         * Should we filter the provided candidates? (by calling
         * {@link #isCandidateSuitable} and {@link #isCandidateClose}).
         * @return true to apply filter
         */
        boolean shouldFilterCandidates ();
    }

    //~ Inner Classes ----------------------------------------------------------

    //-----------------//
    // AbstractAdapter //
    //-----------------//
    /**
     * Basic abstract class to implement the {@link CompoundAdapter}
     * interface.
     */
    public abstract static class AbstractAdapter
        implements CompoundAdapter
    {
        //~ Instance fields ----------------------------------------------------

        /** Dedicated system */
        protected final SystemInfo system;

        /** Maximum grade for a compound */
        protected final double minGrade;

        /** Originating seed */
        protected Glyph seed;

        /** Search box */
        protected PixelRectangle box;

        /** The result of compound evaluation */
        protected Evaluation chosenEvaluation;

        //~ Constructors -------------------------------------------------------

        /**
         * Construct an AbstractAdapter.
         * @param system the containing system
         * @param minGrade maximum acceptable grade for the compound shape
         */
        public AbstractAdapter (SystemInfo system,
                                double     minGrade)
        {
            this.system = system;
            this.minGrade = minGrade;
        }

        //~ Methods ------------------------------------------------------------

        public Evaluation getChosenEvaluation ()
        {
            // By default, use shape and grade from evaluator
            return chosenEvaluation;
        }

        public boolean isCandidateClose (Glyph glyph)
        {
            // By default, use box intersection
            return box.intersects(glyph.getContourBox());
        }

        public PixelRectangle setSeed (Glyph seed)
        {
            this.seed = seed;
            box = computeReferenceBox();

            return box;
        }

        public boolean shouldFilterCandidates ()
        {
            // By default, filter candidates
            return true;
        }

        /**
         * Compute the reference box.
         * This method is called when seed has just been set.
         */
        protected abstract PixelRectangle computeReferenceBox ();
    }

    //-----------//
    // NoAdapter //
    //-----------//
    /**
     * A passthrough fake adapter.
     */
    public static class NoAdapter
        extends AbstractAdapter
    {
        //~ Constructors -------------------------------------------------------

        public NoAdapter (SystemInfo system)
        {
            super(system, 0);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Evaluation getChosenEvaluation ()
        {
            return new Evaluation(null, Evaluation.ALGORITHM);
        }

        @Override
        public boolean isCandidateClose (Glyph glyph)
        {
            return true;
        }

        @Override
        public boolean isCandidateSuitable (Glyph glyph)
        {
            return true;
        }

        @Override
        public boolean isCompoundValid (Glyph compound)
        {
            return true;
        }

        @Override
        public boolean shouldFilterCandidates ()
        {
            return false;
        }

        @Override
        protected PixelRectangle computeReferenceBox ()
        {
            return null;
        }
    }

    //---------------//
    // TopRawAdapter //
    //---------------//
    /**
     * This compound adapter tries to find some specific shapes among
     * the top raw evaluations found for the compound.
     */
    public abstract static class TopRawAdapter
        extends AbstractAdapter
    {
        //~ Instance fields ----------------------------------------------------

        /** Collection of desired shapes for a valid compound */
        protected final EnumSet<Shape> desiredShapes;

        /** Specific predicate for desired shapes */
        protected final Predicate<Shape> predicate = new Predicate<Shape>() {
            public boolean check (Shape shape)
            {
                return desiredShapes.contains(shape);
            }
        };


        //~ Constructors -------------------------------------------------------

        /**
         * Create a TopRawAdapter instance.
         * @param system the containing system
         * @param minGrade maximum acceptable grade on compound shape
         * @param desiredShapes the valid shapes for the compound
         */
        public TopRawAdapter (SystemInfo     system,
                              double         minGrade,
                              EnumSet<Shape> desiredShapes)
        {
            super(system, minGrade);
            this.desiredShapes = desiredShapes;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean isCompoundValid (Glyph compound)
        {
            // Check if a desired shape appears in the top raw evaluations
            final Evaluation vote = GlyphNetwork.getInstance()
                                                .topRawVote(
                compound,
                minGrade,
                predicate);

            if (vote != null) {
                chosenEvaluation = vote;

                return true;
            } else {
                return false;
            }
        }
    }

    //-----------------//
    // TopShapeAdapter //
    //-----------------//
    /**
     * This compound adapter tries to find some specific shapes among
     * the top evaluations found for the compound.
     */
    public abstract static class TopShapeAdapter
        extends TopRawAdapter
    {
        //~ Constructors -------------------------------------------------------

        /**
         * Create a TopShapeAdapter instance.
         * @param system the containing system
         * @param minGrade maximum acceptable grade on compound shape
         * @param desiredShapes the valid shapes for the compound
         */
        public TopShapeAdapter (SystemInfo     system,
                                double         minGrade,
                                EnumSet<Shape> desiredShapes)
        {
            super(system, minGrade, desiredShapes);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean isCompoundValid (Glyph compound)
        {
            // Check if a desired shape appears in the top evaluations
            final Evaluation vote = GlyphNetwork.getInstance()
                                                .topVote(
                compound,
                minGrade,
                system,
                predicate);

            if (vote != null) {
                chosenEvaluation = vote;

                return true;
            } else {
                return false;
            }
        }
    }
}
