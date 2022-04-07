/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004-2008 by JSTOR and the President and Fellows of Harvard College
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 **********************************************************************/
package edu.harvard.hul.ois.jhove.viewer;

import edu.harvard.hul.ois.jhove.App;
import edu.harvard.hul.ois.jhove.ExitCode;
import edu.harvard.hul.ois.jhove.CoreMessageConstants;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.JhoveException;
import edu.harvard.hul.ois.jhove.viewer.JhoveWindow;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Toolkit;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JhoveView - JSTOR/Harvard Object Validation Environment.
 */
public class JhoveView {
    private static final Logger LOGGER = Logger.getLogger(JhoveView.class.getCanonicalName());

    /** Application name. */
    private static final String NAME = "JhoveView"; //$NON-NLS-1$
    /** Application icon. */
    private static final String ICON_PATH = "org/openpreservation/jhove/icon.png"; //$NON-NLS-1$

    /** Stub constructor. */
    private JhoveView() {
    }

    /**
     * Application main entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        // Make sure we have a satisfactory version of Java.
        String version = System.getProperty("java.vm.version"); //$NON-NLS-1$
        if (version.compareTo("1.8.0") < 0) { //$NON-NLS-1$
            LOGGER.log(Level.SEVERE, CoreMessageConstants.EXC_JAVA_VER_INCMPT);
            errorAlert(CoreMessageConstants.EXC_JAVA_VER_INCMPT);
            System.exit(ExitCode.INCOMPATIBLE_VM.getReturnCode());
        }

        // If we're running on a Macintosh, put the menubar at the top
        // of the screen where it belongs.
        System.setProperty("apple.laf.useScreenMenuBar", "true"); //$NON-NLS-1$ //$NON-NLS-2$

        App app = App.newAppWithName(NAME);
        try {

            // Retrieve configuration.
            String configFile = JhoveBase.getConfigFileFromProperties();
            String saxClass = JhoveBase.getSaxClassFromProperties();

            // Pre-parse the command line for -c and -x config options.
            boolean quoted = false;
            for (int i = 0; i < args.length; i++) {
                if (quoted) {
                    int len = args[i].length();
                    if (args[i].charAt(len - 1) == '"') {
                        quoted = false;
                    }
                } else {
                    if ("-c".equals(args[i])) {
                        if (i < args.length - 1) {
                            configFile = args[++i];
                        }
                    } else if ("-x".equals(args[i])) {
                        if (i < args.length - 1) {
                            saxClass = args[++i];
                        }
                    } else if (args[i].charAt(0) == '"') {
                        quoted = true;
                    }
                }
            }

            // Initialize the JHOVE engine.
            JhoveBase je = new JhoveBase();
            try {
                je.init(configFile, saxClass);
            } catch (JhoveException e) {
                errorAlert(e.getMessage());
                // Keep going, so user can correct in editor
            }

            // Create the main window to select a file.
            JhoveWindow jwin = new JhoveWindow(app, je);
            URL url = ClassLoader.getSystemResource(ICON_PATH); // $NON-NLS-1$
            Toolkit kit = Toolkit.getDefaultToolkit();
            jwin.setIconImage(kit.createImage(url));
            jwin.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            LOGGER.log(Level.SEVERE, e.getMessage());
            System.exit(ExitCode.ERROR.getReturnCode());
        }
    }

    /** Displays an error alert. */
    private static void errorAlert(String msg) {
        JFrame hiddenFrame = new JFrame();
        // Truncate long messages so the alert isn't wider than the screen
        String message = (msg.length() > 80) ? msg.substring(0, 79) + "..." : msg;
        LOGGER.log(Level.WARNING, msg);
        JOptionPane.showMessageDialog(hiddenFrame, message, "Jhove Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
