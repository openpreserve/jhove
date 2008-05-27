package edu.harvard.hul.ois.jhove.module;

import edu.harvard.hul.ois.jhove.module.pdf.Name;
import edu.harvard.hul.ois.jhove.module.pdf.NameTreeNode;
import edu.harvard.hul.ois.jhove.module.pdf.Numeric;
import edu.harvard.hul.ois.jhove.module.pdf.PageTreeNode;
import edu.harvard.hul.ois.jhove.module.pdf.PdfArray;
import edu.harvard.hul.ois.jhove.module.pdf.PdfDictionary;
import edu.harvard.hul.ois.jhove.module.pdf.PdfException;
import edu.harvard.hul.ois.jhove.module.pdf.PdfIndirectObj;
import edu.harvard.hul.ois.jhove.module.pdf.PdfObject;
import edu.harvard.hul.ois.jhove.module.pdf.PdfSimpleObject;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This strives to be a valid 1.4 pdf file containing ONLY the required fields.
 * This class can then be extended to allow for more complex test pdf files.
 */
public class SimplestPdfTestModule extends PdfModule {

    private PdfDictionary trailerDict;
    private PdfDictionary catalogDict;
    private PdfDictionary viewPrefDict;
    private PdfDictionary outlineDict;
    private PdfDictionary encryptionDict;
    private PdfDictionary docInfoDict;

    private PageTreeNode pageTreeNode;

    private Map objectMap;




    /**
     * Creates a PdfDictionary with just the ID tag
     * @return
     */
    private PdfDictionary createTrailerDict(int _oNum, PdfDictionary catalogDict){
        PdfDictionary mytrailerDict = new PdfDictionary();

        PdfArray ID = new PdfArray();
        ID.add(createName("SimpleSamplePdf"));
        ID.add(createName("SimpleSamplePdf"));

        mytrailerDict.add("ID",ID);
        mytrailerDict.add("Size",createInteger(_oNum));

        PdfObject refCatalog = ref(catalogDict);
        mytrailerDict.add("Root",refCatalog);

        return mytrailerDict;

    }

    private PdfDictionary createCatalog() throws PdfException {
        PdfDictionary catalogDict = new PdfDictionary();

        catalogDict.add("Type",createName("Catalog"));

        PdfDictionary pages = createPages();
        catalogDict.add("Pages",ref(pages));
        pageTreeNode = new PageTreeNode(this,null,pages);
        pageTreeNode.buildSubtree(true,30);
        return catalogDict;
    }

    private PdfDictionary createPages(){

        PdfDictionary pages = new PdfDictionary();
        PdfIndirectObj PagesRef = ref(pages);


        pages.add("Type",createName("Pages"));


        PdfArray Kids = new PdfArray();
        Kids.add(ref(createPage(PagesRef)));
        Kids.add(ref(createPage(PagesRef)));
        Kids.add(ref(createPage(PagesRef)));
        pages.add("Kids",Kids);
        pages.add("Count",createInteger(3));

        PdfArray mediaBox = new PdfArray();
        mediaBox.add(createInteger(0));//llx
        mediaBox.add(createInteger(300));//lly
        mediaBox.add(createInteger(200));//urx
        mediaBox.add(createInteger(0));//ury
        pages.add("MediaBox",mediaBox);  //Mediabox is inherited in the pages
        return pages;

    }


    private PdfSimpleObject createName(String name){
        Name typeName = new Name();
        typeName.setValue(name);
        return new PdfSimpleObject(typeName);
    }

    private PdfSimpleObject createInteger(int integer){
        Numeric num = new Numeric();
        num.setValue(integer);
        return new PdfSimpleObject(num);
    }



    private PdfDictionary createPage(PdfIndirectObj parent){
        PdfDictionary page = new PdfDictionary();
        Name typeName = new Name();
        typeName.setValue("Page");
        PdfSimpleObject type = new PdfSimpleObject(typeName);
        page.add("Type",type);
        page.add("Parent",parent);
        page.add("Resources",new PdfDictionary());//individual resources for each page
        
        return page;
    }

    private int _oNum= 1;
    private int getNextObjectNumber(){
        return _oNum++;
    }


    private long getObjectMapKey(PdfObject o){

        int _objNumber = o.getObjNumber();
        int _genNumber = o.getGenNumber();
        return ((long) _objNumber << 32) +
                   ((long) _genNumber & 0XFFFFFFFFL);
    }

    private PdfIndirectObj ref(PdfObject o){
        int _objNumber = o.getObjNumber();
        int _genNumber = o.getGenNumber();
        if (_genNumber < 0){
            o.setGenNumber(0);
            _genNumber = o.getGenNumber();
        }
        if (_objNumber < 0){
            o.setObjNumber(getNextObjectNumber());
            _objNumber = o.getObjNumber();
        }
        objectMap.put(new Long(getObjectMapKey(o)),o);
        return new PdfIndirectObj(_objNumber,_genNumber,objectMap);
    }


    public SimplestPdfTestModule() throws PdfException {
        objectMap = new TreeMap();
        catalogDict = createCatalog();
        trailerDict = createTrailerDict(_oNum,catalogDict);
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
        if (indObj instanceof PdfIndirectObj){
            return ((PdfIndirectObj)indObj).getObject();
        }else{
            return indObj;
        }
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
