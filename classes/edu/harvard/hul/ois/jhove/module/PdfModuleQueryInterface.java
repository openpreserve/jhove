package edu.harvard.hul.ois.jhove.module;

import edu.harvard.hul.ois.jhove.module.pdf.PageTreeNode;
import edu.harvard.hul.ois.jhove.module.pdf.PdfDictionary;
import edu.harvard.hul.ois.jhove.module.pdf.PdfObject;
import edu.harvard.hul.ois.jhove.module.pdf.PdfException;
import edu.harvard.hul.ois.jhove.module.pdf.NameTreeNode;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by IntelliJ IDEA. User: abr Date: May 27, 2008 Time: 11:09:03 AM To
 * change this template use File | Settings | File Templates.
 */
public interface PdfModuleQueryInterface {

    boolean mayBePDFACompliant ();

    List getReasonsForPDFANonCompliance();

    PageTreeNode getDocumentTree ();

    PdfDictionary getDocInfo ();

    PdfDictionary getEncryptionDict ();

    boolean getActionsExist ();

    PdfObject resolveIndirectObject(PdfObject indObj)
            throws PdfException, IOException;

    RandomAccessFile getFile ();

    PdfDictionary getCatalogDict ();

    PdfDictionary getTrailerDict ();

    PdfDictionary getViewPrefDict ();

    PdfDictionary getOutlineDict ();

    Map getFontMap (int selector);

    List getFontMaps ();

    NameTreeNode getEmbeddedFiles ();
}
