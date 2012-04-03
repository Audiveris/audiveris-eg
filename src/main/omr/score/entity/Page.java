//----------------------------------------------------------------------------//
//                                                                            //
//                                  P a g e                                   //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright (C) Hervé Bitteur 2000-2011. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.score.entity;

import omr.constant.ConstantSet;

import omr.log.Logger;

import omr.score.Score;
import omr.score.common.PixelDimension;
import omr.score.common.PixelPoint;
import omr.score.visitor.ScoreVisitor;

import omr.sheet.Scale;
import omr.sheet.Sheet;

import omr.step.StepException;

import omr.util.TreeNode;

import java.awt.image.RenderedImage;
import java.util.List;

/**
 * Class {@code Page} represents a page in the score hierarchy,
 * and corresponds to a {@link Sheet} with its specific scale, skew,
 * dimension, etc.
 * Page instances compose a {@link Score}.
 *
 * @author Hervé Bitteur
 */
public class Page
    extends PageNode
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(Page.class);

    //~ Instance fields --------------------------------------------------------

    /** Index of page */
    private final int index;

    /** Page ID */
    private final String id;

    /** Link with image */
    private Sheet sheet;

    /** Page global scale */
    private Scale scale;

    /** ScorePart list for the page */
    private List<ScorePart> partList;

    /** Number of measures in this page */
    private Integer measureCount;

    /** Progression of measure id within this page */
    private Integer deltaMeasureId;

    //~ Constructors -----------------------------------------------------------

    //------//
    // Page //
    //------//
    /**
     * Creates a new Page object.
     * @param score the containing score
     */
    public Page (Score         score,
                 int           index,
                 RenderedImage image)
        throws StepException
    {
        super(score);
        this.index = index;

        if (score.isMultiPage()) {
            id = score.getRadix() + "#" + index;
        } else {
            id = score.getRadix();
        }

        sheet = new Sheet(this, image);
    }

    //~ Methods ----------------------------------------------------------------

    //--------//
    // accept //
    //--------//
    @Override
    public boolean accept (ScoreVisitor visitor)
    {
        return visitor.visit(this);
    }

    //---------------------//
    // computeMeasureCount //
    //---------------------//
    /**
     * Compute the number of (vertical) measures in the page.
     */
    public void computeMeasureCount ()
    {
        int count = 0;

        for (TreeNode sn : getSystems()) {
            ScoreSystem system = (ScoreSystem) sn;
            count += system.getFirstPart()
                           .getMeasures()
                           .size();
        }

        measureCount = count;
    }

    //-------------------//
    // dumpMeasureCounts //
    //-------------------//
    /**
     * Log the detailed number of measures in the score.
     */
    public void dumpMeasureCounts ()
    {
        int           count = 0;
        StringBuilder sb = new StringBuilder();

        for (TreeNode node : getSystems()) {
            ScoreSystem sys = (ScoreSystem) node;
            SystemPart  part = sys.getLastPart();

            if (sb.length() > 0) {
                sb.append(", ");
            }

            sb.append(part.getMeasures().size())
              .append(" in S#")
              .append(sys.getId());
            count += part.getMeasures()
                         .size();
        }

        StringBuilder msg = new StringBuilder();
        msg.append(count);
        msg.append(" raw measure");

        if (count > 1) {
            msg.append('s');
        }

        msg.append(": [")
           .append(sb)
           .append("]");

        logger.info(sheet.getLogPrefix() + msg.toString());
    }

    //-------------------//
    // getDeltaMeasureId //
    //-------------------//
    /**
     * Report the progression of measure IDs within this page.
     * @return the deltaMeasureId
     */
    public Integer getDeltaMeasureId ()
    {
        return deltaMeasureId;
    }

    //--------------//
    // getDimension //
    //--------------//
    /**
     * Report the dimension of the sheet/page.
     * @return the page/sheet dimension in pixels
     */
    public PixelDimension getDimension ()
    {
        return sheet.getDimension();
    }

    //----------------//
    // getFirstSystem //
    //----------------//
    /**
     * Report the first system in the page.
     * @return the first system
     */
    public ScoreSystem getFirstSystem ()
    {
        if (children.isEmpty()) {
            return null;
        } else {
            return (ScoreSystem) children.get(0);
        }
    }

    //-------//
    // getId //
    //-------//
    /**
     * @return the id
     */
    public String getId ()
    {
        return id;
    }

    //----------//
    // getIndex //
    //----------//
    /**
     * @return the page index
     */
    public int getIndex ()
    {
        return index;
    }

    //---------------//
    // getLastSystem //
    //---------------//
    /**
     * Report the last system in the page.
     * @return the last system
     */
    public ScoreSystem getLastSystem ()
    {
        if (children.isEmpty()) {
            return null;
        } else {
            return (ScoreSystem) children.get(children.size() - 1);
        }
    }

    //--------------------//
    // getMeanStaffHeight //
    //--------------------//
    /**
     * Report the mean staff height based on page interline.
     * This should be refined per system, if not per staff
     * @return the page-based average value of staff heights
     */
    public int getMeanStaffHeight ()
    {
        return (Score.LINE_NB - 1) * scale.getInterline();
    }

    //-----------------//
    // getMeasureCount //
    //-----------------//
    /**
     * Report the number of (vertical) measures in this page.
     * @return the number of page measures
     */
    public int getMeasureCount ()
    {
        return measureCount;
    }

    //-------------------//
    // getMinSlotSpacing //
    //-------------------//
    /**
     * Report the minimum acceptable spacing between slots.
     * @return the minimum spacing (in interline fraction)
     */
    public static Scale.Fraction getMinSlotSpacing ()
    {
        return constants.minSlotSpacing;
    }

    //-------------//
    // getPartList //
    //-------------//
    /**
     * Report the global list of parts.
     * @return partList the list of parts
     */
    public List<ScorePart> getPartList ()
    {
        return partList;
    }

    //---------------------//
    // getPrecedingInScore //
    //---------------------//
    /**
     * Report the preceding page of this one within the score.
     * @return the preceding page, or null if none
     */
    public Page getPrecedingInScore ()
    {
        return (Page) getPreviousSibling();
    }

    //----------//
    // getScale //
    //----------//
    /**
     * Report the scale of the page.
     * @return the page scale (basically: number of pixels for main interline)
     */
    @Override
    public Scale getScale ()
    {
        return scale;
    }

    //----------//
    // getSheet //
    //----------//
    /**
     * Report the related sheet entity.
     * @return the related sheet, or null if none
     */
    public Sheet getSheet ()
    {
        return sheet;
    }

    //---------------//
    // getSystemById //
    //---------------//
    /**
     * Report the system for which id is provided.
     * @param id id of desired system
     * @return the desired system
     */
    public ScoreSystem getSystemById (int id)
    {
        return (ScoreSystem) getSystems()
                                 .get(id - 1);
    }

    //------------//
    // getSystems //
    //------------//
    /**
     * Report the collection of systems in that score.
     * @return the systems
     */
    public List<TreeNode> getSystems ()
    {
        return getChildren();
    }

    //--------------//
    // resetSystems //
    //--------------//
    /**
     * Reset the systems collection of a score entity.
     */
    public void resetSystems ()
    {
        // Discard systems
        getSystems()
            .clear();

        // Discard partlists
        if (partList != null) {
            partList.clear();
        }
    }

    //-------------------//
    // setDeltaMeasureId //
    //-------------------//
    /**
     * Assign the progression of measure IDs within this page.
     * @param deltaMeasureId the deltaMeasureId to set
     */
    public void setDeltaMeasureId (Integer deltaMeasureId)
    {
        this.deltaMeasureId = deltaMeasureId;
    }

    //-------------//
    // setPartList //
    //-------------//
    /**
     * Assign a part list valid for the page.
     * @param partList the list of parts
     */
    public void setPartList (List<ScorePart> partList)
    {
        this.partList = partList;
    }

    //----------//
    // setScale //
    //----------//
    /**
     * Assign proper scale for this page.
     * @param scale the general scale for the page
     */
    public void setScale (Scale scale)
    {
        this.scale = scale;
    }

    //----------//
    // setSheet //
    //----------//

    /**
     * Register the name of the corresponding sheet entity.
     * @param sheet the related sheet entity
     */
    public void setSheet (Sheet sheet)
    {
        this.sheet = sheet;
    }

    //----------//
    // systemAt //
    //----------//
    /**
     * Retrieve which system contains the provided point.
     * @param point the point in the <b>SHEET</b> display
     * @return the nearest system.
     */
    public ScoreSystem systemAt (PixelPoint point)
    {
        return getSheet()
                   .getSystemOf(point)
                   .getScoreSystem();
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        return "{Page " + id + "}";
    }

    //~ Inner Classes ----------------------------------------------------------

    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
        extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        /** Minimum spacing between slots before alerting user */
        private final Scale.Fraction minSlotSpacing = new Scale.Fraction(
            1.1d,
            "Minimum spacing between slots before alerting user");
    }
}
