/**********************************************************************
 * JHOVE - JSTOR/Harvard Object Validation Environment
 * Copyright 2005-2007 by the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.SAXParserFactory;

import org.openpreservation.jhove.ReleaseDetails;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.harvard.hul.ois.jhove.handler.AuditHandler;
import edu.harvard.hul.ois.jhove.handler.JsonHandler;
import edu.harvard.hul.ois.jhove.handler.TextHandler;
import edu.harvard.hul.ois.jhove.handler.XmlHandler;
import edu.harvard.hul.ois.jhove.module.BytestreamModule;

/**
 * The JHOVE engine, providing all base services necessary to build an
 * application.
 *
 * More than one JhoveBase may be instantiated and process files in concurrent
 * threads. Any one instance must not be multithreaded.
 */
public class JhoveBase {

    public static final String _name = "JhoveBase";

    private static final ReleaseDetails RELEASE_DETAILS =
            ReleaseDetails.getInstance();

    private static final String JAVA_TEMP_DIR_PROP_KEY = "java.io.tmpdir";
    private static final String HUL_PROPERTY_PREFIX = "edu.harvard.hul.ois.";
    private static final String JHOVE_PROPERTY_PREFIX = HUL_PROPERTY_PREFIX
            + "jhove.";

    /** JHOVE buffer size property. */
    private static final String BUFFER_PROPERTY = JHOVE_PROPERTY_PREFIX
            + "bufferSize";

    /** JHOVE home directory */
    private static final String JHOVE_DIR = "jhove";

    /** JHOVE configuration directory */
    private static final String CONFIG_DIR = "conf";

    /** JHOVE configuration file property. */
    private static final String CONFIG_PROPERTY = JHOVE_PROPERTY_PREFIX
            + "config";

    /** JHOVE default buffer size. */
    private static final int DEFAULT_BUFFER = 131072;

    /** JHOVE default character encoding. */
    private static final String DEFAULT_ENCODING = "utf-8";

    /** Default temporary directory. */
    private static final String DEFAULT_TEMP =
            System.getProperty(JAVA_TEMP_DIR_PROP_KEY);

    /** JHOVE encoding property. */
    private static final String ENCODING_PROPERTY = JHOVE_PROPERTY_PREFIX
            + "encoding";

    /** JHOVE SAX parser class property. */
    private static final String SAX_PROPERTY = JHOVE_PROPERTY_PREFIX
            + "saxClass";

    /** JHOVE temporary directory property. */
    private static final String TEMPDIR_PROPERTY = JHOVE_PROPERTY_PREFIX
            + "tempDirectory";

    /** Flag for aborting activity. */
    protected boolean _abort;
    /** Buffer size for buffered I/O. */
    protected int _bufferSize;
    protected boolean _checksum;
    /** Configuration file pathname. */
    protected String _configFile;
    /** Selected encoding. */
    protected String _encoding;
    /** Ordered list of output handlers. */
    protected List<OutputHandler> _handlerList;
    /** Map of output handlers (for fast access by name). */
    protected Map<String, OutputHandler> _handlerMap;
    /** JHOVE home directory. */
    protected String _jhoveHome;
    /** Ordered list of modules. */
    protected List<Module> _moduleList;
    /** Map of modules (for fast access by name). */
    protected Map<String, Module> _moduleMap;
    protected String _outputFile;
    /** SAX parser class. */
    protected String _saxClass;
    protected boolean _showRaw;
    protected boolean _signature;
    /** Temporary directory. */
    protected String _tempDir;
    /** MIX version. */
    protected String _mixVsn;
    /** Number of bytes for fake signature checking. */
    protected int _sigBytes;
    /** Directory for saving files. */
    protected File _saveDir;
    /** Byte count for digital object */
    protected long _nByte;
    /** Callback function to check for termination. */
    Callback _callback;
    /** Current URL connection. */
    protected URLConnection _conn;
    /** Thread currently parsing a document. */
    protected Thread _currentThread;

    /** Logger for this class. */
    protected Logger _logger;
    /** Logger resource bundle. */
    protected String _logLevel;

    /**
     * Class constructor.
     *
     * Instantiates a <code>JhoveBase</code> object.
     *
     * @throws JhoveException
     *             if invoked with a JVM lower than 1.8
     */
    public JhoveBase() throws JhoveException {

        _logger = Logger.getLogger("edu.harvard.hul.ois.jhove");
        _logger.setLevel(Level.SEVERE); // May be changed by config file

        // Make sure we have a satisfactory version of Java.
        String version = System.getProperty("java.vm.version");
        if (version.compareTo("1.8.0") < 0) {
            _logger.severe(CoreMessageConstants.EXC_JAVA_VER_INCMPT);
            throw new JhoveException(CoreMessageConstants.EXC_JAVA_VER_INCMPT);
        }

        // Tell any HTTPS connections to be accepted automatically.
        HttpsURLConnection.setDefaultHostnameVerifier(
                new NaiveHostnameVerifier());

        // Initialize the engine.
        _moduleList = new ArrayList<>(20);
        _moduleMap = new TreeMap<>();

        _handlerList = new ArrayList<>();
        _handlerMap = new TreeMap<>();

        _abort = false;
        _bufferSize = -1;
        _checksum = false;
        _showRaw = false;
        _signature = false;
        _callback = null;
    }

    /**
     * Initializes the JHOVE engine.
     *
     * This method parses the configuration file and initialises the JHOVE
     * engine based on the values parsed.
     *
     * Version 1.11 would create the configuration file if not found. Version
     * 1.12 changes this behaviour. The file path supplied must be resolvable
     * to an existing JHOVE config file.
     *
     * @param configFile
     *            Configuration file pathname
     * @param saxClass
     *            a SAX parser class, will use JVM default if not supplied
     * @throws JhoveException
     *            when anything goes wrong
     */
    public void init(String configFile, String saxClass) throws JhoveException {

        if (configFile == null) {
            throw new JhoveException(CoreMessageConstants.EXC_CONF_FILE_LOC_MISS);
        }
        _configFile = configFile;

        File config = new File(_configFile);

        if (!config.exists() || !config.isFile()) {
            throw new JhoveException(config.getAbsolutePath() +
                    CoreMessageConstants.EXC_CONF_FILE_INVAL);
        }

        _saxClass = saxClass;
        XMLReader parser = null;

        try {
            if (saxClass == null) {
                // Use Java 1.4 methods to create default parser.
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setNamespaceAware(true);
                parser = factory.newSAXParser().getXMLReader();
            } else {
                parser = XMLReaderFactory.createXMLReader(saxClass);
            }
        } catch (Exception e) {
            // If we can't get a SAX parser, we're stuck.
            throw new JhoveException(CoreMessageConstants.EXC_SAX_PRSR_MISS
                    + saxClass, e);
        }

        _logger.info("Using SAX parser " + parser.getClass().getName());
        ConfigHandler configHandler = new ConfigHandler();
        parser.setContentHandler(configHandler);
        parser.setEntityResolver(configHandler);

        // Attempt to set schema awareness to avoid validation errors.
        try {
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setProperty("http://java.sun.com/xml/jaxp/"
                    + "properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
        } catch (SAXException saxe) {
        }

        try {
            String canonicalPath = config.getCanonicalPath();
            String fileURL = "file://";
            if (canonicalPath.charAt(0) != '/') {
                fileURL += Character.toString('/');
            }
            fileURL += canonicalPath;
            parser.parse(fileURL);
        } catch (IOException ioe) {
            throw new JhoveException(CoreMessageConstants.EXC_CONF_FILE_UNRDBL
                    + configFile, ioe);
        } catch (SAXException saxe) {
            throw new JhoveException(CoreMessageConstants.EXC_CONF_FILE_UNPRS
                    + saxe.getMessage(), saxe);
        }

        // Update the application state to reflect the configuration file,
        // if necessary.
        _jhoveHome = configHandler.getJhoveHome();

        // Set language code if changed in properties
        if (!configHandler.getLanguage().isEmpty()) {
            System.setProperty("module.language", configHandler.getLanguage());
        }

        _encoding = configHandler.getEncoding();
        if (_encoding == null) {
            _encoding = getFromProperties(ENCODING_PROPERTY);
            if (_encoding == null) {
                _encoding = DEFAULT_ENCODING;
            }
        }

        _tempDir = configHandler.getTempDir();
        if (_tempDir == null) {
            _tempDir = getFromProperties(TEMPDIR_PROPERTY);
            if (_tempDir == null) {
                _tempDir = DEFAULT_TEMP;
            }
        }

        // Get the MIX version. If not specified, defaults to 2.0.
        _mixVsn = configHandler.getMixVsn();
        if (_mixVsn == null) {
            _mixVsn = "2.0";
        }

        // Get the maximum number of bytes to examine when doing
        // pseudo-signature checking
        _sigBytes = configHandler.getSigBytes();

        // If a log level was specified in the config file,
        // attempt to set it, unless it was already explicitly set.
        if (_logLevel == null) {
            _logLevel = configHandler.getLogLevel();
            if (_logLevel != null) {
                try {
                    _logger.setLevel(Level.parse(_logLevel));
                } catch (SecurityException se) {
                    // Can't set the logger level due to security exception
                }
            }
        }

        _bufferSize = configHandler.getBufferSize();
        if (_bufferSize < 0) {
            String size = getFromProperties(BUFFER_PROPERTY);
            if (size != null) {
                try {
                    _bufferSize = Integer.parseInt(size);
                } catch (NumberFormatException nfe) {
                    // Not a valid integer, so ignore it.
                }
            }
            if (_bufferSize < 0) {
                _bufferSize = DEFAULT_BUFFER;
            }
        }

        // Retrieve the ordered list of modules
        List<ModuleInfo> modList = configHandler.getModule();
        List<List<String>> params = configHandler.getModuleParams();
        for (int i = 0; i < modList.size(); i++) {
            ModuleInfo modInfo = modList.get(i);
            List<String> param = params.get(i);
            try {
                Class<?> cl = Class.forName(modInfo.clas);
                Module module = (Module) cl.newInstance();
                module.init(modInfo.init);
                module.setDefaultParams(param);

                _moduleList.add(module);
                _moduleMap.put(module.getName().toLowerCase(), module);
                _logger.fine("Initialized " + module.getName());
            } catch (Exception e) {
                throw new JhoveException(CoreMessageConstants.EXC_MODL_INST_FAIL
                        + modInfo.clas, e);
            }
        }

        // Retrieve the list of output handlers
        List<String[]> hanList = configHandler.getHandler();
        params = configHandler.getHandlerParams();
        for (int i = 0; i < hanList.size(); i++) {
            String[] tuple = hanList.get(i);
            List<String> param = params.get(i);
            try {
                Class<?> cl = Class.forName(tuple[0]);
                OutputHandler handler = (OutputHandler) cl.newInstance();
                handler.setDefaultParams(param);

                _handlerList.add(handler);
                _handlerMap.put(handler.getName().toLowerCase(), handler);
            } catch (Exception e) {
                throw new JhoveException(CoreMessageConstants.EXC_HNDL_INST_FAIL
                        + tuple[0], e);
            }
        }

        // The Bytestream module and the Text, XML, and Audit output handlers
        // are always statically loaded.

        Module module = new BytestreamModule();
        module.setDefaultParams(new ArrayList<>());
        _moduleList.add(module);
        _moduleMap.put(module.getName().toLowerCase(), module);

        OutputHandler handler = new TextHandler();
        handler.setDefaultParams(new ArrayList<>());
        _handlerList.add(handler);
        _handlerMap.put(handler.getName().toLowerCase(), handler);

        handler = new XmlHandler();
        handler.setDefaultParams(new ArrayList<>());
        _handlerList.add(handler);
        _handlerMap.put(handler.getName().toLowerCase(), handler);

        handler = new JsonHandler();
        handler.setDefaultParams(new ArrayList<>());
        _handlerList.add(handler);
        _handlerMap.put(handler.getName().toLowerCase(), handler);

        handler = new AuditHandler();
        handler.setDefaultParams(new ArrayList<>());
        _handlerList.add(handler);
        _handlerMap.put(handler.getName().toLowerCase(), handler);
    }

    /**
     * Sets a callback object for tracking progress.
     * By default, the callback is <code>null</code>.
     */
    public void setCallback(Callback callback) {
        _callback = callback;
    }

    /**
     * Processes a file or directory, or outputs information. If
     * <code>dirFileOrUri</code> is null, Does one of the following:
     * <ul>
     * <li>If module is non-null, provides information about the module.
     * <li>Otherwise if <code>aboutHandler</code> is non-null, provides
     * information about that handler.
     * <li>If they're both null, provides information about the application.
     * </ul>
     *
     * @param app
     *            The App object for the application
     * @param module
     *            The module to be used
     * @param aboutHandler
     *            If specified, the handler about which info is requested
     * @param handler
     *            The handler for processing the output
     * @param outputFile
     *            Name of the file to which output should go
     * @param dirFileOrUri
     *            One or more file names or URI's to be analyzed
     */
    public void dispatch(App app, Module module, OutputHandler aboutHandler,
            OutputHandler handler, String outputFile, String[] dirFileOrUri)
            throws Exception {

        _abort = false;

        // If no handler is specified, use the default TEXT handler.
        if (handler == null) {
            handler = _handlerMap.get("text");
        }
        handler.reset();
        _outputFile = outputFile;

        _logger.info("Preparing " + handler.getName()
                + " handler to write to \""
                + (_outputFile == null ? "STDOUT" : _outputFile) + "\"");

        handler.setApp(app);
        handler.setBase(this);
        handler.setWriter(makeWriter(_outputFile, _encoding));

        handler.showHeader();

        if (dirFileOrUri == null) {
            if (module != null) {
                // Show info about module.
                module.applyDefaultParams();
                module.show(handler);
            } else if (aboutHandler != null) {
                // Show info about handler.
                handler.show(aboutHandler);
            } else {
                // Show info about application
                app.show(handler);
            }
        } else {
            for (String aDirFileOrUri : dirFileOrUri) {
                if (!process(app, module, handler, aDirFileOrUri)) {
                    break;
                }
            }
        }

        handler.showFooter();
        handler.close();
    }

    /**
     * Returns <code>false</code> if processing should be aborted.
     * Calls itself recursively for directories.
     */
    public boolean process(App app, Module module, OutputHandler handler,
            String dirFileOrUri) throws Exception {

        if (_abort) {
            return false;
        }
        _logger.info("Processing \"" + dirFileOrUri + "\"");
        File file = null;
        boolean isTemp = false;
        long lastModified = -1;

        // First see if we have a URI, if not it is a directory or a file.
        URI uri = null;
        try {
            uri = new URI(dirFileOrUri);
        } catch (URISyntaxException use) {
            // We may get an exception on Windows paths,
            // if so then fall through and try for a file.
        }
        RepInfo info = new RepInfo(dirFileOrUri);
        if (uri != null && uri.isAbsolute()) {
            URL url = null;
            try {
                url = uri.toURL();
            } catch (MalformedURLException mue) {
                throw new JhoveException(CoreMessageConstants.EXC_URI_CONV_FAIL
                        + dirFileOrUri);
            }
            URLConnection conn = url.openConnection();
            _conn = conn;
            if (conn instanceof HttpsURLConnection) {
                try {
                    TrustManager[] tm = { new RelaxedX509TrustManager() };
                    SSLContext sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(null, tm, new java.security.SecureRandom());
                    SSLSocketFactory sf = sslContext.getSocketFactory();
                    ((HttpsURLConnection) conn).setSSLSocketFactory(sf);
                    int code = ((HttpsURLConnection) conn).getResponseCode();
                    if (200 > code || code >= 300) {
                        throw new JhoveException(CoreMessageConstants.EXC_URL_NOT_FND
                                + dirFileOrUri);
                    }
                } catch (Exception e) {
                    throw new JhoveException(CoreMessageConstants.EXC_URL_NOT_FND + dirFileOrUri);
                }
            }
            lastModified = conn.getLastModified();

            // Convert the URI to a temporary file and use for the input stream.
            try {
                file = connToTempFile(conn, info);
                if (file == null) {
                    // User aborted
                    return false;
                }
                isTemp = true;
            } catch (IOException ioe) {
                _conn = null;
                String message = "Cannot read URL: " + dirFileOrUri;
                _logger.info(message);
                String ioeMessage = ioe.getMessage();
                if (ioeMessage != null) {
                    message += " (" + ioeMessage + ")";
                }
                throw new JhoveException(message);
            }
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).disconnect();
            }
            _conn = null;
        } else {
            file = new File(dirFileOrUri);
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            info = null; // Free up unused RepInfo before recursing

            // Sort the files in ascending order by filename.
            Arrays.sort(files);

            handler.startDirectory(file.getCanonicalPath());
            for (int i = 0; i < files.length; i++) {
                if (!process(app, module, handler, files[i].getCanonicalPath())) {
                    return false;
                }
            }
            handler.endDirectory();
        } else {

            if (!file.exists()) {
                _logger.info("File not found: \"" + file.getPath() + "\"");
                info.setMessage(new ErrorMessage(CoreMessageConstants.JHOVE_CORE_1));
                info.setWellFormed(RepInfo.UNDETERMINED);
                info.show(handler);
            } else if (!file.isFile() || !file.canRead()) {
                _logger.info("File cannot be read: \"" + file.getPath() + "\"");
                info.setMessage(new ErrorMessage(CoreMessageConstants.JHOVE_CORE_2));
                info.setWellFormed(RepInfo.UNDETERMINED);
                info.show(handler);
            } else if (handler.okToProcess(dirFileOrUri)) {
                info.setSize(file.length());
                if (lastModified < 0) {
                    lastModified = file.lastModified();
                }
                info.setLastModified(new Date(lastModified));

                if (module != null) {
                    try {
                        // Invoke the specified module.
                        if (!processFile(app, module, false, file, info)) {
                            return false;
                        }
                    } catch (Exception e) {
                        _logger.log(Level.SEVERE,
                                CoreMessageConstants.JHOVE_CORE_5.getMessage(), e);
                        info.setMessage(new ErrorMessage(
                                CoreMessageConstants.JHOVE_CORE_5));
                        info.setWellFormed(RepInfo.UNDETERMINED);
                    }
                } else {

                    _logger.info("Discovering compatible modules...");

                    // Invoke all modules until one returns well-formed. If a
                    // module doesn't know how to validate, we don't want to
                    // throw arbitrary files at it, so we'll skip it.
                    for (Module mod : _moduleList) {

                        if (mod.hasFeature("edu.harvard.hul.ois.jhove.canValidate")) {
                            RepInfo infc = (RepInfo) info.clone();

                            try {
                                if (!processFile(app, mod, false, file, infc)) {
                                    return false;
                                }
                                if (infc.getWellFormed() == RepInfo.TRUE) {
                                    info.copy(infc);
                                    break;
                                }
                                // We want to know what modules matched the
                                // signature, so we force the sigMatch
                                // property to be persistent.
                                info.setSigMatch(infc.getSigMatch());

                            } catch (Exception e) {
                                // The assumption is that in trying to analyze
                                // the wrong type of file, the module may go off
                                // its track and throw an exception, so we just
                                // continue on to the next module.
                                _logger.fine("JHOVE caught exception: "
                                        + e.getClass().getName());
                            }
                        }
                    }
                }
                info.show(handler);
            }
        }
        if (file != null && isTemp) {
            file.delete();
        }
        return true;
    }

    /**
     * Saves a URLConnection's data stream to a temporary file. This may be
     * interrupted asynchronously by calling <code>abort</code>, in which
     * case it will delete the temporary file and return <code>null</code>.
     */
    public File connToTempFile(URLConnection conn, RepInfo info)
            throws IOException {

        File tempFile;
        try {
            tempFile = newTempFile();
        } catch (IOException ioe) {
            // Throw a more meaningful exception
            throw new IOException(CoreMessageConstants.EXC_TEMP_FILE_CRT);
        }

        OutputStream outstrm = null;
        DataInputStream instrm = null;
        if (_bufferSize > 0) {
            outstrm = new BufferedOutputStream(new FileOutputStream(tempFile),
                    _bufferSize);
        } else {
            outstrm = new BufferedOutputStream(new FileOutputStream(tempFile));
        }
        try {
            if (_bufferSize > 0) {
                instrm = new DataInputStream(new BufferedInputStream(
                        conn.getInputStream(), _bufferSize));
            } else {
                instrm = new DataInputStream(new BufferedInputStream(
                        conn.getInputStream()));
            }
        } catch (UnknownHostException uhe) {
            tempFile.delete();
            throw new IOException(uhe.toString());
        } catch (IOException ioe) {
            // IOExceptions other than UnknownHostException
            tempFile.delete();
            throw ioe;
        } catch (Exception e) {
            // Arbitrary URL's may throw unpredictable expressions;
            // treat them as IOExceptions
            tempFile.delete();
            throw new IOException(e.toString());
        }

        Checksummer ckSummer = null;
        if (_checksum) {
            ckSummer = new Checksummer();
        }
        _nByte = 0;

        int appModulo = 4000;

        // Copy the connection stream to the file.
        // While we're here, calculate the checksums.
        try {
            byte by;
            for (;;) {
                // Make sure other threads can get in occasionally to cancel
                if ((_nByte % appModulo) == 0) {
                    Thread.yield();
                    if (_callback != null) {
                        _callback.callback(1, _nByte);
                    }
                    // In order to avoid doing too many callbacks, limit
                    // the checking to a number of bytes at least 1/10 of
                    // the bytes read so far.
                    if (appModulo * 10 < _nByte) {
                        appModulo = (int) (_nByte / 10);
                    }
                }
                if (_abort) {
                    // Asynchronous abort requested. Clean up.
                    instrm.close();
                    outstrm.close();
                    tempFile.delete();
                    return null;
                }
                int ch = instrm.readUnsignedByte();
                if (ckSummer != null) {
                    ckSummer.update(ch);
                }
                by = Checksum.unsignedByteToByte(ch);
                _nByte++;
                outstrm.write(by);
            }
        } catch (EOFException eofe) {
            // This is the normal way for detecting we're done.
        }

        // The caller is responsible for disconnecting conn.
        instrm.close();
        outstrm.close();

        // Update RepInfo
        info.setSize(_nByte);
        if (ckSummer != null) {
            info.setChecksum(new Checksum(ckSummer.getCRC32(),
                    ChecksumType.CRC32));
            String value = ckSummer.getMD5();
            if (value != null) {
                info.setChecksum(new Checksum(value, ChecksumType.MD5));
            }
            value = ckSummer.getSHA1();
            if (value != null) {
                info.setChecksum(new Checksum(value, ChecksumType.SHA1));
            }
            value = ckSummer.getSHA256();
            if (value != null) {
                info.setChecksum(new Checksum(value, ChecksumType.SHA256));
            }
        }
        return tempFile;
    }

    /**
     * Aborts an activity. This simply sets a flag; whether anything is aborted
     * depends on what activity is happening.
     */
    public void abort() {
        _abort = true;
        HttpsURLConnection conn = null;
        if (_conn instanceof HttpsURLConnection) {
            conn = (HttpsURLConnection) _conn;
        }
        // If we're stuck in socket I/O, then there is no way
        // to kill the thread cleanly. Wait a few seconds,
        // and if we're still not terminated, pull the plug on
        // the socket.
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ie) {
        }
        if (conn != null) {
            // This is a non-deprecated way of bringing the connection
            // to a screeching halt. disconnect will (we hope) close
            // the underlying socket, killing any hanging I/O.
            conn.disconnect();
        }
    }

    /**
     * Processes the file. Returns <code>false</code> if aborted, or if
     * the module is incapable of validation. This shouldn't be called if
     * the module doesn't have the validation feature.
     */
    public boolean processFile(App app, Module module, boolean verbose,
            File file, RepInfo info) throws Exception {

        if (!module.hasFeature("edu.harvard.hul.ois.jhove.canValidate")) {
            return false;
        }

        _logger.info("Processing \"" + file.getPath() + "\" with "
                + module.getName() + " module");

        if (_callback != null) {
            _callback.callback(2, info.getUri());
        }
        module.setApp(app);
        module.setBase(this);
        module.setVerbosity(verbose ? Module.MAXIMUM_VERBOSITY
                : Module.MINIMUM_VERBOSITY);
        module.applyDefaultParams();
        if (module.isRandomAccess()) {

            // Module needs random access input.
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            try {
                if (_signature) {
                    module.checkSignatures(file, raf, info);
                } else {
                    module.parse(raf, info);
                }
            } finally {
                raf.close();
            }
        } else {

            // Module accepts stream input.
            InputStream stream = new FileInputStream(file);
            try {
                if (_signature) {
                    module.checkSignatures(file, stream, info);
                } else {
                    int parseIndex = module.parse(stream, info, 0);
                    // If parse returns non-zero, reparse with a fresh stream.
                    while (parseIndex != 0) {
                        stream.close();
                        stream = new FileInputStream(file);
                        parseIndex = module.parse(stream, info, parseIndex);
                    }
                }
            } finally {
                stream.close();
            }
        }
        return true; // Successful processing
    }

    /**
     * Creates a temporary file with a unique name. The file will be deleted
     * when the application exits.
     */
    public File tempFile() throws IOException {
        File file;

        // If no temporary directory has been specified,
        // use Java's default temporary directory.
        if (_tempDir == null) {
            file = File.createTempFile("JHOV", "");
        } else {
            File dir = new File(_tempDir);
            file = File.createTempFile("JHOV", "", dir);
        }
        file.deleteOnExit();

        return file;
    }

    /**
     * Returns the abort flag.
     */
    public boolean getAbort() {
        return _abort;
    }

    /**
     * Returns buffer size. A value of -1 signifies that the invoking code
     * should assume the default buffer size.
     */
    public int getBufferSize() {
        return _bufferSize;
    }

    /**
     * Returns the configuration file.
     */
    public String getConfigFile() {
        return _configFile;
    }

    /**
     * Returns the engine date (the date at which this instance was created).
     */
    public Date getDate() {
        return RELEASE_DETAILS.getBuildDate();
    }

    /**
     * Returns the output encoding.
     */
    public String getEncoding() {
        return _encoding;
    }

    /**
     * Returns a handler by name.
     */
    public OutputHandler getHandler(String name) {
        OutputHandler handler = null;
        if (name != null) {
            handler = _handlerMap.get(name.toLowerCase());
        }
        return handler;
    }

    /**
     * Returns map of handler names to handlers.
     */
    public Map<String, OutputHandler> getHandlerMap() {
        return _handlerMap;
    }

    /**
     * Returns the list of handlers.
     */
    public List<OutputHandler> getHandlerList() {
        return _handlerList;
    }

    /**
     * Returns the JHOVE home directory.
     */
    public String getJhoveHome() {
        return _jhoveHome;
    }

    /**
     * Returns a module by name.
     */
    public Module getModule(String name) {
        Module module = null;
        if (name != null) {
            module = _moduleMap.get(name.toLowerCase());
        }
        return module;
    }

    /**
     * Returns the map of module names to modules.
     */
    public Map<String, Module> getModuleMap() {
        return _moduleMap;
    }

    /**
     * Returns the list of modules.
     */
    public List<Module> getModuleList() {
        return _moduleList;
    }

    /**
     * Returns the engine name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the output file.
     */
    public String getOuputFile() {
        return _outputFile;
    }

    /**
     * Returns the engine release.
     */
    public String getRelease() {
        return RELEASE_DETAILS.getVersion();
    }

    /**
     * Returns the engine rights statement.
     */
    public String getRights() {
        return RELEASE_DETAILS.getRights();
    }

    /**
     * Returns the SAX class.
     */
    public String getSaxClass() {
        return _saxClass;
    }

    /**
     * Returns the temporary directory path.
     */
    public String getTempDirectory() {
        return _tempDir;
    }

    /**
     * Returns the maximum number of bytes to check, for modules that look for
     * an indefinitely positioned signature or check the first sigBytes bytes
     * in lieu of a signature.
     */
    public int getSigBytes() {
        return _sigBytes;
    }

    /**
     * Returns the directory designated for saving files. This is simply the
     * directory most recently set by <code>setSaveDirectory</code>.
     */
    public File getSaveDirectory() {
        return _saveDir;
    }

    /**
     * Returns <code>true</code> if checksums are requested.
     */
    public boolean getChecksumFlag() {
        return _checksum;
    }

    /**
     * Returns <code>true</code> if raw output is requested. Raw output means
     * numeric rather than symbolic output; its exact interpretation is up to
     * the module, but generally applies to named flags.
     */
    public boolean getShowRawFlag() {
        return _showRaw;
    }

    /**
     * Returns the "check signature only" flag.
     */
    public boolean getSignatureFlag() {
        return _signature;
    }

    /**
     * Returns the requested MIX schema version.
     */
    public String getMixVersion() {
        return _mixVsn;
    }

    /**
     * Sets the buffer size. A value of -1 signifies that the invoking code will
     * assume the default buffer size.
     *
     * Any non-negative value less than 1024 will result in a buffer size of
     * 1024.
     */
    public void setBufferSize(int bufferSize) {
        if (bufferSize >= 0 && bufferSize < 1024) {
            _bufferSize = 1024;
        } else {
            _bufferSize = bufferSize;
        }
    }

    /**
     * Sets the output encoding.
     */
    public void setEncoding(String encoding) {
        _encoding = encoding;
    }

    /**
     * Sets the temporary directory path.
     */
    public void setTempDirectory(String tempDir) {
        _tempDir = tempDir;
    }

    /**
     * Sets the log level. The value should be the name of a predefined instance
     * of java.util.logging.Level, e.g., "WARNING", "INFO", "ALL". This will
     * override the config file setting.
     */
    public void setLogLevel(String level) {
        _logLevel = level;
        if (level != null) {
            try {
                _logger.setLevel(Level.parse(_logLevel));
            } catch (SecurityException se) {
            }
        }
    }

    /**
     * Sets the value to be returned by <code>doChecksum</code>.
     */
    public void setChecksumFlag(boolean checksum) {
        _checksum = checksum;
    }

    /**
     * Sets the value to be returned by <code>getShowRawFlag</code>, which
     * determines if only raw numeric values should be output.
     */
    public void setShowRawFlag(boolean raw) {
        _showRaw = raw;
    }

    /**
     * Sets the "check signature only" flag.
     */
    public void setSignatureFlag(boolean signature) {
        _signature = signature;
    }

    /**
     * Sets the default directory for subsequent save operations.
     */
    public void setSaveDirectory(File dir) {
        _saveDir = dir;
    }

    /**
     * Sets the current thread for parsing.
     */
    public void setCurrentThread(Thread t) {
        _currentThread = t;
    }

    /**
     * Sets the maximum number of bytes to check, for modules that look for
     * an indefinitely positioned signature or check the first sigBytes bytes
     * in lieu of a signature.
     *
     * @param sigBytes max number of bytes to check
     */
    public void setSigBytes(int sigBytes) {
        _sigBytes = sigBytes;
    }

    /**
     * Resets the abort flag. This must be called at the beginning of any
     * activity for which the abort flag may subsequently be set.
     */
    public void resetAbort() {
        _abort = false;
    }

    /**
     * Uses the user.home property to locate the configuration file. The file is
     * expected to be in the subdirectory named by CONFIG_DIR under the home
     * directory, and to be named <code>jhove.conf</code>. Returns
     * <code>null</code> if no such file is found.
     */
    public static String getConfigFileFromProperties() {
        String configFile = getFromProperties(CONFIG_PROPERTY);
        if (configFile == null) {
            try {
                String fs = System.getProperty("file.separator");
                configFile = System.getProperty("user.home") + fs + JHOVE_DIR
                        + fs + CONFIG_DIR + fs + "jhove.conf";
            } catch (Exception e) {
            }
        }
        return configFile;
    }

    /**
     * Returns the value of the property
     * <code>edu.harvard.hul.ois.jhove.saxClass</code>, which should be the name
     * of the main SAX class. Returns <code>null</code> if no such property has
     * been set up.
     */
    public static String getSaxClassFromProperties() {
        return getFromProperties(SAX_PROPERTY);
    }

    /**
     * Returns a named value from the properties file.
     */
    public static String getFromProperties(String name) {
        String value = null;
        try {
            String fs = System.getProperty("file.separator");
            Properties props = new Properties();
            String propsFile = System.getProperty("user.home") + fs + JHOVE_DIR
                    + fs + "jhove.properties";
            FileInputStream stream = new FileInputStream(propsFile);
            props.load(stream);
            stream.close();

            value = props.getProperty(name);
        } catch (Exception e) {
        }
        return value;
    }

    /**
     * Creates an output PrintWriter.
     *
     * @param outputFile
     *            Output filepath. If null, writer goes to System.out.
     * @param encoding
     *            Character encoding. Must not be null.
     */
    protected static PrintWriter makeWriter(String outputFile, String encoding)
            throws JhoveException {

        PrintWriter output = null;
        OutputStreamWriter osw = null;
        if (outputFile != null) {
            try {
                FileOutputStream stream = new FileOutputStream(outputFile);
                osw = new OutputStreamWriter(stream, encoding);
                output = new PrintWriter(osw);
            } catch (UnsupportedEncodingException uee) {
                throw new JhoveException(
                    MessageFormat.format(
                        CoreMessageConstants.JHOVE_CORE_4.getMessage(),
                        encoding));
            } catch (FileNotFoundException fnfe) {
                throw new JhoveException(CoreMessageConstants.EXC_FILE_OPEN
                        + outputFile);
            }
        }
        if (output == null) {
            try {
                osw = new OutputStreamWriter(System.out, encoding);
            } catch (UnsupportedEncodingException uee) {
                throw new JhoveException(
                    MessageFormat.format(
                        CoreMessageConstants.JHOVE_CORE_4.getMessage(),
                        encoding));
            }
            output = new PrintWriter(osw);
        }
        return output;
    }

    /**
     * Creates a temporary file with a unique name. The file will be deleted
     * when the application exits.
     */
    public File newTempFile() throws IOException {
        return tempFile();
    }

    /**
     * A HostnameVerifier for HTTPS connections that will never ask for
     * certificates.
     */
    private static class NaiveHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * A TrustManager which should accept all certificates.
     */
    private static class RelaxedX509TrustManager implements X509TrustManager {
        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] chain) {
            return true;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] chain) {
            return true;
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] chain) {
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] chain, String s) {
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] chain) {
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] chain, String s) {
        }
    }
}
