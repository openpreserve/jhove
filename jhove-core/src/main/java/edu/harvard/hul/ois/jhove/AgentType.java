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
	/** Commercial organisation **/
	COMMERCIAL("Commercial"),
	/** Commercial organisation **/
	GOVERNMENT("Government"),
	/** Educational Institution **/
	EDUCATIONAL("Educational"),
	/** Not for profit organisation **/
	NONPROFIT("Non-profit"),
	/** Standardisation body, e.g ANSI, ISO **/
	STANDARD("Standards body"),
	/** None of the above categories **/
	OTHER("Other");
	/** A String name for the type, used for reporting. */
	public final String name;

	private AgentType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
