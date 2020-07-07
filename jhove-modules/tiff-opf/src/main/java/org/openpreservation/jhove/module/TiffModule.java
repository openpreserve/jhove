/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment Copyright 2003-2012 by
 * JSTOR and the President and Fellows of Harvard College
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 **********************************************************************/

package org.openpreservation.jhove.module;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.easyinnova.implementation_checker.ImplementationCheckerValidator;
import com.easyinnova.implementation_checker.ValidationResult;
import com.easyinnova.implementation_checker.rules.RuleResult;
import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.reader.TiffReader;

import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.Document;
import edu.harvard.hul.ois.jhove.DocumentType;
import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.ExternalSignature;
import edu.harvard.hul.ois.jhove.Identifier;
import edu.harvard.hul.ois.jhove.IdentifierType;
import edu.harvard.hul.ois.jhove.InfoMessage;
import edu.harvard.hul.ois.jhove.InternalSignature;
import edu.harvard.hul.ois.jhove.ModuleBase;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.Signature;
import edu.harvard.hul.ois.jhove.SignatureType;
import edu.harvard.hul.ois.jhove.SignatureUseType;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * Module for identification and validation of TIFF files.
 */
public class TiffModule extends ModuleBase {

    /**
     * Value to write as module params to the default config file.
     */
    public static final String[] defaultConfigParams = { "byteoffset=true" };

    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/

    /** Logger for this class. */
    protected Logger _logger;

    private static final String NAME = "TIFF-opf";
    private static final String RELEASE = "0.1.0";
    private static final int [] DATE = { 2020, 26, 30 };
    private static final String[] FORMAT = { "TIFF", "Tagged Image File Format" };
    private static final String COVERAGE = "TIFF 6.0; "
            + "1.0; Baseline 6.0 bilevel (known in TIFF 5.0 as Class B), ";

    /***
     * These profiles are not reported anymore (SLA, 2004-01-06)
     * "Adobe PageMaker 6.0; Adobe Photoshop 'Advanced TIFF'; " +
     ***/

    private static final String[] MIMETYPE = { "image/tiff", "image/tiff-fx",
            "image/ief" };
    private static final String WELLFORMED = "A TIFF file is well-formed if "
            + "it has a big-endian or little-endian header; at least one IFD; all "
            + "IFDs are 16-bit word aligned; all IFDs have at least one entry; "
            + "all IFD entries are sorted in ascending order by tag number; all "
            + "IFD entries specify the correct type and count; all value offsets "
            + "are 16-bit word aligned; all value offsets reference locations "
            + "within the file; and the final IFD is followed by an offset of 0";
    private static final String VALIDITY = "A TIFF file is valid if "
            + "well-formed; ImageLength, ImageWidth, and "
            + "PhotometricInterpretation tags are defined; strip or tile tags "
            + "are defined; tag values are self-consistent (see JHOVE "
            + "documentation); TileWidth and TileLength values are integral "
            + "multiples of 16; and DateTime tag is properly formatted";
    private static final String REPINFO = "Additional representation "
            + "information includes: NISO Z39.87 Digital Still Image Technical "
            + "Metadata and all other tag values";
    private static final String NOTE = null;
    private static final String RIGHTS = "Copyright 2003-2007 by JSTOR and "
            + "the President and Fellows of Harvard College. "
            + "Released under the GNU Lesser General Public License.";

    /******************************************************************
     * PRIVATE INSTANCE FIELDS.
     ******************************************************************/


    /******************************************************************
     * CLASS CONSTRUCTOR.
     ******************************************************************/

    /**
     * Instantiate a <code>TiffModule</code> object.
     */
    public TiffModule() {
        super(NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
                VALIDITY, REPINFO, NOTE, RIGHTS, true);

        _logger = Logger.getLogger("org.openpreservation.jhove");

        // Define vendor agent (HUL)
        _vendor = Agent.opfInstance();

        // Define TIFF 6.0 document with Adobe agent
        Document doc = new Document("TIFF, Revision 6.0", DocumentType.REPORT);
        Agent adobeAgent = Agent.newAdobeInstance();
        doc.setPublisher(adobeAgent);
        doc.setDate("1992-06-03");
        doc.setEdition("Final");
        doc.setIdentifier(new Identifier("http://partners.adobe.com/asn/"
                + "tech/tiff/specification.jsp", IdentifierType.URL));
        _specification.add(doc);

        // Define TIFF/EP document with ISO agent
        doc = new Document("ISO 12234-2:2001, Electronic still-picture "
                + "imaging -- Removable memory -- "
                + "Part 2: TIFF/EP image data format", DocumentType.STANDARD);
        Agent isoAgent = Agent.newIsoInstance();
        doc.setPublisher(isoAgent);
        doc.setDate("2001-10-15");
        Identifier ident = new Identifier("ISO 12234-2:2001(E)",
                IdentifierType.ISO);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define TIFF/IT document, reusing ISO agent
        doc = new Document("ISO/DIS 12639:2003, Graphic technology -- "
                + "Prepress digital data exchange -- "
                + "Tag image file format for image technology " + "(TIFF/IT)",
                DocumentType.STANDARD);
        /* This uses the same agent (ISO) as the prior doc */
        doc.setPublisher(isoAgent);
        doc.setDate("2003-09-04");
        ident = new Identifier("ISO/DIS 12639:2003(E)", IdentifierType.ISO);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Whew -- finally done with docs

        int[] sigbyteI = { 0x49, 0x49, 42, 0 };
        Signature sig = new InternalSignature(sigbyteI, SignatureType.MAGIC,
                SignatureUseType.MANDATORY_IF_APPLICABLE, 0,
                "Little-endian (least significant byte " + "first)");
        _signature.add(sig);

        int[] sigbyteM = { 0x4D, 0x4D, 0, 42 };
        sig = new InternalSignature(sigbyteM, SignatureType.MAGIC,
                SignatureUseType.MANDATORY_IF_APPLICABLE, 0,
                "Big-endian (most significant byte first)");
        _signature.add(sig);

        sig = new ExternalSignature("TIFF", SignatureType.FILETYPE,
                SignatureUseType.OPTIONAL);
        _signature.add(sig);

        sig = new ExternalSignature(".tif", SignatureType.EXTENSION,
                SignatureUseType.OPTIONAL);
        _signature.add(sig);
    }

    /******************************************************************
     * PUBLIC INSTANCE METHODS.
     *
     * Parsing methods.
     ******************************************************************/

    /**
     * Parse the TIFF for well-formedness and validity, accumulating
     * representation information.
     *
     * @param raf
     *            Open TIFF file
     * @param info
     *            Representation informatino
     */
    @Override
    public final void parse(final RandomAccessFile raf, final RepInfo info)
            throws IOException {
    	String iso = "TIFF_Baseline_Core_6_0";
    	try {
			TiffReader tr = new TiffReader();
			int readerRes = tr.readFile(raf, true);
	        switch (readerRes) {
		        case -1:
			      throw new RuntimeException("File not found");
		        case -2:
		          throw new RuntimeException("IO Exception reading file");
		        default:
		          break;
	        }
	        ImplementationCheckerValidator implementationCheckerValidator = new ImplementationCheckerValidator();
	        List<String> isos = new ArrayList<>();
	        isos.add(iso);
			Map<String, ValidationResult> resultsMap = implementationCheckerValidator.check(tr, isos);
			for (ValidationResult results: resultsMap.values()) {
		    	processResults(results, info);
	        }
		} catch (ReadTagsIOException | ReadIccConfigIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Failed to initialise TiffReader()");
		} catch (ParserConfigurationException | SAXException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Exception validating implementationCheckerValidator.check(tr, isos)");
		}
    }
    
    private void processResults(final ValidationResult results, final RepInfo info) {
    	for (RuleResult result : results.getInfos()) {
    		info.setMessage(new InfoMessage(JhoveMessages.getMessageInstance(result.getRule().getId(), formatMessage(result), result.getReference())));
    	}
    	for (RuleResult result : results.getWarnings(false)) {
    		info.setMessage(new InfoMessage(JhoveMessages.getMessageInstance(result.getRule().getId(), formatMessage(result), result.getReference())));
    	}
    	for (RuleResult result : results.getErrors()) {
    		info.setValid(false);
    		info.setMessage(new ErrorMessage(JhoveMessages.getMessageInstance(result.getRule().getId(), formatMessage(result), result.getReference())));
    	}
    }
    
    private String formatMessage(final RuleResult result) {
    	StringBuilder builder = new StringBuilder(result.getMessage());
    	builder.append(" At: ");
    	builder.append(result.getLocation());
    	return builder.toString();
    }

    /******************************************************************
     * PRIVATE INSTANCE METHODS.
     ******************************************************************/

}