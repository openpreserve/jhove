/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.jpeg2000;

import java.io.*;
//import edu.harvard.hul.ois.jhove.*;
//import edu.harvard.hul.ois.jhove.module.Jpeg2000Module;

/**
 * Resolution box.
 * See I.5.3.7 in ISO/IEC 15444-1:2000
 *
 * @author Gary McGath
 *
 * @see CaptureResolutionBox
 * @see DDResolutionBox
 */
public class ResolutionBox extends JP2Box {


    /**
     *  Constructor with superbox.
     * 
     *  @param   parent   parent superbox of this box
     */
    public ResolutionBox (RandomAccessFile raf, BoxHolder parent)
    {
        super (raf, parent);
        //_parentBox = parent;
    }


    /* (non-Javadoc)
     * @see edu.harvard.hul.ois.jhove.module.jpeg2000.JP2Box#readBox()
     */
    @Override
	public boolean readBox() throws IOException {
        initBytesRead ();
        this.hasBoxes = true;

        //NisoImageMetadata niso = _module.getDefaultNiso ();
        // Later have to implement support for compositing layers,
        // assigning an appropriate value to niso
        JP2Box box;
        while (hasNext ()) {
            
            box = (JP2Box) next();
            if (box == null) {
                break;
            }
            if (box instanceof CaptureResolutionBox) {
                // Capture resolution box
                if (!box.readBox ()) {
                    return false;
                }
            }
            else if (box instanceof DDResolutionBox) {
                // Default Display Resolution box
                if (!box.readBox ()) {
                    return false;
                }
            }
            else {
                // Skip over other boxes.
                box.skipBox ();
            }
        }
        finalizeBytesRead ();
        return true;
    }

    /** Returns the name of the Box.  */
    @Override
	protected String getSelfPropName ()
    {
        return "Resolution Box";
    }
}
