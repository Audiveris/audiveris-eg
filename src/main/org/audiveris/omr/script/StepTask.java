//----------------------------------------------------------------------------//
//                                                                            //
//                              S t e p T a s k                               //
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
package org.audiveris.omr.script;

import org.audiveris.omr.sheet.Sheet;

import org.audiveris.omr.step.Step;
import org.audiveris.omr.step.StepException;
import org.audiveris.omr.step.Stepping;
import org.audiveris.omr.step.Steps;

import java.util.Collections;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Class {@code StepTask} performs a step on a whole score.
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
public class StepTask
        extends ScriptTask
{
    //~ Instance fields --------------------------------------------------------

    /** The step launched */
    private Step step;

    //~ Constructors -----------------------------------------------------------
    //----------//
    // StepTask //
    //----------//
    /**
     * Create a task to apply a given step to the related sheet.
     *
     * @param step the step to apply
     */
    public StepTask (Step step)
    {
        this.step = step;
    }

    //----------//
    // StepTask //
    //----------//
    /** No-arg constructor needed by JAXB */
    private StepTask ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //------//
    // core //
    //------//
    @Override
    public void core (final Sheet sheet)
            throws StepException
    {
        Stepping.processScore(
                Collections.singleton(step),
                null,
                sheet.getScore());
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        return " step " + step + super.internalsString();
    }

    //--------------//
    // isRecordable //
    //--------------//
    /**
     * This is an implementation trick, because of a "chicken and egg
     * problem" to allow to run the LOAD step while the sheet does not exist
     * yet!
     *
     * @return false!
     */
    @Override
    boolean isRecordable ()
    {
        return false;
    }

    //---------//
    // getStep // Meant for JAXB
    //---------//
    private String getStep ()
    {
        return step.getName();
    }

    //---------//
    // setStep // Meant for JAXB
    //---------//
    @XmlAttribute(name = "name")
    private void setStep (String name)
    {
        step = Steps.valueOf(name.toUpperCase());
    }
}
