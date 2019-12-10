/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.jpeg2000;

import java.io.*;

import edu.harvard.hul.ois.jhove.*;

/**
 * Capture Resolution Box.
 * See I.5.3.7.1 in ISO/IEC 15444-1:2000
 *
 * @author Gary McGath
 *
 */
public class CaptureResolutionBox extends JP2Box {


    /**
     *  Constructor with superbox.
     * 
     *  @param   parent   parent superbox of this box
     */
    public CaptureResolutionBox (RandomAccessFile raf, BoxHolder parent)
    {
        super (raf, parent);
    }

    /** Reads the box, putting appropriate information in
     *  the RepInfo object.  setModule, setBoxHeader,
     *  setRepInfo and setDataInputStream must be called
     *  before <code>readBox</code> is called. 
     *  <code>readBox</code> must completely consume the
     *  box, so that the next byte to be read by the
     *  DataInputStream is the <code>FF</code> byte of the next Box.
     */
    @Override
	public boolean readBox() throws IOException {
        if (!(_parentBox instanceof ResolutionBox)) {
            wrongBoxContext ();
            return false;
        }
        initBytesRead ();
        // The information consists of two values, horizontal and
        // vertical, with numerator, denominator, and exponent,
        // in dots per meter.  Not clear whether to present this 
        // as raw data, turn it into a dots/cm rational, or what.
        // I'll put it up as raw data for now.

        // Vertical Capture grid resolution num & denom
        int vrcNum = _module.readUnsignedShort (_dstrm);
        int vrcDenom = _module.readUnsignedShort (_dstrm);
        
        // Horizontal Capture grid resolution num & denom
        int hrcNum = _module.readUnsignedShort (_dstrm);
        int hrcDenom = _module.readUnsignedShort (_dstrm);
        
        // Vertical and Horizontal capture grid exponents
        int vrcExp = ModuleBase.readUnsignedByte (_dstrm, _module);
        int hrcExp = ModuleBase.readUnsignedByte (_dstrm, _module);
        
        // And the two resolution properties are subsumed into
        // one property for the Module.
        Property[] topProps = new Property[2];
        topProps[0] = ResolutionBox.makeResolutionProperty("HorizResolution", 
        		hrcNum, hrcDenom, hrcExp);
        topProps[1] = ResolutionBox.makeResolutionProperty("VertResolution", 
        		vrcNum, vrcDenom, vrcExp);
        _module.addProperty(new Property ("CaptureResolution",
                PropertyType.PROPERTY,
                PropertyArity.ARRAY,
                topProps));

        // Add to NISO
        Rational vrc = ResolutionBox.convertToRational(vrcNum, vrcDenom, vrcExp);
        Rational hrc = ResolutionBox.convertToRational(hrcNum, hrcDenom, hrcExp); 
        NisoImageMetadata niso = _module.getCurrentNiso ();
        niso.setYSamplingFrequency (vrc);
        niso.setXSamplingFrequency (hrc);
        final int RESOLUTION_UNIT_CM = 3;
        niso.setSamplingFrequencyUnit (RESOLUTION_UNIT_CM);
        
        finalizeBytesRead ();
        return true;
    }

    /** Returns the name of the Box.  */
    @Override
	protected String getSelfPropName ()
    {
        return "Capture Resolution Box";
    }
}
