/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.tiff;

import edu.harvard.hul.ois.jhove.*;

/**
 *  Profile checker for TIFF Class R (Baseline Palette color).
 *
 *  @author Gary McGath
 */
public final class TiffProfileClassR extends TiffProfile
{
    public TiffProfileClassR ()
    {
        super ();
        _profileText =  "Baseline RGB (Class R)";
    }

    /**
     *  Returns true if the IFD satisfies the requirements of a
     *  Class R profile.  See the TIFF 6.0 specification for
     *  details.
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
        if (niso.getImageWidth () == NisoImageMetadata.NULL ||
            niso.getImageLength () == NisoImageMetadata.NULL ||
            niso.getStripOffsets () == null ||
            niso.getRowsPerStrip () == NisoImageMetadata.NULL ||
	    niso.getStripByteCounts () == null ||
            niso.getXSamplingFrequency () == null ||
            niso.getYSamplingFrequency () == null) {
            return false;
        }

        /* Check required values. */
        int [] bps = niso.getBitsPerSample ();
        if (bps == null || bps.length < 3 || 
	    bps[0] != 8 || bps[1] != 8 || bps[2] != 8) {
            return false;
        }

	if (!satisfiesCompression (tifd, new int [] {1, 32773} )) {
            return false;
        }

	if (!satisfiesPhotometricInterpretation (tifd, 2)) {
            return false;
        }

	if (niso.getSamplesPerPixel () < 3) {
            return false;
        }

        return satisfiesResolutionUnit (tifd, new int [] {1, 2, 3} );
    }
}
