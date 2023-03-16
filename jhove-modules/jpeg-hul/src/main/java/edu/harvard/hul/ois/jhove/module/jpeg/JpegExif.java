/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.jpeg;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.ListIterator;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.NisoImageMetadata;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.JpegModule;
import edu.harvard.hul.ois.jhove.module.tiff.ExifIFD;
import edu.harvard.hul.ois.jhove.module.tiff.TiffIFD;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileExif;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileExifIFD;

/**
 * Reader of Exif data embedded in a JPEG App1 block.  This makes use
 * of the TIFF module, since an Exif stream is really an embedded TIFF
 * file; but it is designed to fail cleanly if the TIFF module is absent.
 * 
 * @author Gary McGath
 *
 */
public final class JpegExif {

    private boolean _exifProfileOK;
    private String _profileText;
    private NisoImageMetadata _exifNiso;
    private JpegModule module;
    
    public JpegExif(final JpegModule module) {
        this.module = module;
        _exifProfileOK = false;
        _profileText = null;
        _exifNiso = null;
    }

    /**
     * Checks if the TIFF module is available.
     */
    public static boolean isTiffAvailable() {
        try {
            Class.forName ("edu.harvard.hul.ois.jhove.module.TiffModule");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Reads the Exif data from the current point at the data stream,
     *  puts it into a temporary file, and makes a RepInfo object
     *  available.  This should be called only if isTiffAvailable()
     *  has returned <code>true</code>.
     */
    public RepInfo readExifData (DataInputStream dstream, JhoveBase je, 
            int length) {
        File tiffFile = null;
        RepInfo info = new RepInfo ("tempfile");
        /*
         * We're now at the beginning of the TIFF data.
	 * Copy it into a temporary file, then parse that
	 * as a TIFF file. 
	 */
        try {
            tiffFile = je.tempFile ();
        } catch (IOException e) {
            info.setMessage(new ErrorMessage(MessageConstants.JHOVE_SYS_1,
                     e.getMessage ()));
            return info;
        }
        try (FileOutputStream fos = new FileOutputStream (tiffFile)) {
            int bufSize = je.getBufferSize ();
            int tiffLen = length - 8;
            /* Set a default buffer size if the app doesn't specify one. */
            if (bufSize <= 0) {
                bufSize = 32768;
            }
            if (bufSize > tiffLen) {
                // can buffer whole file in one buffer
                bufSize = tiffLen;
            }
            try (BufferedOutputStream bos = new BufferedOutputStream (fos, bufSize)) {
	            byte[] buf = new byte[bufSize]; 
	            while (tiffLen > 0) {
	                //int len;
	                int sz;
	                if (tiffLen < bufSize) {
	                    sz = tiffLen;
                    } else {
	                    sz = bufSize;
	                }
	                sz = dstream.read (buf, 0, sz);
	                bos.write(buf, 0, sz);
	                tiffLen -= sz;
	            }
            }
            fos.flush ();
            edu.harvard.hul.ois.jhove.module.TiffModule tiffMod = new edu.harvard.hul.ois.jhove.module.TiffModule();
            tiffMod.setByteOffsetValid(true);
            // Now parse the file, using a special parsing method.
            // Close only after we're all done.
            List ifds = null;
            try (RandomAccessFile tiffRaf = new RandomAccessFile (tiffFile, "r")) {
	            ifds = tiffMod.exifParse (tiffRaf, info);
            }
            if (ifds == null) {
                return info;
            }
            
            // Locate the Exif IFD.  (We probably also want the
            // Interoperability IFD eventually.)
            ListIterator iter = ifds.listIterator();
            boolean first = true;
            boolean haveNisoMetadata = false;
            NisoImageMetadata niso = null;
            while (iter.hasNext()) {
                Object ifd = iter.next ();
                if (ifd instanceof TiffIFD && first) {
                    // The TIFF IFD has useful information, which gets put
                    // into its NISO metadata.  Make it available to the caller.
                    TiffIFD tifd = (TiffIFD)ifd;
                    niso = tifd.getNisoImageMetadata ();
                    // The first one is presumed to be the interesting one.
                    info.setProperty (new Property ("NisoImageMetadata",
                                           PropertyType.NISOIMAGEMETADATA,
                                           niso));
                    haveNisoMetadata = true;
                    TiffProfileExif exifProfile = new TiffProfileExif ();
                    _exifProfileOK = exifProfile.satisfiesProfile (tifd);
                    if (_exifProfileOK) {
                            _profileText = exifProfile.getText();
                    }
                }
                if (ifd instanceof ExifIFD) {
                    // Now for complicated stuff copying out the appropriate properties.
                    // Probably I just want to go through them and match interesting
                    // properties one by one, and copy them directly out.
                    // Or do I just want to copy the whole Exif property?
                    ExifIFD eifd = (ExifIFD) ifd;
                    Property ifdProp = eifd.getProperty( (je.getShowRawFlag ()));
                    List exifList = null;
                    if (ifdProp != null) {
                        exifList = eifd.exifProps (ifdProp);
                    }
                    if (first || _exifProfileOK) {
                        TiffProfileExifIFD exifIFDProfile = new TiffProfileExifIFD ();
                        _exifProfileOK = exifIFDProfile.satisfiesProfile(eifd);
                        if (!_exifProfileOK) {
                        	_profileText = null;
                        }
                    }
                    if (exifList != null) {
                        info.setProperty(new Property ("Exif",
                                PropertyType.PROPERTY,
                                PropertyArity.LIST,
                                exifList));
                    }
                    // See if we have any interesting NISO metadata. If so, and
                    // we haven't gotten real NISO metadata, use it.
                    if (!haveNisoMetadata) {
                        niso = eifd.getNisoImageMetadata ();
                        info.setProperty (new Property ("NisoImageMetadata",
                                PropertyType.NISOIMAGEMETADATA,
                                niso));
                    } else {
                    	// Get the exif version
                        _exifNiso = eifd.getNisoImageMetadata ();
                    }
                }
                first = false;
            }
        } catch (IOException e) {
            info.setMessage(new ErrorMessage(MessageConstants.JPEG_HUL_3,
		 e.getMessage ()));
            // Maybe should put this directly in the parent's
            // RepInfo, otherwise I have to copy the message afterwards.
        } finally {
            if (tiffFile != null) {
                try {
                    tiffFile.delete();
                } catch (Exception e) {
                }
            }
        }
        return info;
    }
    
    /**
     * Returns <code>true</code> if the Exif IFD is present and satisfies
     *  the profile requirements.
     */
    public boolean isExifProfileOK() {
        return _exifProfileOK;
    }

    /**
     *  Returns the text which describes the exif profile.
     */
    public String getProfileText() {
        return _profileText;
    }

    /**
     *  Returns the NisoImageMetadata from the Exif SubIFD
     */
	public NisoImageMetadata getExifNiso() {
		return _exifNiso;
	}
}
