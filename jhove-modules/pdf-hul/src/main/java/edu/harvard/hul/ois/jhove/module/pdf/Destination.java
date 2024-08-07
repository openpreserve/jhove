/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import java.io.IOException;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;
import edu.harvard.hul.ois.jhove.module.PdfModule;

/**
 * Class encapsulating PDF destination objects, which refer
 * to a page in the document.
 *
 * We need to make two different kinds of distinctions: between
 * destinations that make an indirect and a direct reference to
 * a page; and between destinations that have been reached by
 * a direct and an indirect reference. The PDF spec allows
 * only one level of indirection, so each of these forms has
 * options not available to the other.
 *
 * We call a destination which has been reached directly an
 * unnamed destination, and one which has been reached indirectly
 * a named destination. We call a destination which has an
 * indirect target an indirect destination, and one which has
 * a page object as a target a direct destination. Applying
 * the PDF documentation, we find that a destination can never
 * be both named and indirect. In other words, there are really
 * two cases, involving three kinds of destinations:
 *
 * <UL>
 * <LI>An unnamed, direct destination, which refers to the page
 * object.
 * <LI>An unnamed, indirect destination, which refers to a
 * named, direct destination, which refers to the page object.
 * </UL>
 */
public final class Destination {
	/******************************************************************
	 * PRIVATE CLASS FIELDS.
	 ******************************************************************/

	/* Flag indicating destination is indirect. */
	private boolean _indirect = false;

	/* Name of indirect destination. */
	private PdfSimpleObject _indirectDest;

	/* Page object for explicit destination. */
	private PdfDictionary _pageDest;

	/**
	 * Constructor. If this is a named destination, the destObj
	 * may be a PdfArray or a PdfDictionary; if this is not a
	 * named destination, the destObj may be a PdfSimpleObject
	 * (encapsulating a Literal or Name) or a PdfDictionary.
	 *
	 * @param destObj
	 *            The destination object
	 * @param module
	 *            The invoking PdfModule
	 * @param named
	 *            Flag indicating whether this object came
	 *            from a named destination.
	 */
	public Destination(final PdfObject destObj, final PdfModule module,
			final boolean named) throws PdfException, IOException {
    	if (destObj == null) {
    		throw new IllegalArgumentException("Parameter destObj cannot be null.");
    	}
		if (!named && destObj instanceof PdfSimpleObject) {
			_indirect = true;
			_indirectDest = (PdfSimpleObject) destObj;
			return;
		} else if (!named && destObj instanceof PdfIndirectObj) {
			_pageDest =  findDirectDest(module, (PdfArray) module.resolveIndirectObject(destObj));
			return;
		}
		PdfArray destArray = null;
		try {
			if (destObj instanceof PdfArray) {
				destArray = (PdfArray) destObj;
				// We extract only the page reference, not the view.
				_pageDest = findDirectDest(module, destArray);
			} else if (named && destObj instanceof PdfDictionary) {
				destArray = (PdfArray) ((PdfDictionary) destObj).get("D");
				// The D entry is just like the array above.
				_pageDest = findDirectDest(module, destArray);
			} else {
				throw new PdfInvalidException(MessageConstants.PDF_HUL_1); // PDF-HUL-1
			}
		} catch (ClassCastException e) {
			throw new PdfInvalidException(MessageConstants.PDF_HUL_2); // PDF-HUL-2
		} catch (IOException e) {
			JhoveMessage message = JhoveMessages.getMessageInstance(
					MessageConstants.PDF_HUL_3.getId(),
					String.format(MessageConstants.PDF_HUL_3.getMessage(), // PDF-HUL-3
							e.getLocalizedMessage(), Integer.valueOf(destArray._objNumber)));
			throw new PdfInvalidException(message);
		}
	}

	/**
	 * Returns <code>true</code> if the destination is indirect.
	 */
	public boolean isIndirect() {
		return _indirect;
	}

	/**
	 * Returns the string naming the indirect destination.
	 * Returns null if the destination is not indirect.
	 */
	public PdfSimpleObject getIndirectDest() {
		return _indirectDest;
	}

	/**
	 * Returns the page object dictionary if the destination
	 * is direct. Returns null if the destination is not
	 * direct.
	 */
	public PdfDictionary getPageDest() {
		return _pageDest;
	}

	/**
	 * Returns the object number of the page object dictionary
	 * if the destination is direct. Throws a NullPointerException
	 * otherwise.
	 */
	public int getPageDestObjNumber() throws NullPointerException {
		return _pageDest.getObjNumber();
	}

	private static PdfDictionary findDirectDest(final PdfModule module,
			final PdfArray destObj) throws PdfException, IOException {
		return (PdfDictionary) module
				.resolveIndirectObject(destObj.getContent().elementAt(0));
	}
}
