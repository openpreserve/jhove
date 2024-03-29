/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.tiff;

import edu.harvard.hul.ois.jhove.*;

/**
 *  Profile checker for TIFF FX, Profile J (lossless JBIG).
 *
 *  Image data content is not checked for profile conformance.
 *  Only tags are checked.
 *
 * @author Gary McGath
 *
 */
public class TiffProfileFXJ extends TiffFXBase {

    /**
     *  Constructor.
     */
    public TiffProfileFXJ ()
    {
        super ();
        _profileText = "TIFF-FX (Profile J)";
        _mimeClass = MIME_FX;
    }


    /**
     *  Returns true if the IFD satisfies the requirements of a
     *  TIFF/FX J profile.  See the TIFF/FX specification for
     *  details.
     */
    @Override
	public boolean satisfiesThisProfile(IFD ifd) {
        if (!(ifd instanceof TiffIFD)) {
            return false;
        }
        TiffIFD tifd = (TiffIFD) ifd;
        if (!satisfiesClass (tifd)) {
            return false;
        }
        NisoImageMetadata niso = tifd.getNisoImageMetadata ();
        int[] bps = niso.getBitsPerSample ();
        if (bps[0] != 1) {
            return false;
        }
        int cmp = niso.getCompressionScheme();
        if (cmp != 9) {
            return false;
        }
        if (!satisfiesFillOrder (tifd,
                new int[] {1, 2} )) {
            return false;
            // RFC 2301 (1998) is internally inconsistent about
            // whether a FillOrder of 1 is permitted.  The latest
            // working draft allows a FillOrder of 1, so I've
            // resolved the conflict in favor of that interpretation.
        }
        // We've already established that if the compression
        // scheme is 3, T4Options exists.  But we must establish
        // that if it's 4, T6Options exists and has a value of 0.
        if (cmp == 4 && tifd.getT6Options () != 0) {
            return false;
        }

        // XResolution, YResolution, and ImageWidth have codependencies.
        boolean xywOK = false;    // guilty till proven innocent
        long xRes = niso.getXSamplingFrequency ().toLong();
        long yRes = niso.getYSamplingFrequency ().toLong();
        if (niso.getSamplingFrequencyUnit() == 3) {
            // Convert from units/cm to units/inch, with rounding
            xRes = perCMtoPerInch ((int) xRes);
            yRes = perCMtoPerInch ((int) yRes);
        }
        long wid = niso.getImageWidth();
        if (((xRes == 200 && yRes == 100) ||
                (xRes == 204 && yRes == 98) ||
                (xRes == 200 && yRes == 200) ||
                (xRes == 204 && yRes == 196) ||
                (xRes == 204 && yRes == 391)) && 
                (wid == 1728 || wid == 2048 || wid == 2432)) {
            xywOK = true;
        }
        if (xRes == 300 && yRes == 300 && 
                (wid == 2592 || wid == 3072 || wid == 3648)) {
            xywOK = true;
        }
        if (((xRes == 408 && yRes == 391) ||
                (xRes == 400 && yRes == 400)) && 
                (wid == 3456 || wid == 4096 || wid == 4864)) {
            xywOK = true;
        }
        // passed all tests
        return xywOK;         
    }

}
