/**********************************************************************
 * JhoveView - JSTOR/Harvard Object Validation Environment
 * Copyright 2003-2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.viewer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.harvard.hul.ois.jhove.App;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.Module;
import edu.harvard.hul.ois.jhove.OutputHandler;

/**
 * This window is for presenting information about the JHOVE application.
 */
public class AppInfoWindow extends InfoWindow {

	private JTextArea texta;

	public AppInfoWindow(App app, JhoveBase jbase) {
		super("JHOVE Information", app, jbase);
		setSaveActionListener(e -> saveInfo());

		texta = new JTextArea();
		texta.setColumns(72);
		JScrollPane scrollpane = new JScrollPane(texta);
		texta.setFont(new Font("sansserif", Font.PLAIN, 10));
		texta.setLineWrap(true);
		texta.setWrapStyleWord(true);
		// Getting Swing to accept what you want for dimensions
		// apparently requires setting as many dimension restrictions
		// as possible, and hoping it will pay attention to some
		// of them.
		scrollpane.setMinimumSize(new Dimension(240, 240));
		scrollpane.setMaximumSize(new Dimension(500, 250));
		scrollpane.setPreferredSize(new Dimension(500, 250));
		getContentPane().add(scrollpane, "Center");

		// Add a small panel at the bottom, since on some OS's there
		// may be stuff near the bottom of a window which will conflict
		// with the scroll bar.
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(8, 8));
		getContentPane().add(panel, "South");

		showApp(app, jbase);
		pack();

		// Scroll to the top.
		texta.setEditable(false);
		texta.select(0, 0);
		Rectangle r = new Rectangle(0, 0, 1, 1);
		texta.scrollRectToVisible(r);
	}

	private void showApp(App app, JhoveBase jbase) {
		String appName = app.getName();
		if (appName != null) {
			texta.append("Name: " + appName + eol);
		}
		String rel = app.getRelease();
		if (rel != null) {
			texta.append("Release: " + rel);
		}
		Date dt = app.getDate();
		if (dt != null) {
			texta.append("   " + _dateFmt.format(dt) + eol);
		}
		String configFile = jbase.getConfigFile();
		if (configFile != null) {
			texta.append("Configuration: " + configFile + eol);
		}
		String saxClass = jbase.getSaxClass();
		if (saxClass != null) {
			texta.append("SAX parser: " + saxClass + eol);
		}
		for (String moduleName : jbase.getModuleMap().keySet()) {
			Map<String, Module> moduleMap = jbase.getModuleMap();
			Module module = moduleMap.get(moduleName);
			texta.append(" Module: " + module.getName() + " "
					+ module.getRelease() + eol);
		}
		// Reporting Handlers makes no sense in the viewer app; skip
		String rights = app.getRights();
		if (rights != null) {
			texta.append(" Rights: " + rights + eol);
		}
	}

	/**
	 * Saves the information to a file
	 */
	private void saveInfo() {
		PrintWriter wtr = doSaveDialog();
		if (wtr == null) {
			return;
		}
		OutputHandler handler = selectHandler();
		try {
			handler.setWriter(wtr);
			handler.show(_app);
			wtr.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"Error writing file", JOptionPane.ERROR_MESSAGE);
		}
	}
}
