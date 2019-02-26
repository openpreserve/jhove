/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.xml;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.xml.sax.ext.LexicalHandler;

/**
 * 
 * This implementation of LexicalHandler takes care of
 * comments, DTD's, entities and other stuff for XmlModule.
 * The caller has to make sure the LexicalHandler property
 * is supported by the SAX implementation, and set that
 * property to this class.
 * 
 * @author Gary McGath
 *
 */
public class XmlLexicalHandler implements LexicalHandler {

    private List<String> _comments;
    private Set<String> _entityNames;
    public XmlLexicalHandler ()
    {
        _comments = new LinkedList<> ();
        _entityNames = new HashSet<> ();
    }
    
    
    
    /**
     * Report the end of a CDATA section.
     * Does nothing.
     * @see org.xml.sax.ext.LexicalHandler#endCDATA()
     */
    @Override
	public void endCDATA() {
        // no action necessary
    }

    /**
     * Report the end of DTD declarations.
     * Does nothing.
     * @see org.xml.sax.ext.LexicalHandler#endDTD()
     */
    @Override
	public void endDTD() {

    }

    /** 
     * Report the start of a CDATA section.
     * Does nothing.
     * @see org.xml.sax.ext.LexicalHandler#startCDATA()
     */
    @Override
	public void startCDATA() {
        // no action necessary
    }

    /**
     * Gathers comments into the comments list.
     * 
     * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
     */
    @Override
	public void comment(char[] text, int start, int length) {
        _comments.add (String.copyValueOf (text, start, length));
    }

    /**
     * Accumulates entity names into the entity set.  This will be
     * used for determining which entities are actually used.
     * 
     * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
     */
    @Override
	public void startEntity(String name) 
    {
        _entityNames.add (name);
    }

    /**
     * Report the end of an entity.
     * Does nothing.
     * 
     * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
     */
    @Override
	public void endEntity(String name) 
    {
        // No action necessary
    }


    /**
     * Report the start of DTD declarations, if any.
     * Does nothing.
     * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
	public void startDTD(String arg0, String arg1, String arg2) 
    {

    }


    /**
     *  Returns the value of the comments list, which is
     *  a List of Strings.
     */
    public List<String> getComments () 
    {
        return _comments;
    }
    
    
    /**
     *  Returns the Set of entity names.
     */
    public Set<String> getEntityNames ()
    {
        return _entityNames;
    }
}
