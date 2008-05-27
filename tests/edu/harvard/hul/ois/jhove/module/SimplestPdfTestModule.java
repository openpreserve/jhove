package edu.harvard.hul.ois.jhove.module;

import edu.harvard.hul.ois.jhove.module.pdf.PageTreeNode;
import edu.harvard.hul.ois.jhove.module.pdf.PdfDictionary;
import edu.harvard.hul.ois.jhove.module.pdf.PdfObject;
import edu.harvard.hul.ois.jhove.module.pdf.PdfException;
import edu.harvard.hul.ois.jhove.module.pdf.NameTreeNode;
import edu.harvard.hul.ois.jhove.module.pdf.PdfSimpleObject;
import edu.harvard.hul.ois.jhove.module.pdf.Token;
import edu.harvard.hul.ois.jhove.module.pdf.Literal;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by IntelliJ IDEA. User: abr Date: May 27, 2008 Time: 11:17:05 AM To
 * change this template use File | Settings | File Templates.
 */
public class SimplestPdfTestModule implements PdfModuleQueryInterface {

    private PdfDictionary trailerDict;
    private PdfDictionary catalogDict;
    private PdfDictionary viewPrefDict;
    private PdfDictionary outlineDict;
    private PdfDictionary encryptionDict;
    private PdfDictionary docInfoDict;

    private PageTreeNode pageTreeNode;


    /**
     * Creates a PdfDictionary with just the ID tag
     * @return
     */
    private PdfDictionary createSparseTrailerDict(){
        PdfDictionary mytrailerDict = new PdfDictionary();
        Literal id = new Literal();
        id.setValue("SimpleSamplePdf");
        PdfObject ID_tag = new PdfSimpleObject(id);
        mytrailerDict.add("ID",ID_tag);
        return mytrailerDict;

    }


    public SimplestPdfTestModule() {
        trailerDict = createSparseTrailerDict();

    }

    public boolean mayBePDFACompliant() {
        return true;
    }

    public List getReasonsForPDFANonCompliance() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PageTreeNode getDocumentTree() {
        return pageTreeNode;
    }

    public PdfDictionary getDocInfo() {
        return docInfoDict;
    }

    public PdfDictionary getEncryptionDict() {
        return encryptionDict;
    }

    public boolean getActionsExist() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PdfObject resolveIndirectObject(PdfObject indObj)
            throws PdfException, IOException {
        return indObj;
    }

    public RandomAccessFile getFile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PdfDictionary getCatalogDict() {
        return catalogDict;
    }

    public PdfDictionary getTrailerDict() {
        return trailerDict;
    }

    public PdfDictionary getViewPrefDict() {
        return viewPrefDict;
    }

    public PdfDictionary getOutlineDict() {
        return outlineDict;
    }

    public Map getFontMap(int selector) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List getFontMaps() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NameTreeNode getEmbeddedFiles() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
