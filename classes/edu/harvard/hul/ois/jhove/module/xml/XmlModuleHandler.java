/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004-2007 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.xml;

//import java.io.*;
import edu.harvard.hul.ois.jhove.*;
import edu.harvard.hul.ois.jhove.module.html.HtmlMetadata;
import edu.harvard.hul.ois.jhove.module.html.DTDMapper;
import java.util.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.Attributes;

/**
 * 
 * This handler does the parsing work of the XML module.
 * 
 * @author Gary McGath
 *
 */
public class XmlModuleHandler extends DefaultHandler {

    /* List of entities String[2], { public ID, system ID} */
    private List _entities;
    
    /* Map of namespaces, prefix (String) to URI (String) */
    private Map _namespaces;
    
    /* List of processing instructions.  Each element
     * is an array of two strings, giving the target
     * and data respectively. */
    private List _processingInsts;
    
    /* List of generated Messages. */
    private List _messages;

    /* Validity flag. */
    private boolean _valid;
    
    /* Qualified name of the root element. */
    private String _root;
    
    /* URI for DTD specificiation */
    private String _dtdURI;
    
    /* List of schema URI's.  Each element is a String[2],
     * consisting of the namespace URI and the schema location. */
    private List _schemas;
    
    /* List of unparsed entities. Each is an array String[4];
     * name, public ID, system ID and notation name
     * respectively. */
    private List _unparsedEntities;
    
    /* Error counter. */
    private int _nErrors;
    
    /* Notations list. Each is an array String[3]: 
     * name, public ID, and system ID. */
    private List _notations;
    
    /* List of all the attributes.  This is used to
     * check on the use of unparsed entities. */
    private Set _attributeVals;
    
    /* Limit on number of errors to report. */
    private static final int MAXERRORS = 2000;

    /* XHTML flag, only for XHTML documents referred
     * by the HTML module. */
    private boolean _xhtmlFlag;
    
    /* HTMLMetadata object; used only with XHTML documents. */
    private HtmlMetadata _htmlMetadata;

    /* Flag set if we've seen any components.  This is an indirect
     * way of checking if the "signature" (the XML declaration)
     * has been seen. */
    private boolean _sigFlag;

    
    /**
     *  Constructor.
     */
    public XmlModuleHandler ()
    {
        _xhtmlFlag = false;
        _htmlMetadata = null;
        _entities = new LinkedList ();
        _namespaces = new HashMap ();
        _processingInsts = new LinkedList ();
        _messages = new LinkedList ();
        _attributeVals = new HashSet ();
        _dtdURI = null;
        _root = null;
        _valid = true;
        _nErrors = 0;
        _schemas = new LinkedList ();
        _unparsedEntities = new LinkedList ();
        _notations = new LinkedList ();
        _sigFlag = false;
    }
    
    
    /**
     *  Sets the value of the XHTML flag.  Special properties
     *  are extracted if this is an XHTML document. */
    public void setXhtmlFlag (boolean flag)
    {
        _xhtmlFlag = flag;
    }
    
    /**
     *  Returns the HTML metadata object.  Will be non-null only
     *  for a document recognized as XHTML. 
     */
    public HtmlMetadata getHtmlMetadata ()
    {
        return _htmlMetadata;
    }
    
    /**
     *  Looks for the first element encountered.  Stores
     *  its name as the value to be returned by getRoot,
     *  qualified name by preference, local name if the
     *  qualified name isn't available.
     */
    public void startElement (String namespaceURI,
                String localName,
                String qualifiedName,
                Attributes atts) throws SAXException
    {
        // The first element we encounter is the root.
        // Save it.
        if (_root == null) {
            _sigFlag = true;
            if (!"".equals(qualifiedName)) {
                _root = qualifiedName;
            }
            else {
                _root = localName;
            }
        }
        if (atts != null) {
            int natts = atts.getLength ();
            for (int i = 0; i < natts; i++) {
                String name = atts.getLocalName (i);
                String namespace = atts.getURI (i);  // namespace URI
                String val = atts.getValue (i);
                if ("http://www.w3.org/2001/XMLSchema-instance".equals
                        (namespace)) {

                    String[] schArr = new String[2];
                    if ("schemaLocation".equals (name)) {
                        /* val should consist of two tokens, giving the
			 * URI and the location respectively.
			 */
                        String[] toks = val.split ("\\s", 2);
                        /* Could be a length 0 or 1 array in pathological
			 * cases, so convert it to a length-2 array.
			 * Note that while the schemaLocation attribute
			 * SHOULD have two white-space separated elements,
			 * this may not be the case, so always check the
			 * array length before referencing its elements.
			 */
                        if (toks.length > 0 && toks[0] != null) {
                            schArr[0] = toks[0].trim ();
                        }
                        else {
                            schArr[0] = "";
                        }
                        if (toks.length > 1 && toks[1] != null) {
                            schArr[1] = toks[1].trim ();
                        }
                        else {
                            schArr[1] = "";
                        }
                        _schemas.add (schArr);
                    }
                    if ("noNamespaceSchemaLocation".equals (name)) {
                        schArr[0] = "[None]";
                        schArr[1] = val;
                        _schemas.add (schArr);
                    }
                }
                // Collect all attribute values.
                _attributeVals.add (val);
            }
        }
        if (_xhtmlFlag) {
            if (_htmlMetadata == null) {
                _htmlMetadata = new HtmlMetadata ();
            }
            XhtmlProcessing.processElement 
                (localName, qualifiedName, atts, _htmlMetadata);
        }
    }
    
    
    /** The only action taken here is some bookkeeping in connection
     *  with the HTML metadata.*/
    public void endElement(String namespaceURI, String localName, String qName)
    {
        if (_htmlMetadata != null) {
            _htmlMetadata.finishPropUnderConstruction ();
        }
    }
    
    /** Processes PCData characters.  This does things only
     *  in connection with properties under construction in
     *  HTML metadata.
     */
    public void characters(char[] ch, int start, int length)
    {
        if (_htmlMetadata != null && 
                 _htmlMetadata.getPropUnderConstruction () != null) {
            _htmlMetadata.addToPropUnderConstruction 
                        (ch, start, length);
        }
    }
    
    /**
     *  Begin the scope of a prefix-URI Namespace mapping.
     *  Prefixes mappings are stored in _namespaces.
     */
    public void startPrefixMapping(String prefix,
                                   String uri)
                            throws SAXException
    {
        //THL we want the root namespace even if it declares no prefix !!!
        //if (!"".equals (prefix)) {
            _namespaces.put(prefix, uri);
        //}
    }
    
    
    /**
     *   Handles a processing instruction.  Adds it to
     *   the list that will be returned by <code>getProcessingInstructions</code>.
     *   Each element of the list is an array of two Strings.  Element 0 of 
     *   the array is the target, and element 1 is the data.
     */
    public void processingInstruction(String target,
                                      String data)
                               throws SAXException
    {
        _sigFlag = true;
        if (data == null) {
            data = "";
        }
        String[] pi = new String[2];
        pi[0] = target;
        pi[1] = data;
        _processingInsts.add (pi);
    }
    
    
    /**
     *  Puts all notations into the notation list.  A list entry
     *  is a String[3], consisting of name, public ID, and system
     *  ID. 
     */
    public void notationDecl (String name, String publicID, String systemID)
            throws SAXException
    {
        String[] notArr = new String[3];
        notArr[0] = name;
        notArr[1] = publicID;
        notArr[2] = systemID;
        _notations.add (notArr);
    }
    
    /** Overrides standard resolveEntity.  First looks for DTD and
     *  entity files that are stored as resources, and uses those
     *  if available.  (Faster and more reliable than grabbing them
     *  over the Net.)  If that fails, calls the superclass's resolveEntity.
     *  Regardless, it then looks for anything
     *  that appears to be a DTD and puts it in the DTD URI field. 
     *  If the superclass's attempt to resolve the entity results in
     *  an IOException, we just ignore it.
     * 
     */  
    public InputSource resolveEntity(String publicId,
                                     String systemId)
                              throws SAXException

    {
        // Do special-case checking for the XHTML DTD's
        if (!_xhtmlFlag) {
            if (DTDMapper.isXHTMLDTD (publicId)) {
                _xhtmlFlag = true;
            }
        }
        InputSource ent = DTDMapper.publicIDToFile(publicId);
        if (ent == null) {
        	try {
	            ent = super.resolveEntity(publicId, systemId);
        	}
        	catch (SAXException ee) {
        		throw ee;
        	}
        	catch (Exception e) {
        		// Depending on the JDK version, super.resolveEntity
        		// may or may not be formally capable of throwing an IOException.
        		// This hack allows compatibility in either case.
        		throw new SAXException (e);
        	}
        }
        else {
            // A little magic so SAX won't give up in advance on
            // relative URI's.
            ent.setSystemId ("http://hul.harvard.edu/hul");
        }
        
        // Report in entity properties
        String[] entArr = new String[2];
        entArr[0] = publicId;
        entArr[1] = systemId;
        _entities.add (entArr);
        if (systemId.endsWith (".dtd")) {
	    /* Assume that the first system ID in the file with a .dtd
	     * extension is the actual DTD
	     */
	    if (_dtdURI == null) {
		_dtdURI = systemId;
	    }
        }
        return ent;
    }
    
                                   
    /**
     *  Picks up unparsed entity declarations, after calling the 
     * superclass's unparsedEntityDecl, and puts their information
     * into the unparsed entity declaration list as an array of
     * four strings: [ name, publicId, systemId, notationName].
     * Null values are converted into empty strings.
     */
    public void unparsedEntityDecl (String name,
              String publicId,
              String systemId,
              String notationName) throws SAXException 
    {
        super.unparsedEntityDecl (name, publicId, systemId, notationName);
        String[] info = new String[4];
        info[0] = name == null ? "" : name;
        info[1] = publicId == null ? "" : publicId;
        info[2] = systemId == null ? "" : systemId;
        info[3] = notationName == null ? "" : notationName;
        _unparsedEntities.add (info);
    }
                                   
    /**
     *  Processes a warning.  We just add an InfoMessage.
     */
    public void warning (SAXParseException e)
    {
        _messages.add (new InfoMessage (e.getMessage()));
    }
    
    
    
    /**
     *  Processes a parsing exception.  An ill-formed piece
     *  of XML will get a fatalError (I think), so we can assume
     *  that any error here indicates only invalidity.
     */
    public void error(SAXParseException e)
    {
       _valid = false;
        if (_nErrors == MAXERRORS) {
            _messages.add (new InfoMessage
                ("Error messages in excess of " + MAXERRORS +
                 " not reported"));
        }
        else if (_nErrors < MAXERRORS) {
            int line = e.getLineNumber();
            int col = e.getColumnNumber();
            _messages.add (new ErrorMessage 
                (e.getMessage ().toString (), 
                 "Line = " + line +
                 ", Column = " + col));
        }
        ++_nErrors;
     }
    
    
    /**
     *  Returns the set of attribute values.
     */
    public Set getAttributeValues ()
    {
        return _attributeVals;
    }


    /**
     *  Returns the list of schemas.  The elements of the list
     *  are Strings, giving the URI's for the schemas.
     */
    public List getSchemas ()
    {
        return _schemas;
    }
    
    /**
     *  Returns the list of unparsed entities. The elements of the
     *  list are arrays of four Strings, giving the name, public
     *  ID, system ID and notation name respectively.
     */
    public List getUnparsedEntities ()
    {
        return _unparsedEntities;
    }
    
    
    /**
     *  Returns the map of prefixes to namespaces.  The keys
     *  and values are Strings.
     */
    public Map getNamespaces ()
    {
        return _namespaces;
    }
    
    
    /**
     *  Returns the DTD URI.  May be null.
     */
    public String getDTDURI ()
    {
        return _dtdURI;
    }



    /** Returns the List of processing instructions.  Each element
     * is an array of two strings, giving the target
     * and data respectively.
     */
    public List getProcessingInstructions ()
    {
        return _processingInsts;
    }
    
    
    /**
     *  Returns the list of notations. Each is an array String[3]: 
     * name, public ID, and system ID.
     */ 
    public List getNotations ()
    {
        return _notations;
    }

    /** Returns the qualified name of the root element. */
    public String getRoot ()
    {
        return _root;
    }


     
    /** Returns the List of messages generated during the parse. */
    public List getMessages ()
    {
        return _messages;
    }
    
    
    /** Returns the validity state.  If <code>error</code>
     *  has been called, the return value will be <code>false</code>.
     */
    public boolean isValid ()
    {
        return _valid;
    }
    
    /** Returns <code>true</code> if we have seen an element or a
     *  processing instruction, which implies that we've seen an
     *  XML declaration.
     */
    public boolean getSigFlag ()
    {
        return _sigFlag;
    }
}
