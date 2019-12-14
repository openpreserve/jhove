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
	private static final String[] ACCEPTED_EXIF_VERSIONS = { "0200", "0210",
			"0220", "0221", "0230" };

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
    @Override
	public boolean satisfiesThisProfile (IFD ifd) 
    {
        if (!(ifd instanceof ExifIFD)) {
            return false;
        }
        ExifIFD eifd = (ExifIFD) ifd;
        String version = eifd.getExifVersion ();
        for (String acceptedVersion : ACCEPTED_EXIF_VERSIONS) {
        	if (acceptedVersion.equals (version)) {
                _majVersion = Integer.parseInt(version.substring(0, 2));
                _minVersion = Integer.parseInt(version.substring(2, 4));
                break;
        	}
        }
        if (_majVersion == -1) {
            // Other versions aren't accepted
            return false;
        }
        
        if (!("0100".equals(eifd.getFlashpixVersion ()))) {
            return false;
        }
        int colspc = eifd.getColorspace ();
        return !(colspc != 1 && colspc != 65535);
    }

    public int getMajorVersion() {
		return _majVersion;
	}

    public int getMinorVersion() {
		return _minVersion;
	}
}
