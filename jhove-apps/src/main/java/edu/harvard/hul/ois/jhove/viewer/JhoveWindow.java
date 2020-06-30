/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003-2005 by JSTOR and the President and Fellows of Harvard College
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

package edu.harvard.hul.ois.jhove.viewer;

import java.awt.Color;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.harvard.hul.ois.jhove.App;
import edu.harvard.hul.ois.jhove.Callback;
import edu.harvard.hul.ois.jhove.ConfigHandler;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.MacStuff;
import edu.harvard.hul.ois.jhove.Module;

/**
 * Main window of JHoveViewer application.
 */
public class JhoveWindow extends JFrame implements Callback, DropTargetListener {

	private App _app;
	private JhoveBase _base;
	private AppInfoWindow _appInfoWin;
	private ModuleInfoWindow _moduleInfoWin;
	private JMenu _moduleSubmenu;
	private JMenuItem _openFileItem;
	private JMenuItem _openURLItem;
	private JMenuItem _closeAllItem;
	private ButtonGroup _moduleGroup;
	private String syncStr = ""; // object just for synchronizing
	private boolean _rawOutput;
	private boolean _doChecksum;

	private ProgressWindow _progWind;
	private PrefsWindow _prefsWindow;

	private File _lastDir;
	private String _selectedModule;
	private ActionListener _moduleMenuListener;
	private JPanel logo;
	private ViewHandler _viewHandler;

	private static final String NEVER = "never";
	private static final String CONFIG_ERROR = "Config Error";

	/** Logger for a module class. */
	protected Logger _logger;

	public JhoveWindow(App app, JhoveBase base) {
		super("JHOVE");
		_logger = Logger.getLogger("edu.harvard.hul.ois.jhove.viewer");
		_app = app;
		_base = base;
		_moduleMenuListener = e -> _selectedModule = e.getActionCommand();

		_lastDir = null;
		_moduleGroup = new ButtonGroup();
		addMenus();
		Container rootPane = getContentPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Define a Comparator function for Modules
		Comparator<Module> modListComparator = (m1, m2) ->
				String.CASE_INSENSITIVE_ORDER.compare(
						m1.getName(), m2.getName());

		// Build combo box of available modules
		List<Module> moduleList = base.getModuleList();
		// Clone the list so we can display it in sorted order
		// without munging the app's preferred order
		List<Module> menuModuleList = new ArrayList<>(moduleList.size());
		menuModuleList.addAll(moduleList);
		menuModuleList.sort(modListComparator);
		JRadioButtonMenuItem modItem = null;
		String itemName = null;

		for (Module module : menuModuleList) {
			itemName = module.getName();
			modItem = new JRadioButtonMenuItem(itemName);
			modItem.setActionCommand(itemName);
			modItem.addActionListener(_moduleMenuListener);
			_moduleSubmenu.add(modItem);
			_moduleGroup.add(modItem);
		}

		logo = new JPanel();

		// Add the image, which should be in jhove-logo.gif in
		// the viewer directory
		URL logoURL = JhoveWindow.class.getResource("jhovelogo.png");
		if (logoURL != null) {
			ImageIcon icn = new ImageIcon(logoURL);
			icn.setDescription("JHOVE logo");
			setNormalBackground();
			JLabel logoLabel = new JLabel(icn);
			logo.add(logoLabel);
		}

		// Allow files to be dragged to the logo pane.
		DropTarget dt = new DropTarget(logo, this);

		rootPane.add(logo);
		pack();

		// Center main window
		setLocationRelativeTo(null);

		// Set up a companion progress window. This will
		// be hidden and displayed as needed.
		_progWind = new ProgressWindow(e -> _base.abort());
		_progWind.setLocationRelativeTo(null);

		// Set up a Handler which is tailored to this application.
		_viewHandler = new ViewHandler(this, _app, _base);
	}

	/** Set up the menu bar and menus. */
	private void addMenus() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		_openFileItem = new JMenuItem("Open file or directory...");
		fileMenu.add(_openFileItem);
		// The following allows accelerator modifier keys to be set which are
		// appropriate to the host OS
		_openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		_openFileItem.addActionListener(e -> pickAndAnalyzeFile());
		_openURLItem = new JMenuItem("Open URL...");
		fileMenu.add(_openURLItem);
		_openURLItem.addActionListener(e -> pickAndAnalyzeURL());

		_closeAllItem = new JMenuItem("Close all document windows");
		fileMenu.add(_closeAllItem);
		// Action listeners are added by document windows

		if (!MacStuff.isMacintosh()) {
			// Token attempt at Mac friendliness: the Exit item
			// in the File menu is redundant under OS X
			JMenuItem quitItem = new JMenuItem("Exit");
			fileMenu.add(quitItem);
			quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			quitItem.addActionListener(e -> System.exit(0));
		}

		JMenu editMenu = new JMenu("Configuration");
		editMenu.setMnemonic(KeyEvent.VK_C);
		menuBar.add(editMenu);
		_moduleSubmenu = new JMenu("Select module");
		editMenu.add(_moduleSubmenu);
		JRadioButtonMenuItem noModuleItem =
				new JRadioButtonMenuItem("Automatic");
		noModuleItem.setActionCommand("");
		noModuleItem.setSelected(true);
		noModuleItem.addActionListener(_moduleMenuListener);
		_moduleSubmenu.add(noModuleItem);
		_moduleSubmenu.addSeparator();
		_moduleGroup.add(noModuleItem);
		_selectedModule = "";
		// Modules will be added later

		JMenuItem editConfigItem = new JMenuItem("Edit configuration file...");
		editMenu.add(editConfigItem);
		editConfigItem.addActionListener(e -> openConfigWindow());

		JMenuItem prefItem = new JMenuItem("Edit temporary preferences...");
		editMenu.add(prefItem);
		prefItem.addActionListener(e -> {
			if (_prefsWindow == null) {
				_prefsWindow = new PrefsWindow(this);
				_prefsWindow.pack();
				_prefsWindow.setLocationRelativeTo(this);
			}
			_prefsWindow.saveAndShow();
		});

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(helpMenu);
		JMenuItem aboutModuleItem = new JMenuItem("About module");
		helpMenu.add(aboutModuleItem);
		aboutModuleItem.addActionListener(e -> showModuleInfo());
		JMenuItem aboutAppItem = new JMenuItem("About JHOVE");
		helpMenu.add(aboutAppItem);
		aboutAppItem.addActionListener(e -> showAppInfo());

		setJMenuBar(menuBar);
	}

	/** Set the normal background color. */
	private void setNormalBackground() {
		logo.setBackground(new Color(255, 255, 255));
	}

	/** Set the background color for drag-over. */
	private void setDragBackground() {
		logo.setBackground(new Color(180, 240, 140));
	}

	/**
	 * Here we let the user pick a file, then analyze it.
	 */
	public void pickAndAnalyzeFile() {
		// Only one thread can be associated with a JhoveBase.
		// Make sure we can't have concurrent threads.
		_openFileItem.setEnabled(false);
		_openURLItem.setEnabled(false);
		File file = null;
		synchronized (syncStr) {
			JFileChooser chooser = new JFileChooser();
			makeChooserOpaque(chooser);
			if (_lastDir != null) {
				chooser.setCurrentDirectory(_lastDir);
			}
			chooser.setDialogTitle("Pick a file or directory to analyze");
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int ok = chooser.showOpenDialog(this);
			if (ok == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				_lastDir = chooser.getCurrentDirectory();
				ParseThread thr = new ParseThread(this);
				thr.setFile(file);
				thr.setModule(getSelectedModule());
				thr.start();
				_base.setCurrentThread(thr);
			} else {
				_openFileItem.setEnabled(true);
				_openURLItem.setEnabled(true);
			}
		}
	}

	/**
	 * Makes a JFileChooser dialog treat Mac OS packages and applications
	 * as opaque entities. Has no effect on other platforms.
	 */
	public static void makeChooserOpaque(JFileChooser chooser) {
		// Apple TN 2042 LIES; we need to set both properties.
		chooser.putClientProperty("JFileChooser.appBundleIsTraversable",
				NEVER);
		chooser.putClientProperty("JFileChooser.packageIsTraversable", NEVER);
	}

	/**
	 * This method does the actual work of pickAndAnalyzeFile, called from a
	 * thread so it can run asynchronously.
	 */
	public void pickAndAnalyzeFile1(File file, Module module) {
		String name = file.getName();
		_base.resetAbort();
		_progWind.setDocName(name, false);
		_progWind.setProgressState(ProgressWindow.PROCESSING, false);
		_progWind.setByteCount(-1, true);
		_progWind.setVisible(true);

		try {
			List<File> files = new ArrayList<>(1);
			files.add(file);
			openAndParse(files, module);
		} catch (ThreadDeath d) {
			_openFileItem.setEnabled(true);
			_openURLItem.setEnabled(true);
			throw d;
		}
		_openFileItem.setEnabled(true);
		_openURLItem.setEnabled(true);
	}

	/** This is called to analyze a List of files. */
	public void pickAndAnalyzeFileList1(List<File> files, Module module) {
		if (files.isEmpty()) {
			return;
		}
		// Set up progress window for the first file
		File file = files.get(0);
		String name = file.getName();
		_base.resetAbort();
		_progWind.setDocName(name, false);
		_progWind.setProgressState(ProgressWindow.PROCESSING, false);
		_progWind.setByteCount(-1, true);
		_progWind.setVisible(true);

		try {
			openAndParse(files, module);
		} catch (ThreadDeath d) {
			_openFileItem.setEnabled(true);
			_openURLItem.setEnabled(true);
			throw d;
		}
		_openFileItem.setEnabled(true);
		_openURLItem.setEnabled(true);
	}

	/**
	 * Returns the list of files found by recursing through
	 * the given directory and all of its subdirectories.
	 */
	public static List<File> getFileList(File directory) {
		List<File> fileList = new ArrayList<>();
		File[] files = directory.listFiles();
		if (files != null) {
			// Walk through the directory
			for (File file : files) {
				if (file.isDirectory()) {
					// Continue through to subdirectory
					fileList.addAll(getFileList(file));
				} else {
					fileList.add(file);
				}
			}
		}
		return fileList;
	}

	/** Here we let the user pick a URL, then analyze it. */
	public void pickAndAnalyzeURL() {
		// There are multithreading issues which haven't been resolved.
		// Rather than do a serious rewrite of the code, it's sufficient
		// to make sure there can't be more than one file being processed
		// at a time.
		_openFileItem.setEnabled(false);
		_openURLItem.setEnabled(false);

		String uri = null;
		synchronized (syncStr) {
			String urlStr = (String) JOptionPane.showInputDialog(this,
					"Choose a URL to analyze", "Select URL",
					JOptionPane.PLAIN_MESSAGE, null, null, "http://");

			if (urlStr == null) {
				_openFileItem.setEnabled(true);
				_openURLItem.setEnabled(true);
				return; // user cancelled
			}
			uri = urlStr.trim();
		}
		ParseThread thr = new ParseThread(this);
		thr.setURI(uri);
		thr.setModule(getSelectedModule());
		thr.start();
	}

	/**
	 * This method does the actual work of pickAndAnalyzeURL, called from a
	 * thread so it can run asynchronously.
	 */
	public void pickAndAnalyzeURL1(String uri, Module module) {
		_progWind.setDocName(uri.toString(), false);
		_progWind.setProgressState(ProgressWindow.DOWNLOADING, false);
		_progWind.setContentLength(0, false);
		_progWind.setByteCount(0, true);
		_progWind.setVisible(true);
		try {
			_base.dispatch(_app, module, null, // AboutHandler
					_viewHandler, null, // output file
					new String[] { uri });
		} catch (Exception e) {
			reportError("Error processing URL", e.getMessage());
		}
		_progWind.setVisible(false);
		_openFileItem.setEnabled(true);
		_openURLItem.setEnabled(true);
	}

	/**
	 * Implementation of Callback.callback.
	 *
	 * @param selector
	 *            1 signifies update of byte count. 2 signifies change of URI.
	 *            Other values result in no action.
	 * @param parm
	 *            If selector = 1, must be a Long that evaluates to the number
	 *            of bytes processed to date. If selector = 2, must be a String
	 *            naming the object being processed. Will be truncated at the
	 *            left if longer than 64 characters.
	 */
	@Override
	public int callback(int selector, Object parm) {
		switch (selector) {
		case 1:
			long bytecnt = (Long) parm;
			_progWind.setByteCount(bytecnt, true);
			break;
		case 2:
			String name = (String) parm;
			if (name.length() > 48) {
				name = "..." + name.substring(name.length() - 48);
			}
			_progWind.setDocName(name, true);
			break;
		default:
			break;
		}
		return 0;
	}

	/**
	 * Sets the raw output flag. If set to true, raw numeric values are
	 * displayed; if false, explanatory text may be substituted.
	 */
	public void setRawOutput(boolean rawOutput) {
		_rawOutput = rawOutput;
	}

	/**
	 * Sets the checksum flag. If set to true, checksums are reported.
	 */
	public void setDoChecksum(boolean checksum) {
		_doChecksum = checksum;
	}

	private void openAndParse(List<File> files, Module module) {

		// Turn a list of files into an array of strings.
		String[] paths = new String[files.size()];
		Iterator<File> iter = files.iterator();
		for (int i = 0; iter.hasNext(); i++) {
			File fil = iter.next();
			paths[i] = fil.getAbsolutePath();
			if (!fil.exists()) {
				_progWind.setVisible(false);
				return; // shouldn't happen -- we just picked it!
			}
			if (!fil.canRead()) {
				_progWind.setVisible(false);
				reportError("File not readable", fil.getName());
				return;
			}
		}

		_base.setShowRawFlag(_rawOutput);
		_base.setChecksumFlag(_doChecksum);

		/*
		 * With the new defaults for the PDF module being maximum information,
		 * it no longer makes sense to set maximum verbosity, since that would
		 * make all parameter settings ineffective. In fact, verbosity may be a
		 * kludge now that the config file handles parameters.
		 */
		// if (module != null) {
		// module.setVerbosity (Module.MAXIMUM_VERBOSITY);
		// // A problem (which I think we've always had):
		// // If no particular module is specified, we don't
		// // set its verbosity as we should.
		// }

		// Parse formatted object.
		try {
			_base.dispatch(_app, module, null, // AboutHandler
					_viewHandler, null, // output file
					paths);
		} catch (Exception e) {
			// Do SOMETHING useful here.
			_logger.warning(e.toString());
		}

		_progWind.setVisible(false);
	}

	/** Open a configuration dialog. */
	private void openConfigWindow() {
		String configFile = _base.getConfigFile();
		ConfigHandler configHandler = new ConfigHandler();
		XMLReader parser = null;
		String saxClass = _base.getSaxClass();
		try {
			if (saxClass != null) {
				parser = XMLReaderFactory.createXMLReader(saxClass);
			} else {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setNamespaceAware(true);
				parser = factory.newSAXParser().getXMLReader();
			}
			// Need to do this carefully to keep all parsers happy
			File config = new File(configFile);
			String canonicalPath = config.getCanonicalPath();
			String fileURL = "file://";
			if (canonicalPath.charAt(0) != '/') {
				fileURL += '/';
			}
			fileURL += canonicalPath;
			parser.setContentHandler(configHandler);
			parser.setEntityResolver(configHandler);
			parser.setFeature("http://xml.org/sax/features/validation", true);
			parser.parse(fileURL);
		} catch (IOException e) {
			reportError(CONFIG_ERROR, "Cannot read configuration file");
			return;
		} catch (SAXException e) {
			reportError(CONFIG_ERROR, "SAX parser not found: " + saxClass);
			return;
		} catch (ParserConfigurationException e) {
			reportError(CONFIG_ERROR, "ParserConfigurationException");
		}
		ConfigWindow confWin = new ConfigWindow(this, new File(configFile),
				configHandler);
		confWin.setLocationRelativeTo(this);
		confWin.setVisible(true);
	}

	private void showModuleInfo() {
		Module module = getSelectedModule();
		if (_moduleInfoWin == null) {
			_moduleInfoWin = new ModuleInfoWindow(_app, _base, module);
		} else {
			_moduleInfoWin.showModule(module);
		}
		_moduleInfoWin.setLocationRelativeTo(this);
		_moduleInfoWin.setVisible(true);
	}

	private void showAppInfo() {
		if (_appInfoWin == null) {
			_appInfoWin = new AppInfoWindow(_app, _base);
		}
		_appInfoWin.setLocationRelativeTo(this);
		_appInfoWin.setVisible(true);
	}

	private Module getSelectedModule() {
		if ("".equals(_selectedModule)) {
			return null;
		}
		return _base.getModuleMap().get(_selectedModule.toLowerCase());
	}

	private void reportError(String title, String msg) {
		synchronized (syncStr) {
			JOptionPane.showMessageDialog(this, msg, title,
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/* DropTargetListener methods. */

	/**
	 * Invoked when the drag enters the component. Accepts the drag if it's a
	 * file which is being dragged, and changes the background color to give
	 * visual feedback.
	 */
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		DataFlavor[] flavors = dtde.getCurrentDataFlavors();
		if (dataFlavorOK(flavors)) {
			setDragBackground();
			dtde.acceptDrag(dtde.getDropAction());
		} else {
			dtde.rejectDrag();
		}
	}

	/**
	 * Invoked when the drag leaves the component. Restores the default
	 * background color.
	 */
	@Override
	public void dragExit(DropTargetEvent dte) {
		setNormalBackground();
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}

	/**
	 * Called when the thingy is dropped on the component. This causes the file
	 * to be opened. The default background color will be restored;
	 * theoretically this should already have happened, but Windows appears to
	 * require it be done here.
	 */
	@Override
	public void drop(DropTargetDropEvent dtde) {
		DataFlavor[] flavors = dtde.getCurrentDataFlavors();
		if (dataFlavorOK(flavors)) {
			dtde.acceptDrop(dtde.getDropAction());

			// Now get the file(s) and open it (them)
			Transferable thingy = dtde.getTransferable();
			try {
				List<File> fileList = (List<File>) thingy
						.getTransferData(DataFlavor.javaFileListFlavor);
				ParseThread thr = new ParseThread(this);
				thr.setFileList(fileList);
				thr.setModule(getSelectedModule());
				thr.start();
				_base.setCurrentThread(thr);
				dtde.dropComplete(true);
			} catch (Exception e) {
				// Really shouldn't happen
				dtde.dropComplete(false);
				return;
			}
		} else {
			dtde.rejectDrop();
		}
		setNormalBackground();
	}

	/**
	 * Called if the drop action changes during the drag (e.g., by changing the
	 * modifier keys). Does nothing, as we treat copy and move identically.
	 */
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {

	}

	/**
	 * Returns the "Close all document windows" menu item. This allows document
	 * windows to add themselves as listeners.
	 */
	protected JMenuItem getCloseAllItem() {
		return _closeAllItem;
	}

	/** Called to see if the DropTargetEvent's data flavor is OK */
	private boolean dataFlavorOK(DataFlavor[] flavors) {
		for (DataFlavor flavor : flavors) {
			if (flavor.isFlavorJavaFileListType()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * A local class for creating threads.
	 */
	class ParseThread extends Thread {

		private JhoveWindow _win;
		private String _uri;
		private File _file;
		private List<File> _fileList;
		private Module _module;

		/** Constructor. */
		protected ParseThread(JhoveWindow win) {
			_win = win;
		}

		/**
		 * The method invoked by running the thread. Analyzes the URI, file, or
		 * file list provided to this thread object.
		 */
		@Override
		public void run() {
			_base.resetAbort();
			try {
				if (_uri != null) {
					_win.pickAndAnalyzeURL1(_uri, _module);
				} else if (_file != null) {
					if (_file.isDirectory()) {
						_fileList = getFileList(_file);
					} else {
						_win.pickAndAnalyzeFile1(_file, _module);
					}
				}
				if (_fileList != null) {
					_win.pickAndAnalyzeFileList1(_fileList, _module);
				}
				_base.setCurrentThread(null);
			} catch (ThreadDeath d) {
				_progWind.setVisible(false);
				throw d;
			}
		}

		/**
		 * Designates a URI to parse. Only one of setURI, setFile, and
		 * setFileList should be called for a given thread.
		 */
		protected void setURI(String uri) {
			_uri = uri;
		}

		/**
		 * Designates a file to parse. Only one of setURI, setFile, and
		 * setFileList should be called for a given thread.
		 */
		protected void setFile(File file) {
			_file = file;
		}

		/**
		 * Designates a list of files to parse sequentially. Only one of setURI,
		 * setFile, and setFileList should be called for a given thread.
		 */
		protected void setFileList(List<File> fileList) {
			_fileList = fileList;
		}

		/**
		 * Set the module. This is called at the start of thread setup, in case
		 * the user changes the module selection while the thread's running.
		 */
		protected void setModule(Module module) {
			_module = module;
		}

	}
}
