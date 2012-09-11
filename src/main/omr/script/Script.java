//----------------------------------------------------------------------------//
//                                                                            //
//                                S c r i p t                                 //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur 2000-2012. All rights reserved.                 //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.script;

import omr.log.Logger;

import omr.score.Score;
import omr.score.entity.Page;

import omr.sheet.Sheet;

import omr.step.ProcessingCancellationException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class {@code Script} handles a complete script applied to a score.
 *
 * <p>A script is a sequence of {@link ScriptTask} instances tasks that are
 * recorded as the user interacts with the score data.
 *
 * <p>A script can be stored and reloaded/replayed.
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "script")
public class Script
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(Script.class);

    //~ Instance fields --------------------------------------------------------
    /** Score to which the script is applied */
    private Score score;

    /** Full path to the Score image file */
    @XmlAttribute(name = "file")
    private final String scorePath;

    /** Sequence of tasks that compose the script */
    @XmlElements({
        @XmlElement(name = "assign",
                    type = AssignTask.class),
        @XmlElement(name = "barline",
                    type = BarlineTask.class),
        @XmlElement(name = "boundary",
                    type = BoundaryTask.class),
        @XmlElement(name = "delete",
                    type = DeleteTask.class),
        @XmlElement(name = "export",
                    type = ExportTask.class),
        @XmlElement(name = "insert",
                    type = InsertTask.class),
        @XmlElement(name = "parameters",
                    type = ParametersTask.class),
        @XmlElement(name = "print",
                    type = PrintTask.class),
        @XmlElement(name = "rational",
                    type = RationalTask.class),
        @XmlElement(name = "remove",
                    type = RemoveTask.class),
        @XmlElement(name = "segment",
                    type = SegmentTask.class),
        @XmlElement(name = "slur",
                    type = SlurTask.class),
        @XmlElement(name = "step",
                    type = StepTask.class),
        @XmlElement(name = "text",
                    type = TextTask.class)
    })
    private final List<ScriptTask> tasks = new ArrayList<>();

    /** Flag a script that needs to be stored */
    private boolean modified;

    //~ Constructors -----------------------------------------------------------
    //--------//
    // Script //
    //--------//
    /**
     * Create a script
     *
     * @param score the related score
     */
    public Script (Score score)
    {
        this.score = score;
        scorePath = score.getImagePath();
    }

    //--------//
    // Script //
    //--------//
    /** No-arg constructor for JAXB */
    private Script ()
    {
        scorePath = null;
    }

    //~ Methods ----------------------------------------------------------------
    //---------//
    // addTask //
    //---------//
    /**
     * Add a task to the script
     *
     * @param task the task to add at the end of the current sequence
     */
    public synchronized void addTask (ScriptTask task)
    {
        tasks.add(task);
        setModified(true);
        logger.fine("Script: added {0}", task);
    }

    //------//
    // dump //
    //------//
    /**
     * Meant for debug
     */
    public void dump ()
    {
        logger.info(toString());

        for (ScriptTask task : tasks) {
            logger.info(task.toString());
        }
    }

    //----------//
    // getScore //
    //----------//
    /**
     * Report the score this script is linked to
     *
     * @return the score concerned
     */
    public Score getScore ()
    {
        return score;
    }

    //------------//
    // isModified //
    //------------//
    /**
     * Has the script been modified (wrt its backup on disk)?
     *
     * @return the modified
     */
    public boolean isModified ()
    {
        return modified;
    }

    //-----//
    // run //
    //-----//
    /**
     * This methods runs sequentially and synchronously the various
     * tasks of the script. 
     * It is up to the caller to run this method in a separate thread if so 
     * desired.
     */
    public void run ()
    {
        logger.fine("Running {0}{1}",
                    new Object[]{this, (score != null) ? (" on score " + score.
                                                          getRadix()) : ""});

        // Make score concrete (with its pages/sheets)
        if (score == null) {
            if (scorePath == null) {
                logger.warning("No score defined in script");

                return;
            }

            score = new Score(new File(scorePath));
            score.createPages();
        }

        // Run the tasks in sequence
        try {
            for (ScriptTask task : tasks) {
                Page page = null;

                if (task instanceof SheetTask) {
                    Integer pageIndex = ((SheetTask) task).getPageIndex();
                    page = score.getPage(pageIndex);

                    if (page == null) {
                        logger.warning("Script error. No page for index {0}",
                                       pageIndex);

                        continue;
                    }
                } else {
                    page = score.getFirstPage();
                }

                Sheet sheet = page.getSheet();
                logger.fine("Running {0} on {1}", new Object[]{task, sheet});

                try {
                    // Run the task synchronously (prolog/core/epilog)
                    task.run(sheet);
                } catch (ProcessingCancellationException pce) {
                    throw pce;
                } catch (Exception ex) {
                    logger.warning("Error running " + task, ex);
                    throw new RuntimeException(task.toString());
                }
            }

            logger.fine("All tasks run on {0}", score);
        } catch (ProcessingCancellationException pce) {
            throw pce;
        } catch (Exception ex) {
            logger.warning("Script aborted", ex);
        } finally {
            // Flag the (active) script as up-to-date
            score.getScript().setModified(false);
        }
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{Script");

        if (modified) {
            sb.append(" modified");
        }

        if (scorePath != null) {
            sb.append(" ").append(scorePath);
        } else if (score != null) {
            sb.append(" ").append(score.getRadix());
        }

        if (tasks != null) {
            sb.append(" tasks:").append(tasks.size());
        }

        sb.append("}");

        return sb.toString();
    }

    //-------------//
    // setModified //
    //-------------//
    /**
     * Flag the script as modified (wrt disk)
     *
     * @param modified the modified to set
     */
    void setModified (boolean modified)
    {
        this.modified = modified;
    }
}
