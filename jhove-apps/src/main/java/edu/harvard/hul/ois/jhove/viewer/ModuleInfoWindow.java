/**********************************************************************
 * JhoveView - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.viewer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.App;
import edu.harvard.hul.ois.jhove.Document;
import edu.harvard.hul.ois.jhove.Identifier;
import edu.harvard.hul.ois.jhove.InternalSignature;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.Module;
import edu.harvard.hul.ois.jhove.OutputHandler;
import edu.harvard.hul.ois.jhove.Signature;
import edu.harvard.hul.ois.jhove.SignatureType;

/**
 * This window is for presenting information about the selected module. If no
 * module is selected, a brief message is put into the window.
 */
public class ModuleInfoWindow extends InfoWindow {

	private JTextArea texta;
	private int _level;
	private Module _module;

	private static final String DATE = "Date: ";
	private static final String NOTE = "Note: ";
	private static final String TYPE = "Type: ";

	/**
	 * Constructor.
	 *
	 * @param app
	 *            The associated App object.
	 * @param base
	 *            The associated JhoveBase object.
	 * @param module
	 *            The Module whose information is to be presented.
	 */
	public ModuleInfoWindow(App app, JhoveBase base, Module module) {
		super("Module Information", app, base);
		_module = module;
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
		scrollpane.setMaximumSize(new Dimension(600, 500));
		scrollpane.setPreferredSize(new Dimension(600, 500));
		getContentPane().add(scrollpane, "Center");

		// Add a small panel at the bottom, since on some OS's there
		// may be stuff near the bottom of a window which will conflict
		// with the scroll bar.
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(8, 8));
		getContentPane().add(panel, "South");

		showModule(module);
		pack();
	}

	/**
	 * Formats and presents the module information in the window.
	 */
	public void showModule(Module module) {
		_module = module;
		if (module == null) {
			texta.setText("(No module selected)");
		} else {
			_level = 0;
			texta.setText("");
			String margin = getIndent(++_level);
			texta.append(margin + "Module: " + module.getName() + eol);
			texta.append(margin + "Release: " + module.getRelease() + eol);
			texta.append(margin + DATE + _dateFmt.format(module.getDate())
					+ eol);
			String[] ss = module.getFormat();
			if (ss.length > 0) {
				texta.append(margin + "Format: " + ss[0]);
				for (int i = 1; i < ss.length; i++) {
					texta.append(", " + ss[i]);
				}
				texta.append(eol);
			}

			String s = module.getCoverage();
			if (s != null) {
				texta.append(margin + "Coverage: " + s + eol);
			}
			ss = module.getMimeType();
			if (ss.length > 0) {
				texta.append(margin + "MIMEtype: " + ss[0]);
				for (int i = 1; i < ss.length; i++) {
					texta.append(", " + ss[i]);
				}
				texta.append(eol);
			}
			for (Signature sig : module.getSignature()) {
				showSignature(sig);
			}
			for (Document spec : module.getSpecification()) {
				showDocument(spec, "Specification");
			}
			texta.append(margin + " Features:\n");
			for (String feature : module.getFeatures()) {
				texta.append(margin + "  " + feature + "\n");
			}
			texta.append(margin + "Methodology:\n");
			if ((s = module.getWellFormedNote()) != null) {
				texta.append(margin + "Well-formed: " + s + eol);
			}
			if ((s = module.getValidityNote()) != null) {
				texta.append(margin + "Validity: " + s + eol);
			}
			if ((s = module.getRepInfoNote()) != null) {
				texta.append(margin + "RepresentationInformation: " + s + eol);
			}
			Agent vendor = module.getVendor();
			if (vendor != null) {
				showAgent(vendor, "Vendor");
			}
			if ((s = module.getNote()) != null) {
				texta.append(margin + NOTE + s + eol);
			}
			if ((s = module.getRights()) != null) {
				texta.append(margin + "Rights: " + s + eol);
			}
		}
		// Scroll to the top.
		texta.setEditable(false);
		texta.select(0, 0);
		Rectangle r = new Rectangle(0, 0, 1, 1);
		texta.scrollRectToVisible(r);
	}

	private void showSignature(Signature signature) {
		String margin = getIndent(++_level);

		String sigValue;
		if (signature.isStringValue()) {
			sigValue = signature.getValueString();
		} else {
			sigValue = signature.getValueHexString();
		}
		texta.append(margin + signature.getType().toString() + ": " + sigValue
				+ eol);
		if (signature.getType().equals(SignatureType.MAGIC)) {
			if (((InternalSignature) signature).hasFixedOffset()) {
				texta.append(margin + "Offset: "
						+ ((InternalSignature) signature).getOffset() + eol);
			}
		}
		String note = signature.getNote();
		if (note != null) {
			texta.append(margin + NOTE + note + eol);
		}
		String use = signature.getUse().toString();
		if (use != null) {
			texta.append(margin + "Use: " + use + eol);
		}
		--_level;
	}

	private void showDocument(Document document, String label) {
		String margin = getIndent(++_level);

		texta.append(margin + label + ": " + document.getTitle() + eol);
		texta.append(margin + TYPE + document.getType() + eol);
		for (Agent agent : document.getAuthor()) {
			showAgent(agent, "Author");
		}
		for (Agent publisher : document.getPublisher()) {
			showAgent(publisher, "Publisher");
		}
		String s = document.getEdition();
		if (s != null) {
			texta.append(margin + "Edition: " + s + eol);
		}
		if ((s = document.getDate()) != null) {
			texta.append(margin + DATE + s + eol);
		}
		if ((s = document.getEnumeration()) != null) {
			texta.append(margin + "Enumeration: " + s + eol);
		}
		if ((s = document.getPages()) != null) {
			texta.append(margin + "Pages: " + s + eol);
		}
		for (Identifier id : document.getIdentifier()) {
			showIdentifier(id);
		}
		if ((s = document.getNote()) != null) {
			texta.append(margin + NOTE + s + eol);
		}
		_level--;
	}

	private void showAgent(Agent agent, String label) {
		String margin = getIndent(++_level);

		texta.append(margin + label + ": " + agent.getName() + eol);
		texta.append(margin + TYPE + agent.getType().toString() + eol);
		String s = agent.getAddress();
		if (s != null) {
			texta.append(margin + "Address: " + s + eol);
		}
		if ((s = agent.getTelephone()) != null) {
			texta.append(margin + "Telephone: " + s + eol);
		}
		if ((s = agent.getFax()) != null) {
			texta.append(margin + "Fax: " + s + eol);
		}
		if ((s = agent.getEmail()) != null) {
			texta.append(margin + "Email: " + s + eol);
		}
		if ((s = agent.getWeb()) != null) {
			texta.append(margin + "Web: " + s + eol);
		}
		_level--;
	}

	private void showIdentifier(Identifier identifier) {
		String margin = getIndent(++_level);

		texta.append(margin + "Identifier: " + identifier.getValue() + eol);
		texta.append(margin + TYPE + identifier.getType().toString() + eol);
		String note = identifier.getNote();
		if (note != null) {
			texta.append(margin + NOTE + note + eol);
		}
		_level--;
	}

	private String getIndent(int lev) {
		switch (lev) {
		case 1:
			return " ";
		case 2:
			return "  ";
		case 3:
			return "   ";
		case 4:
			return "    ";
		default:
			return "";
		}
	}

	/**
	 * Saves the information to a file
	 */
	private void saveInfo() {
		if (_module == null) {
			JOptionPane.showMessageDialog(this, "No module selected",
					"Can't save", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		PrintWriter wtr = doSaveDialog();
		if (wtr == null) {
			return;
		}
		OutputHandler handler = selectHandler();
		try {
			handler.setWriter(wtr);
			handler.show(_module);
			wtr.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"Error writing file", JOptionPane.ERROR_MESSAGE);
		}
	}
}
