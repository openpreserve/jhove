/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.jpeg2000;

import edu.harvard.hul.ois.jhove.*;
import java.io.*;
import java.util.*;

/**
 * Instruction Set Box (JPX).
 * See ISO/IEC FCD15444-2: 2000, L.9.10.2
 * 
 *
 * @author Gary McGath
 *
 */
public class InstructionSetBox extends JP2Box {


    /**
     *  Constructor with superbox.
     * 
     *  @param   parent   parent superbox of this box
     */
    public InstructionSetBox(RandomAccessFile raf, BoxHolder parent) {
        super(raf, parent);
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
        
        // Flags indicating which parameters are in instructions

        // Can be found only in a Composition Box
        if (!(_parentBox instanceof CompositionBox)) {
            wrongBoxContext ();
            return false;
        }

        initBytesRead ();
        
        // ityp flags indicate which parameters are present
        int ityp = _module.readUnsignedShort (_dstrm);
        boolean hasXO_YO = ((ityp & 1) != 0);
        boolean hasWid_Ht = ((ityp & 2) != 0);
        boolean hasAnimation = ((ityp & 8) != 0);
        boolean hasCrop = ((ityp & 0X20) != 0);
        
        // Get the repeat count
        _module.readUnsignedShort (_dstrm);
        
        // Get the tick duration.  Ignored (but still takes up
        // space) if hasAnimation is false.
        _module.readUnsignedInt (_dstrm);
        
        int sizeLeft = (int) _boxHeader.getDataLength () - 8;
        // If all significant bits of ityp are 0, there are no instructions
        if ((ityp & 0X2B) == 0) {
            if (sizeLeft != 0) {
                _repInfo.setMessage (new ErrorMessage
                    (MessageConstants.JPEG2000_HUL_29,
                    _module.getFilePos ()));
                _repInfo.setWellFormed (false);
                return false;
            }
        }
        else {
            List<Property> instProps = new ArrayList<> (11);
            // Loop to read instructions
            while (sizeLeft >= 0) {
                if (hasXO_YO) {
                    long xo = _module.readUnsignedInt (_dstrm);
                    instProps.add (new Property ("HorizontalOffset",
                            PropertyType.LONG, xo));
                    long yo = _module.readUnsignedInt (_dstrm);
                    instProps.add (new Property ("VerticalOffset",
                            PropertyType.LONG, yo));
                    sizeLeft -= 8;
                }
                if (hasWid_Ht) {
                    long width = _module.readUnsignedInt (_dstrm);
                    instProps.add (new Property ("Width",
                            PropertyType.LONG, width));
                    long height = _module.readUnsignedInt (_dstrm);
                    instProps.add (new Property ("Height",
                            PropertyType.LONG, height));
                    sizeLeft -= 8;
                }
                if (hasAnimation) {
                    long life = _module.readUnsignedInt (_dstrm);
                    // The high bit of life is the persistence flag
                    boolean persist = ((life & 0X80000000) != 0);
                    instProps.add (new Property ("Persist",
                            PropertyType.BOOLEAN, persist));
                    life &= 0X7FFFFFFF;
                    instProps.add (new Property ("Life",
                            PropertyType.LONG, life));

                    // Sloppy documentation: I'm assuming that N
                    // and NEXT-USE are the same thing.
                    long nextuse = _module.readUnsignedInt (_dstrm);
                    instProps.add (new Property ("NextUse",
                            PropertyType.LONG, nextuse));
                    sizeLeft -= 8;
                }
                if (hasCrop) {
                    long xc = _module.readUnsignedInt (_dstrm);
                    instProps.add (new Property ("HorizontalCropOffset",
                            PropertyType.LONG, xc));
                    long yc = _module.readUnsignedInt (_dstrm);
                    instProps.add (new Property ("VerticalCropOffset",
                            PropertyType.LONG, yc));
                    long wc = _module.readUnsignedInt (_dstrm);
                    instProps.add (new Property ("CroppedWidth",
                            PropertyType.LONG, wc));
                    long hc = _module.readUnsignedInt (_dstrm);
                    instProps.add (new Property ("CroppedHeight",
                            PropertyType.LONG, hc));
                    sizeLeft -= 16;
                }
                if (sizeLeft < 0) {
                    _repInfo.setMessage (new ErrorMessage
                        (MessageConstants.JPEG2000_HUL_28,
                        _module.getFilePos ()));
                    _repInfo.setWellFormed (false);
                    return false;
                }
            }
        }
        
        finalizeBytesRead ();
        return true;
    }

}
