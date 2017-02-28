//----------------------------------------------------------------------------//
//                                                                            //
//                             A u d i v e r i s                              //
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
import org.audiveris.omr.WellKnowns;

/**
 * Class {@code Audiveris} is simply the entry point to OMR, which
 * delegates the call to {@link org.audiveris.omr.Main#doMain}.
 *
 * @author Hervé Bitteur
 */
public final class Audiveris
{
    //~ Constructors -----------------------------------------------------------

    //-----------//
    // Audiveris //
    //-----------//
    /** To avoid instantiation */
    private Audiveris ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //------//
    // main //
    //------//
    /**
     * The main entry point, which just calls {@link org.audiveris.omr.Main#doMain}.
     *
     * @param args These args are simply passed to Main
     */
    public static void main (final String[] args)
    {
        // We need class WellKnowns to be elaborated before class Main
        WellKnowns.ensureLoaded();

        // Then we call Main...
        org.audiveris.omr.Main.doMain(args);
    }
}
