//----------------------------------------------------------------------------//
//                                                                            //
//                             S t o p W a t c h                              //
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
package org.audiveris.omr.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code StopWatch}
 *
 * @author Hervé Bitteur
 */
public class StopWatch
{
    //~ Instance fields --------------------------------------------------------

    /** Name for this watch instance */
    private final String name;

    /** Sequence of all tasks so far */
    private final List<Task> tasks = new ArrayList<>(128);

    /** Current task (null if not running) */
    private Task task;

    /** Current sum of tasks times */
    private long total;

    //~ Constructors -----------------------------------------------------------
    //-----------//
    // StopWatch //
    //-----------//
    /**
     * Creates a new StopWatch object.
     *
     * @param name name of the watch instance
     */
    public StopWatch (String name)
    {
        this.name = name;
    }

    //~ Methods ----------------------------------------------------------------
    //-------//
    // print //
    //-------//
    public void print ()
    {
        print(System.out);
    }

    //-------//
    // print //
    //-------//
    public void print (PrintStream out)
    {
        stop();

        final String format = "%5d %3d%% %s";
        final String dashes = "-----------------------------------";
        out.println(getClass().getSimpleName() + " \"" + name + "\"");
        out.println(dashes);
        out.println("   ms    % Task");
        out.println(dashes);

        for (Task t : tasks) {
            if (t != task) {
                out.println(
                        String.format(
                        format,
                        t.elapsed,
                        (total != 0) ? ((100 * t.elapsed) / total) : 100,
                        t.label));
            }
        }

        out.println(dashes);
        out.println(String.format(format, total, 100, "Total"));
    }

    //-------//
    // start //
    //-------//
    public void start (String label)
    {
        if (task != null) {
            stop();
        }

        tasks.add(task = new Task(label));
    }

    //------//
    // stop //
    //------//
    public void stop ()
    {
        if (task != null) {
            task.elapsed = System.currentTimeMillis() - task.start;
            total += task.elapsed;
            task = null;
        }
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(" \"")
                .append(name)
                .append("\"");

        sb.append(" tasks:")
                .append(tasks.size());

        sb.append(" total:")
                .append(total);

        if (task != null) {
            sb.append(" running");
        } else {
            sb.append(" stopped");
        }

        return sb.toString();
    }

    //~ Inner Classes ----------------------------------------------------------
    //------//
    // Task //
    //------//
    private static class Task
    {
        //~ Instance fields ----------------------------------------------------

        /** Label for this task */
        private final String label;

        /** Start time */
        private final long start;

        /** Elapsed time */
        private long elapsed;

        //~ Constructors -------------------------------------------------------
        public Task (String label)
        {
            this.label = label;
            start = System.currentTimeMillis();
        }
    }
}
