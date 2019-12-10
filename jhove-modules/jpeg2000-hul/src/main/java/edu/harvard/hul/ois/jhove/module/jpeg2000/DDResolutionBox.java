/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.jpeg2000;

import java.io.*;

import edu.harvard.hul.ois.jhove.*;

/**
 * Default Display Resolution Box.
 * See I.5.3.7.2 in ISO/IEC 15444-1:2000
 * 
 * @author Gary McGath
 *
 */
public class DDResolutionBox extends JP2Box {


    /**
     *  Constructor with superbox.
     * 
     *  @param   parent   parent superbox of this box
     */
    public DDResolutionBox (RandomAccessFile raf, BoxHolder parent)
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
        initBytesRead ();
        if (!(_parentBox instanceof ResolutionBox)) {
            wrongBoxContext();
            return false;
        }
        
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
        _module.addProperty(new Property ("DefaultDisplayResolution",
                PropertyType.PROPERTY,
                PropertyArity.ARRAY,
                topProps));
        
        // If the resolution is not set by a CaptureResolutionBox, we assign it
        // We need to set resolution in NisoImageMetadata
        // as a Rational.  It seems unlikely that negative
        // exponents will be used (signifying resolutions
        // less than 1 dpi), so we figure the exponent into
        // the numerator.  Also, this resolution is in 
        // dots per meter, which isn't a NISO standard unit,
        // so we multiply the denominator by 100 to give
        // units per centimeter.
        NisoImageMetadata niso = _module.getCurrentNiso ();
        if (niso.getXSamplingFrequency() == null) {
            Rational vrc = ResolutionBox.convertToRational(vrcNum, vrcDenom, vrcExp);
            Rational hrc = ResolutionBox.convertToRational(hrcNum, hrcDenom, hrcExp); 
	        niso.setYSamplingFrequency (vrc);
	        niso.setXSamplingFrequency (hrc);
	        final int RESOLUTION_UNIT_CM = 3;
	        niso.setSamplingFrequencyUnit (RESOLUTION_UNIT_CM);
        }
        finalizeBytesRead ();
        return true;
    }

    /** Returns the name of the Box.  */
    @Override
	protected String getSelfPropName ()
    {
        return "Default Display Resolution Box";
    }
}
