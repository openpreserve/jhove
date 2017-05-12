/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.tiff;

/**
 * Profile checker for the Exif IFD of a TIFF file which potentially
 * matches the TIFF profile.  This is called from TiffProfileExif
 * to check the Exif IFD.
 *
 * @author Gary McGath
 *
 */
public class TiffProfileExifIFD extends TiffProfile {

    private int _majVersion;
    private int _minVersion;

    public TiffProfileExifIFD ()
    {        
        super ();
        // This isn't used directly to report a profile, so the
        // profile text is irrelevant.
        _profileText = null;
        _majVersion = -1;
        _minVersion = -1;
    }

    /**
     *  Returns true if the IFD satisfies the requirements of an
     *  Exif profile.  See the Exif specification for details.
     */
    public boolean satisfiesThisProfile (IFD ifd) 
    {
        if (!(ifd instanceof ExifIFD)) {
            return false;
        }
        ExifIFD eifd = (ExifIFD) ifd;
        String version = eifd.getExifVersion ();
        if ("0230".equals (version)) {
            _majVersion = 2;
            _minVersion = 3;
        }
        else if ("0221".equals (version)) {
            _majVersion = 2;
            _minVersion = 21;
        }
        else if ("0220".equals (version)) {
            _majVersion = 2;
            _minVersion = 2;
        }
        else if ("0210".equals (version)) {
            _majVersion = 2;
            _minVersion = 1;
        }
        else if ("0200".equals (version)) {
            _majVersion = 2;
            _minVersion = 0;
        }
        else {
            // Other versions aren't accepted
            return false;
        }
        if (!("0100".equals(eifd.getFlashpixVersion ()))) {
            return false;
        }
        int colspc = eifd.getColorspace ();
        if (colspc != 1 && colspc != 65535) {
            return false;
        }
        return true;
    }

    public int getMajorVersion() {
		return _majVersion;
	}

    public int getMinorVersion() {
		return _minVersion;
	}
}
