/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import edu.harvard.hul.ois.jhove.module.PdfModule;
import java.io.*;

/**
 *  Abstract class for nodes of a PDF document tree.
 */
public abstract class DocNode 
{
    /** The PdfModule this node is associated with. */
    protected PdfModule _module;
    
    /** The parent node of this node. */
    protected PageTreeNode _parent;
    
    /** The dictionary which defines this node. */
    protected PdfDictionary _dict;  
    
    /** True if this node is a PageObject. */
    protected boolean _pageObjectFlag;
    
    /** Set to true when all subnodes of this node
     *  have been iterated through following a StartWalk. */
    protected boolean _walkFinished;

    /**
     *  Superclass constructor.
     *  @param module     The PdfModule under which we're operating
     *  @param parent     The parent node in the document tree;
     *                    may be null only for the root node
     *  @param dict       The dictionary object on which this node
     *                    is based
     */
    public DocNode (PdfModule module,
                PageTreeNode parent, 
                PdfDictionary dict) throws PdfMalformedException
    {
         if (dict == null) {
             throw new PdfMalformedException (MessageConstants.PDF_HUL_4); // PDF-HUL-4
         }
        _module = module;
        _parent = parent;
        _dict = dict;
    }
    
    /**
     *  Returns true if this node is a PageObject.
     */
    public boolean isPageObject ()
    {
        return _pageObjectFlag;
    }
    
    /**
     *  Initialize an iterator through the descendants of this node.
     */
    public abstract void startWalk ();

    /**
     *   Get the next PageObject which is under this node.  
     */
    public abstract PageObject nextPageObject () throws PdfMalformedException;

    /**
     *   Get the next DocNode which is under this node.
     *   All PageTreeNodes and PageObjects are eventually returned
     *   by walking through a structure with nextNode.
     */
    public abstract DocNode nextDocNode () throws PdfMalformedException;
    
    /**
     *   Returns the parent of this node.
     */
    public DocNode getParent ()
    {
        return _parent;
    }

    /**
     *  Returns the page object or page tree node dictionary from 
     *  which this object was constructed.
     */
    public PdfDictionary getDict ()
    {
	return _dict;
    }


    /**
     *  Get the Resources dictionary.  Either a PageTreeNode or
     *  a PageObject can have a Resources dictionary. Returns
     *  null if there is no Resources dictionary.  The object
     *  may be referenced indirectly.
     */
    public PdfDictionary getResources () throws PdfException
    {
        PdfObject resdict = _dict.get ("Resources");
        try {
            resdict = _module.resolveIndirectObject (resdict);
            return (PdfDictionary) resdict;
        }
        catch (ClassCastException | IOException f) {
            throw new PdfInvalidException(MessageConstants.PDF_HUL_5); // PDF-HUL-5
        }
    }

    /**
     *  Returns the dictionary of fonts within the node's Resources
     *  dictionary, if both exist.  Otherwise returns null.
     *  The dictionary will most often have indirect object
     *  references as values.  What is returned is not a
     *  Font dictionary, but rather a dictionary of Font
     *  dictionaries.
     */
    public PdfDictionary getFontResources () throws PdfException
    {
        PdfDictionary resdict = getResources ();
        if (resdict == null) {
            return null;
        }
        PdfObject fontdict = resdict.get("Font");
        try {
            fontdict = _module.resolveIndirectObject (fontdict);
            return (PdfDictionary) fontdict;
        }
        catch (ClassCastException | IOException e) {
            throw new PdfMalformedException
                    (MessageConstants.PDF_HUL_6); // PDF-HUL-6
        }
    }
    
    /**
     *  Get the MediaBox of this node.  MediaBox is an inheritable
     *  property, so it walks up the chain of ancestors if it doesn't
     *  contain one.  Returns null if none.  Throws a 
     *  PdfInvalidException if an invalid MediaBox is found.
     */
    public PdfArray getMediaBox () throws PdfInvalidException
    {
        PdfArray mbox = null;
        try {
            mbox = (PdfArray) get ("MediaBox", true);
        }
        catch (ClassCastException e) {
            throw new PdfInvalidException (MessageConstants.PDF_HUL_7); // PDF-HUL-7
        }
        if (mbox != null && mbox.toRectangle () == null) {
            // There's a MediaBox, but it's not a rectangle
            throw new PdfInvalidException (MessageConstants.PDF_HUL_8); // PDF-HUL-8
        }
        return mbox;
    }

    /**
     *  Get an named property.  If this object doesn't
     *  have the specified property and <code>inheritable</code>
     *  is true, walks up the chain of ancestors
     *  to try to find one.  If no ancestor has the property or
     *  inheritable is false, returns null.
     */
    public PdfObject get (String key, boolean inheritable) 
    {
	PdfObject val = _dict.get (key);
	if (val == null) {
	    if (_parent == null || !inheritable) {
		return null;
	    }
        return _parent.get (key, inheritable);
	}
    return val;
    }
}
