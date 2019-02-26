/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.jpeg2000;

/**
 * This is a subclass of MarkerSegment for Markers.
 * Markers are those elements of a codestream which have
 * no parameters.  It can be subclassed for specific Markers,
 * or used directly to provide default behavior.
 * 
 * @author Gary McGath
 *
 */
public class Marker extends MarkerSegment {

    public Marker ()
    {
    }

    /** Overrides the superclass to return 0 without consuming
     *  any bytes from the DataInputStream.
     */
    @Override
	protected int readMarkLen ()
    {
        return 0;
    }
    
    
    /**  Default processing.  Does nothing, and always returns true. */
    @Override
	protected boolean process (int bytesToEat)
    {
        return true;
    }

}
