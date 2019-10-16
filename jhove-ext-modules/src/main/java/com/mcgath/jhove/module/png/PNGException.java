package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;

/** Exception class specific to the PNG module */
public class PNGException extends Exception {
	
	/**
	 * Just to keep the compiler happy
	 */
	private static final long serialVersionUID = 1L;

	public PNGException (String msg) {
		
	}

	public PNGException(JhoveMessage msg) {
		super (msg.getMessage());
	}
}
