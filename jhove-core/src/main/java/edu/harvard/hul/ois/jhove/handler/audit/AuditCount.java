/**********************************************************************
 * Audit output handler
 * Copyright 2004 by the President and Fellows of Harvard College
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 **********************************************************************/

package edu.harvard.hul.ois.jhove.handler.audit;

/**
 * Count object for the JHOVE Audit output handler.
 */
public class AuditCount {

	/** Number of valid files. */
	protected int _valid;

	/** Number of well-formed files. */
	protected int _wellFormed;

	/** Number of not-well-formed files. */
	protected int _notWellFormed;

	/** Number of undetermined files. */
	protected int _undetermined;

	/**
	 * Instantiates an <code>AuditCount</code> object.
	 */
	public AuditCount() {
		_valid = 0;
		_wellFormed = 0;
		_notWellFormed = 0;
		_undetermined = 0;
	}

	/** Returns the total number of processed files. */
	public int getTotal() {
		return _valid + _wellFormed + _notWellFormed + _undetermined;
	}

	/** Returns the total number of valid files. */
	public int getValid() {
		return _valid;
	}

	/** Returns the total number of well-formed files. */
	public int getWellFormed() {
		return _wellFormed;
	}

	/** Returns the count of not-well-formed files. */
	public int getNotWellFormed() {
		return _notWellFormed;
	}

	/** Returns the count of undetermined files. */
	public int getUndetermined() {
		return _undetermined;
	}

	/** Sets the count of valid files. */
	public void setValid(int valid) {
		_valid = valid;
	}

	/** Sets the count of well-formed files. */
	public void setWellFormed(int wellFormed) {
		_wellFormed = wellFormed;
	}

	/** Sets the count of not-well-formed files. */
	public void setNotWellFormed(int notWellFormed) {
		_notWellFormed = notWellFormed;
	}

	/** Sets the count of undetermined files. */
	public void setUndetermined(int undetermined) {
		_undetermined = undetermined;
	}
}
