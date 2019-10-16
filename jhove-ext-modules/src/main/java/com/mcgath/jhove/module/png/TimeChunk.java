package com.mcgath.jhove.module.png;

import java.util.Calendar;
import java.util.TimeZone;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The tIME chunk, which gives the latest modified time. */
public class TimeChunk extends PNGChunk {

	/** Constructor */
	public TimeChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = false;
	}
	
	/** The tIME chunk has the following: 2 bytes year (short),
	 *  1 byte month, 1 byte day, 1 byte hour, 1 byte minute,
	 *  1 byte second. It's supposed to be the UTC time. */
	@Override
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon(info);
		if (length < 7) {
			ErrorMessage msg = new ErrorMessage(MessageConstants.PNG_GDM_56);
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (MessageConstants.PNG_GDM_57);
		}
		int year = readUnsignedShort();
		int month = readUnsignedByte();
		int day = readUnsignedByte();
		int hour = readUnsignedByte();
		int minute = readUnsignedByte();
		int second = readUnsignedByte();
		// Sanity checks.
		if (month == 0 || month > 12 ||
				day == 0 || day > 31 ||
				hour > 23 ||
				minute > 59 ||
				second > 60) {		// 60 can be valid with a leap second
			ErrorMessage msg = new ErrorMessage (MessageConstants.PNG_GDM_58);
			info.setMessage(msg);
			info.setValid(false);		// just call this invalid, not ill-formed
			return;
		}
		// Java Calendar is based January as 0.
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(year,
				month - 1,
				day,
				hour,
				minute,
				second);
		Property prop = new Property ("Time modified",
				PropertyType.DATE,
				cal.getTime());
		_propList.add(prop);
	}
}
