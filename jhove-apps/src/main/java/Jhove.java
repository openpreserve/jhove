
/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004-2007 by the President and Fellows of Harvard College
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

import edu.harvard.hul.ois.jhove.App;
import edu.harvard.hul.ois.jhove.ExitCode;
import edu.harvard.hul.ois.jhove.CoreMessageConstants;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.Module;
import edu.harvard.hul.ois.jhove.OutputHandler;
import picocli.CommandLine;

import java.util.logging.Level;
import java.util.logging.Logger;

//TODO  why is that never used?
//import picocli.CommandLine;
//import picocli.CommandLine.Option;
//import picocli.CommandLine.Parameters;

public class Jhove {
	/** Application name. */
	private static final String NAME = "Jhove";
	/** Logger for this class. */
	private static final Logger LOGGER = Logger.getLogger(Jhove.class.getCanonicalName());

	private Jhove() {
		throw new AssertionError("Should never enter private constructor");
	}

	private static final String NOT_FOUND = "' not found";
	private static final String HANDLER = "Handler '";

	/**
	 * MAIN ENTRY POINT.
	 */
	public static void main(String[] args) {
		// Make sure we have a satisfactory version of Java.
		String version = System.getProperty("java.vm.version");
		if (version.compareTo("1.8.0") < 0) {
			LOGGER.log(Level.SEVERE, CoreMessageConstants.EXC_JAVA_VER_INCMPT);
			System.exit(ExitCode.INCOMPATIBLE_VM.getReturnCode());
		}

		// Set up config
		JhoveConfig config = new JhoveConfig();
		// Parse the args passed
		new CommandLine(config).parseArgs(args);
		//TODO Carl can delete it if he doesn't want it
		// Test the use of -k for checksumming.
		/*
		 * System.out.println("-c opt is: <" + config.configFile + ">");
		 * System.out.println("-x opt is: <" + config.saxClass + ">");
		 * System.out.println("Checksum opt is: <" + config.isCheckum + ">");
		 * System.exit(0);
		 */

		try {
			// Initialize the application state object.
			App app = App.newAppWithName(NAME);

			JhoveBase je = new JhoveBase();
			// Only set the log level if a param value was assigned
			if (config.logLevel != null) {
				je.setLogLevel(config.logLevel);
			}
			je.init(config.configFile, config.saxClass);
			if (config.encoding == null) {
				config.encoding = je.getEncoding();
			}
			if (config.tempDir == null) {
				config.tempDir = je.getTempDirectory();
			}
			if (config.bufferSize < 0) {
				config.bufferSize = je.getBufferSize();
			}
			Module module = je.getModule(config.moduleName);
			if (module == null && config.moduleName != null) {
				LOGGER.log(Level.SEVERE, "Module '" + config.moduleName + NOT_FOUND);
				System.exit(ExitCode.ERROR.getReturnCode());
			}
			OutputHandler about = je.getHandler(config.aboutHandler);
			if (about == null && config.aboutHandler != null) {
				LOGGER.log(Level.SEVERE, HANDLER + config.aboutHandler + NOT_FOUND);
				System.exit(ExitCode.ERROR.getReturnCode());
			}
			OutputHandler handler = je.getHandler(config.handlerName);
			if (handler == null && config.handlerName != null) {
				LOGGER.log(Level.SEVERE, HANDLER + config.handlerName + NOT_FOUND);
				System.exit(ExitCode.ERROR.getReturnCode());
			}

			// Invoke the JHOVE engine.
			je.setEncoding(config.encoding);
			je.setTempDirectory(config.tempDir);
			je.setBufferSize(config.bufferSize);
			je.setChecksumFlag(config.checksum);
			je.setShowRawFlag(config.showRaw);
			je.setSignatureFlag(config.signature);
			je.dispatch(app, module, about, handler, config.outputFile, config.dirFileOrUri);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			e.printStackTrace(System.err);
			System.exit(ExitCode.ERROR.getReturnCode());
		}
	}
}
