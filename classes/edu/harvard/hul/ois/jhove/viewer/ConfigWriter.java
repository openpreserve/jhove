/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 *
 **********************************************************************/

package edu.harvard.hul.ois.jhove.viewer;

import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * 
 * Class to write out configuration information to the configuration file.
 * To minimize the chance of getting into a bad state, it writes to a
 * temporary file, then replaces the old config file with that file,
 * rather than directly overwriting the existing file.
 * 
 * @author Gary McGath
 *
 */
public class ConfigWriter {

    private PrintWriter _confOut;
    private File _tempFile;
    private File _confFile;
    ConfigWindow _parent;

    /**
     *  Constructor.
     *  Creates a temporary file for writing and creates an OutputStreamWriter
     *  to write to it.  If there is already a file located by
     *  <code>file</code>, it will not be replaced or overwritten
     *  until <code>writeFile</code> has successfully written out
     *  the temporary file.
     * 
     *  @param  file    Location of the configuration file
     * 
     *  @param  parent  The ConfigWindow which invoked this instance
     */
    public ConfigWriter (File file, ConfigWindow parent) throws IOException
    {
        _confFile = file;
        _parent = parent;
        // Set up a temporary file to write to. 
        String path = file.getParent();
        _tempFile = File.createTempFile ("jho", ".conf", new File (path));
        //_tempFile.createNewFile();   
        FileOutputStream ostrm = new FileOutputStream (_tempFile);
        OutputStreamWriter osw = new OutputStreamWriter (ostrm, "UTF-8");
        _confOut = new PrintWriter (osw);
    }
    

    /**
     *   Writes out the content of the file to the temporary file,
     *   then deletes the existing configuration file (as specified 
     *   by the constructor parameter) and renames the temporary file 
     *   to the configuration file.
     * 
     *   If the temporary file can't be written, or the configuration
     *   file can't be replaced, a warning dialog is put up and the
     *   configuration file remains unchanged.
     */
    public void writeFile (List modules,
            List handlers,
            File homeDir,
            File tempDir,
            String encoding,
            int bufferSize) throws IOException
    {
        writeHead ();
        
        // Write out the modules
        ListIterator iter = modules.listIterator ();
        while (iter.hasNext ()) {
            String[] tuple = (String []) iter.next ();
            String clas = tuple[0];
            String init = tuple[1];
            // The class must be non-null, but init may be null.
            // If the class is empty, it's a user error (probably
            // clicked "Add" and then lost track of it).  Don't
            // write it out.
            if (!"".equals (clas)) {
                _confOut.println (" <module>");
                _confOut.println ("   <class>" + encodeContent (clas) +
                         "</class>");
                if (init != null && init.length () > 0) {
                    _confOut.println ("   <init>" + encodeContent (init) +
                         "</init>");
                }
                _confOut.println (" </module>");
            }
        }
        
        // Write out the handlers
        iter = handlers.listIterator ();
        while (iter.hasNext ()) {
            String handler = (String) iter.next ();
            _confOut.println (" <handler>");
            _confOut.println ("   <class>" + encodeContent (handler) +
                     "</class>");
            _confOut.println (" </handler>");
        }
        
        // Write the buffer size if not default
        if (bufferSize > 0) {
            _confOut.println (" <bufferSize>" + bufferSize + 
                    "</bufferSize>");
        }
        
        // Write out the encoding
        if (encoding != null && encoding.length() > 0) {
            _confOut.println (" <defaultEncoding>" +
                    encodeContent (encoding) + "</defaultEncoding>");
        }
        
        // Write the home and temp directories.  Home must always be valid.
        _confOut.println (" <jhoveHome>" + 
                    encodeContent (homeDir.getPath ()) + 
                    "</jhoveHome>");
        if (tempDir != null) {
            _confOut.println (" <tempDirectory>" + 
                    encodeContent (tempDir.getPath ()) + 
                    "</tempDirectory>");
        }
        writeTail ();
        _confOut.close ();
        
        // Replace the old file with the new.
        if (_confFile.exists () && !_confFile.delete ()) {
            JOptionPane.showMessageDialog(_parent, 
                    "Can't replace old config file", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            _tempFile.delete ();
        }
        else {
            _tempFile.renameTo (_confFile);
        }
    }
    
    /* Write the fixed lines which begin the config file */
    private void writeHead () throws IOException
    {
        _confOut.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        _confOut.println("<jhoveConfig version=\"1.0\"");
        _confOut.println("             xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        _confOut.println("             xmlns=\"http://hul.harvard.edu/ois/xml/ns/jhove/jhoveConfig\"");

        // Note that the open quote of this line isn't closed till the next line; that's intentional.
        _confOut.println("xsi:schemaLocation=\"http://hul.harvard.edu/ois/xml/ns/jhove/jhoveConfig");

        _confOut.println("             http://hul.harvard.edu/ois/xml/xsd/jhove/jhoveConfig.xsd\">");
    }
    
    /* Write out the fixed end of the config file */
    private void writeTail () throws IOException
    {
        _confOut.println("</jhoveConfig>");
    }
    
    
        /**
     *   Encodes a content String in XML-clean form, converting characters
     *   to entities as necessary.  The null string will be
     *   converted to an empty string.
     */
    private static String encodeContent (String content)
    {
        if (content == null) {
            content = "";
        }
        StringBuffer buffer = new StringBuffer (content);

        int n = 0;
        while ((n = buffer.indexOf ("&", n)) > -1) {
            buffer.insert (n+1, "amp;");
            n +=5;
        }
        n = 0;
        while ((n = buffer.indexOf ("<", n)) > -1) {
            buffer.replace (n, n+1, "&lt;");
            n += 4;
        }
        n = 0;
        while ((n = buffer.indexOf (">", n)) > -1) {
            buffer.replace (n, n+1, "&gt;");
            n += 4;
        }

        return buffer.toString ();
    }
}
