package edu.harvard.hul.ois.jhove.module;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.jwat.common.Diagnosis;
import org.jwat.common.Diagnostics;
import org.jwat.common.InputStreamNoSkip;
import org.jwat.common.RandomAccessFileInputStream;
import org.jwat.gzip.GzipEntry;
import org.jwat.gzip.GzipReader;

import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.Agent.Builder;
import edu.harvard.hul.ois.jhove.AgentType;
import edu.harvard.hul.ois.jhove.Document;
import edu.harvard.hul.ois.jhove.DocumentType;
import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Identifier;
import edu.harvard.hul.ois.jhove.IdentifierType;
import edu.harvard.hul.ois.jhove.InfoMessage;
import edu.harvard.hul.ois.jhove.JhoveException;
import edu.harvard.hul.ois.jhove.ModuleBase;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.gzip.GzipEntryProperties;

/**
 * JHOVE module for identifying, validating and characterizing GZIP files.
 * Ported from the JHOVE2 GZIP module, created by lbihanic, selghissassi, nicl
 * 
 * JHOVE2 GZip module.  This module parses and validates GZip files
 * in compliance with
 * <a href="http://www.ietf.org/rfc/rfc1952.txt">RFC 1952</a> (GZIP
 * file format specification version 4.3) and supports multiple member
 * GZIP files.</p>
 * 
 * This is a non-recursive validation. It only validates the GZIP file format, 
 * not the actual content within the WARC records.
 * 
 * @author jolf@kb.dk
 */
public class GzipModule extends ModuleBase {
    /*------------ MODULE DEFINITIONS ---------------*/
    private static final Agent KB_AGENT = new Builder(
            "Royal Library of Denmark", AgentType.STANDARD)
    .address("Søren Kierkegaards Plads 1, 1219 København K, Denmark")
    .fax("+45 3393 2218")
    .web("http://kb.dk").build();

    private static final String NAME = "GZIP-kb";
    private static final String RELEASE = "0.1";
    private static final int[] DATE = {2015, 12, 8};
    private static final String[] FORMAT = {"GZIP"};
    private static final String COVERAGE = "GZIP, https://tools.ietf.org/html/rfc1952";
    private static final String[] MIMETYPE = {"application/gzip", "application/x-gzip"};
    private static final String WELLFORMED = "";
    private static final String VALIDITY = "The file is well-formed";
    private static final String REPINFO = "";
    private static final String NOTE = "";
    private static final String RIGHTS = "Copyright 2015 by The Royal Library of Denmark. " +
            "Released under the GNU Lesser General Public License.";

    /** 
     * List of Property elements for the entry of the GZIP-file. 
     * Each Property contains a map of all properties for a given entry.
     */
    private List<Property> entryProperties;

    /**
     * Constructor.
     */
    public GzipModule() {
        super(NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
                VALIDITY, REPINFO, NOTE, RIGHTS, false);
        setVendorAndSpecification();
        initialiseVariables();
    }
    
    /**
     * Sets the vendor and specification for this module.
     */
    private void setVendorAndSpecification() {
        _vendor = KB_AGENT;

        Document doc = new Document("GZIP",
                DocumentType.RFC);
        // Should probably have IIPC and others as authors
        Agent ietfAgent = new Agent.Builder("IETF", AgentType.STANDARD).web(
                "http://www.ietf.org").build();
        doc.setPublisher(ietfAgent);
        doc.setDate("1996");
        doc.setIdentifier(new Identifier("https://www.ietf.org/rfc/rfc1952.txt",
                IdentifierType.RFC));
        _specification.add(doc);
        
        // TODO figure out, why the tests fail, when the signature is added.
//        Signature sig = new ExternalSignature(".gz", SignatureType.EXTENSION,
//                SignatureUseType.OPTIONAL);
//        _signature.add(sig);
    }

    /**
     * Initializes the variables.
     */
    private void initialiseVariables() {
        entryProperties = new ArrayList<Property>();
    }

    /** Reset parameter settings.
     *  Returns to a default state without any parameters.
     */
    @Override
    public void resetParams() throws Exception {
        initialiseVariables();
    }

    @Override
    public void parse(RandomAccessFile file, RepInfo info) throws IOException {
        InputStream stream = new RandomAccessFileInputStream(file);
        parse(stream, info, 0);
    }

    @Override
    public int parse(InputStream stream, RepInfo info, int parseIndex) throws IOException {
        GzipReader reader = new GzipReader(new InputStreamNoSkip(stream), 8192);
        try {
            parseRecords(reader);

            info.setValid(reader.isCompliant());
            info.setWellFormed(reader.isCompliant());

            reportResults(reader, info);

            info.setSigMatch(_name);
            info.setFormat(_format[0]);
            info.setVersion("4.3"); // Is it really version 4.3?
            info.setMimeType(_mimeType[0]);
        } catch (Exception e) {
            info.setMessage(new ErrorMessage(e.getMessage()));
            info.setValid(false);
            info.setWellFormed(false);
        } finally {
            if(reader != null) {
                reader.close();
                reader = null;
            }
        }
        return 0;
    }
    
    /**
     * Parse GZIP entries. Parsing should be straight forward with all records accessible through the same source.
     * @param reader GZIP reader used to parse records
     * @throws EOFException if EOF occurs prematurely
     * @throws IOException if an IO error occurs while processing
     * @throws JHOVE2Exception if a serious problem needs to be reported
     */
    protected void parseRecords(GzipReader reader) throws EOFException, IOException, JhoveException {
        if (reader != null) {
            GzipEntry entry;
            while ((entry = reader.getNextEntry()) != null) {
                processEntry(entry);
                reader.diagnostics.addAll(entry.diagnostics);
            }
        } else {
            throw new JhoveException("WarcReader is has not been properly instantiated.");
        }
    }

    /**
     * Process a GZIP entry.
     * Extracts all the properties of the entry into a map, and puts this map on the list.
     * @param entry ZGIP entry from GZIP reader
     * @throws EOFException if EOF occurs prematurely
     * @throws IOException if an IO error occurs while processing
     * @throws JhoveException if a serious problem needs to be reported
     */
    protected void processEntry(GzipEntry entry) throws EOFException, IOException, JhoveException {
        GzipEntryProperties properties = new GzipEntryProperties(entry);
        Property p = new Property("Record", PropertyType.STRING, PropertyArity.MAP, properties.getProperties());
        
        entryProperties.add(p);

        entry.close();
    }
    
    /**
     * Report the results of the characterization.
     * @param reader The GZIP reader, which has read the GZIP-file. 
     * @param repInfo The representation info, where to report the results.
     * @throws JhoveException
     * @throws IOException
     */
    private void reportResults(GzipReader reader, RepInfo repInfo) throws JhoveException, IOException {
        Diagnostics<Diagnosis> diagnostics = reader.diagnostics;
        if (diagnostics.hasErrors()) {
            for (Diagnosis d : diagnostics.getErrors()) {
                repInfo.setMessage(new ErrorMessage(extractDiagnosisType(d), extractDiagnosisMessage(d)));
            }
            repInfo.setConsistent(false);
        }
        if (diagnostics.hasWarnings()) {
            // Report warnings on source object.
            for (Diagnosis d : diagnostics.getWarnings()) {
                repInfo.setMessage(new InfoMessage(extractDiagnosisType(d), extractDiagnosisMessage(d)));
            }
        }
        repInfo.setProperty(new Property("Records", PropertyType.PROPERTY, PropertyArity.LIST, entryProperties));
        repInfo.setSize(reader.getConsumed());
    }
    
    /**
     * Extracts the diagnosis type.
     * @param d The diagnosis whose type should be extracted
     * @return The type of diagnosis
     */
    private String extractDiagnosisType(Diagnosis d) {
        return d.type.name();
    }

    /**
     * Extracts the message from the diagnosis.
     * @param d The diagnosis
     * @return The message containing entity and informations.
     */
    private String extractDiagnosisMessage(Diagnosis d) {
        StringBuilder res = new StringBuilder();
        res.append("Entity: " + d.entity);
        for(String i : d.information) {
            res.append(", " + i);           
        }
        return res.toString();
    }
}
