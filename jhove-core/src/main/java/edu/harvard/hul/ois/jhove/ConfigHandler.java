/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX Parser for the configuration file.
 */
public class ConfigHandler
        extends DefaultHandler {
    /******************************************************************
     * PRIVATE INSTANCE FIELDS.
     ******************************************************************/

    private String _class;
    protected StringBuffer _content;

    /** The schema name */
    private static final String configSchemaName = "jhoveConfig.xsd";

    /**
     * The list of handlers. Each element in the List is an
     * array of two Strings representing the class and the initialization
     * string.
     */
    private List<String[]> _handler;

    /**
     * The list of handler parameters. Each element in the List is
     * a List of Strings (which may be empty but not null) representing
     * parameters to be passed to the module. List elements are in
     * one-to-one correspondence with _handler.
     */
    protected List<List<String>> _handlerParams;
    private String _init;
    private List<String> _param;
    private String _tempDir;
    private String _mixVsn;
    private String _encoding;
    private String language = "";
    private String _logLevel;
    private int _bufferSize;
    private String _jhoveHome;
    private int _sigBytes;

    protected boolean _isHandler;
    protected boolean _isLanguage = false;

    /*
     * _isModule is protected rather than private so that subclasses
     * can add elements to the module element.
     */
    protected boolean _isModule;

    private boolean _isTempDir;
    private boolean _isMixVsn;
    private boolean _isEncoding;
    private boolean _isBufferSize;
    private boolean _isJhoveHome;
    private boolean _isLogLevel;
    private boolean _isSigBytes;

    /**
     * The list of modules. Each element in the List is an
     * array of two Strings representing the class and the initialization
     * string.
     */
    protected List<ModuleInfo> _module;

    /**
     * The list of module parameters. Each element in the List is
     * a List of Strings (which may be empty but not null) representing
     * parameters to be passed to the module. List elements are in
     * one-to-one correspondence with _module.
     */
    protected List<List<String>> _modParams;

    /******************************************************************
     * CLASS CONSTRUCTOR.
     ******************************************************************/

    /**
     * Creates a ConfigHandler.
     */
    public ConfigHandler() {
        _module = new ArrayList<>();
        _handler = new ArrayList<>();
        _modParams = new ArrayList<>();
        _handlerParams = new ArrayList<>();

        _isModule = false;
        _isHandler = false;
        _isTempDir = false;
        _isEncoding = false;
        _isBufferSize = false;
        _isJhoveHome = false;
        _isLogLevel = false;

        _bufferSize = -1;
        _encoding = null;
        _tempDir = null;
        _mixVsn = null;
        _sigBytes = 1024;
        _logLevel = null;
    }

    /******************************************************************
     * PUBLIC INSTANCE METHODS.
     *
     * Accessor methods.
     ******************************************************************/

    /**
     * Returns the List of Modules specified by the config file.
     * Each element of the List is a String[2] whose elements are
     * the module class name and initialization value.
     *
     * @see Module
     */
    public List<ModuleInfo> getModule() {
        return _module;
    }

    /**
     * Returns the List of module parameters specified by the config file.
     * Each element of the List is a List (possibly empty) of Strings
     * whose elements are parameters to pass to the module. The
     * values returned by <code>getModuleParams()</code> are in
     * one-to-one correspondence with those return by <code>getModule()</code>.
     */
    public List<List<String>> getModuleParams() {
        return _modParams;
    }

    /**
     * Returns the List of handler parameters specified by the config file.
     * Each element of the List is a List (possibly empty) of Strings
     * whose elements are parameters to pass to the output handler. The
     * values returned by <code>getHandlerParams()</code> are in
     * one-to-one correspondence with those return by <code>getHandler()</code>.
     */
    public List<List<String>> getHandlerParams() {
        return _handlerParams;
    }

    /**
     * Returns the List of OutputHandlers specified by the config file.
     *
     * @see OutputHandler
     */
    public List<String[]> getHandler() {
        return _handler;
    }

    /**
     * Returns the temporary directory path specified by the config file,
     * with final path separator.
     */
    public String getTempDir() {
        return _tempDir;
    }

    /**
     * Returns the MIX schema version specified by the config file.
     * Acceptable values are "0.2" and "1.0" and "2.0".
     */
    public String getMixVsn() {
        return _mixVsn;
    }

    /**
     * Returns the number of bytes to examine when looking for an
     * indefinitely positioned signature, or checking the first
     * sigBytes bytes of a file in lieu of a signature.
     */
    public int getSigBytes() {
        return _sigBytes;
    }

    /**
     * Returns the character encoding specified by the config file.
     */
    public String getEncoding() {
        return _encoding;
    }

    /**
     * Returns the buffer size specified in the config file.
     *
     * @return the buffer size, or -1 if none specified
     */
    public int getBufferSize() {
        return _bufferSize;
    }

    /**
     * Returns the path to the application's home directory,
     * with final path separator.
     */
    public String getJhoveHome() {
        return _jhoveHome;
    }

    /**
     * Returns the name of the desired log level. This should be the
     * name of one of the predefined values of java.util.logging.Level,
     * e.g., "WARNING", "INFO", "ALL". The default level is SEVERE.
     */
    public String getLogLevel() {
        return _logLevel;
    }

    /**
     * @return a language code if set
     */
    public String getLanguage() {
        return this.language;
    }

    /******************************************************************
     * SAX parser methods.
     ******************************************************************/

    /**
     * SAX parser callback method.
     */
    @Override
    public void startElement(String namespaceURI, String localName,
            String rawName, Attributes atts) {
        _content = new StringBuffer();

        if ("module".equals(rawName)) {
            _isModule = true;
            _init = null;
            _param = new ArrayList<>(1);
            _class = null;
        } else if ("outputHandler".equals(rawName)) {
            _isHandler = true;
            _init = null;
            _param = new ArrayList<>(1);
            _class = null;
        } else if ("languageCode".equals(rawName)) {
            _isLanguage = true;
            _init = null;
            _param = new ArrayList<>(1);
            _class = null;
        } else if ("tempDirectory".equals(rawName)) {
            _isTempDir = true;
        } else if ("mixVersion".equals(rawName)) {
            _isMixVsn = true;
        } else if ("defaultEncoding".equals(rawName)) {
            _isEncoding = true;
        } else if ("bufferSize".equals(rawName)) {
            _isBufferSize = true;
        } else if ("jhoveHome".equals(rawName)) {
            _isJhoveHome = true;
        } else if ("logLevel".equals(rawName)) {
            _isLogLevel = true;
        } else if ("sigBytes".equals(rawName)) {
            _isSigBytes = true;
        }
    }

    /**
     * SAX parser callback method.
     */
    @Override
    public void characters(char[] ch, int start, int length) {
        _content.append(ch, start, length);
    }

    /**
     * SAX parser callback method.
     */
    @Override
    public void endElement(String namespaceURI, String localName,
            String rawName) {
        if (_isModule) {
            if ("class".equals(rawName)) {
                _class = _content.toString();
            } else if ("init".equals(rawName)) {
                _init = _content.toString();
            } else if ("param".equals(rawName)) {
                _param.add(_content.toString());
            } else if ("module".equals(rawName)) {
                ModuleInfo modInfo = new ModuleInfo(_class, _init);
                _module.add(modInfo);
                _modParams.add(_param);
                _isModule = false;
            }
        } else if (_isHandler) {
            if ("class".equals(rawName)) {
                _class = _content.toString();
            } else if ("init".equals(rawName)) {
                _init = _content.toString();
            } else if ("param".equals(rawName)) {
                _param.add(_content.toString());
            } else if ("outputHandler".equals(rawName)) {
                String[] tuple = { _class, _init };
                _handler.add(tuple);
                _handlerParams.add(_param);
                _isHandler = false;
            }
        } else if (_isLanguage) {
            language = _content.toString().trim();
            _isLanguage = false;
        } else if (_isTempDir) {
            _tempDir = _content.toString().trim();
            _isTempDir = false;
        } else if (_isMixVsn) {
            _mixVsn = _content.toString().trim();
            _isMixVsn = false;
        } else if (_isSigBytes) {
            try {
                _sigBytes = Integer.parseInt(_content.toString().trim());
            } catch (NumberFormatException e) {
            }
            _isSigBytes = false;
        } else if (_isEncoding) {
            _encoding = _content.toString().trim();
            _isEncoding = false;
        } else if (_isJhoveHome) {
            _jhoveHome = _content.toString().trim();
            _isJhoveHome = false;
        } else if (_isLogLevel) {
            _logLevel = _content.toString().trim();
            _isLogLevel = false;
        } else if (_isBufferSize) {
            try {
                _bufferSize = Integer.parseInt(_content.toString().trim());
            } catch (NumberFormatException e) {
                /* Just ignore a malformed number */
            }
            _isBufferSize = false;
        }
    }

    /**
     * EntityResolver designed to locate the config schema. It tries to find it
     * as a local resource.
     * 
     * It appears that not all SAX implementations will actually call this
     * function for schema resolution, so this isn't a guarantee that the
     * schema in the config file won't be called directly. But hopefully
     * it will cut down on the burden on the server with the official
     * schema copy.
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
        if (systemId.endsWith(configSchemaName)) {
            try {
                URL resURL = this.getClass().getResource(configSchemaName);
                InputStream strm = resURL.openStream();
                return new InputSource(strm);
            } catch (Exception e) {
            }
        }
        // If we couldn't get the local resource, use default location methods
        return super.resolveEntity(publicId, systemId);
    }
}
