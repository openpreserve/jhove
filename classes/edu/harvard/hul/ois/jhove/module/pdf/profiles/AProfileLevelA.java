/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004-2005 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf.profiles;

import edu.harvard.hul.ois.jhove.module.PdfModule;
import edu.harvard.hul.ois.jhove.module.pdf.PdfArray;
import edu.harvard.hul.ois.jhove.module.pdf.PdfDictionary;
import edu.harvard.hul.ois.jhove.module.pdf.PdfObject;
import edu.harvard.hul.ois.jhove.module.pdf.PdfSimpleObject;
import edu.harvard.hul.ois.jhove.module.pdf.profiles.tagged.TaggedProfile;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *  PDF profile checker for PDF/A-1 documents, Level A.
 *  See ISO draft ISO/TC171/SC2, "Document Imaging Applications
 *  Application Issues".
 *
 *  This profile checker is dependent on AProfileLevelB and TaggedProfile. In
 *  addition, it performs checks of the fonts, as required by the Level A specs
 *
 * @author Gary McGath
 *
 */
public class AProfileLevelA extends PdfProfile {

        private boolean _levelA;

    /* TaggedProfile to which this profile is linked. */
    private TaggedProfile _taggedProfile;


    /* AProfileLevelB to which this profile is linked. */
    private AProfileLevelB _aProfileLevelB;

    /**
     *   Constructor.
     *   Creates an AProfileLevelA object for subsequent testing.
     *
     *   @param  module   The module under which we are checking the profile.
     *
     */
    public AProfileLevelA(PdfModule module) {
        super (module);
        _profileText = "ISO PDF/A-1, Level A";
    }

    /**
     * Returns <code>true</code> if the document satisfies the profile
     * at Level A.  This returns a meaningful result only if 
     * <code>satisfiesThisProfile()</code> has previously
     * been called on the profile assigned by <code>setAProfile</code>.
     *
     */
    public boolean satisfiesThisProfile() {
        // Conforming to the TaggedProfile requirements is necessary
        // for Level A
        _levelA = true;
        if (_taggedProfile != null &&
            !_taggedProfile.isAlreadyOK ()) {
            _levelA = false;
            reportReasonForNonCompliance(ErrorCodes.pdfa_a.not_a_compliant_tagged_pdf,
                                         "Not a compliant "
                                         +_taggedProfile.getText());

        }

        if (_aProfileLevelB != null &&
            !_aProfileLevelB.isAlreadyOK ()) {
            _levelA = false;
            reportReasonForNonCompliance(ErrorCodes.pdfa_a.not_a_compliant_pdfa_lvl_b,
                                         "Not a compliant "
                                         +_aProfileLevelB.getText());

        }


        return fontsOK() && _levelA;


    }

    /**
     *  Calling setAProfile links this AProfile to a TaggedProfile.
     *  This class gets all its information from the linked AProfile,
     *  so calling this is mandatory.
     * @param tpr the levelB profile to attach this profile to
     */
    public void setAProfile (AProfileLevelB tpr)
    {
        _aProfileLevelB = tpr;
    }

    /**
     *  Calling setTaggedProfile links this AProfile to a TaggedProfile.
     *
     * @param tpr  the TaggedProfile to attach
     */
    public void setTaggedProfile (TaggedProfile tpr)
    {
        _taggedProfile = tpr;
    }


    //Firstlevel check
    private boolean fontsOK ()
    {

        // For each type of font (just because that's the easiest way
        // to get the fonts from the PdfModule), check that each font
        // has a ToUnicode entry which is a CMap stream.
        List lst = _module.getFontMaps ();
        Iterator iter = lst.listIterator ();
        try {
            while (iter.hasNext ()) {
                Map fmap = (Map) iter.next ();
                Iterator iter1 = fmap.values ().iterator ();
                while (iter1.hasNext ()) {
                    PdfDictionary font = (PdfDictionary) iter1.next ();
                    if (!fontOK (font)) {
                        return false;
                    }
                }
            }
        }
        catch (Exception e) {
            reportReasonForNonCompliance(
                    ErrorCodes.pdfa_a.exception_was_thrown, e.getMessage());
            return false;
        }
        return true;
    }


    /* Check a font for validity. SecondLevel Check from fontsOK */
    private boolean fontOK (PdfDictionary font)
    {

        /*
         * If the font is type0, and (not an adobe ordering or one of the t
         * hree encodings)
         * then it must have a unicode representation for level A
         */
        try {
            // The ToUnicode entry is required only for Level A,
            // and there are an assortment of exceptions.
            PdfSimpleObject fType = (PdfSimpleObject) font.get("Subtype");
            String fTypeStr = fType.getStringValue ();
            if ("Type1".equals (fTypeStr)) {
                // The allowable Type 1 fonts are open ended, so
                // allow all Type 1 fonts..
                return true;
            }
            if ("Type0".equals (fTypeStr)) {
                // Type 0 fonts are OK if the descendant CIDFont uses one of
                // four specified character collections.
                //PdfObject order = font.get ("Ordering");

                PdfArray descendants = (PdfArray) font.get("DescendantFonts");
                Vector descVector = descendants.getContent();
                boolean orderingOK=true;
                for (int i=0;i<descVector.size();i++){
                    PdfDictionary cidfont = (PdfDictionary) descVector.get(i);  //each of these is a CIDFont 
                    PdfDictionary info = (PdfDictionary) cidfont.get("CIDSystemInfo"); //Which must have a CIDSystemInfo 
                    PdfObject order =  info.get("Ordering"); //Which must have an Ordering 

                    if (order instanceof PdfSimpleObject) {  //Which must be a String

                        String ordText =
                                ((PdfSimpleObject) order).getStringValue ();
                        if (!("Adobe-GB1".equals (ordText) ||
                              "Adobe-CNS1".equals (ordText) ||
                              "Adobe-Japan1".equals (ordText) ||
                              "Adobe-Korea1".equals (ordText))) {
                            orderingOK = false;

                        }
                    }
                }
                if (orderingOK){ //If all the descendants were ok, this font is ok.
                    return true;
                }
                PdfObject enc = font.get ("Encoding");
                if (enc instanceof PdfSimpleObject) {
                    String encName = ((PdfSimpleObject) enc).getStringValue ();
                    if ("WinAnsiEncoding".equals (encName) ||
                        "MacRomanDecoding".equals (encName) ||
                        "MacExpertDecoding".equals (encName)) {
                        return true;
                    }//TODO: Else. IS IT ALLRIGHT, IF IT DOES NOT EQUAL??
                    //TODO: WHY NO CATCH??
                }
                /*
             * Fixed contributed by FCLA, 2007-05-30, to permit
             * indirect as well as direct stream object.
             *
             * PdfStream toUni = (PdfStream) font.get ("ToUnicode");
             */
                PdfObject toUni = (PdfObject) font.get ("ToUnicode");//TODO:
                if (toUni == null) {
                    _levelA = false;
                }
            }
        } catch (Exception e) {
            reportReasonForNonCompliance(ErrorCodes.pdfa_a.exception_was_thrown,
                                         e.getMessage());
            return false;
        }
        return true;
    }



}
