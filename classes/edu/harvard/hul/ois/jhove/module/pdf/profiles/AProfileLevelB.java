/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/


package edu.harvard.hul.ois.jhove.module.pdf.profiles;

import edu.harvard.hul.ois.jhove.XMPHandler;
import edu.harvard.hul.ois.jhove.module.PdfModule;
import edu.harvard.hul.ois.jhove.module.PdfModuleQueryInterface;
import edu.harvard.hul.ois.jhove.module.pdf.DocNode;
import edu.harvard.hul.ois.jhove.module.pdf.PageObject;
import edu.harvard.hul.ois.jhove.module.pdf.PageTreeNode;
import edu.harvard.hul.ois.jhove.module.pdf.PdfArray;
import edu.harvard.hul.ois.jhove.module.pdf.PdfDictionary;
import edu.harvard.hul.ois.jhove.module.pdf.PdfObject;
import edu.harvard.hul.ois.jhove.module.pdf.PdfSimpleObject;
import edu.harvard.hul.ois.jhove.module.pdf.PdfStream;
import edu.harvard.hul.ois.jhove.module.pdf.PdfXMPSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParserFactory;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *  PDF profile checker for PDF/A-1 documents.
 *  See ISO draft ISO/TC171/SC2, "Document Imaging Applications
 *  Application Issues".
 *
 *  Revised to reflect the November 11, 2004 draft.  With the new
 *  terminology, this profile is specific to PDF/A-1; there may be
 *  additional standards in the PDF/A family later on.  "PDF/A"
 *  means "PDF/A-1" in the documentation of this code.
 *
 *  There are two levels of conformance, called Level A and Level B.
 *  We report these as two different profiles.  To accomplish this,
 *  we use AProfileLevelA, linked to an instance of this, which
 *  simply checks if this profile established Level A compliance.
 */
public final class AProfileLevelB extends PdfProfile
{
    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/

    //private boolean _levelA;
    /** Flag for DeviceRGB*/
    private boolean hasDevRGB;

    /** Flag for DeviceCMYK */
    private boolean hasDevCMYK;

    /** Flag for DeviceGray*/
    private boolean hasUncalCS;

    /** Allowable annotation types.  Movie, Sound and FileAttachment
     are allowed in PDF, but not in PDF/A. */
    private String[] annotTypes = {
            "Text", "Link", "FreeText", "Line", "Square", "Circle",
            "Polygon", "Polyline", "Highlight", "Underline",
            "Squiggly", "StrikeOut", "Stamp", "Caret",
            "Ink", "Popup", "Widget", "Screen",
            "PrinterMark", "TrapNet"
    };

    /** The disallowed annotation types, from section 6.5.2*/
    private String[] annotTypesNotAllowed = {
            "Movie", "Sound", "FileAttachment"
    };


    /** The following are the annotation types which are considered
     non-text annotations. */
    private String[] nonTextAnnotTypes = {
            "Link", "Line", "Square", "Circle",
            "Polygon", "Polyline", "Stamp", "Caret",
            "Ink", "Popup", "Widget", "Screen",
            "PrinterMark", "TrapNet"
    };

    /** The actions excluded by Section 6.6.1*/
    private String[] excludedActions = {
            "Launch", "Sound", "Movie", "ResetForm",
            "ImportData", "JavaScript" ,"SetState" , "NOP"
    };

    /** The only allowed named actions by Section 6.6.1*/
    private String[] allowedNamedActions = {
            "NextPage", "PrevPage", "FirstPage", "LastPage"
    };

    /** The following filters are not allowed. Section 6.1.10 */
    private String[] excludedFilters = {
            "LZWDecode"
    };

    /**The four allowed rendering intents*/
    private String[] validIntentStrings = {
            "RelativeColorimetric",
            "AbsoluteColorimetric",
            "Perceptual",
            "Saturation"
    };


    /**
     *   Constructor.
     *   Creates an AProfile object for subsequent testing.
     *
     *   @param  module   The module under which we are checking the profile.
     *
     */
    public AProfileLevelB(PdfModuleQueryInterface module)
    {
        super (module);
        _profileText = "ISO PDF/A-1, Level B";
    }




    /**
     * Returns <code>true</code> if the document satisfies the PDF/A-1, Level B
     * profile.
     *
     */
    public boolean satisfiesThisProfile ()
    {
        boolean _return = true; //so that all tests are run, even if some fails

        // The module has already done some syntactic checks.
        // If those failed, the file isn't compliant.
        if (!_module.mayBePDFACompliant ()) {
            //_levelA = false;
            //TODO: HAck, fixit
            reportReasonsForNonCompliance(_module.getReasonsForPDFANonCompliance());
            _return = false;
        }


        hasDevCMYK = false;
        hasDevRGB = false;
        hasUncalCS = false;

        /*Format of the methods:
         * All methods start by declaring a variable boolean _return = true;
         * If some check fails, _return = false;
         * In the end, after all checks return _return;
         *
         * When _return = false, use reportReason to give the reason why. The description
         * should be unique, and can be updated by the ProfileErrorBundle.properties
         *
         * Only the most serious errors should cause an immediate stop to the
         * parsing. In that case throw an exception with the reason. This exception
         * will be caught here, and the reason added to the list.
          */
        try {
            //Perform the various checks. Use | rather than || to ensure that all are run
            if (_module.getEncryptionDict () != null){
                // Encryption dictionary is not allowed.
                _return = false;
            }
            if ( !trailerDictOK ()){
                _return = false;
            }
            if ( !catalogOK () ){
                _return = false;
            }
            if ( !resourcesOK () ){
                _return = false;
            }
            if ( !fontsOK () ) {
                _return = false;
            }

        }
        catch (RuntimeException e) {//Possible: reportBroken file method or something?
            //TODO: HAck, fix
            reportReasonForNonCompliance(
                    e.getMessage());
            _return = false;

        }

        return _return;  // Passed all tests
    }


    /** The Encrypt entry isn't allowed in the trailer
     dictionary. The ID entry is required. Section 6.1.3
     * @return boolean True if the Trailer Dictionary adheres to these demands
     */
    protected boolean trailerDictOK ()  {
        boolean _return = true;
        PdfDictionary trailerDict = _module.getTrailerDict ();
        if (trailerDict == null) {
            throw new RuntimeException("Pdf.has.no.trailer.dictionary");
        }

        if (trailerDict.get ("Encrypt") != null) { //as per section 6.1.3
            reportReasonForNonCompliance(
                    "PDF.trailer.dict.has.a.Encrypt.entry");
            _return = false;
        }
        if (trailerDict.get ("ID") == null) { //as per section 6.1.3
            reportReasonForNonCompliance("PDF.trailer.dict.has.no.ID.entry");
            _return = false;
        }

        return _return;
    }

    /**
     * Check if the required Catalog Dictionary is ok, according the <b>many</b>
     * sections of the spec.
     * @return true, if the catalog adhered to all the requirements. Otherwise
     * false, and the reason will be reported.
     */
    protected boolean catalogOK ()  {
        boolean _return = true;
        PdfDictionary cat = _module.getCatalogDict ();
        if (cat == null) {
            throw new RuntimeException("PDF.has.no.catalog.Dictionary");
        }


        try {
            // It must have an unfiltered Metadata stream, section 6.7.2
            PdfStream metadata = toPdfStream(cat.get ("Metadata"));
            if (!metadataOK (metadata)) {
                reportReasonForNonCompliance("There.is.an.yet.undefined.metadata.problem.temp.message");
                _return = false;
            }

            // If it has an interactive form, it must meet certain criteria
            PdfDictionary form = toPdfDictionary(cat.get ("AcroForm"));
            if (form != null) {
                if (!formOK (form)) {
                    _return = false;
                }
            }

            // It may not contain an AA entry , section 6.6.2
            if (cat.get ("AA") != null){
                reportReasonForNonCompliance("PDF.catalog.has.AA.key");
                _return = false;
            }

            // It may not contain an OCProperties entry, section 6.1.13
            if (cat.get ("OCProperties") != null) {
                reportReasonForNonCompliance("PDF.catalog.has.OCProperties.key");
                _return = false;
            }

            //The names dictionary must not contain the entry EmbeddedFiles, section 6.1.11
            PdfDictionary names = toPdfDictionary(cat.get("Names"));

            if (names != null && names.get("EmbeddedFiles") != null) {
                reportReasonForNonCompliance("PDf.catalog.names.has.EmbeddedFiles.key");
                _return = false;
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);//if the message should appear in the log
        }

        //TODO: OutputIntents (Restricted) , StructTreeRoot(Recommended)


        return _return;
    }

    //Firstlevel check
    protected boolean fontsOK ()
    {
        if (!type0FontsOK ()) {//TODO: reports down in the method
            return false;
        }
        return true;
    }



    //TODO: NOT DONE, TOO COMPLEX
    /* Check the type 0 font map for compatibility with
       CIDFont and CMap dictionaries */
    private boolean type0FontsOK ()
    {
        boolean _return = true;
        Map type0Map = _module.getFontMap (PdfModule.F_TYPE0);
        if (type0Map == null) { //There are no Type0 fonts, so nothing to check
            return true;
        }
        try {
            PdfSimpleObject ob;
            Iterator iter = type0Map.values().iterator ();
            while (iter.hasNext ()) {
                String registry = null;
                String ordering = null;
                PdfDictionary font = toPdfDictionary(iter.next ());
                //TODO: Font might be null now, but unlikely

                // The Encoding entry can be a predefined name
                // or a dictionary.  If it's a dictionary, it
                // must be compatible with the CIDSystemInfo
                // dictionaries.
                PdfObject enc = font.get ("Encoding");
                //TODO: Apparently the predefined encoding just need to not be
                // a dictionary. What if it is null??
                if (enc instanceof PdfDictionary) {

                    // it's a CMap dictionary.
                    PdfDictionary info =
                            toPdfDictionary(toPdfDictionary(enc).get ("CIDSystemInfo"));
                    ob = toPdfSimpleObject( info.get ("Registry"));
                    registry = ob.getStringValue ();
                    ob = toPdfSimpleObject( info.get ("Ordering"));
                    ordering = ob.getStringValue ();
                    //TODO: This code just throws if something is not expected??
                }//TODO: Else??


                PdfArray descendants =
                        toPdfArray(font.get ("DescendantFonts"));
                // PDF 1.4 and previous allow only a single
                // descendant font, and this must be a CIDFont.
                // While Adobe warns that this may change in a
                // later version, we require here that the
                // first descendant be a CIDFont, and ignore any others.
                Vector subfonts = descendants.getContent ();

                /*
             * Fix contributed by FCLA, 2007-05-30, to permit the
             * subfonts array to store PdfObject as well as
             * PdfDictionary.
             *
             * PdfDictionary subfont =
             *                 (PdfDictionary) subfonts.elementAt (0);
             * subfont = (PdfDictionary)
             *                  _module.resolveIndirectObject (subfont);
             */
                PdfObject objFont = deref( subfonts.elementAt (0));
                PdfDictionary subfont = toPdfDictionary(objFont);
                PdfSimpleObject subtype =
                        toPdfSimpleObject(subfont.get ("Subtype"));
                /*
                * Fix conributed by FCLA, 2007-05-30, to permit the
                * comparison of a general PdfSimpleObject to a string.
                *
                * if (!"CIDFontType0".equals (subtype) &&
                *     !"CIDFontType2".equals (subtype)) {
                */
                if (!subtype.getStringValue ().equals ("CIDFontType0") &&
                    !subtype.getStringValue ().equals ("CIDFontType2")) {
                    _return = false;
                }

                // If there's no CMap dictionary and this is the
                // first subfont, save the registration and
                // ordering strings.  Otherwise make sure they match.
                PdfDictionary info =
                        toPdfDictionary(subfont.get ("CIDSystemInfo"));
                ob = toPdfSimpleObject(info.get ("Registry"));
                String obstr = ob.getStringValue ();
                if (registry == null) {
                    registry = obstr;
                }
                else {
                    if (!registry.equals (obstr)) {
                        return false;
                    }
                }
                ob = toPdfSimpleObject(info.get ("Ordering"));
                obstr = ob.getStringValue ();
                /* Fix contributed by FCLA, 2007-05-30, to fix an apparent
             * typo.
             *
             * if (registry == null) {
             */
                if (ordering == null) {
                    ordering = obstr;
                }
                else {
                    if (!ordering.equals (obstr)) {
                        return false;
                    }
                }
                // A type 2 subfont must meet certain restrictions
                if ("CIDFontType2".equals (subtype)) {
                    PdfObject cgmap = deref(subfont.get ("CIDToGIDMap"));
                    if (cgmap == null) {
                        return false;
                    }
                    if (cgmap instanceof PdfSimpleObject) {
                        if (!"Identity".equals ((toPdfSimpleObject(cgmap)).getStringValue ())) {
                            return false;
                        }
                    }
                    else if (!(cgmap instanceof PdfStream)) {
                        return false;
                    }
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    /* Check if a font has an acceptable encoding. This applies
       only to TrueType fonts. */
    private boolean ttFontEncodingOK (PdfDictionary font)
    {
        try {
            PdfDictionary desc = toPdfDictionary(font.get ("FontDescriptor"));
            // Not all fonts -- in particular, the standard 14 --
            // are required to have FontDescriptors.  How do we
            // handle encoding in those cases?
            if (desc == null) {
                return true;  // for now, give benefit of doubt
            }
            PdfSimpleObject flagObj = toPdfSimpleObject(
                    desc.get ("Flags"));
            int flags = flagObj.getIntValue ();
            if ((flags & 4) == 0) {
                // It's a nonsymbolic font, check the Encoding
                PdfSimpleObject encoding =
                        toPdfSimpleObject(font.get ("Encoding"));
                String encStr = encoding.getStringValue ();
                if (!"MacRomanEncoding".equals (encStr) &&
                    !"WinAnsiEncoding".equals (encStr)) {
                    return false;
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }


    /** Check if the interactive form is OK, as specified by section 6.9
     * @param form the form dictionary
     * @return true if the form adheres to the standard.
     */
    private boolean formOK (PdfDictionary form)
    {
        boolean _return = true;
        // Guess what?  It's another hierarchy of dictionaries!
        // So let's walk through the fields...

        // The NeedAppearances flag either shall not be present
        // or shall be false. Section 6.9
        PdfSimpleObject needapp = toPdfSimpleObject(form.get ("NeedAppearances"));
        if (needapp != null) {
            if (!needapp.isFalse ()) {
                reportReasonForNonCompliance("The.interactive.form.has.NeedAppearances.set.see.section.6.9");
                _return = false;
            }
        }

        PdfArray fields = toPdfArray(form.get ("Fields"));
        Vector fieldVec = fields.getContent ();
        for (int i = 0; i < fieldVec.size (); i++) {
            PdfDictionary field = toPdfDictionary(fieldVec.elementAt (i));
            if (!fieldOK (field)) {
                _return = false;
            }
        }

        return _return;
    }


    /** Check a form field for validity.  We don't allow form fields
     *to have A or AA (Additional Actions) dictionaries per section 6.6.2 and 6.9
     * @param field the form field to check
     * @return true if the field is ok
     */
    private boolean fieldOK (PdfDictionary field)
    {
        boolean _return = true;

        // A Widget annotation dictionary or Field dictionary
        // shall not contain the AA keys, section 6.6.2
        if (field.get ("AA") != null) {
            reportReasonForNonCompliance("PDF.form.field.has.AA.key");
            _return = false;
        }

        // A Widget annotation dictionary or Field dictionary
        // shall not contain the A keys, section 6.9
        if (field.get ("A") != null) {
            reportReasonForNonCompliance("PDF.has.action.associated.with.interactive.form.field");
            _return = false;
        }

        // Every form field shall have an appearance dictionary
        // associated with the field's data. Section 6.9
        if (field.get ("DR") == null) {
            reportReasonForNonCompliance("No.appearance.dictionary.associated.with.interactive.field");
            _return = false;
        }



        PdfArray kids = toPdfArray(field.get ("Kids"));
        // Now, just to complicate things, the contents of
        // the array might be subfield dictionaries or might
        // be widget annotations.  Oh, and neither one has
        // a required Type entry.
        // We only case about subfields.
        if (kids != null) {
            Vector kidVec = kids.getContent ();
            for (int i = 0; i < kidVec.size (); i++) {
                PdfDictionary kid = toPdfDictionary(kidVec.elementAt (i));
                // The safest way to check if this is a field seems
                // to be to look for the required Parent entry.
                if (kid.get ("Parent") != null) {
                    if (!fieldOK (kid)) {
                        _return = false;
                    }
                }
            }
        }


        return _return;
    }



    /** Walk through the page tree and check all Resources dictionaries
     that we find.  Along the way, we check several things:
     <ul>
     <li>Color spaces. The document may not have both CMYK and
     RGB color spaces.
     <li>Extended graphic states.
     <li>XObjects.
     </ul>
     * @return if all the checks are positive
     */
    protected boolean resourcesOK () {
        boolean _return = true;
        PageTreeNode docTreeRoot = _module.getDocumentTree ();
        //The Tree consist of PageTree objects, and Page objects as the leaves.
        //Most fields must be defined for the Page objects, but are allowed in
        //the pagetree objects. If a Page object lacks the field, it inherits it
        // if from a PageTree node.
        // The order of the tree has nothing to do with the order of the pages.
        // For linearized PDFs the tree is balanced.
        try {

            docTreeRoot.startWalk ();
            DocNode docNode;
            for (;;) {

                docNode = docTreeRoot.nextDocNode ();
                if (docNode == null) {
                    break;
                }
                //docNode is now a Page object or a PageTree object

                PdfDictionary docNodeDict = docNode.getDict();

                //Page objects may not have AA dictionaries, section 6.6.2
                if (docNodeDict.get("AA") != null){
                    reportReasonForNonCompliance("PDF.contain.embedded.javascript");
                    _return = false;
                }

                if (docNode instanceof PageObject){
                    PdfArray annots = ((PageObject) docNode).getAnnotations ();
                    if (annots != null) {
                        Vector annVec = annots.getContent ();
                        for (int i = 0; i < annVec.size (); i++) {
                            annotationOK(toPdfDictionary(annVec.get(i))) ;
                            _return = false;
                        }
                    }
                }
                //Interesting fields:  Resources, Contents(Stream), Metadata (Stream)
                // and more

                // Check for node-level resources
                PdfDictionary rsrc = docNode.getResources ();
                if (rsrc != null) {

                    // Check color spaces.
                    PdfDictionary cs = toPdfDictionary(rsrc.get ("ColorSpace"));
                    if (!colorSpaceOK (cs)) {//TODO: colours... DO ya thing
                        _return = false;
                    }

                    // Check extended graphics state.
                    PdfDictionary gs = toPdfDictionary(rsrc.get ("ExtGState"));
                    if (!extGStateOK (gs)) {//TODO: colours... DO ya thing
                        _return = false;
                    }

                    // Check XObjects.
                    PdfDictionary xo = toPdfDictionary(rsrc.get ("XObject"));
                    if (!xObjectsOK (xo)) {
                        _return = false;
                    }
                }

                // Check content streams for  resources
                if (docNode instanceof PageObject) {
                    List streams =
                            ((PageObject) docNode).getContentStreams ();
                    if (streams != null) {
                        Iterator iter = streams.listIterator ();
                        while (iter.hasNext ()) {
                            PdfStream stream = toPdfStream( iter.next ());
                            if (!streamOK(stream)){
                                _return = false;
                            }
                            PdfDictionary dict = stream.getDict ();
                            PdfDictionary rs =
                                    toPdfDictionary(dict.get ("Resources"));
                            if (rs != null) {
                                PdfDictionary cs = toPdfDictionary(rs.get ("ColorSpace"));
                                if (!colorSpaceOK (cs)) {
                                    _return = false;
                                }

                                PdfDictionary gs = toPdfDictionary(rs.get ("ExtGState"));
                                if (!extGStateOK (gs)) {
                                    _return = false;
                                }

                                PdfDictionary xo = toPdfDictionary(rs.get ("XObject"));
                                if (!xObjectsOK (xo)) {
                                    _return = false;
                                }
                            }
                            // Also check for filters
                            PdfObject filters =
                                    dict.get ("Filter");
                            if (hasFilters (filters, excludedFilters)) {
                                reportReasonForNonCompliance("PDF.uses.filter.from.the.excluded.list");
                                _return = false;
                            }
                        }
                    }


                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        }


        return _return;
    }

    /**
     * Checks if a stream obeys the rules that applies to all streams. Specifically
     * that it does not reference external content.
     * @param stream the stream to examine
     * @return true if the stream is ok
     */
    private boolean streamOK(PdfStream stream){
        PdfDictionary streamDict = stream.getDict();
        boolean _return = true;

        if (streamDict.get("F") != null){
            reportReasonForNonCompliance("PDF.reference.external.content");
            _return = false;
        }
        if (streamDict.get("FFilter") != null){
            reportReasonForNonCompliance("PDF.reference.external.content");
            _return = false;
        }
        if (streamDict.get("FDecodeParams") != null){
            reportReasonForNonCompliance("PDF.reference.external.content");
            _return = false;
        }

        return _return;
    }

    private boolean annotationOK(PdfDictionary annDict) {

        boolean _return = true;

        // Also check page objects for annotations.
        // Must be one of the prescribed types, but not
        // Movie, Sound, or FileAttachment.
        PdfSimpleObject subtypeObj = toPdfSimpleObject(annDict.get ("Subtype"));
        String subtypeVal = subtypeObj.getStringValue ();
        boolean stOK = false;

        List allowedAnnotations = Arrays.asList(annotTypes);
        List disallowedAnnotations = Arrays.asList(annotTypesNotAllowed);
        if (disallowedAnnotations.contains(subtypeVal)){ //Explicitly forbidden subtype, section 6.5.2
            _return = false;
            //TODO: Log this error

        } else if (allowedAnnotations.contains(subtypeVal)){ //Allowed subtype


        } else{//It was an unknown subtype, which is not allowed in pdf/a. section 6.5.2
            _return = false;
            //TODO: Log this error
        }





        // If it's a Widget, it can't have an AA entry. section 6.6.2
        if ("Widget".equals (subtypeVal)) {
            //Widgets cannot normally have AA entries, but if a field has only one
            if (annDict.get ("AA") != null) {
                _return = false;
                //TODO: Log this unlikely error
            }
        }

        // if the CA key is present, it must have a
        // value of 1.0. Section 6.5.3
        PdfSimpleObject ca = toPdfSimpleObject(
                annDict.get ("CA"));
        if (ca != null) {
            double caVal = ca.getDoubleValue ();
            if (caVal != 1.0) {
                _return = false;
                //TODO: Log this error
            }
        }


        //section 6.5.3
        int printFlag = 1 << 2;
        int hiddenFlag = 1 << 1;
        int invisibleFlag = 1 << 0;
        int noViewFlag = 1 << 5;
        PdfSimpleObject f = toPdfSimpleObject(
                annDict.get ("F"));
        int fint = f.getIntValue();

        if ((fint & printFlag) != printFlag) {//error
            //TODO: log
            _return = false;
        }
        if ( (fint & hiddenFlag) == hiddenFlag){//error
            //TODO: log
            _return = false;
        }
        if ( (fint & invisibleFlag) == invisibleFlag){//error
            //TODO: log
            _return = false;
        }
        if ( (fint & noViewFlag) == noViewFlag){//error
            //TODO: log
            _return = false;
        }

        if ("Text".equals(subtypeVal)){//only for Text annotations
            int noZoomFlag = 1 << 3;
            int noRotate = 1 << 4;
            if ((fint & noZoomFlag) != noZoomFlag){
                _return = false;
                //TODO: log
            }
            if ((fint & noRotate) != noRotate){
                _return = false;
                //TODO: log
            }
        }



        // For non-text annotation types, the
        // Contents key is required. Section 6.8.6 and ONLY for level A
        List nonTextAnnotations = Arrays.asList(nonTextAnnotTypes);
        if (nonTextAnnotations.contains(subtypeVal)){
            if (annDict.get("Contents") == null) {
                _return = false; //TODO log
            }
        }


        PdfDictionary ap = toPdfDictionary(annDict.get("AP"));
        if (ap != null){
            PdfObject n = ap.get("N");
            if (!(n instanceof PdfStream)) {
                //See section 8.4.4 in pdf1.4 for name trees mappings not covered by this
                _return = false;
                //The N key must be a stream, as per 6.5.3
            }


            Iterator it = ap.iterator();
            int size = 0;
            while (it.hasNext()){
                it.next();
                size++;
            }
            if (size > 1){//as per section 6.5.3, the AP dictionary must not contain other keys than N
                _return = false;
                //TODO: log.
            }
        }

        return _return;

    }


    /** Check if a color space dictionary is conformant
     * @param cs
     * @return true for conformant
     */
    private boolean colorSpaceOK (PdfDictionary cs)
    {
        // If it's null, that's fine.
        if (cs == null) {
            return true;
        }
        // Walk through the color space dictionary,
        // checking device ("uncalibrated") color spaces
        Iterator iter = cs.iterator ();
        while (iter.hasNext ()) {
            PdfObject res = (PdfObject)(iter.next ());
            if (res instanceof PdfArray) {
                Vector resv = ((PdfArray) res).getContent ();
                PdfSimpleObject snameobj = toPdfSimpleObject(resv.elementAt (0));
                String sname = snameobj.getStringValue ();
                boolean oldHasUncalCS = hasUncalCS;
                if ("DeviceCMYK".equals (sname)) {
                    hasDevCMYK = true;
                    hasUncalCS = true;
                }
                else if ("DeviceRGB".equals (sname)) {
                    hasDevRGB = true;
                    hasUncalCS = true;
                }
                else if ("DeviceGray".equals (sname)) {
                    hasUncalCS = true;
                }
                // If this is the first time we've hit an uncalibrated
                // color space, check for an appropriate OutputIntent dict.
                if (hasUncalCS && !oldHasUncalCS) {
                    if (!checkUncalIntent ()) {//TODO: Follow this method down.
                        reportReasonForNonCompliance("PDF.has.a.uncalibrated."
                                                     + "colour.space.without.an."
                                                     + "appropriate.OutputInt"
                                                     + "ent.dict");
                        return false;
                    }
                }
                if (hasDevRGB && hasDevCMYK) {
                    reportReasonForNonCompliance("PDF.has.both.DeviceRGB.and.DeviceCMYK.colourspaces");
                    return false;   // can't have both in same file
                }
            }
        }
        return true;   // passed all tests
    }

    public PdfArray getOutputIntensArray(){
        // First off, there must be an OutputIntents array
        // in the document catalog dictionary.
        PdfDictionary catDict = _module.getCatalogDict ();
        PdfArray intentsArray = null;

        intentsArray = toPdfArray(catDict.get ("OutputIntents"));
        return intentsArray;
    }

    /* If there is an uncalibrated color space, then there must be a
     * "PDF/A-1 OutputIntent." */
    private boolean checkUncalIntent ()
    {
        try {
            PdfArray intentsArray = getOutputIntensArray();
            if (intentsArray == null) {
                return false;
            }
            Vector intVec = intentsArray.getContent ();
            PdfStream theOutProfile = null;
            boolean pdfaProfileSeen = false;
            for (int i = 0; i < intVec.size (); i++) {
                // Multiple intents arrays are allowed, but all must use
                // the same DestOutputProfile object or none, and there
                // must be at least one that as one and has GTS_PDFA1 as
                // the value of its S key.
                PdfDictionary intent = toPdfDictionary(intVec.elementAt (0));
                PdfSimpleObject outCond =
                        toPdfSimpleObject(intent.get ("OutputCondition"));
                if (outCond != null) {
                    PdfStream outProfile = toPdfStream
                            (intent.get ("DestOutputProfile"));
                    if (outProfile != null) {
                        if (theOutProfile != null) {
                            // all output profiles must be the same.
                            if (outProfile != theOutProfile) {
                                return false;
                            }
                        }
                        else {
                            // All subsequent output profiles must matcht his.
                            theOutProfile = outProfile;
                        }
                        PdfSimpleObject subtype = toPdfSimpleObject( intent.get ("S"));
                        if (subtype != null) {
                            if ("GTS_PDFA1".equals (subtype.getStringValue())) {
                                pdfaProfileSeen = true;
                            }
                        }
                    }
                }
            }
            if (theOutProfile == null || !pdfaProfileSeen) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }




    /* Check if the outlines (if any) are OK.  This is a check
       on Actions, and the module has already checked if there
       are Actions in the outlines, so if there aren't any,
       we save the time to do this test. */
    private boolean outlinesOK ()
    {
        if (!_module.getActionsExist ()) {
            return true;
        }
        PdfDictionary outlineDict = _module.getOutlineDict ();
        if (outlineDict == null) {
            return true;
        }
        try {
            PdfDictionary item = toPdfDictionary(outlineDict.get ("First"));
            while (item != null) {
                if (!checkOutlineItem (item)) {
                    return false;
                }
                item = toPdfDictionary(item.get ("Next"));
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }


    /* Check an outline item, going down recursively */
    private boolean checkOutlineItem (PdfDictionary item)
    {
        // Check if there are actions for this item
        try {
            PdfDictionary action = toPdfDictionary(item.get ("A"));
            if (action != null) {
                if (!actionOK (action)) {
                    return false;
                }
            }
            PdfDictionary child = toPdfDictionary(item.get ("First"));
            while (child != null) {
                if (!checkOutlineItem (child)) {
                    return false;
                }
                child = toPdfDictionary(child.get ("Next"));
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }


    /* Validate an Action dictionary.  Actions exclude certain types. */
    private boolean actionOK (PdfDictionary action)
    {
        int i;
        // For some reason, an action's type is an "S" entry, not
        // a "Subtype" entry.
        try {
            PdfSimpleObject actType = toPdfSimpleObject( action.get ("S"));
            String actStr = actType.getStringValue ();

            for (i = 0; i < excludedActions.length; i++) {
                if (excludedActions[i].equals (actStr)) {
                    return false;
                }
            }

            if ("Named".equals(actStr)){//Only 4 named actions allowed
                PdfSimpleObject actName = toPdfSimpleObject(action.get("N"));
                String actNameStr = actName.getStringValue();
                for (i = 0; i < allowedNamedActions.length; i++) {
                    if (!allowedNamedActions[i].equals (actStr)) {
                        return false;
                    }
                }
            }

            // An action can have a "Next" entry which is either
            // another action or an array of actions.  Need to follow
            // the whole tree to make sure all actions are legit.
            PdfObject next = deref(action.get ("Next"));
            if (next instanceof PdfDictionary) {
                if (!actionOK ((PdfDictionary) next)) {
                    return false;
                }
            }
            else if (next instanceof PdfArray) {
                Vector nextVec = ((PdfArray) next).getContent ();
                for (i = 0; i < nextVec.size (); i++) {
                    PdfDictionary nact = toPdfDictionary(
                            nextVec.elementAt (i));
                    if (!actionOK (nact)) {
                        return false;
                    }
                }
            }
            else if (next != null) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }



    /* The ExtGState resource may not have a TR key, or a
       TR2 with a value other than "Default". */
    private boolean extGStateOK (PdfDictionary gs)
    {
        if (gs == null) {
            // no object means no problem
            return true;
        }
        try {
            PdfObject tr = gs.get ("TR");
            PdfObject tr2 = gs.get ("TR2");

            if (tr != null) {
                return false;
            }
            if (tr2 != null) {
                String tr2Val = toPdfSimpleObject( tr2).getStringValue ();
                if (!"Default".equals (tr2Val)) {
                    return false;
                }
            }

            // RI is restricted to the traditional 4 rendering intents
            PdfSimpleObject ri = toPdfSimpleObject(gs.get ("RI"));
            if (ri != null) {
                String riVal = ri.getStringValue ();
                if (!validIntentString (riVal)) {
                    return false;
                }
            }

            // SMask is allowed only with a value of "None".
            PdfSimpleObject smask = toPdfSimpleObject(gs.get ("SMask"));
            if (smask != null) {
                String smVal = smask.getStringValue ();
                if (!"None".equals (smVal)) {
                    return false;
                }
            }

            // BM, if present, must be "Normal" or "Compatible"
            PdfSimpleObject blendMode =
                    toPdfSimpleObject( gs.get ("BM"));
            if (blendMode != null) {
                String bmVal = blendMode.getStringValue ();
                if (!"Normal".equals (bmVal) &&
                    !"Compatible".equals (bmVal)) {
                    return false;
                }
            }

            // CA and ca must be 1.0, if present
            PdfSimpleObject ca = toPdfSimpleObject( gs.get ("CA"));
            double caVal;
            if (ca != null) {
                caVal = ca.getDoubleValue ();
                if (caVal != 1.0) {
                    return false;
                }
            }
            ca = toPdfSimpleObject( gs.get ("ca"));
            if (ca != null) {
                caVal = ca.getDoubleValue ();
                if (caVal != 1.0) {
                    return false;
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;   // passed all tests
    }


    protected boolean xObjectOK (PdfDictionary xo)
    {
        if (xo == null) {//TODO: Think about this one...
            // no XObject means no problem
            return true;
        }
        boolean _return = true;
        try {
            // PostScript XObjects aren't allowed.
            // Image XObjects must meet certain tests.
            PdfSimpleObject subtype = toPdfSimpleObject( xo.get ("Subtype"));
            if (subtype != null) {
                String subtypeVal = subtype.getStringValue ();
                if ("PS".equals (subtypeVal)) {
                    // PS XObjects aren't allowed.
                    _return = false;
                    //TODO: Log here
                }
                if ("Image".equals (subtypeVal)) {
                    if (!imageObjectOK (xo)) {
                        _return = false;
                    }
                }
                if ("Form".equals (subtypeVal)) {
                    PdfSimpleObject subtype2 = toPdfSimpleObject( xo.get("Subtype2"));

                    if ("PS".equals(subtype2.getStringValue())){//really a Postscript object
                        _return = false;
                        //TODO: log
                    } else if (!formObjectOK (xo)) {
                        _return = false;
                    }
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return _return;
    }

    /** Checks if a Form xobject is valid.  This overrides the method in
     XProfileBase. */
    protected boolean formObjectOK (PdfDictionary xo)
    {
        boolean _return = true;

        if (xo.get("OPI") != null){//section 6.2.5
            _return = false;
            //TODO: log
        }
        if (xo.get("PS") != null){//section 6.2.5
            _return = false;
            //TODO: log
        }

        if (xo.get ("Ref") != null) {
            _return = false;
            //TODO: log
        }
        return _return;
    }


    /**
     * Checks an image XObjects, according to the rules in section 6.2.4 in
     * the pdf/a spec. Reports any errors encountered.
     * @param xo the XObject dictionary to examine
     * @return true if the image checks out, otherwise false;
     */
    protected boolean imageObjectOK (PdfDictionary xo)
    {
        boolean _return = true;
        try {
            // OPI and Alternates keys are disallowed
            if (xo.get ("OPI") != null){//section 6.2.4
                _return = false;
                //TODO: log
            }
            if (xo.get ("Alternates") != null) {//section 6.2.4
                _return = false;
                //TODO: log
            }

            // Interpolate is allowed only if its value is false.
            PdfSimpleObject interp = toPdfSimpleObject( xo.get ("Interpolate"));
            if (interp != null) {
                if (!interp.isFalse ()) {
                    _return = false;
                    //TODO: log
                }
            }

            // Intent must be one of the four standard rendering intents,
            // if present.
            PdfSimpleObject intent = toPdfSimpleObject( xo.get ("Intent"));
            if (intent != null) {
                String intentStr = intent.getStringValue ();
                if(!validIntentString(intentStr)){
                    _return = false;
                    //TODO: log
                }
            }

        }
        catch (Exception e) {
            _return = false;
            //TODO: log
        }
        return _return;
    }

    /**
     * Quick method to determine if a rendering intent string is one of the four
     * allowed strings, defined in validIntentStrings
     * @param str The string to examine
     * @return true if str was in the list, otherwise false;
     */
    private boolean validIntentString (String str)
    {
        List validIntentStringList = Arrays.asList(validIntentStrings);
        return validIntentStringList.contains(str);
    }

    /** See if the metadata stream from the catalog dictionary is OK
     * @param metadata the metadata stream to examine
     * @return true if the metadata stream is OK
     */
    private boolean metadataOK (PdfStream metadata)
    {  //TODO: THIS METHOD DOES NOT REPORT
        // Presence of metadata is required
        if (metadata == null) {
            return false;
        }
        try {
            PdfDictionary metaDict = metadata.getDict ();
            if (metaDict.get ("Filter") != null) {
                // We just metadata we didn't like. Filters aren't allowed.
                return false;
            }

            // Create an InputSource to feed the parser.
            SAXParserFactory factory =
                    SAXParserFactory.newInstance();
            factory.setNamespaceAware (true);
            XMLReader parser = factory.newSAXParser ().getXMLReader ();
            //InputStream stream = new StreamInputStream (metadata, _module.getFile ());
            PdfXMPSource src = new PdfXMPSource(metadata, _module.getFile ());
            XMPHandler handler = new XMPHandler ();
            parser.setContentHandler (handler);
            parser.setErrorHandler (handler);
            // We have to parse twice.  The first time, we may get
            // an encoding change as part of an exception thrown.  If this
            // happens, we create a new InputSource with the encoding, and
            // continue.
            try {
                parser.parse (src);
            }
            catch (SAXException se) {
                String msg = se.getMessage ();
                if (msg != null && msg.startsWith ("ENC=")) {
                    String encoding = msg.substring (5);
                    try {
                        //Reader rdr = new InputStreamReader (stream, encoding);
                        src = new PdfXMPSource (metadata, _module.getFile (), encoding);
                        parser.parse (src);
                    }
                    catch (UnsupportedEncodingException uee) {
                        return false;
                    }
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

}
