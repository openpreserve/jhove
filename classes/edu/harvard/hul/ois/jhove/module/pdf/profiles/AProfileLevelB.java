/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/


package edu.harvard.hul.ois.jhove.module.pdf.profiles;

import edu.harvard.hul.ois.jhove.RFC1766Lang;
import edu.harvard.hul.ois.jhove.XMPHandler;
import edu.harvard.hul.ois.jhove.module.PdfModule;
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
    private boolean hasDevRGB;
    private boolean hasDevCMYK;
    private boolean hasUncalCS;  // flag for DeviceGray, DeviceCMYK or DeviceRGB
    /* Allowable annotation types.  Movie, Sound and FileAttachment
       are allowed in PDF, but not in PDF/A. */
    private String[] annotTypes = {
        "Text", "Link", "FreeText", "Line", "Square", "Circle",
        "Polygon", "Polyline", "Highlight", "Underline",
        "Squiggly", "StrikeOut", "Stamp", "Caret",
        "Ink", "Popup", "Widget", "Screen",
        "PrinterMark", "TrapNet"
    };

    /* The following are the annotation types which are considered
       non-text annotations. */
    private String[] nonTextAnnotTypes = {
        "Link", "Line", "Square", "Circle",
        "Polygon", "Polyline", "Stamp", "Caret",
        "Ink", "Popup", "Widget", "Screen",
        "PrinterMark", "TrapNet"
    };

    private String[] excludedActions = {
        "Launch", "Sound", "Movie", "ResetForm",
        "ImportData", "JavaScript" ,"SetState" , "NOP"
    };

    private String[] allowedNamedActions = {
            "NextPage", "PrevPage", "FirstPage", "LastPage"
    };

    /* The following filters are not allowed */
    private String[] excludedFilters = {
        "LZWDecode" //TODO: The spec only mentions LZW
    };


    

    /**
     *   Constructor.
     *   Creates an AProfile object for subsequent testing.
     *
     *   @param  module   The module under which we are checking the profile.
     *
     */
    public AProfileLevelB(PdfModule module)
    {
        super (module);
        _profileText = "ISO PDF/A-1, Level B";
    }



    /**
     * Returns <code>true</code> if the document satisfies the profile
     * at Level B or better.  Also sets the level A flag to the
     * appropriate value, so that <code>satisfiesLevelA</code> can subsequently
     * be called.
     *
     */
    public boolean satisfiesThisProfile ()
    {
        boolean _return = true; //so that all tests are run, even if some fails

        // Assume level A compliance.
        //_levelA = true;

        // The module has already done some syntactic checks.
        // If those failed, the file isn't compliant.
        if (!_module.mayBePDFACompliant ()) {
            //_levelA = false;
            reportReasonsForNonCompliance(_module.getReasonsForPDFANonCompliance());
            _return = false;
        }


        hasDevCMYK = false;
        hasDevRGB = false;
        hasUncalCS = false;

        try {
            //Perform the various checks. Use | rather than || to ensure that all are run
            if (_module.getEncryptionDict () != null  // Encryption dictionary is not allowed.
                | !trailerDictOK ()
                | !catalogOK ()
                | !resourcesOK ()
                | !fontsOK () ) {
                //_levelA = false;
                _return = false;
            }
        }
        catch (Exception e) {
            reportReasonForNonCompliance(ErrorCodes.pdfa_b.exception_was_thrown,
                                         e.getMessage());
            //_levelA = false;
            _return = false;

        }

        return _return;  // Passed all tests
    }

    /* The Encrypt and Info entries aren't allowed in the trailer
       dictionary. The ID entry is required. */
    private boolean trailerDictOK ()
    {  //TODO: DONE
        boolean _return = true;
        PdfDictionary trailerDict = _module.getTrailerDict ();
        if (trailerDict == null) {
            reportReasonForNonCompliance(ErrorCodes.pdfa_b.no_trailer_dict,
                                         "PDF.has.no.trailer.dictionary");
            return false; //If this happens, do not continue
                // really shouldn't happen
        }

        try {
            if (trailerDict.get ("Encrypt") != null/* ||
                  trailerDict.get ("Info") != null*/) { //TODO: WHY???
                reportReasonForNonCompliance(
                        ErrorCodes.pdfa_b.trailer_dict_has_encrypt
                        ,"PDF.trailer.dict.has.a.Encrypt.entry");
                _return = false;

            }
            if (trailerDict.get ("ID") == null) {
                reportReasonForNonCompliance(ErrorCodes.pdfa_b.trailer_has_no_ID,"PDF.trailer.dict.has.no.ID.entry");
                _return = false;

            }
        }
        catch (Exception e) {
            //Not any obvious ways to get here
            _return = false;
        }
        return _return;
    }

    //Firstlevel Check
    private boolean catalogOK ()
    {    //TODO: DONE
        boolean _return = true;
        PdfDictionary cat = _module.getCatalogDict ();
        if (cat == null) {
            reportReasonForNonCompliance(ErrorCodes.pdfa_b.no_catalog_dict,"PDF.has.no.catalog.Dictionary");
            return false; //If this happens, do not continue
        }
        try {

            // The document catalog dictionary language "should" be present.
            // If it does, the value "shall" contain
            // a valid RFC1766 language string.

            PdfSimpleObject lang = (PdfSimpleObject) cat.get ("Lang");
            if (lang != null) { //So, no error if the object is not there. Is this intentional?
                String langstring = lang.getStringValue();
                if (langstring != null){
                    RFC1766Lang l = new RFC1766Lang (langstring);
                    if (!l.isSyntaxCorrect ()) {
                        reportReasonForNonCompliance(ErrorCodes.pdfa_b.invalid_lang,"PDF.catalog.dictionary.has.a.lang.entry.in.a.invalid.syntax");
                        _return = false;
                    }
                }else{
                    reportReasonForNonCompliance(ErrorCodes.pdfa_b.lang_entry_without_string,"PDF.catalog.dictionary.has.a.lang.entry.with.no.lang.string");
                    _return = false;
                }
            }


            // It must have an unfiltered Metadata stream
            PdfStream metadata = (PdfStream)
                _module.resolveIndirectObject (cat.get ("Metadata"));
            if (!metadataOK (metadata)) {//TODO: Should the reporting not be done down there???
                _return = false;
            }

            // If it has an interactive form, it must meet certain criteria
            PdfDictionary form = (PdfDictionary)
                _module.resolveIndirectObject (cat.get ("AcroForm"));
            if (form != null) {
                if (!formOK (form)) {//TODO: Should the reporting not be done down there???
                    _return = false;
                }
            }

            // It may not contain an AA entry or an OCProperties entry
            if (cat.get ("AA") != null){
                reportReasonForNonCompliance(
                        ErrorCodes.pdfa_b.catalog_dict_has_AA_entry,
                        "PDF.catalog.dictionary.has.a.AA.entry");
                _return = false;
            }

            if (cat.get ("OCProperties") != null) {
                reportReasonForNonCompliance(
                        ErrorCodes.pdfa_b.catalog_dict_has_OCProperties_entry,
                        "PDF.catalog.dictionary.has.a.OCProperties.entry");
                _return = false;
            }
        }
        catch (Exception e) {
            //TODO: Beautify this line
            reportReasonForNonCompliance(20009,"PDF.catalog.dictionary.validation.failed.with.error."+e.getMessage());
            _return = false;
        }
        return _return;
    }

    //Firstlevel check
    private boolean fontsOK ()
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
                PdfDictionary font = (PdfDictionary) iter.next ();
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
                        (PdfDictionary) _module.resolveIndirectObject
                            (((PdfDictionary) enc).get ("CIDSystemInfo"));
                    ob = (PdfSimpleObject) info.get ("Registry");
                    registry = ob.getStringValue ();
                    ob = (PdfSimpleObject) info.get ("Ordering");
                    ordering = ob.getStringValue ();
                    //TODO: This code just throws if something is not expected??
                }//TODO: Else??


                PdfArray descendants =
                    (PdfArray) font.get ("DescendantFonts");
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
                PdfObject objFont = (PdfObject) subfonts.elementAt (0);
                PdfDictionary subfont = (PdfDictionary)
                        _module.resolveIndirectObject (objFont);
                PdfSimpleObject subtype =
                       (PdfSimpleObject) subfont.get ("Subtype");
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
                    (PdfDictionary) _module.resolveIndirectObject
                        (subfont.get ("CIDSystemInfo"));
                ob = (PdfSimpleObject) info.get ("Registry");
                String obstr = ob.getStringValue ();
                if (registry == null) {
                    registry = obstr;
                }
                else {
                    if (!registry.equals (obstr)) {
                        return false;
                    }
                }
                ob = (PdfSimpleObject) info.get ("Ordering");
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
                    PdfObject cgmap = subfont.get ("CIDToGIDMap");
                    if (cgmap == null) {
                        return false;
                    }
                    if (cgmap instanceof PdfSimpleObject) {
                        if (!"Identity".equals (((PdfSimpleObject)cgmap).getStringValue ())) {
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
            PdfDictionary desc = (PdfDictionary)
                _module.resolveIndirectObject (font.get ("FontDescriptor"));
            // Not all fonts -- in particular, the standard 14 --
            // are required to have FontDescriptors.  How do we
            // handle encoding in those cases?
            if (desc == null) {
                return true;  // for now, give benefit of doubt
            }
            PdfSimpleObject flagObj = (PdfSimpleObject)
                desc.get ("Flags");
            int flags = flagObj.getIntValue ();
            if ((flags & 4) == 0) {
                // It's a nonsymbolic font, check the Encoding
                PdfSimpleObject encoding =
                        (PdfSimpleObject) font.get ("Encoding");
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


    /* Check if the interactive form is OK */
    private boolean formOK (PdfDictionary form)
    {
        // Guess what?  It's another hierarchy of dictionaries!
        // So let's walk through the fields...
        try {
            PdfArray fields = (PdfArray) form.get ("Fields");
            Vector fieldVec = fields.getContent ();
            for (int i = 0; i < fieldVec.size (); i++) {
                PdfDictionary field = (PdfDictionary) fieldVec.elementAt (i);
                if (!fieldOK (field)) {
                    return false;
                }
            }
            // The NeedAppearances flag either shall not be present
            // or shall be false.
            PdfSimpleObject needapp = (PdfSimpleObject) form.get ("NeedAppearances");
            if (needapp != null) {
                if (!needapp.isFalse ()) {
                    return false;
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }


    /* Check a form field for validity.  We don't allow form fields
       to have AA (Additional Actions) dictionaries */
    private boolean fieldOK (PdfDictionary field)
    {
        try {
            // A Widget annotation dictionary or Field dictionary
            // shall not contain the A or AA keys.
            if (field.get ("AA") != null) {
                return false;
            }
            if (field.get ("A") != null) {
                return false;
            }
            // Every form field shall have an appearance dictionary
            // associated with the field's data.
            if (field.get ("DR") == null) {
                return false;
            }
            PdfArray kids = (PdfArray) field.get ("Kids");
            // Now, just to complicate things, the contents of
            // the array might be subfield dictionaries or might
            // be widget annotations.  Oh, and neither one has
            // a required Type entry.
            // We only case about subfields.
            if (kids != null) {
                Vector kidVec = kids.getContent ();
                for (int i = 0; i < kidVec.size (); i++) {
                    PdfDictionary kid = (PdfDictionary) kidVec.elementAt (i);
                    // The safest way to check if this is a field seems
                    // to be to look for the required Parent entry.
                    if (kid.get ("Parent") != null) {
                        if (!fieldOK (kid)) {
                            return false;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    /* Walk through the page tree and check all Resources dictionaries
       that we find.  Along the way, we check several things:

       Color spaces. The document may not have both CMYK and
       RGB color spaces.

       Extended graphic states.

       XObjects.
     */
    private boolean resourcesOK ()
    {
        boolean _return = true;
        PageTreeNode docTreeRoot = _module.getDocumentTree ();
        try {
            docTreeRoot.startWalk ();
            DocNode docNode;
            for (;;) {
                docNode = docTreeRoot.nextDocNode ();
                if (docNode == null) {
                    break;
                }
                // Check for node-level resources
                PdfDictionary rsrc = docNode.getResources ();
                if (rsrc != null) {

                    // Check color spaces.
                    PdfDictionary cs = (PdfDictionary)
                        _module.resolveIndirectObject
                            (rsrc.get ("ColorSpace"));
                    if (!colorSpaceOK (cs)) {
                        _return = false;
                    }

                    // Check extended graphics state.
                    PdfDictionary gs = (PdfDictionary)
                        _module.resolveIndirectObject
                            (rsrc.get ("ExtGState"));
                    if (!extGStateOK (gs)) {
                        _return = false;
                    }

                    // Check XObjects.
                    PdfDictionary xo = (PdfDictionary)
                        _module.resolveIndirectObject
                            (rsrc.get ("XObject"));
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
                            PdfStream stream = (PdfStream) iter.next ();
                            PdfDictionary dict = stream.getDict ();
                            PdfDictionary rs =
                                (PdfDictionary) dict.get ("Resources");
                            if (rs != null) {
                                PdfDictionary cs = (PdfDictionary)
                                    _module.resolveIndirectObject
                                        (rs.get ("ColorSpace"));
                                if (!colorSpaceOK (cs)) {
                                    return false;
                                }

                                PdfDictionary gs = (PdfDictionary)
                                    _module.resolveIndirectObject
                                        (rs.get ("ExtGState"));
                                if (!extGStateOK (gs)) {
                                    return false;
                                }

                                PdfDictionary xo = (PdfDictionary)
                                    _module.resolveIndirectObject
                                        (rs.get ("XObject"));
                                if (!xObjectsOK (xo)) {
                                    return false;
                                }
                            }
                            // Also check for filters
                            PdfObject filters =
                                dict.get ("Filter");
                            if (hasFilters (filters, excludedFilters)) {
                                return false;
                            }
                        }
                    }

                    // Also check page objects for annotations.
                    // Must be one of the prescribed types, but not
                    // Movie, Sound, or FileAttachment.
                    PdfArray annots = ((PageObject) docNode).getAnnotations ();
                    if (annots != null) {
                        Vector annVec = annots.getContent ();
                        for (int i = 0; i < annVec.size (); i++) {
                            PdfDictionary annDict = (PdfDictionary)
                                annVec.elementAt (i);
                            PdfSimpleObject subtypeObj = (PdfSimpleObject) annDict.get ("Subtype");
                            String subtypeVal = subtypeObj.getStringValue ();
                            boolean stOK = false;
                            int j;
                            for (j = 0; j < annotTypes.length; j++) {
                                if (annotTypes[j].equals (subtypeVal)) {

                                    stOK = true;
                                    break;
                                }
                            }
                            if (stOK) {
                                return false;
                            }

                            // If it's a Widget, it can't have an AA entry
                            if ("Widget".equals (subtypeVal)) {
                                if (annDict.get ("AA") != null) {
                                    return false;
                                }
                            }
                            // For non-text annotation types, the
                            // Contents key is required.
                            for (j = 0; i < nonTextAnnotTypes.length; j++) {
                                if (nonTextAnnotTypes[i].equals (subtypeVal)) {
                                    if (annDict.get ("Contents") == null) {
                                        return false;
                                    }
                                    else {
                                        // Contents found, this dict OK
                                        break;
                                    }
                                }
                            }

                            // if the CA key is present, it must have a
                            // value of 1.0.
                            PdfSimpleObject ca = (PdfSimpleObject)
                                    annDict.get ("CA");
                            if (ca != null) {
                                double caVal = ca.getDoubleValue ();
                                if (caVal != 1.0) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;   // passed all tests
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
            PdfObject res = (PdfObject) iter.next ();
            if (res instanceof PdfArray) {
                Vector resv = ((PdfArray) res).getContent ();
                PdfSimpleObject snameobj = (PdfSimpleObject) resv.elementAt (0);
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
                        reportReasonForNonCompliance(20010,"PDF.has.a.uncalibrated."
                                                     + "colour.space.without.an."
                                                     + "appropriate.OutputInt"
                                                     + "ent.dict");
                        return false;
                    }
                }
                if (hasDevRGB && hasDevCMYK) {
                    reportReasonForNonCompliance(20011,"PDF.has.both.DeviceRGB.and.DeviceCMYK.colourspaces");
                    return false;   // can't have both in same file
                }
            }
        }
        return true;   // passed all tests
    }

    /* If there is an uncalibrated color space, then there must be a
     * "PDF/A-1 OutputIntent." */
    private boolean checkUncalIntent ()
    {
        try {
            // First off, there must be an OutputIntents array
            // in the document catalog dictionary.
            PdfDictionary catDict = _module.getCatalogDict ();
            PdfArray intentsArray = (PdfArray) _module.resolveIndirectObject
                    (catDict.get ("OutputIntents"));
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
                PdfDictionary intent = (PdfDictionary) intVec.elementAt (0);
                PdfSimpleObject outCond =
                    (PdfSimpleObject) intent.get ("OutputCondition");
                if (outCond != null) {
                    PdfStream outProfile = (PdfStream) _module.resolveIndirectObject
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
                        PdfSimpleObject subtype = (PdfSimpleObject) intent.get ("S");
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
            PdfDictionary item = (PdfDictionary) outlineDict.get ("First");
            while (item != null) {
                if (!checkOutlineItem (item)) {
                    return false;
                }
                item = (PdfDictionary) _module.resolveIndirectObject
                        (((PdfDictionary) item).get ("Next"));
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
            PdfDictionary action = (PdfDictionary) item.get ("A");
            if (action != null) {
                if (!actionOK (action)) {
                    return false;
                }
            }
            PdfDictionary child = (PdfDictionary)
                     _module.resolveIndirectObject (item.get ("First"));
            while (child != null) {
                if (!checkOutlineItem (child)) {
                    return false;
                }
                child = (PdfDictionary)
                    _module.resolveIndirectObject (child.get ("Next"));
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
            PdfSimpleObject actType = (PdfSimpleObject) action.get ("S");
            String actStr = actType.getStringValue ();

            for (i = 0; i < excludedActions.length; i++) {
                if (excludedActions[i].equals (actStr)) {
                    return false;
                }
            }

            if ("Named".equals(actStr)){//Only 4 named actions allowed
                PdfSimpleObject actName = (PdfSimpleObject) action.get("N");
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
            PdfObject next = action.get ("Next");
            if (next instanceof PdfDictionary) {
                if (!actionOK ((PdfDictionary) next)) {
                    return false;
                }
            }
            else if (next instanceof PdfArray) {
                Vector nextVec = ((PdfArray) next).getContent ();
                for (i = 0; i < nextVec.size (); i++) {
                    PdfDictionary nact = (PdfDictionary)
                        nextVec.elementAt (i);
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
                String tr2Val = ((PdfSimpleObject) tr2).getStringValue ();
                if (!"Default".equals (tr2Val)) {
                    return false;
                }
            }

            // RI is restricted to the traditional 4 rendering intents
            PdfSimpleObject ri = (PdfSimpleObject) gs.get ("RI");
            if (ri != null) {
                String riVal = ri.getStringValue ();
                if (!validIntentString (riVal)) {
                    return false;
                }
            }

            // SMask is allowed only with a value of "None".
            PdfSimpleObject smask = (PdfSimpleObject) gs.get ("SMask");
            if (smask != null) {
                String smVal = smask.getStringValue ();
                if (!"None".equals (smVal)) {
                    return false;
                }
            }

            // BM, if present, must be "Normal" or "Compatible"
            PdfSimpleObject blendMode =
                (PdfSimpleObject) gs.get ("BM");
            if (blendMode != null) {
                String bmVal = blendMode.getStringValue ();
                if (!"Normal".equals (bmVal) &&
                !"Compatible".equals (bmVal)) {
                return false;
                }
            }

            // CA and ca must be 1.0, if present
            PdfSimpleObject ca = (PdfSimpleObject) gs.get ("CA");
            double caVal;
            if (ca != null) {
                caVal = ca.getDoubleValue ();
                if (caVal != 1.0) {
                return false;
                }
            }
            ca = (PdfSimpleObject) gs.get ("ca");
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


    /**
     *  Checks a single XObject for xObjectsOK.  Always returns <code>true</code>.
     */
    protected boolean xObjectOK (PdfDictionary xo)
    {
        if (xo == null) {
            // no XObject means no problem
            return true;
        }
        try {
            // PostScript XObjects aren't allowed.
            // Image XObjects must meet certain tests.
            PdfSimpleObject subtype = (PdfSimpleObject) xo.get ("Subtype");
            if (subtype != null) {
                String subtypeVal = subtype.getStringValue ();
                if ("PS".equals (subtypeVal)) {
                    // PS XObjects aren't allowed.
                    return false;
                }
                if ("Image".equals (subtypeVal)) {
                    if (!imageObjectOK (xo)) {
                        return false;
                    }
                }
                if ("Form".equals (subtypeVal)) {
                    if (!formObjectOK (xo)) {
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

    /** Checks if a Form xobject is valid.  This overrides the method in
       XProfileBase. */
    protected boolean formObjectOK (PdfDictionary xo)
    {
        // PDF/A elements can't have an OPI or Ref key in Form xobjects.
        if (xo.get ("OPI") != null || xo.get ("Ref") != null) {
            return false;
        }
        return true;
    }


    /** Checks if a single image XObject fits the profile */
    protected boolean imageObjectOK (PdfDictionary xo)
    {
        try {
            // OPI and Alternates keys are disallowed
            if (xo.get ("OPI") != null ||
                xo.get ("Alternates") != null) {
                return false;
            }

            // Interpolate is allowed only if its value is false.
            PdfSimpleObject interp = (PdfSimpleObject) xo.get ("Interpolate");
            if (interp != null) {
                if (!interp.isFalse ()) {
                    return false;
                }
            }

            // Intent must be one of the four standard rendering intents,
            // if present.
            PdfSimpleObject intent = (PdfSimpleObject) xo.get ("Intent");
            if (intent != null) {
                String intentStr = intent.getStringValue ();
                if (! validIntentString (intentStr)) {
                    return false;
                }
            }

        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean validIntentString (String str)
    {
        return ("RelativeColorimetric".equals (str) ||
               "AbsoluteColorimetric".equals (str) ||
               "Perceptual".equals (str) ||
               "Saturation".equals (str));
    }

    // See if the metadata stream from the catalog dictionary is OK
    private boolean metadataOK (PdfStream metadata)
    {
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
