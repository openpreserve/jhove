/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import edu.harvard.hul.ois.jhove.module.PdfModule;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 *  Class for PDF document structure tree.
 *  See section 9.6, "Logical Structure," of PDF Reference,
 *  Version 1.4, for an explanation of the document structure.
 *
 *  @see StructureElement
 */
public class StructureTree 
{    
    /** Logger for this class. */
    protected Logger _logger;
    
    public static final int MAX_PAGE_TREE_DEPTH = 100;

    private PdfModule _module;
    private PdfDictionary _rootDict;
    private PdfDictionary _roleMap;
    private boolean _present;
    private boolean _valid;
    private boolean _transient;

    /**
     *  Constructor. If there is a document structure tree,
     *  this fills in the appropriate information. If there isn't,
     *  it does nothing. Call {@code isPresent} to determine whether
     *  there is a document structure tree. A {@code PdfInvalidException}
     *  may be thrown if there is a structure tree but it is invalid.
     *
     *  @param module     The PdfModule under which we're operating
     */
    public StructureTree (PdfModule module)
        {
            this (module, false);
        }
    
    /**
     *  Constructor with transient flag. Calling this can save a lot of memory
     *  if the tree is being validated as it's built (which happens to be the
     *  only case, but I don't want to throw out the more general code).
     */
    public StructureTree (PdfModule module,
            boolean tranzhent)  {
        _logger = Logger.getLogger ("edu.harvard.hul.ois.jhove.module");
        
        _module = module;
        _transient = tranzhent;
        //_raf = raf;
        //_parser = parser;
        try {
            PdfDictionary docCatDict = module.getCatalogDict ();
    	    // There must be an entry in the catalog dictionary
    	    // named StructTreeRoot.  If there isn't, set _present
    	    // to false.
           _rootDict = null;
            try {
            _rootDict = (PdfDictionary) _module.resolveIndirectObject
                (docCatDict.get ("StructTreeRoot"));
            }
            catch (IOException e) {}
            if (_rootDict == null) {
                _present = false;
                _valid = false;
                return;
    	    }
            _present = true;
            validateRoot ();
            checkRoleMap ();
            checkChildren ();
            _valid = true; 
    	}
        catch (Exception e) {
            _valid = false;
        }
    }
    
    /**
     *  Returns <code>true</code> if and only if the document
     *  structure exists. 
     */
    public boolean isPresent ()
    {
        return _present;
    }


    /**
     *   Returns <code>true</code> if and only if no errors were
     *   detected.
     */
    public boolean isValid ()
    {
        return _valid;
    }


    /** Returns the module associated with this object. */
    public PdfModule getModule ()
    {
        return _module;
    }

    protected boolean isTransient () {
        return _transient;
    }

    /**
     *  Dereference a name in the role map.
     *  If there is no role map, or if the parameter is not
     *  mapped by the role map, the original parameter will
     *  be returned.  The string will be looked up through
     *  multiple levels in the role map.  The maximum number
     *  of levels is limited to 50, in case of circular
     *  mappings.  The value returned will be null if the
     *  role map contains invalid data or the limit of 50
     *  lookups is reached.
     */
    public String dereferenceStructType (String st) 
    {
        if (_roleMap == null) {
            return st;
        }
        // There could be a circular mapping, so we limit the
        // number of concatenated lookups.
        for (int i = 0; i < 50; i++) {
            try {
                PdfSimpleObject mapped = 
                    (PdfSimpleObject) _roleMap.get (st);
                if (mapped == null) {
                    return st;
                }
                st = mapped.getStringValue ();
            }
            catch (Exception e) {
                return null;  // BAD dictionary! No mapping!
            }
        }
        return null;    // Looks like an infinite loop
    }


    /* See if the root is valid.  If not, throw a PDFException. */
    private void validateRoot () throws PdfException
    {
        try {
            PdfSimpleObject typ = 
        	(PdfSimpleObject)_rootDict.get ("Type");
            if (!"StructTreeRoot".equals (typ.getStringValue ())) {
                throw new PdfInvalidException (MessageConstants.PDF_HUL_59); // PDF-HUL-59
            }
        }
        catch (NullPointerException | ClassCastException e) {
            throw new PdfInvalidException (MessageConstants.PDF_HUL_60); // PDF-HUL-60
        }
    }

    /**
     *  Replaces a string with a string to which the role map
     *  maps it.  This may involve multiple levels of lookup.
     */

    /* Build a list of the children of the root. The
       elements of the list are StructureElements.
       Returns null if there are none. */
    private void checkChildren () throws PdfException
    {
	List<StructureElement> kidsList = null;
	PdfObject kids = null;
	try {
	    kids = _module.resolveIndirectObject
		(_rootDict.get ("K"));
	}
	catch (IOException e) {}
	if (kids == null) {
	    return;
	}

	if (kids instanceof PdfDictionary) {
	    // Only one child
	    StructureElement se = new StructureElement 
		((PdfDictionary) kids, this);
		se.buildSubtree(MAX_PAGE_TREE_DEPTH);
	    se.checkAttributes ();
	    return;
	}
	else if (kids instanceof PdfArray) {
	    // Multiple children
	    Vector<PdfObject> kidsVec = ((PdfArray) kids).getContent ();
	    kidsList = new ArrayList<> (kidsVec.size ());
	    for (int i = 0; i < kidsVec.size (); i++) {
		PdfObject kid;
		try {
		    kid = _module.resolveIndirectObject
			(kidsVec.elementAt (i));
		}
		catch (IOException e) {
		    throw new PdfMalformedException (MessageConstants.PDF_HUL_61); // PDF-HUL-61
		}
		StructureElement se = new StructureElement 
			((PdfDictionary) kid, this);
		se.buildSubtree(MAX_PAGE_TREE_DEPTH);
		se.checkAttributes ();
		kidsList.add (se);
	    }
	}
	else {
	    throw new PdfInvalidException (MessageConstants.PDF_HUL_62); // PDF-HUL-62
	}
    }


    /*  Extract and save the role map, if any.  Throw an
        exception if RoleMap names something that isn't
        a dictionary. It's legitimate for roleMap to be null. */
    private void checkRoleMap () throws PdfException
    {
	try {
	    _roleMap = (PdfDictionary) _module.resolveIndirectObject
		(_rootDict.get ("RoleMap"));
	}
	catch (Exception e) {
	    throw new PdfInvalidException (MessageConstants.PDF_HUL_63); // PDF-HUL-63
	}
    }

}
