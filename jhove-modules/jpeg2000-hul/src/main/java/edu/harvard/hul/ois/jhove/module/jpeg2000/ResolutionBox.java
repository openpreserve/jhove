/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.jpeg2000;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.Rational;

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

    public static Property makeResolutionProperty(String name, int num, int denom, int exp) {
        final int NUMBER_FOR_RATIONAL = 3;
        List resList = new ArrayList(NUMBER_FOR_RATIONAL);
        resList.add (new Property ("Numerator",
                PropertyType.INTEGER, new Integer (num)));
        resList.add (new Property ("Denominator",
                PropertyType.INTEGER, new Integer (denom)));
        resList.add (new Property ("Exponent",
                PropertyType.INTEGER, new Integer (exp)));
        // The three properties for each direction are subsumed into
        // a property.
        Property res = new Property (name,
                PropertyType.PROPERTY,
                PropertyArity.LIST,
                resList);
        return res;
    }

    public static Rational convertToRational(int num, int denom, int exp) {
        // We need to set resolution in NisoImageMetadata
        // as a Rational.  It seems unlikely that negative
        // exponents will be used (signifying resolutions
        // less than 1 dpi), so we figure the exponent into
        // the numerator.  Also, this resolution is in 
        // dots per meter, which isn't a NISO standard unit,
        // so we multiply the denominator by 100 to give
        // units per centimeter.
        final int POWER = 10;
        final int FROM_M_TO_CM = 100;
        return new Rational 
                    ((int) (num * Math.pow (POWER, exp)),
                     denom * FROM_M_TO_CM);
    }

    

    /** Returns the name of the Box.  */
    @Override
	protected String getSelfPropName ()
    {
        return "Resolution Box";
    }
}
