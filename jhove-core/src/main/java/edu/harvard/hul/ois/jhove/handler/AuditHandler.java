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

package edu.harvard.hul.ois.jhove.handler;

import edu.harvard.hul.ois.jhove.Checksum;
import edu.harvard.hul.ois.jhove.ChecksumType;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.handler.audit.AuditCount;
import edu.harvard.hul.ois.jhove.handler.audit.AuditState;

import java.util.*;

/**
 * JHOVE audit output handler, derived from the standard JHOVE XML handler.
 * It is expected that this class will be used as the parent for other, more
 * interesting output handlers. Subclasses should override the implementations
 * of the Impl methods, e.g. endDirectoryImpl().
 */
public class AuditHandler extends XmlHandler {

	/** Audit output handler name. */
	private static final String NAME = "Audit";

	/** Audit output handler release ID. */
	private static final String RELEASE = "1.1";

	/** Audit output handler release date. */
	private static final int[] DATE = {2005, 4, 22};

	/** Audit output handler informative note. */
	private static final String NOTE =
			"This output handler is derived from the standard JHOVE XML output " +
			"handler. It is intended to be used as the parent class for other, " +
			"more interesting handlers.";

	/** Audit output handler rights statement. */
	private static final String RIGHTS =
			"Copyright 2004-2005 by the President and Fellows of Harvard College. " +
			"Released under the GNU LGPL license.";

	/** Home directory of the audit. */
	protected String _home;

	/** Number of files processed by MIME type. */
	protected Map<String, AuditCount> _mimeType;

	/** State map. */
	protected Map<String, AuditState> _stateMap;

	/** State stack. */
	protected Stack<AuditState> _stateStack;

	/** Initial time. */
	protected long _t0;

	/** Number of files audited. */
	protected int _nAudit;

	/**
	 * Instantiate an <code>AuditHandler</code> object.
	 */
	public AuditHandler() {
		super(NAME, RELEASE, DATE, NOTE, RIGHTS);

		// Define the standard output handler properties.
		_name = NAME;
		_release = RELEASE;
		Calendar calendar = new GregorianCalendar();
		calendar.set(DATE[0], DATE[1] - 1, DATE[2]);
		_date = calendar.getTime();
		_note = NOTE;
		_rights = RIGHTS;

		// Initialize the handler.
		_mimeType = new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder()));
		_stateMap = new TreeMap<>();
		_stateStack = new Stack<>();
		_nAudit = 0;
	}

	/**
	 * Callback indicating a directory is finished being processed. Pop the
	 * state stack and place the current directory file count into the directory
	 * hash.
	 */
	@Override
	public final void endDirectory() {
		AuditState state = _stateStack.pop();
		_stateMap.put(state.getDirectory(), state);

		endDirectoryImpl();
	}

	/**
	 * Local extension to the standard callback indicating a directory is
	 * finished being processed.
	 */
	public void endDirectoryImpl() {
	}

	/**
	 * Outputs the information contained in a RepInfo object.
	 *
	 * @param info Object representation information
	 */
	@Override
	public void show(RepInfo info) {
		AuditState state = _stateStack.peek();

		String mime = info.getMimeType();
		AuditCount count = _mimeType.get(mime);
		if (count == null) count = new AuditCount();

		if (info.getWellFormed() == RepInfo.TRUE) {
			if (info.getValid() == RepInfo.TRUE) {
				state.setValid(state.getValid() + 1);
				count.setValid(count.getValid() + 1);
			} else {
				state.setWellFormed(state.getWellFormed() + 1);
				count.setWellFormed(count.getWellFormed() + 1);
			}
		} else if (info.getWellFormed() == RepInfo.FALSE) {
			state.setNotWellFormed(state.getNotWellFormed() + 1);
			count.setNotWellFormed(count.getNotWellFormed() + 1);
		} else {
			state.setUndetermined(state.getUndetermined() + 1);
			count.setUndetermined(count.getUndetermined() + 1);
		}

		_mimeType.put(mime, count);

		showImpl(info);
	}

	/**
	 * Local extension to the standard callback that outputs the information
	 * contained in a RepInfo object.
	 *
	 * @param info  Object representation information
	 */
	public void showImpl(RepInfo info) {

		String status;
		switch (info.getWellFormed()) {

			case RepInfo.TRUE:
				if (info.getValid() == RepInfo.TRUE)
					status = "valid";
				else
					status = "well-formed";
				break;

			case RepInfo.FALSE:
				status = "not well-formed";
				break;

			default:
				status = "unknown";
		}

		// Retrieve the MD5 checksum, if available.
		String md5 = null;
		for (Checksum checksum : info.getChecksum()) {
			if (checksum.getType().equals(ChecksumType.MD5)) {
				md5 = checksum.getValue();
				break;
			}
		}

		if (_nAudit == 0) {
			String margin = getIndent(++_level);

			String[][] attrs = {{"home", _home}};
			_writer.println(margin + elementStart("audit", attrs));
		}

		String margn2 = getIndent(_level) + " ";

		String uri = info.getUri();

		// Change the URI to a relative path by removing the home
		// directory prefix.
		int n = uri.indexOf(_home);
		if (n == 0) {
			uri = uri.substring(_home.length() + 1);
		}

		String mime = info.getMimeType();

		String[][] attrs = {{"mime", mime}, {"status", status},	{"md5", md5}};
		_writer.println(margn2 + element("file", attrs, uri));
		_nAudit++;
	}

	/**
	 * Do the final output. This should be in a suitable format for including
	 * multiple files between the header and the footer, and the XML of the
	 * header and footer must balance out.
	 */
	@Override
	public void showFooter() {
		AuditState state = _stateStack.pop();
		if (state.getTotal() > 0) {
			_stateMap.put(state.getDirectory(), state);
		}

		showFooterImpl();

		_writer.println("<!-- Summary by MIME type:");
		_writer.println("<!-- [mime type]: [file count] " +
				"([valid],[well-formed],[not well-formed],[unknown])");
		int nTotal = 0;
		int nValid = 0;
		int nWellFormed = 0;
		int nNotWellFormed = 0;
		int nUndetermined = 0;

		for (Map.Entry<String, AuditCount> entry : _mimeType.entrySet()) {
			String mime = entry.getKey();
			AuditCount count = entry.getValue();
			int total = count.getTotal();
			int valid = count.getValid();
			int wellFormed = count.getWellFormed();
			int notWellFormed = count.getNotWellFormed();
			int undetermined = count.getUndetermined();
			if (mime == null) mime = "None";

			_writer.println(mime + ": " + total + " (" + valid + "," +
					wellFormed + "," + notWellFormed + "," + undetermined + ")");

			nTotal += total;
			nValid += valid;
			nWellFormed += wellFormed;
			nNotWellFormed += notWellFormed;
			nUndetermined += undetermined;
		}
		_writer.println("Total: " + nTotal + " (" + nValid + "," +
				nWellFormed + "," + nNotWellFormed + "," + nUndetermined + ")");
		_writer.println("-->");

		_writer.println("<!-- Summary by directory:");
		_writer.println("<!-- [directory]: [file count] " +
				"([valid],[well-formed],[not well-formed],[unknown])");
		nTotal = 0;
		nValid = 0;
		nWellFormed = 0;
		nNotWellFormed = 0;
		nUndetermined = 0;

		for (Map.Entry<String, AuditState> entry : _stateMap.entrySet()) {
			String directory = entry.getKey();
			state = entry.getValue();
			int total = state.getTotal();
			int valid = state.getValid();
			int wellFormed = state.getWellFormed();
			int notWellFormed = state.getNotWellFormed();
			int undetermined = state.getUndetermined();

			_writer.println(directory + ": " + total + " (" + valid + "," +
					wellFormed + "," + notWellFormed + "," + undetermined + ")");

			nTotal += total;
			nValid += valid;
			nWellFormed += wellFormed;
			nNotWellFormed += notWellFormed;
			nUndetermined += undetermined;
		}
		_writer.println("Total: " + nTotal + " (" + nValid + "," +
				nWellFormed + "," + nNotWellFormed + "," + nUndetermined + ")");
		_writer.println("-->");

		// Update the elapsed time.
		long dt = (System.currentTimeMillis() - _t0 + 999) / 1000;

		long ss = dt % 60;
		long dm = dt / 60;
		long mm = dm % 60;
		long hh = dm / 60;

		_writer.println("<!-- Elapsed time: " + hh + ":" +
				(mm > 9 ? "" : "0") + mm + ":" +
				(ss > 9 ? "" : "0") + ss + " -->");
		_writer.flush();
	}

	/**
	 * Local extension to the standard callback that does the final output. This
	 * should be in a suitable format for including multiple files between the
	 * header and the footer, and the XML of the header and footer must balance
	 * out.
	 */
	public void showFooterImpl() {
		if (_nAudit > 0) {
			String margin = getIndent(_level--);
			_writer.println(margin + elementEnd("audit"));
		}
		super.showFooter();
	}

	/**
	 * Do the initial output. This should be in a suitable format for including
	 * multiple files between the header and the footer, and the XML of the
	 * header and footer must balance out.
	 */
	@Override
	public void showHeader() {

		// Initialize the handler.
		_mimeType = new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder()));
		_stateMap = new TreeMap<>();
		_stateStack = new Stack<>();
		_nAudit = 0;

		_t0 = System.currentTimeMillis();

		// Instantiate a state object and initialize with the values
		// of the global configuration file.
		AuditState state = showHeaderImpl(".");
		_stateStack.push(state);
		_home = state.getDirectory();
	}

	/**
	 * Local extension to the standard callback that does the initial output.
	 * This should be in a suitable format for including multiple files between
	 * the header and the footer, and the XML of the header and footer must
	 * balance out.
	 *
	 * @param directory Current directory filepath
	 */
	public AuditState showHeaderImpl(String directory) {
		super.showHeader();
		return new AuditState(directory);
	}

	/**
	 * Callback indicating a new directory is being processed.
	 *
	 * Additional state information can be added to the AuditState object
	 * in the showHeaderImpl() method before it is pushed onto the stack.
	 */
	@Override
	public void startDirectory(String directory) {
		try {
			AuditState state = (AuditState)
					_stateStack.peek().clone(directory);

			startDirectoryImpl(state);
			_stateStack.push(state);
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	/**
	 * Local extension to the standard callback indicating a new directory is
	 * being processed.
	 *
	 * @param state Audit handler state
	 */
	public void startDirectoryImpl(AuditState state) {
	}
}
