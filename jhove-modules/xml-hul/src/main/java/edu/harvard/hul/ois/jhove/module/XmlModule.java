/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004-2007 by JSTOR and the President and Fellows of Harvard College
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.harvard.hul.ois.jhove.*;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;
import edu.harvard.hul.ois.jhove.module.utf8.Utf8BlockMarker;
import edu.harvard.hul.ois.jhove.module.xml.*;

/**
 * Module for identification and validation of XML files.
 *
 * @author Gary McGath
 */
public class XmlModule extends ModuleBase {

    private static final String NAME = "XML-hul";
    private static final String RELEASE = "1.5.5";
    private static final int[] DATE = { 2024, 8, 22 };
    private static final String[] FORMAT = { "XML", "XHTML" };
    private static final String COVERAGE = "XML 1.0";
    private static final String[] MIMETYPE = { "text/xml", "application/xml",
            "text/html" };
    private static final String WELLFORMED = "An XML file is well-formed if "
            + "it meets the criteria defined in Section 2.1 of the XML "
            + "specification (W3C Recommendation, 3rd edition, 2004-02-04)";
    private static final String VALIDITY = "An XML file is valid if "
            + "well-formed, and the file has an associated DTD or XML Schema and "
            + "the file meets the constraints defined by that DTD or Schema";
    private static final String REPINFO = "Additional representation "
            + "information includes: version, endcoding, standalone flag, DTD or "
            + "schema, namespaces, notations, character references, entities, "
            + "processing instructions, and comments";
    private static final String NOTE = "This module determines "
            + "well-formedness and validity using the SAX2-conforming parser "
            + "specified by the invoking application";
    private static final String RIGHTS = "Copyright 2004-2007 by JSTOR and "
            + "the President and Fellows of Harvard College. "
            + "Released under the GNU Lesser General Public License.";

    /** Top-level property list. */
    protected List<Property> _propList;

    /** Top-level property. */
    protected Property _metadata;

    /** Doctype for XHTML documents only, otherwise null. */
    protected String _xhtmlDoctype;

    /** Base URL for DTDs. If null, all DTD URLs are absolute. */
    protected String _baseURL;

    /**
     * Flag to control signature checking behavior. If true,
     * checkSignatures insists on an XML document declaration; if
     * false, it will parse the file if there is no document
     * declaration.
     */
    protected boolean _sigWantsDecl;

    /**
     * Flag to indicate we're invoking the parser from checkSignatures.
     * When true, it's up to checkSignatures to mark a signature as present.
     */
    protected boolean _parseFromSig;

    /** Flag to indicate if TextMD metadata should be reported. */
    protected boolean _withTextMD;

    /** TextMD metadata for the file being processed. */
    protected TextMDMetadata _textMD;

    /** Map of URLs to locally stored schemas. */
    protected Map<String, File> _localSchemas;

    /**
     * Class constructor.
     *
     * Instantiate an <code>XmlModule</code> object.
     */
    public XmlModule() {
        super(NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
                VALIDITY, REPINFO, NOTE, RIGHTS, false);

        _vendor = Agent.harvardInstance();

        Document doc = new Document(
                "Extensible Markup Language (XML) 1.0 (Third Edition)",
                DocumentType.REPORT);
        doc.setPublisher(Agent.newW3CInstance());
        doc.setDate("2004-02-04");
        doc.setIdentifier(new Identifier("http://www.w3.org/TR/REC-xml",
                IdentifierType.URL));
        _specification.add(doc);

        doc = new Document("SAX", DocumentType.WEB);
        doc.setIdentifier(new Identifier("http://sax.sourceforge.net/",
                IdentifierType.URL));
        _specification.add(doc);

        Signature sig = new ExternalSignature(".xml", SignatureType.EXTENSION,
                SignatureUseType.OPTIONAL);
        _signature.add(sig);

        // Initialize module parameters
        resetParams();
    }

    /**
     * Sets the value of the doctype string, assumed to have been forced
     * to upper case. This is set only when the HTML module invokes the
     * XML module for an XHTML document.
     */
    public void setXhtmlDoctype(String doctype) {
        _xhtmlDoctype = doctype;
        if (_textMD != null) {
            _textMD.setMarkup_language(_xhtmlDoctype);
        }
    }

    /**
     * Reset parameter settings.
     * Returns to a default state without any parameters.
     */
    @Override
    public void resetParams() {
        _baseURL = null;
        _localSchemas = new HashMap<>();
        _parseFromSig = false;
        _sigWantsDecl = false;
        _withTextMD = false;
    }

    /**
     * Parse configuration parameters for the module.
     *
     * If the parameter starts with "schema=", then the part to the
     * right of the equals sign specifies a schema location URI
     * followed by a path to a local copy of that schema to be used
     * in its place, separated by a semicolon. Example:
     *
     * schema=http://example.com/schema.xsd;/schemas/example.xsd
     *
     * If the first character is "s" or "S", and the parameter isn't
     * "schema", then XML document declarations are required for
     * signature checks.
     *
     * If the parameter begins with "b" or "B", then the remainder of
     * the parameter is used as a base URL for relative URIs. Otherwise
     * it is ignored and there is no base URL. Example:
     *
     * bhttp://example.com/schemas/
     *
     * If the parameter is "withtextmd=true", then textMD metadata is
     * included in the JHOVE report.
     *
     * @param param
     *              the module parameter to parse.
     */
    @Override
    public void param(String param) {
        if (param != null) {
            param = param.trim();
            String lowerCaseParam = param.toLowerCase();
            if (lowerCaseParam.startsWith("schema=")) {
                addLocalSchema(param);
            } else if (lowerCaseParam.startsWith("s")) {
                _sigWantsDecl = true;
            } else if (lowerCaseParam.startsWith("b")) {
                _baseURL = param.substring(1);
            } else if (lowerCaseParam.equals("withtextmd=true")) {
                _withTextMD = true;
            } else {
                _logger.warning("Ignoring unrecognized module parameter \""
                        + param + "\"");
            }
        }
    }

    /**
     * Parse the content of a purported XML digital object and store the
     * results in RepInfo.
     *
     * This is designed to be called in two passes. On the first pass,
     * a non-validating parse is done. If this succeeds, and the presence
     * of DTDs or schemas is detected, then parse returns 1 so that it
     * will be called again to do a validating parse. If there is nothing
     * to validate, we consider it "valid."
     *
     * @param stream
     *                   An InputStream, positioned at its beginning,
     *                   which is generated from the object to be parsed.
     *                   If multiple calls to <code>parse</code> are made
     *                   on the basis of a nonzero value being returned,
     *                   a new InputStream must be provided each time.
     *
     * @param info
     *                   A fresh (on the first call) RepInfo object
     *                   which will be modified
     *                   to reflect the results of the parsing
     *                   If multiple calls to <code>parse</code> are made
     *                   on the basis of a nonzero value being returned,
     *                   the same RepInfo object should be passed with each
     *                   call.
     *
     * @param parseIndex
     *                   Must be 0 in first call to <code>parse</code>. If
     *                   <code>parse</code> returns a nonzero value, it must be
     *                   called again with <code>parseIndex</code>
     *                   equal to that return value.
     */
    @Override
    public int parse(InputStream stream, RepInfo info, int parseIndex) {

        boolean canValidate = true;
        super.initParse();
        info.setFormat(_format[0]);
        info.setMimeType(_mimeType[0]);
        info.setModule(this);

        if (_textMD == null || parseIndex == 0) {
            _textMD = new TextMDMetadata();
            _xhtmlDoctype = null;
        }

        // Setup the data stream, will determine if we use checksum stream
        setupDataStream(stream, info);

        _propList = new LinkedList<>();
        _metadata = new Property("XMLMetadata", PropertyType.PROPERTY,
                PropertyArity.LIST, _propList);

        XMLReader parser = null;
        InputSource src = null;
        XmlModuleHandler handler = null;
        XmlLexicalHandler lexHandler = new XmlLexicalHandler();
        XmlDeclHandler declHandler = new XmlDeclHandler();

        // The XmlDeclStream filters the characters, looking for an
        // XML declaration, since there's no way to get that info
        // out of SAX.
        XmlDeclStream xds = new XmlDeclStream(_dstream);
        try {
            // Create an InputSource to feed the parser.
            // If a SAX class was specified, use it, otherwise use
            // the default parser.
            src = new InputSource(xds);
            // To correctly resolve relative URIs in XML, we need to know the
            // XML document's system identifier, i.e. its location, in order
            // to derive the base URI other URIs should be relative to.
            // Unfortunately JHOVE doesn't currently provide such information
            // to its modules. In lieu of that, this module has a parameter
            // which can be set to be used as the base URI for all relative
            // URI resolution in the document being parsed.
            if (_baseURL != null) {
                src.setSystemId(new File(_baseURL).toURI().toURL().toString());
            }
            String saxClass = _je.getSaxClass();
            if (saxClass == null) {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setNamespaceAware(true);
                parser = factory.newSAXParser().getXMLReader();
            } else {
                parser = XMLReaderFactory.createXMLReader(saxClass);
            }
            handler = new XmlModuleHandler();
            handler.setXhtmlFlag(_xhtmlDoctype != null);
            handler.setLocalSchemas(_localSchemas);
            parser.setContentHandler(handler);
            parser.setErrorHandler(handler);
            parser.setEntityResolver(handler);
            parser.setDTDHandler(handler);
            try {
                parser.setProperty(
                        "http://xml.org/sax/properties/lexical-handler",
                        lexHandler);
            } catch (SAXException e) {
                info.setMessage(new InfoMessage(MessageConstants.XML_HUL_5));
            }
            try {
                parser.setProperty(
                        "http://xml.org/sax/properties/declaration-handler",
                        declHandler);
            } catch (SAXException e) {
                info.setMessage(new InfoMessage(MessageConstants.XML_HUL_6));
            }

        } catch (Exception f) {
            info.setMessage(new ErrorMessage(CoreMessageConstants.JHOVE_CORE_5, f.getMessage()));
            info.setWellFormed(false); // actually not the file's fault
            return 0;
        }
        try {
            // On the first pass, we parse without validation.
            parser.setFeature("http://xml.org/sax/features/validation",
                    parseIndex != 0);
        } catch (SAXException se) {
            if (parseIndex != 0) {
                info.setMessage(new InfoMessage(MessageConstants.XML_HUL_8));
            }
            canValidate = false;
        }
        try {
            parser.setFeature("http://xml.org/sax/features/namespaces", true);
        } catch (SAXException se) {
            info.setMessage(new InfoMessage(MessageConstants.XML_HUL_7));
        }
        // This property for supporting schemas is a JAXP 1.2
        // recommendation, not likely to be supported widely as
        // of this (February 2004) writing, and not supported in
        // standard Crimson. But it looks like the way to prepare
        // for schema validation in the future, and at least the
        // info message will tell users why they're getting bogus
        // invalid status.

        // Try 2 different ways of setting schema validation;
        // it appears that no one way works for all parsers.
        if (parseIndex > 0) {
            try {
                parser.setFeature(
                        "http://apache.org/xml/features/validation/schema",
                        true);
            } catch (SAXException ee) {
                try {
                    parser.setProperty(
                            "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                            "http://www.w3.org/2001/XMLSchema");
                } catch (SAXException e) {
                    info.setMessage(
                            new InfoMessage(MessageConstants.XML_HUL_9));
                }
            }
        }
        try {
            parser.parse(src);
        } catch (FileNotFoundException fnfe) {
            // Make this particular exception a little more user-friendly
            info.setMessage(new ErrorMessage(MessageConstants.XML_HUL_10,
                    fnfe.getMessage()));
            info.setWellFormed(false);
            return 0;
        } catch (UTFDataFormatException udfe) {
            if (handler.getSigFlag() && !_parseFromSig) {
                info.setSigMatch(_name);
            }
            info.setMessage(new ErrorMessage(MessageConstants.XML_HUL_11));
            info.setWellFormed(false);
            return 0;
        } catch (IOException ioe) {
            // We may get an IOException from trying to resolve an
            // external entity.
            if (handler.getSigFlag() && !_parseFromSig) {
                info.setSigMatch(_name);
            }
            String mess = ioe.getClass().getName() + ": " + ioe.getMessage();
            info.setMessage(new ErrorMessage(
                    CoreMessageConstants.JHOVE_CORE_2, mess));
            info.setWellFormed(false);
            return 0;
        } catch (SAXParseException spe) {
            // Document failed to parse.
            if (handler.getSigFlag() && !_parseFromSig) {
                info.setSigMatch(_name);
            }
            info.setMessage(new ErrorMessage(MessageConstants.INSTANCE.makeSaxParseMessage(spe)));
            info.setWellFormed(false);
            return 0;
        } catch (SAXException se) {
            // Other SAX error.
            if (handler.getSigFlag()) {
                info.setSigMatch(_name);
            }
            // Sometimes the message will be null and another message
            // wrapped inside it. Try to report that.
            JhoveMessage message = JhoveMessages.getMessageInstance(
                    MessageConstants.XML_HUL_3.getId(),
                    MessageFormat.format(
                            MessageConstants.XML_HUL_3.getMessage(),
                            se.getMessage() != null ? se.getMessage() : ""));
            Throwable ee = se.getCause();
            String subMess = (ee != null)
                    ? MessageFormat.format(
                            MessageConstants.XML_HUL_12.getMessage(),
                            ee.getClass().getName())
                    : MessageConstants.XML_HUL_13.getMessage();
            info.setMessage(new ErrorMessage(message, subMess));
            info.setWellFormed(false);
            return 0;
        }

        // Check if user has aborted
        if (_je.getAbort()) {
            return 0;
        }

        if (handler.getSigFlag() && parseIndex == 0) {
            info.setSigMatch(_name);
        }
        // If it's the first pass, check if we found a DTD
        // or schema. If so, re-parse with validation enabled.
        String dtdURI = handler.getDTDURI();
        List<SchemaInfo> schemaList = handler.getSchemas();

        // To find the "primary" markup language we check the following,
        // in order of preference:
        // 1) the first schema's namespace URI
        // 2) the first schema's location URI
        // 3) the DTD's URI
        // It should be noted that later on, when we check the namespace
        // of the root element, if it has an associated URI, that will
        // be used instead.
        boolean hasRootSchema = false;
        if (!schemaList.isEmpty()) {
            SchemaInfo schItems = schemaList.get(0);
            if (isNotEmpty(schItems.namespaceURI)) {
                _textMD.setMarkup_language(schItems.namespaceURI);
            } else if (isNotEmpty(schItems.location)) {
                _textMD.setMarkup_language(schItems.location);
            }

            if (isNotEmpty(schItems.location)
                    || (isNotEmpty(schItems.namespaceURI) && _localSchemas.containsKey(schItems.namespaceURI))) {
                hasRootSchema = true;
            }
        } else if (isNotEmpty(dtdURI)) {
            _textMD.setMarkup_language(dtdURI);
            hasRootSchema = true;
        }

        if (parseIndex == 0) {
            if (canValidate && hasRootSchema) {
                return 1;
            }
            info.setValid(RepInfo.UNDETERMINED);
            // This may get downgraded to false, but won't
            // be upgraded to true.
        }

        // Take a deep breath. We parsed it. Now assemble the properties.
        info.setProperty(_metadata);

        // If it's XHTML, add the HTML property.
        HtmlMetadata hMetadata = handler.getHtmlMetadata();
        if (hMetadata != null) {
            info.setProperty(
                    hMetadata.toProperty(_withTextMD ? _textMD : null));
        }

        // Report the parser in a property.
        _propList.add(new Property("Parser", PropertyType.STRING,
                parser.getClass().getName()));

        // Add the version property. Give precedence to XHTML doctype.
        String vers = null;
        if (_xhtmlDoctype != null) {
            vers = DTDMapper.getXHTMLVersion(_xhtmlDoctype);
            _textMD.setMarkup_language_version(vers);
        }
        if (vers != null) {
            info.setVersion(vers);
        } else {
            vers = xds.getVersion();
            if (vers != null) {
                info.setVersion(vers);
            }
        }
        _textMD.setMarkup_basis_version(vers);

        // Add the encoding property.
        String encoding = xds.getEncoding();
        if (encoding == null) {
            // If no explicit encoding, use default (Bugzilla 136)
            encoding = "UTF-8";
        }
        _propList.add(new Property("Encoding", PropertyType.STRING, encoding));

        _textMD.setCharset(encoding);
        _textMD.setByte_size("8");
        _textMD.setByte_order(_bigEndian ? TextMDMetadata.BYTE_ORDER_BIG : TextMDMetadata.BYTE_ORDER_LITTLE);
        _textMD.setCharacter_size(_textMD.getCharset().contains("UTF") ? "variable" : "1");

        // CRLF from XmlDeclStream ...
        String lineEnd = xds.getKindOfLineEnd();
        if (lineEnd == null) {
            info.setMessage(new InfoMessage(MessageConstants.XML_HUL_4));
            _textMD.setLinebreak(TextMDMetadata.NILL);
        } else if ("CR".equalsIgnoreCase(lineEnd)) {
            _textMD.setLinebreak(TextMDMetadata.LINEBREAK_CR);
        } else if ("LF".equalsIgnoreCase(lineEnd)) {
            _textMD.setLinebreak(TextMDMetadata.LINEBREAK_LF);
        } else if ("CRLF".equalsIgnoreCase(lineEnd)) {
            _textMD.setLinebreak(TextMDMetadata.LINEBREAK_CRLF);
        }

        // Add the standalone property.
        String sa = xds.getStandalone();
        if (sa != null) {
            _propList.add(new Property("Standalone", PropertyType.STRING, sa));
        }

        // Add the DTD property.
        if (dtdURI != null) {
            _propList.add(new Property("DTD_URI", PropertyType.STRING, dtdURI));
        }

        if (!schemaList.isEmpty()) {
            // Build a List of Properties, which will be the value
            // of the Schemas Property.
            List<Property> schemaPropList = new ArrayList<>(schemaList.size());
            // Iterate through all the schemas.
            for (SchemaInfo schema : schemaList) {
                // Build a Property (Schema) whose value is an array
                // of two Properties (NamespaceURI and SchemaLocation).
                Property[] schItemProps = new Property[2];
                schItemProps[0] = new Property("NamespaceURI",
                        PropertyType.STRING, schema.namespaceURI);
                schItemProps[1] = new Property("SchemaLocation",
                        PropertyType.STRING, schema.location);
                schemaPropList.add(new Property("Schema",
                        PropertyType.PROPERTY,
                        PropertyArity.ARRAY,
                        schItemProps));
            }
            // Now add the list to the metadata
            _propList.add(new Property("Schemas",
                    PropertyType.PROPERTY,
                    PropertyArity.LIST,
                    schemaPropList));
        }

        // Add the root element.
        String root = handler.getRoot();
        String rootPrefix = null;
        if (root != null) {
            _propList.add(new Property("Root", PropertyType.STRING, root));
            if ("html".equals(root)) {
                // Specify format as XHTML
                info.setFormat(_format[1]);
                // Set the version according to the doctype... how?

            }
            // Get the prefix of root
            int indexOfColon = root.indexOf(':');
            if (indexOfColon != -1) {
                rootPrefix = root.substring(0, indexOfColon);
            }
        }
        if (rootPrefix == null) {
            rootPrefix = "";
        }

        // Declare properties we're going to add. They have
        // some odd interdependencies, so we create them all
        // and then add them in the right (specified) order.
        Property namespaceProp = null;
        Property notationsProp = null;
        Property charRefsProp = null;
        Property entitiesProp = null;
        Property procInstProp = null;
        Property commentProp = null;
        Property unicodeBlocksProp = null;

        Map<String, String> ns = handler.getNamespaces();
        if (!ns.isEmpty()) {
            Set<String> keys = ns.keySet();
            List<Property> nsList = new ArrayList<>(keys.size());
            for (String key : keys) {
                String val = ns.get(key);
                Property[] supPropArr = new Property[2];
                supPropArr[0] = new Property("Prefix",
                        PropertyType.STRING, key);
                supPropArr[1] = new Property("URI",
                        PropertyType.STRING, val);
                Property onens = new Property("Namespace",
                        PropertyType.PROPERTY,
                        PropertyArity.ARRAY,
                        supPropArr);
                nsList.add(onens);

                // Try to find the namespace URI of root
                if (rootPrefix.equalsIgnoreCase(key) && isNotEmpty(val)) {
                    _textMD.setMarkup_language(val);
                }
            }
            namespaceProp = new Property("Namespaces",
                    PropertyType.PROPERTY,
                    PropertyArity.LIST,
                    nsList);
        }

        // CharacterReferences property goes here.
        // Report as a list of 4-digit hexadecimal strings,
        // e.g., 003C, 04AA, etc.
        // Also build the Unicode blocks here.
        List<Integer> refs = xds.getCharacterReferences();
        if (!refs.isEmpty()) {
            Utf8BlockMarker utf8BM = new Utf8BlockMarker();
            List<String> refList = new ArrayList<>(refs.size());
            for (Integer refint : refs) {
                refList.add(intTo4DigitHex(refint));
                utf8BM.markBlock(refint);
            }
            charRefsProp = new Property("CharacterReferences",
                    PropertyType.STRING,
                    PropertyArity.LIST,
                    refList);
            unicodeBlocksProp = utf8BM
                    .getBlocksUsedProperty("UnicodeCharRefBlocks");
        }

        // Entities property
        // External unparsed entities
        Set<String> entNames = lexHandler.getEntityNames();
        Set<String> attributeVals = handler.getAttributeValues();
        List<Property> entProps = new LinkedList<>();
        List<String[]> uent = handler.getUnparsedEntities();
        List<String> unparsedNotationNames = new LinkedList<>();
        if (!uent.isEmpty()) {
            for (String[] entarr : uent) {
                // We check external parsed entities against
                // the list of attribute values which we've
                // accumulated. If a parsed entity name matches an
                // attribute value, we assume it's used.
                String name = entarr[0];
                if (attributeVals.contains(name)) {
                    // Add the notation name to the list
                    // unparsedNotationNames, so we can use it
                    // in determining which notations are used.
                    unparsedNotationNames.add(entarr[3]);
                    List<Property> subPropList = new ArrayList<>(6);
                    subPropList.add(new Property("Name",
                            PropertyType.STRING, name));
                    subPropList.add(new Property("Type",
                            PropertyType.STRING, "External unparsed"));
                    subPropList.add(new Property("PublicID",
                            PropertyType.STRING, entarr[1]));
                    subPropList.add(new Property("SystemID",
                            PropertyType.STRING, entarr[2]));
                    subPropList.add(new Property("NotationName",
                            PropertyType.STRING, entarr[3]));

                    entProps.add(new Property("Entity",
                            PropertyType.PROPERTY,
                            PropertyArity.LIST,
                            subPropList));
                }
            }
        }

        // Internal entities
        List<String[]> declEnts = declHandler.getInternalEntityDeclarations();
        if (!declEnts.isEmpty()) {
            for (String[] entarr : declEnts) {
                String name = entarr[0];
                // include only if the entity was actually used
                if (entNames.contains(name)) {
                    List<Property> subPropList = new ArrayList<>(4);
                    subPropList.add(new Property("Name",
                            PropertyType.STRING, name));
                    subPropList.add(new Property("Type",
                            PropertyType.STRING, "Internal"));
                    subPropList.add(new Property("Value",
                            PropertyType.STRING, entarr[1]));
                    entProps.add(new Property("Entity",
                            PropertyType.PROPERTY,
                            PropertyArity.LIST,
                            subPropList));
                }
            }
        }

        // External parsed entities
        declEnts = declHandler.getExternalEntityDeclarations();
        if (!declEnts.isEmpty()) {
            for (String[] entarr : declEnts) {
                String name = entarr[0];
                // include only if the entity was actually used
                if (entNames.contains(name)) {
                    List<Property> subPropList = new ArrayList<>(4);
                    subPropList.add(new Property("Name",
                            PropertyType.STRING, name));
                    subPropList.add(new Property("Type",
                            PropertyType.STRING, "External parsed"));
                    if (entarr[1] != null) {
                        subPropList.add(new Property("PublicID",
                                PropertyType.STRING, entarr[1]));
                    }
                    if (entarr[2] != null) {
                        subPropList.add(new Property("SystemID",
                                PropertyType.STRING, entarr[2]));
                    }

                    entProps.add(new Property("Entity",
                            PropertyType.PROPERTY,
                            PropertyArity.LIST,
                            subPropList));
                }
            }
        }

        if (!entProps.isEmpty()) {
            entitiesProp = new Property("Entities",
                    PropertyType.PROPERTY,
                    PropertyArity.LIST,
                    entProps);
        }

        List<ProcessingInstructionInfo> pi = handler
                .getProcessingInstructions();
        List<String> piTargets = new LinkedList<>();
        if (!pi.isEmpty()) {
            // Build a property, which consists of a list
            // of properties, each of which is an array of
            // two String properties, named Target and
            // Data respectively.
            List<Property> piPropList = new ArrayList<>(pi.size());
            for (ProcessingInstructionInfo pistr : pi) {
                Property[] subPropArr = new Property[2];
                // Accumulate targets in a list, so we can tell
                // which Notations use them.
                // Wait a minute -- what we're doing here can't work!!
                // TODO: What's supposed to be happening?
                // piTargets.add (subPropArr[0]);
                subPropArr[0] = new Property("Target",
                        PropertyType.STRING, pistr.target);
                subPropArr[1] = new Property("Data",
                        PropertyType.STRING, pistr.data);
                piPropList.add(new Property("ProcessingInstruction",
                        PropertyType.PROPERTY,
                        PropertyArity.ARRAY,
                        subPropArr));
            }
            procInstProp = new Property("ProcessingInstructions",
                    PropertyType.PROPERTY,
                    PropertyArity.LIST,
                    piPropList);
        }

        // Notations property. We list notations only if they're
        // "actually used," meaning that they designate either
        // the target of a processing instruction or the ndata
        // of an unparsed entry which is itself "actually used."
        List<String[]> notations = handler.getNotations();
        if (!notations.isEmpty()) {
            List<Property> notProps = new ArrayList<>(notations.size());
            ListIterator<String[]> iter = notations.listIterator();
            List<Property> subPropList = new ArrayList<>(3);
            while (iter.hasNext()) {
                String[] notArray = iter.next();
                String notName = notArray[0];
                // Check for use of Notation before including
                // TODO this is implemented wrong! Need to reinvestigate
                if (piTargets.contains(notName)
                        || unparsedNotationNames.contains(notName)) {
                    // notArray has name, public ID, system ID
                    subPropList.add(new Property("Name",
                            PropertyType.STRING, notName));
                    if (notArray[1] != null) {
                        subPropList.add(new Property("PublicID",
                                PropertyType.STRING, notArray[1]));
                    }
                    if (notArray[2] != null) {
                        subPropList.add(new Property("SystemID",
                                PropertyType.STRING, notArray[2]));
                    }
                    notProps.add(new Property("Notation",
                            PropertyType.PROPERTY,
                            PropertyArity.LIST,
                            subPropList));
                }
            }
            // Recheck emptiness in case only unprocessed notations were found
            if (!notProps.isEmpty()) {
                notationsProp = new Property("Notations",
                        PropertyType.PROPERTY,
                        PropertyArity.LIST,
                        notProps);
            }
        }

        // Now add all the properties we created.
        if (namespaceProp != null) {
            _propList.add(namespaceProp);
        }
        if (notationsProp != null) {
            _propList.add(notationsProp);
        }
        if (charRefsProp != null) {
            _propList.add(charRefsProp);
        }
        if (unicodeBlocksProp != null) {
            _propList.add(unicodeBlocksProp);
        }
        if (entitiesProp != null) {
            _propList.add(entitiesProp);
        }
        if (procInstProp != null) {
            _propList.add(procInstProp);
        }

        List<String> comm = lexHandler.getComments();
        if (!comm.isEmpty()) {
            commentProp = new Property("Comments",
                    PropertyType.STRING,
                    PropertyArity.LIST,
                    comm);
        }
        if (commentProp != null) {
            _propList.add(commentProp);
        }

        // Check if parse detected invalid XML
        if (!handler.isValid()) {
            info.setValid(false);
        }

        if (info.getWellFormed() == RepInfo.TRUE) {
            if (_xhtmlDoctype != null) {
                info.setMimeType(_mimeType[2]);
            } else {
                info.setMimeType(_mimeType[0]);
            }
        }

        // Add any messages from the parse.
        List<Message> msgs = handler.getMessages();
        for (Message msg : msgs) {
            info.setMessage(msg);
        }

        if (info.getVersion() == null) {
            info.setVersion("1.0");
        }

        if (_withTextMD) {
            _textMD.setMarkup_basis(info.getFormat());
            _textMD.setMarkup_basis_version(info.getVersion());
            Property property = new Property("TextMDMetadata",
                    PropertyType.TEXTMDMETADATA,
                    PropertyArity.SCALAR, _textMD);
            _propList.add(property);
        }

        // Set the checksums in the report if they're calculated
        setChecksums(this._ckSummer, info);

        return 0;
    }

    /**
     * Check if the digital object conforms to this Module's
     * internal signature information.
     *
     * XML is a particularly messy case; in general, there's no
     * even moderately good way to check "signatures" without parsing
     * the whole file, since the document declaration is optional.
     * We provide the user two choices, based on the "s" parameter.
     * If 's' is the first character of the module parameter, then
     * we look for an XML document declaration, and say there's no
     * signature if it's missing. (This can reject well-formed
     * XML files, though not valid ones.) Otherwise, if there's no
     * document declaration, we parse the whole file.
     *
     * @param file
     *               A File object for the object being parsed
     * @param stream
     *               An InputStream, positioned at its beginning,
     *               which is generated from the object to be parsed
     * @param info
     *               A fresh RepInfo object which will be modified
     *               to reflect the results of the test
     */
    @Override
    public void checkSignatures(File file, InputStream stream, RepInfo info)
            throws IOException {
        _parseFromSig = false;
        info.setFormat(_format[0]);
        info.setMimeType(_mimeType[0]);
        info.setModule(this);
        String sigStr = "<?xml";
        int sigidx = 0;
        JhoveBase jb = getBase();
        int sigBytes = jb.getSigBytes();
        Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        int charsRead = 0;
        try {
            while (charsRead < sigBytes) {
                char ch = (char) reader.read();
                ++charsRead;
                // Skip over all whitespace till we reach "xml"
                if (sigidx <= 2 && Character.isWhitespace(ch)) {
                    continue;
                }
                if (ch == sigStr.charAt(sigidx)) {
                    if (++sigidx >= sigStr.length()) {
                        info.setSigMatch(_name);
                        return; // sig matches
                    }
                } else
                    break;
            }
        } catch (IOException e) {
            info.setWellFormed(false);
            return;
        }
        if (_sigWantsDecl) {
            // No XML declaration, and it's mandatory according to the param.
            info.setWellFormed(false);
            return;
        }

        // No XML signature, but we're allowed to parse the file now.
        // This means rewinding back to the start of the file.
        int parseIndex = 1;
        _parseFromSig = true; // we set the sig match ourselves
        while (parseIndex != 0) {
            stream.close();
            stream = new FileInputStream(file);
            parseIndex = parse(stream, info, parseIndex);
        }
        if (info.getWellFormed() == RepInfo.TRUE) {
            info.setSigMatch(_name);
        }
    }

    /**
     * Converts an int to a 4-digit hex value, e.g.,
     * 003F or F10A. This is used for Character References.
     */
    protected static String intTo4DigitHex(int n) {
        StringBuilder sb = new StringBuilder(4);
        for (int i = 3; i >= 0; i--) {
            int d = (n >> (4 * i)) & 0XF; // extract a nybble
            if (d < 10) {
                sb.append((char) ('0' + d));
            } else {
                sb.append((char) ('A' + (d - 10)));
            }
        }
        return sb.toString();
    }

    /**
     * Check that a string contains something other than "[None]".
     *
     * @param value
     *              string to test
     * @return
     *         <code>true</code> if the string contains something
     *         other than "[None]", <code>false</code> otherwise
     */
    protected static boolean isNotEmpty(String value) {
        return ((value != null) && (value.length() != 0)
                && !("[None]".equals(value)));
    }

    /**
     * Parse a "schema" configuration argument and map the schema
     * location URI to a local file after validating both components.
     *
     * @param param
     *              a module parameter string of the form
     *              "schema=[location-URI];[local-path]"
     */
    private void addLocalSchema(String param) {
        int eq = param.indexOf('=');
        int semi = param.indexOf(';');
        try {
            String uriParam = param.substring(eq + 1, semi);
            String localParam = param.substring(semi + 1);
            try {
                String locationUri = new URI(uriParam).toString();
                File localFile = new File(localParam);
                if (localFile.exists()) {
                    _localSchemas.put(locationUri, localFile);
                } else {
                    _logger.warning("Ignoring module parameter with "
                            + "unresolvable path: \"" + localParam + "\"");
                }
            } catch (URISyntaxException use) {
                _logger.warning("Ignoring module parameter with "
                        + "invalid URI syntax: \"" + uriParam + "\"");
            }
        } catch (IndexOutOfBoundsException ioobe) {
            _logger.warning("Ignoring malformed module parameter \""
                    + param + "\"");
        }
    }
}
