//----------------------------------------------------------------------------//
//                                                                            //
//                          S e c t i o n F o c u s                           //
//                                                                            //
//  Copyright (C) Herve Bitteur 2000-2006. All rights reserved.               //
//  This software is released under the terms of the GNU General Public       //
//  License. Please contact the author at herve.bitteur@laposte.net           //
//  to report bugs & suggestions.                                             //
//----------------------------------------------------------------------------//
//
package omr.lag;


/**
 * Interface <code>SectionFocus</code> define the features related to setting a
 * focus determined by a section, it is thus an input entity.
 *
 * @author Herv&eacute; Bitteur
 * @version $Id$
 */
public interface SectionFocus<S extends Section>
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Focus on a section
     *
     * @param section the section to focus upon
     */
    void setFocusSection (S section);

    /**
     * Focus on a section, knowing its id
     *
     * @param id the section is
     */
    void setFocusSection (int id);

    /**
     * Can retrieve a section knowing its id
     *
     * @param id id of the desired section
     * @return the section found, or null otherwise
     */
    Section getSectionById (int id);
}
