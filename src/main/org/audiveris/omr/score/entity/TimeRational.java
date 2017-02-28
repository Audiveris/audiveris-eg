//----------------------------------------------------------------------------//
//                                                                            //
//                          T i m e R a t i o n a l                           //
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
package org.audiveris.omr.score.entity;

import org.audiveris.omr.math.Rational;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class {@code TimeRational} is a marshallable and non-mutable
 * structure, meant to carry the actual rational members of a
 * TimeSignature.
 * <p>Note for example that (3/4) and (6/8) share the same rational value,
 * but with different actual members.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "rational")
public class TimeRational
{
    //~ Instance fields --------------------------------------------------------

    /** The actual denominator */
    @XmlAttribute
    public final int den;

    /** The actual numerator */
    @XmlAttribute
    public final int num;

    //~ Constructors -----------------------------------------------------------
    //--------------//
    // TimeRational //
    //--------------//
    /**
     * Creates a new TimeRational object.
     *
     * @param num the actual numerator
     * @param den the actual denominator
     */
    public TimeRational (int num,
                         int den)
    {
        this.num = num;
        this.den = den;
    }

    //--------------//
    // TimeRational //
    //--------------//
    /** To please JAXB */
    private TimeRational ()
    {
        den = num = 0;
    }

    //~ Methods ----------------------------------------------------------------
    //--------//
    // equals //
    //--------//
    @Override
    public boolean equals (Object obj)
    {
        if (!(obj instanceof TimeRational)) {
            return false;
        } else {
            TimeRational that = (TimeRational) obj;

            return (this.num == that.num) && (this.den == that.den);
        }
    }

    //----------//
    // getValue //
    //----------//
    public Rational getValue ()
    {
        return new Rational(num, den);
    }

    //----------//
    // hashCode //
    //----------//
    @Override
    public int hashCode ()
    {
        int hash = 7;
        hash = (97 * hash) + this.num;
        hash = (97 * hash) + this.den;

        return hash;
    }

    //----------//
    // toString //
    //----------//
    /** {@inheritDoc } */
    @Override
    public String toString ()
    {
        return num + "/" + den;
    }
}
