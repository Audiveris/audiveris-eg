//----------------------------------------------------------------------------//
//                                                                            //
//                   B a s i c A d m i n i s t r a t i o n                    //
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
package org.audiveris.omr.glyph.facets;

import org.audiveris.omr.glyph.Nest;

/**
 * Class {@code BasicAdministration} is a basic implementation of glyph
 * administration facet
 *
 * @author Hervé Bitteur
 */
class BasicAdministration
        extends BasicFacet
        implements GlyphAdministration
{
    //~ Instance fields --------------------------------------------------------

    /** The containing glyph nest. */
    protected Nest nest;

    /** Glyph instance identifier. (Unique in the containing nest) */
    protected int id;

    /** Flag to remember processing has been done. */
    private boolean processed = false;

    /** VIP flag. */
    protected boolean vip;

    /** Related id string (prebuilt once for all) */
    protected String idString;

    //~ Constructors -----------------------------------------------------------
    //---------------------//
    // BasicAdministration //
    //---------------------//
    /**
     * Create a new BasicAdministration object
     *
     * @param glyph our glyph
     */
    public BasicAdministration (Glyph glyph)
    {
        super(glyph);
    }

    //~ Methods ----------------------------------------------------------------
    //--------//
    // dumpOf //
    //--------//
    @Override
    public String dumpOf ()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Glyph: %s@%s%n",
                glyph.getClass().getName(),
                Integer.toHexString(glyph.hashCode())));
        sb.append(String.format("   id=%d%n", getId()));
        sb.append(String.format("   nest=%s%n", getNest()));

        return sb.toString();
    }

    //-------//
    // getId //
    //-------//
    @Override
    public int getId ()
    {
        return id;
    }

    //---------//
    // getNest //
    //---------//
    @Override
    public Nest getNest ()
    {
        return nest;
    }

    //----------//
    // idString //
    //----------//
    @Override
    public final String idString ()
    {
        return idString;
    }

    //-------------//
    // isProcessed //
    //-------------//
    @Override
    public boolean isProcessed ()
    {
        return processed;
    }

    //-------------//
    // isTransient //
    //-------------//
    @Override
    public boolean isTransient ()
    {
        return nest == null;
    }

    //-------//
    // isVip //
    //-------//
    @Override
    public boolean isVip ()
    {
        return vip;
    }

    //-----------//
    // isVirtual //
    //-----------//
    @Override
    public boolean isVirtual ()
    {
        return false;
    }

    //-------//
    // setId //
    //-------//
    @Override
    public void setId (int id)
    {
        this.id = id;
        idString = "glyph#" + id;
    }

    //---------//
    // setNest //
    //---------//
    @Override
    public void setNest (Nest nest)
    {
        this.nest = nest;
    }

    //--------------//
    // setProcessed //
    //--------------//
    @Override
    public void setProcessed (boolean processed)
    {
        this.processed = processed;
    }

    //--------//
    // setVip //
    //--------//
    @Override
    public void setVip ()
    {
        vip = true;
    }
}
