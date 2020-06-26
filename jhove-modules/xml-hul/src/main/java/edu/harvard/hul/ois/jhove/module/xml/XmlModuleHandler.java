/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004-2007 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.InfoMessage;
import edu.harvard.hul.ois.jhove.Message;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * This handler does the parsing work of the XML module.
 *
 * @author Gary McGath
 */
public class XmlModuleHandler extends DefaultHandler {

	/** Map of namespace prefixes to URIs. */
	private Map<String, String> _namespaces;

	/**
	 * List of processing instructions. Each element
	 * is an array of two strings, giving the target
	 * and data respectively.
	 */
	private List<ProcessingInstructionInfo> _processingInsts;

	/** List of generated Messages. */
	private List<Message> _messages;

	/** Validity flag. */
	private boolean _valid;

	/** Qualified name of the root element. */
	private String _root;

	/** URI for DTD specification. */
	private String _dtdURI;

	/**
	 * List of schema URI's. Each element is a String[2],
	 * consisting of the namespace URI and the schema location.
	 */
	private List<SchemaInfo> _schemas;

	/**
	 * List of unparsed entities. Each is an array String[4];
	 * name, public ID, system ID and notation name
	 * respectively.
	 */
	private List<String[]> _unparsedEntities;

	/** Error counter. */
	private int _nErrors;

	/**
	 * Notations list. Each is an array String[3]:
	 * name, public ID, and system ID.
	 */
	private List<String[]> _notations;

	/**
	 * List of all the attributes. This is used to
	 * check on the use of unparsed entities.
	 */
	private Set<String> _attributeVals;

	/** Limit on number of errors to report. */
	private static final int MAXERRORS = 2000;

	/**
	 * XHTML flag, only for XHTML documents referred
	 * by the HTML module.
	 */
	private boolean _xhtmlFlag;

	/** HTMLMetadata object; used only with XHTML documents. */
	private HtmlMetadata _htmlMetadata;

	/**
	 * Flag set if we've seen any components. This is an indirect
	 * way of checking if the "signature" (the XML declaration)
	 * has been seen.
	 */
	private boolean _sigFlag;

	/** Map from URIs to local schema files. */
	private Map<String, File> _localSchemas;

	/**
	 * Constructor.
	 */
	public XmlModuleHandler() {
		_xhtmlFlag = false;
		_htmlMetadata = null;
		_namespaces = new HashMap<>();
		_processingInsts = new LinkedList<>();
		_messages = new LinkedList<>();
		_attributeVals = new HashSet<>();
		_dtdURI = null;
		_root = null;
		_valid = true;
		_nErrors = 0;
		_schemas = new LinkedList<>();
		_unparsedEntities = new LinkedList<>();
		_notations = new LinkedList<>();
		_sigFlag = false;
	}

	/**
	 * Sets the value of the XHTML flag. Special properties
	 * are extracted if this is an XHTML document.
	 */
	public void setXhtmlFlag(boolean flag) {
		_xhtmlFlag = flag;
	}

	/**
	 * Sets a map of schema URIs to local files. This information
	 * comes from jhove.conf parameters.
	 */
	public void setLocalSchemas(Map<String, File> schemas) {
		_localSchemas = schemas;
	}

	/**
	 * Returns the HTML metadata object. Will be non-null only
	 * for a document recognized as XHTML.
	 */
	public HtmlMetadata getHtmlMetadata() {
		return _htmlMetadata;
	}

	/**
	 * Looks for the first element encountered. Stores
	 * its name as the value to be returned by getRoot,
	 * qualified name by preference, local name if the
	 * qualified name isn't available.
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qualifiedName, Attributes atts) {
		// The first element we encounter is the root.
		// Save it.
		if (_root == null) {
			_sigFlag = true;
			if (!"".equals(qualifiedName)) {
				_root = qualifiedName;
			} else {
				_root = localName;
			}
		}
		if ((namespaceURI != null) && (namespaceURI.length() != 0)) {
			SchemaInfo schi = new SchemaInfo();
			schi.namespaceURI = namespaceURI;
			schi.location = "";
			if (!hasSchemaURI(schi)) {
				_schemas.add(schi);
			}
		}
		if (atts != null) {
			for (int i = 0; i < atts.getLength(); i++) {
				String name = atts.getLocalName(i);
				String namespace = atts.getURI(i);
				String val = atts.getValue(i);
				if ("http://www.w3.org/2001/XMLSchema-instance"
						.equals(namespace)) {
					if ("schemaLocation".equals(name)) {
						// schemaLocation should contain pairs of namespace
						// and location URIs separated by white space. Any
						// number of such pairs may be declared in a single
						// schemaLocation attribute.
						String[] uris = val.split("\\s");
						for (int j = 0; j < uris.length; j += 2) {
							SchemaInfo schema = new SchemaInfo();
							schema.namespaceURI = uris[j].trim();
							if (uris.length > j + 1) {
								schema.location = uris[j + 1].trim();
							} else {
								schema.location = "";
							}
							if (!hasSchemaURI(schema)) {
								_schemas.add(schema);
							}
						}
					}
					if ("noNamespaceSchemaLocation".equals(name)) {
						SchemaInfo schema = new SchemaInfo();
						schema.location = val;
						schema.namespaceURI = "";
						if (!hasSchemaURI(schema)) {
							_schemas.add(schema);
						}
					}
				}
				// Collect all attribute values.
				_attributeVals.add(val);
			}
		}
		if (_xhtmlFlag) {
			if (_htmlMetadata == null) {
				_htmlMetadata = new HtmlMetadata();
			}
			XhtmlProcessing.processElement(localName, qualifiedName, atts,
					_htmlMetadata);
		}
	}

	/**
	 * The only action taken here is some bookkeeping in connection
	 * with the HTML metadata.
	 */
	@Override
	public void endElement(String namespaceURI, String localName,
			String qName) {
		if (_htmlMetadata != null) {
			_htmlMetadata.finishPropUnderConstruction();
		}
	}

	/**
	 * Processes PCData characters. This does things only
	 * in connection with properties under construction in
	 * HTML metadata.
	 */
	@Override
	public void characters(char[] ch, int start, int length) {
		if (_htmlMetadata != null
				&& _htmlMetadata.getPropUnderConstruction() != null) {
			_htmlMetadata.addToPropUnderConstruction(ch, start, length);
		}
	}

	/**
	 * Begin the scope of a prefix-URI Namespace mapping.
	 * Prefixes mappings are stored in _namespaces.
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri) {
		// THL we want the root namespace even if it declares no prefix !!!
		// if (!"".equals (prefix)) {
		_namespaces.put(prefix, uri);
		// }
	}

	/**
	 * Handles a processing instruction. Adds it to
	 * the list that will be returned by <code>getProcessingInstructions</code>.
	 * Each element of the list is an array of two Strings. Element 0 of
	 * the array is the target, and element 1 is the data.
	 */
	@Override
	public void processingInstruction(String target, String data) {
		_sigFlag = true;
		if (data == null) {
			data = "";
		}
		ProcessingInstructionInfo pi = new ProcessingInstructionInfo();
		pi.target = target;
		pi.data = data;
		_processingInsts.add(pi);
	}

	/**
	 * Puts all notations into the notation list. A list entry
	 * is a String[3], consisting of name, public ID, and system
	 * ID.
	 */
	@Override
	public void notationDecl(String name, String publicID, String systemID) {
		String[] notArr = new String[3];
		notArr[0] = name;
		notArr[1] = publicID;
		notArr[2] = systemID;
		_notations.add(notArr);
	}

	/**
	 * Overrides standard <code>resolveEntity</code> method to
	 * provide special-case handling for external entity resolution.
	 *
	 * First looks for external entities that are stored as
	 * local resources and uses those if available (faster and
	 * more reliable than using the internet), preferring
	 * user-supplied resources over packaged resources. Then
	 * attempts to resolve any URLs that result in redirection
	 * requests.
	 *
	 * Finally, returns <code>null</code> if the entity doesn't
	 * require special handling, allowing the SAX implementation
	 * to attempt its own resolution.
	 */
	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException
	{
		// Check for XHTML DTDs
		if (!_xhtmlFlag && DTDMapper.isXHTMLDTD(publicId)) {
			_xhtmlFlag = true;
		}
		// Assume that the first system ID in the file with a .dtd
		// extension is the actual DTD
		if (systemId.endsWith(".dtd") && _dtdURI == null) {
			_dtdURI = systemId;
		}

		// Attempt to resolve system identifiers to schemas from config.
		// This takes precedence, allowing users to override JHOVE resources.
		File fil = _localSchemas.get(systemId.toLowerCase());
		if (fil != null) {
			try {
				FileInputStream inStrm = new FileInputStream(fil);
				return new InputSource(inStrm);
			} catch (FileNotFoundException fnfe) {
				// File locations should be verified before
				// being added to the list of local schemas.
			}
		}

		// Attempt to resolve public identifiers to local JHOVE resources.
		InputSource ent = DTDMapper.publicIDToFile(publicId);

		// Attempt to resolve redirected URLs.
		if (ent == null) {
			try {
				URLConnection conn = new URL(systemId).openConnection();
				if (conn instanceof HttpURLConnection) {
					int status = ((HttpURLConnection) conn).getResponseCode();
					if (status == HttpURLConnection.HTTP_MOVED_TEMP
							|| status == HttpURLConnection.HTTP_MOVED_PERM
							|| status == HttpURLConnection.HTTP_SEE_OTHER) {

						String newUrl = conn.getHeaderField("Location");
						conn = new URL(newUrl).openConnection();
						ent = new InputSource(conn.getInputStream());
					}
				}
			}
			catch (IOException ioe) {
				// Malformed URL or bad connection.
				throw new SAXException(ioe);
			}
		} else {
			// A little magic so SAX won't give up in advance on
			// relative URI's.
			ent.setSystemId("http://hul.harvard.edu/hul");
		}

		return ent;
	}

	/**
	 * Picks up unparsed entity declarations, after calling the
	 * superclass's unparsedEntityDecl, and puts their information
	 * into the unparsed entity declaration list as an array of
	 * four strings: [ name, publicId, systemId, notationName ].
	 * Null values are converted into empty strings.
	 */
	@Override
	public void unparsedEntityDecl(String name, String publicId,
			String systemId, String notationName) throws SAXException {
		super.unparsedEntityDecl(name, publicId, systemId, notationName);
		String[] info = new String[4];
		info[0] = name == null ? "" : name;
		info[1] = publicId == null ? "" : publicId;
		info[2] = systemId == null ? "" : systemId;
		info[3] = notationName == null ? "" : notationName;
		_unparsedEntities.add(info);
	}

	/**
	 * Processes a warning. We just add an InfoMessage.
	 */
	@Override
	public void warning(SAXParseException spe) {
		_messages.add(new InfoMessage(
				MessageConstants.XML_HUL_1,
				spe.getMessage()));
	}

	/**
	 * Processes a parsing exception. An ill-formed piece
	 * of XML will get a fatalError (I think), so we can assume
	 * that any error here indicates only invalidity.
	 */
	@Override
	public void error(SAXParseException spe) {
		_valid = false;
		if (_nErrors == MAXERRORS) {
			JhoveMessage message = JhoveMessages.getMessageInstance(
					MessageConstants.XML_HUL_2.getId(),
					MessageFormat.format(
							MessageConstants.XML_HUL_2.getMessage(),
							MAXERRORS));
			_messages.add(new InfoMessage(message));
		} else if (_nErrors < MAXERRORS) {
			_messages.add(new ErrorMessage(
					MessageConstants.XML_HUL_1,
					MessageFormat.format(
							MessageConstants.XML_HUL_1_SUB.getMessage(),
							spe.getMessage(),
							spe.getLineNumber(),
							spe.getColumnNumber()
					)));
		}
		++_nErrors;
	}

	/**
	 * Returns the set of attribute values.
	 */
	public Set<String> getAttributeValues() {
		return _attributeVals;
	}

	/**
	 * Returns the list of schemas. The elements of the list
	 * are Strings, giving the URI's for the schemas.
	 */
	public List<SchemaInfo> getSchemas() {
		return _schemas;
	}

	/**
	 * Returns the list of unparsed entities. The elements of the
	 * list are arrays of four Strings, giving the name, public
	 * ID, system ID and notation name respectively.
	 */
	public List<String[]> getUnparsedEntities() {
		return _unparsedEntities;
	}

	/**
	 * Returns the map of prefixes to namespaces. The keys
	 * and values are Strings.
	 */
	public Map<String, String> getNamespaces() {
		return _namespaces;
	}

	/**
	 * Returns the DTD URI. May be null.
	 */
	public String getDTDURI() {
		return _dtdURI;
	}

	/**
	 * Returns the List of processing instructions. Each element
	 * is an array of two strings, giving the target
	 * and data respectively.
	 */
	public List<ProcessingInstructionInfo> getProcessingInstructions() {
		return _processingInsts;
	}

	/**
	 * Returns the list of notations. Each is an array String[3]:
	 * name, public ID, and system ID.
	 */
	public List<String[]> getNotations() {
		return _notations;
	}

	/** Returns the qualified name of the root element. */
	public String getRoot() {
		return _root;
	}

	/** Returns the List of messages generated during the parse. */
	public List<Message> getMessages() {
		return _messages;
	}

	/**
	 * Returns the validity state. If <code>error</code>
	 * has been called, the return value will be <code>false</code>.
	 */
	public boolean isValid() {
		return _valid;
	}

	/**
	 * Returns <code>true</code> if we have seen an element or a
	 * processing instruction, which implies that we've seen an
	 * XML declaration.
	 */
	public boolean getSigFlag() {
		return _sigFlag;
	}

	/**
	 * Check if we already know about this schema URI. If we do
	 * but the new info provides a location, quietly stuff the
	 * old one into a sewer and pretend it was never there.
	 */
	public boolean hasSchemaURI(SchemaInfo newinfo) {
		for (SchemaInfo schema : _schemas) {
			if (newinfo.namespaceURI.equals(schema.namespaceURI)) {
				if (schema.location.isEmpty() && !newinfo.location.isEmpty()) {
					_schemas.remove(schema);
					return false;    // we like the new info better
				}
				return true;
			}
		}
		return false;
	}
}
