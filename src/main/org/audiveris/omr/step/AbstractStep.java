//----------------------------------------------------------------------------//
//                                                                            //
//                          A b s t r a c t S t e p                           //
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

import org.audiveris.omr.Main;

import org.audiveris.omr.sheet.Sheet;
import org.audiveris.omr.sheet.SystemInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Abstract class {@code AbstractStep} provides a convenient basis for
 * {@link Step} implementation.
 *
 * @author Hervé Bitteur
 */
public abstract class AbstractStep
        implements Step
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            AbstractStep.class);

    //~ Instance fields --------------------------------------------------------
    /** Step name */
    protected final String name;

    /** Score level only, or sheet level possible? */
    protected final Level level;

    /** Is the step mandatory? */
    protected final Mandatory mandatory;

    /** Related short label */
    protected final String label;

    /** Description of the step */
    protected final String description;

    //~ Constructors -----------------------------------------------------------
    //
    /**
     * Creates a new AbstractStep object.
     *
     * @param name        the step name
     * @param level       score level only or sheet level
     * @param mandatory   step must be done before any output
     * @param label       The title of the related (or most relevant) view tab
     * @param description A step description for the end user
     */
    public AbstractStep (String name,
                         Level level,
                         Mandatory mandatory,
                         String label,
                         String description)
    {
        this.name = name;
        this.level = level;
        this.mandatory = mandatory;
        this.label = label;
        this.description = description;
    }

    //~ Methods ----------------------------------------------------------------
    //-------------//
    // clearErrors //
    //-------------//
    @Override
    public void clearErrors (Sheet sheet)
    {
        if (Main.getGui() != null) {
            sheet.getErrorsEditor()
                    .clearStep(this);
        }
    }

    //
    //-----------//
    // displayUI //
    //-----------//
    @Override
    public void displayUI (Sheet sheet)
    {
        // Void by default
    }

    //--------//
    // doStep //
    //--------//
    @Override
    public void doStep (Collection<SystemInfo> systems,
                        Sheet sheet)
            throws StepException
    {
        try {
            logger.debug("{}Starting {}", sheet.getLogPrefix(), this);
            started(sheet);
            Stepping.notifyStep(sheet, this); // Start

            clearErrors(sheet);

            doit(systems, sheet);

            done(sheet); // Full completion
            logger.debug("{}Finished {}", sheet.getLogPrefix(), this);
        } finally {
            // Make sure we reset the sheet "current" step, always.
            if (sheet != null) {
                sheet.setCurrentStep(null);
                Stepping.notifyStep(sheet, this); // Stop
            }
        }
    }

    //------//
    // done //
    //------//
    @Override
    public void done (Sheet sheet)
    {
        sheet.done(this);
    }

    //----------------//
    // getDescription //
    //----------------//
    @Override
    public String getDescription ()
    {
        return description;
    }

    //---------//
    // getName //
    //---------//
    @Override
    public String getName ()
    {
        return name;
    }

    //--------//
    // getTab //
    //--------//
    @Override
    public String getTab ()
    {
        return label;
    }

    //--------//
    // isDone //
    //--------//
    @Override
    public boolean isDone (Sheet sheet)
    {
        return sheet.isDone(this);
    }

    //-------------//
    // isMandatory //
    //-------------//
    @Override
    public boolean isMandatory ()
    {
        return mandatory == Step.Mandatory.MANDATORY;
    }

    //--------------//
    // isScoreLevel //
    //--------------//
    @Override
    public boolean isScoreLevel ()
    {
        return level == Step.Level.SCORE_LEVEL;
    }

    //---------//
    // started //
    //---------//
    /**
     * Flag this step as started
     */
    public void started (Sheet sheet)
    {
        sheet.setCurrentStep(this);
    }

    //--------------//
    // toLongString //
    //--------------//
    @Override
    public String toLongString ()
    {
        StringBuilder sb = new StringBuilder("{Step");
        sb.append(" ")
                .append(name);
        sb.append(" ")
                .append(level);
        sb.append(" ")
                .append(mandatory);
        sb.append(" label:")
                .append(label);
        sb.append("}");

        return sb.toString();
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        return name;
    }

    //------//
    // doit //
    //------//
    /**
     * Actually perform the step.
     * This method must be defined for any concrete Step.
     *
     * @param systems the collection of systems to process, or null
     * @param sheet   the related sheet
     * @throws StepException raised if processing failed
     */
    protected abstract void doit (Collection<SystemInfo> systems,
                                  Sheet sheet)
            throws StepException;
}
