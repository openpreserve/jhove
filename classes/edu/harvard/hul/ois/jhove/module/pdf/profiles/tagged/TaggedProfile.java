package edu.harvard.hul.ois.jhove.module.pdf.profiles.tagged;

import edu.harvard.hul.ois.jhove.module.PdfModule;
import edu.harvard.hul.ois.jhove.module.pdf.PdfDictionary;
import edu.harvard.hul.ois.jhove.module.pdf.PdfSimpleObject;
import edu.harvard.hul.ois.jhove.module.pdf.profiles.PdfProfile;

/**
 *  PDF profile checker for Tagged PDF documents.
 *  See section 9.7, "Tagged PDF", of the PDF Reference,
 *  Version 1.4, for an explanation of tagged PDF.
 */
public final class TaggedProfile extends PdfProfile
{
    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/

    /** 
     *   Constructor.
     *   Creates a TaggedProfile object for subsequent testing.
     *
     *   @param  module   The module under which we are checking the profile.
     *
     */
    public TaggedProfile (PdfModule module) 
    {
        super (module);
        _profileText = "Tagged PDF";
    }


    /**
     * Returns <code>true</code> if the document satisfies the profile.
     * We check only the dictionaries, not the stream contents.
     *
     */
    public boolean satisfiesThisProfile ()
    {
        try {
            PdfDictionary docCatDict = _module.getCatalogDict ();
            if (docCatDict == null){
                reportReasonForNonCompliance(
                                             "PDF.has.no.catalog.dictionary");
                return false;
            }

            // An entry named markInfo must be in the doc catalog,
            // and must be a dictionary.  The dictionary must
            // contain an entry named Marked, which must have a value
            // of true.
            PdfDictionary markInfo;
            try{
                 markInfo = (PdfDictionary)
                        _module.resolveIndirectObject (docCatDict.get ("MarkInfo"));
                if (markInfo == null) {
                    reportReasonForNonCompliance(

                            "PDF.catalog.dictionary.has.no.MarkInfo.entry");
                    return false;
                }
            } catch (ClassCastException e){
                reportReasonForNonCompliance(
                                             "PDF.catalog.dictionary.entry."
                                             + "MarkInfo.is.not.a.dictionary");
                return false;
            }

            try {
                PdfSimpleObject marked =
                        (PdfSimpleObject) markInfo.get ("Marked");
                if (!marked.isTrue ()) {
                    reportReasonForNonCompliance(

                            "PDF.catalog.dictionary.entry.MarkInfo.entry."
                            + "Marked.is.false");
                    return false;
                }
            } catch (ClassCastException e){
                reportReasonForNonCompliance(
                                             "PDF.catalog.dictionary.entry."
                                             + "MarkInfo.entry.Marked.is.not.a."
                                             + "simple.object");
                return false;

            }
            // So much for MarkInfo.  Now see if there is a
            // valid structure tree.
            StructureTree stree = new StructureTree (_module,
                        _raf, _parser);
            if (!stree.isPresent () || !stree.isValid ()) {
                reportReasonsForNonCompliance(stree.whatsWrong());
                return false;
            }
        }
        catch (Exception e) {
            // An exception thrown anywhere means some assumption
            // has been violated, so it doesn't meet the profile.
            reportReasonForNonCompliance(
                                         e.getMessage());
            return false;
        }
        return true;    // passed all tests
    }


}



