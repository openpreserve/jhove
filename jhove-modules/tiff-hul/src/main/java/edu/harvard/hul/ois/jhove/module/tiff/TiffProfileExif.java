/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.tiff;

import edu.harvard.hul.ois.jhove.*;


/**
 *  Profile checker for Exif.  This applies to the main IFD
 *  of the file.  To satisfy the Exif profile, the thumbnail
 *  IFD must also satisfy TiffProfileExifThumb.
 * 
 *  @see TiffProfileExifThumb
 *  @see TiffProfileExifIFD
 */
public final class TiffProfileExif extends TiffProfile
{
    /* The profile text depends on the version. */
	private static final String[] EXIF_VERSIONS = { "0200", "0210", "0220", "0221", "0230" };

	private static final String[] PROFILE_TEXTS = { "Exif 2.0",
            "Exif 2.1 (JEIDA-49-1998)",
            "Exif 2.2 (JEITA CP-3451)",
            "Exif 2.21 (JEITA CP-3451A)",
            "Exif 2.3 (JEITA CP-3451C)"
            };

	private TiffProfileExifIFD _exifIFDProfile;
    
    public TiffProfileExif ()
    {
	super ();
        _exifIFDProfile = new TiffProfileExifIFD ();
    }

    /**
     *  Returns true if the IFD satisfies the requirements of an
     *  Exif profile.  See the Exif specification for details.
     */
    @Override
	public boolean satisfiesThisProfile (IFD ifd) 
    {
	if (!(ifd instanceof TiffIFD)) {
	    return false;
	}
	TiffIFD tifd = (TiffIFD) ifd;

	/* Check required tags. */
	NisoImageMetadata niso = tifd.getNisoImageMetadata ();
        if (niso.getXSamplingFrequency () == null ||
                niso.getYSamplingFrequency () == null) {
            return false;
        }

        if (!tifd.isFirst () || tifd.getTheExifIFD () == null) {
        	// The first image is not a Exif JPEG => the compression should be verify
	    	if (satisfiesCompression (tifd, 1)) {
	            if (niso.getImageWidth () == NisoImageMetadata.NULL ||
	                    niso.getImageLength () == NisoImageMetadata.NULL ||
	                    niso.getStripOffsets () == null ||
	                    niso.getRowsPerStrip () == NisoImageMetadata.NULL ||
	                    niso.getStripByteCounts () == null)  {
	    	        return false;
	            }
	            if (niso.getSamplesPerPixel () != 3) {
	                return false;
	            }
	            /* BitsPerSample must be [8, 8, 8] */
	            int[] bps = niso.getBitsPerSample ();
	            if (bps == null || bps.length < 3 || bps[0] != 8 || bps[1] != 8 ||
	                bps[2] != 8) {
	                return false;
	            }
	            int pInterpretation = niso.getColorSpace ();
	            if (!(pInterpretation == 2 || pInterpretation == 6)) {
	                return false;
	            }
	            if (pInterpretation == 6 && (
                            niso.getYCbCrSubSampling () == null ||
                            niso.getYCbCrPositioning () == NisoImageMetadata.NULL)) {
                        return false;
	            }
	    	}
	        else {
	            // If the compression isn't 1, then the JPEGInterchangeFormat
	            // tag must be present, but other requirements are lifted.
	            if (tifd.getJpegInterchangeFormat() == NisoImageMetadata.NULL) {
	                return false;
	            }
	        }
        }




	if (!satisfiesResolutionUnit (tifd, new int [] {2, 3} )) {
	    return false;
	}

        /* for the first IFD only, there must be an Exif subifd */
	if (tifd.isFirst ()) {
            ExifIFD eifd = tifd.getTheExifIFD ();
            if (eifd == null) {
                return false;
            }
            // The Exif IFD must satisfy the profile requirements
            if (!_exifIFDProfile.satisfiesThisProfile(eifd)) {
                return false;
            }
            String version = eifd.getExifVersion ();
            for (int idx = 0; idx < EXIF_VERSIONS.length; idx++) {
            	if (EXIF_VERSIONS[idx].equals (version)) {
                    _profileText = PROFILE_TEXTS[idx];
            		break;
            	}
            }
        }

	return true;
    }
}
