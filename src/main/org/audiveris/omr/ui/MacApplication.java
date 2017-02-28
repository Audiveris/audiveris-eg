//----------------------------------------------------------------------------//
//                                                                            //
//                        M a c A p p l i c a t i o n                         //
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
package org.audiveris.omr.ui;

import org.audiveris.omr.WellKnowns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Class {@code MacApplication} provides dynamic hooks into the
 * OSX-only eawt package, registering Audiveris actions for the
 * Preferences, About, and Quit menu items.
 *
 * @author Brenton Partridge
 * @author Max Poliakovski
 */
public class MacApplication
        implements InvocationHandler
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            MacApplication.class);

    //~ Methods ----------------------------------------------------------------
    /**
     * Invocation handler for
     * <code>
     * com.apple.eawt.xxxHandler where
     * xxx denotes About, Quit and Preferences interfaces respectively</code>.
     * This method should not be manually called;
     * it is used by the proxy to forward calls.
     *
     * @throws Throwable
     */
    @Override
    public Object invoke (Object proxy,
                          Method method,
                          Object[] args)
            throws Throwable
    {
        String name = method.getName();

        switch (name) {
        case "handlePreferences":
            GuiActions.getInstance().defineOptions(null);

            break;

        case "handleQuitRequestWith":
            GuiActions.getInstance().exit(null);

            break;

        case "handleAbout":
            GuiActions.getInstance().showAbout(null);

            break;
        }

        return null;
    }

    /**
     * Registers actions for preferences, about, and quit.
     *
     * @return true if successful, false if platform is not
     *         Mac OS X or if an error occurs
     */
    @SuppressWarnings("unchecked")
    public static boolean setupMacMenus ()
    {
        if (!WellKnowns.MAC_OS_X) {
            return false;
        }
        
        try {
            // we will be using reflection here to call methods
            // from the Mac OS X specific com.apple.eawt.* package in order
            // to avoid compile and runtime errors on non-Mac platforms
            Class<?> macAppClass = Class.forName("com.apple.eawt.Application");
            Method getAppMethod = macAppClass.getMethod("getApplication");
            Object macApp = getAppMethod.invoke(null);
            
            Class<?> aboutHandlerClass = Class.forName("com.apple.eawt.AboutHandler");
            Method setAboutHandler = macAppClass.getMethod(
                    "setAboutHandler", aboutHandlerClass);
            
            Class<?> quitHandlerClass = Class.forName("com.apple.eawt.QuitHandler");
            Method setQuitHandler = macAppClass.getMethod("setQuitHandler",
                    quitHandlerClass);
            
            Class<?> prefHandlerClass = Class.forName("com.apple.eawt.PreferencesHandler");
            Method setPreferencesHandler = macAppClass.getMethod(
                    "setPreferencesHandler", prefHandlerClass);
            
            setAboutHandler.invoke(macApp, Proxy.newProxyInstance(
                    MacApplication.class.getClassLoader(),
                    new Class<?>[]{aboutHandlerClass},
                    new MacApplication()));
            
            setQuitHandler.invoke(macApp, Proxy.newProxyInstance(
                    MacApplication.class.getClassLoader(),
                    new Class<?>[]{quitHandlerClass},
                    new MacApplication()));
            
            setPreferencesHandler.invoke(macApp, Proxy.newProxyInstance(
                    MacApplication.class.getClassLoader(),
                    new Class<?>[]{prefHandlerClass},
                    new MacApplication()));
            
            return true;
        } catch (Exception ex) {
            logger.warn("Unable to setup Mac OS X GUI integration", ex);
            return false;
        }
    }
}
