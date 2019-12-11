/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

import java.io.*;
import java.lang.reflect.*;
//import com.apple.eio.FileManager;


/**
 * Code specific to Macintosh Java. This class consists of static
 * methods, and should not be instantiated. Its methods should be
 * called only on the Macintosh OS X platform. It requires the
 * package {@code com.apple.eio.FileManager}.
 *
 * @author Gary McGath
 */
public class MacStuff {

    /** Private constructor to prevent instantiation.*/
    private MacStuff() {
    }

    /**
     * Determines if we're running on a Macintosh, so appropriate UI
	 * adjustments can be made. In accordance with Apple's recommendations,
	 * this checks for the existence of the {@code mrj.version} property
	 * rather than checking the {@code os.name} property.
     */
    public static boolean isMacintosh() {
        return (System.getProperty("mrj.version") != null);
    }

    /**
     * Returns {@code true} if a file has the given file type.
     * This method uses {@code FileManager} in a dynamic way, so
     * that it will merely throw a {@code ClassNotFound} exception
     * if it fails.
     * <p>
     * Currently this code isn't actually used, since the
     * Jhove application is specified as checking only internal
     * signatures. Should some future version or add-on code
     * wish to use it, the code should look something like this:
     * <pre>
	 * {@code
     *     try {
     *         if (sig.getType() == SignatureType.FILETYPE &&
     *                 MacStuff.isMacintosh()) {
     *             if (!MacStuff.fileHasType(file, sig.getValueString())) {
     *                 info.setConsistent(false);
     *             }
     *         }
     *     }
     *     catch (ClassNotFoundException cnfe) {
     *         // Mac classes missing -- can't check filetype.
     *     }
	 * }
     * </pre>
     */
    public static boolean fileHasType(File file, String type)
            throws ClassNotFoundException
    {
        // Need to make this completely dynamic.
        //return type.equals (FileManager.getFileType (file));
        try {
            if (type == null) {
                return false;
            }
            Class<?> fmClass = Class.forName("com.apple.eio.FileManager");
            Class<?>[] params = new Class[1];
            params[0] = Class.forName("java.io.File");
            Object[] args = new Object[1];
            args[0] = file;
            Method method = fmClass.getMethod("getFileType", params);
            String fType = (String) method.invoke(null, args);
            return (type.equals(fType));
        }
        catch (ClassNotFoundException cnfe) {
            throw cnfe;
        }
        catch (Exception e) {
            return false;
        }
    }
}
