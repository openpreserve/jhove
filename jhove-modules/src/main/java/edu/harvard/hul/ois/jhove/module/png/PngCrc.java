package edu.harvard.hul.ois.jhove.module.png;

/** Accumulator for the CRC of a chunk. The CRC is calculated on
 *  the type and content of a chunk, but the length field is
 *  excluded.
 */
public class PngCrc {

	/* Static CRC table for driving calculations */
	private static long crcTable[];
	/* TODO insert a static block with the equivalent of the following C code:
	 * 
	 *    void make_crc_table(void)
   {
     unsigned long c;
     int n, k;
   
     for (n = 0; n < 256; n++) {
       c = (unsigned long) n;
       for (k = 0; k < 8; k++) {
         if (c & 1)
           c = 0xedb88320L ^ (c >> 1);
         else
           c = c >> 1;
       }
       crc_table[n] = c;
     }
     crc_table_computed = 1;
   }
   */
	private long crcVal;
	
	public PngCrc() {
		crcVal = 0;
	}
}
