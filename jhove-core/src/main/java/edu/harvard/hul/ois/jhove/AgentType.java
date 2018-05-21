/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

/**
 * This class defines enumerated types for an Agent.
 * Applications will not create or modify AgentTypes, but will
 * use one of the predefined AgentType instances COMMERCIAL, GOVERNMENT,
 * EDUCATIONAL, NONPROFIT, STANDARD, or OTHER.
 *
 * @see Agent
 */
public enum AgentType {
	/******************************************************************
	 * PUBLIC STATIC INSTANCES.
	 ******************************************************************/
	COMMERCIAL("Commercial"),
	GOVERNMENT("Government"),
	EDUCATIONAL("Educational"),
	NONPROFIT("Non-profit"),
	STANDARD("Standards body"),
	OTHER("Other");

	public final String name;

	/******************************************************************
	 * CLASS CONSTRUCTOR.
	 ******************************************************************/
	private AgentType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
