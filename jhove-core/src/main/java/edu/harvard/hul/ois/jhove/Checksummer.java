/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003-2006 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;
import java.security.*;
import java.util.zip.*;

/**
 *  The Checksummer class encapsulates the calculation of the
 *  CRC32, MD5, SHA-1 and SHA-256  checksums.
 */
public class Checksummer implements java.util.zip.Checksum
{
    /******************************************************************
     * PRIVATE INSTANCE FIELDS.
     ******************************************************************/

    /** Byte count. */
    protected long _nByte;
    /** CRC32 object. */
    private CRC32 _crc32;
    /** MD5 message digest. */
    private MessageDigest _md5;
    /** SHA-1 message digest. */
    private MessageDigest _sha1;
    /** SHA-256 message digest. */
    private MessageDigest _sha256;

    /**
     *  Creates a Checksummer, with instances of each of
     *  CRC32, MD5, SHA-1 and SHA-256 MessageDigest.
     *  If one or both of the MessageDigests aren't supported
     *  on the current platform, they are left as null.
     *
     *  @see CRC32
     *  @see MessageDigest
     */
    public Checksummer ()
    {
	reset ();
    }

    /** Resets all checksums and the byte count to their
     *  initial values.
     */
    @Override
    public void reset ()
    {
        _nByte = 0;
        _crc32 = new CRC32 ();
        try {
            _md5  = MessageDigest.getInstance ("MD5");
            _sha1 = MessageDigest.getInstance ("SHA-1");
            _sha256 = MessageDigest.getInstance ("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
          throw new IllegalStateException("Missing checksum algorithm.", e);
        }
    }

    /** getValue is required by the Checksum interface, but
     *  we can return only one of the three values.  We
     *  return the CRC32 value, since that's the one which
     *  is guaranteed to be available.
     */
    @Override
    public long getValue ()
    {
        return _crc32.getValue ();
    }

    /**
     *  Updates the checksum with the argument.
     *  Called when a signed byte is available.
     */
    public void update (byte b)
    {
	_crc32.update (b);
	if (_md5 != null) {
	    _md5.update (b);
	}
    if (_sha1 != null) {
        _sha1.update (b);
	}
    if (_sha256 != null) {
        _sha256.update (b);
	}
    }

    /**
     *  Updates the checksum with the argument.
     *  Called when an unsigned byte is available.
     */
    @Override
    public void update (int b)
    {
	byte sb;
	if (b > 127) {
	    sb = (byte) (b - 256);
	}
	else {
	    sb = (byte) b;
	}
	update (sb);
    }

    /**
     *  Updates the checksum with the argument.
     *  Called when a byte array is available.
     */
    public void update (byte[] b)
    {
	_crc32.update (b);
	if (_md5 != null) {
	    _md5.update (b);
	}
	if (_sha1 != null) {
	    _sha1.update (b);
	}
  if (_sha256 != null) {
       	    _sha256.update (b);
	}
    }

    /**
     *  Updates the checksum with the argument.
     *  Called when a byte array is available.
     */
    @Override
    public void update (byte[] b, int off, int len)
    {
	_crc32.update (b, off, len);
	if (_md5 != null) {
	    _md5.update (b, off, len);
	}
    if (_sha1 != null) {
	    _sha1.update (b, off, len);
	}
    if (_sha256 != null) {
	    _sha256.update (b, off, len);
	}
    }

    /**
     *  Returns the value of the CRC32 as a hex string.
     */
    public String getCRC32 ()
    {
	return padLeadingZeroes
            (Long.toHexString (_crc32.getValue ()), 8);
    }

    /**
     *  Returns the value of the MD5 digest as a hex string.
     *  Returns null if the digest is not available.
     */
    public String getMD5 ()
    {
      if (_md5 == null) {
        return null;
      }
      return convertToHex(_md5.digest());
    }

    /**
     *  Returns the value of the SHA-1 digest as a hex string.
     *  Returns null if the digest is not available.
     */
    public String getSHA1 ()
    {
      if (_sha1 == null) {
        return null;
      }
      return convertToHex(_sha1.digest());
    }

    /**
     *  Returns the value of the SHA-256 digest as a hex string.
     *  Returns null if the digest is not available.
     */
    public String getSHA256 ()
    {
      if (_sha256 == null) {
        return null;
      }
      return convertToHex(_sha256.digest());
	  }

    private String convertToHex(final byte [] digest) {
        StringBuffer buffer = new StringBuffer();
	      for (int i=0; i<digest.length; i++) {
		        int un = (digest[i] >= 0) ? digest[i] : 256 + digest[i];
		        buffer.append(padLeadingZeroes(Integer.toHexString(un), 2));
	      }
	      return buffer.toString();
    }

    /** Pad a hexadecimal (or other numeric) string out to
     *  the specified length with leading zeroes. */
    private static String padLeadingZeroes (String str, int len)
    {
        StringBuffer buff = new StringBuffer();
        int padLen = len - str.length();
        while (padLen > 0) {
          buff.append("0");
          padLen--;
        }
        buff.append(str);
        return buff.toString();
    }
    
    /**
     * Returns the hex string for the given byte array
     * (with uppercase letters)
     */
    public static String outputHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));  // (lower case: "%02x");
        }
        return result.toString();
        
        //alternative implementation:
        //return convertToHex(bytes).toUpperCase(Locale.US);
    }
}
