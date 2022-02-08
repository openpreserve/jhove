/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import edu.harvard.hul.ois.jhove.module.PdfModule;
import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 *  Class for element of PDF document structure tree.
 *
 *  @see StructureTree
 */
public class StructureElement 
{
    private StructureTree _tree;
    private PdfDictionary _dict;
    private PdfModule _module;
    private List<StructureElement> children;
    private String _structType;
    private boolean _structIsInline;
    private boolean _attrIsBlock;
    
    private Logger _logger;

    /* Attributes which should occur only in block level elements */
    private static final String blockLevelAttrs [] = {
        "SpaceBefore", "SpaceAfter", "StartIndent",
        "EndIndent", "TextIndent", "TextAlign", "BBox",
        "Width", "Height", "BlockAlign", "InlineAlign"
    };



    /**
     *  Constructor.
     *  @param dict       A PdfDictionary corresponding to a structure
     *                    element
     *  @param tree       The root StructureTree object
     */
    public StructureElement (PdfDictionary dict, StructureTree tree)
                throws PdfException
    {
        _logger = Logger.getLogger ("edu.harvard.hul.ois.jhove.module");
        _tree = tree;
        _dict = dict;
        _module = tree.getModule ();
        _structType = null;
        
        // If this element has a standard structure type, find it.
        try {
            PdfObject s = _module.resolveIndirectObject (dict.get ("S"));
            Token tok = ((PdfSimpleObject) s).getToken ();
            String st = ((Name) tok).getValue ();
            st = _tree.dereferenceStructType (st);
            if (StdStructTypes.includes (st)) {
                _structType = st;
            }
        }
        catch (IOException e) {
            _logger.warning(e.getClass().getName());
        }
    }
    
    /**
     *   Build this element's subtree, if any
     *   This checks the "K" entry in the dictionary and 
     *   locates all referened structure elements. These
     *   are put into StructureElement objects, which have
     *   their own subtrees built, and these StructureElements
     *   are accumulated into <code>children</code>.
     */
    public void buildSubtree(int recGuard) throws PdfException
    {
        /* Guard against infinite recursion */
        if (recGuard <= 0) {
            throw new PdfMalformedException(MessageConstants.PDF_HUL_32); // PDF-HUL-32
        }

        _logger.info(MessageConstants.LOG_SUBTREE_BUILDING);
        PdfObject k = null;
        try {
            k = _module.resolveIndirectObject (_dict.get ("K"));
        }
        catch (IOException e) {
            throw new PdfInvalidException(MessageConstants.PDF_HUL_58); // PDF-HUL-58
        }
        children = null;
        
        // The "K" element is complicated, having five variants.
        if (k instanceof PdfSimpleObject) {
            // A marked-content identifier. We don't explore further.
            return;
        }
        else if (k instanceof PdfDictionary) {
            // Could be any of three kinds of dictionaries:
            // - A marked-content reference dictionary
                // - A PDF object reference dictionary
            // - A structure element reference dictionary
            // The only one we check seriously is a structure element.
            _logger.info (MessageConstants.LOG_K_ELEM_IS_DICT);
            PdfDictionary kdict = (PdfDictionary) k;
            if (isStructElem (kdict)) {
                PdfObject kidsObject = kdict.get("K");
                if(kidsObject instanceof PdfSimpleObject) {
                    PdfSimpleObject kids = (PdfSimpleObject)kidsObject;
                    Token tok = kids.getToken();
                    if(tok instanceof Numeric) {
                        //if the kids value is zero then there are no child objects; exit method
                        if(((Numeric)tok).getValue()==0) {
                            children = null;
                            return;
                        }
                    }
                }
                StructureElement se = new StructureElement(kdict, _tree);
                se.buildSubtree(recGuard - 1);

                se.checkAttributes ();
                children = new ArrayList<> (1);
                children.add (se);
            }
            else if (!isMarkedContent (kdict) && !isObjectRef (kdict)) {
                throw new PdfInvalidException 
                       (MessageConstants.PDF_HUL_49); // PDF-HUL-49
            }
        }
        else if (k instanceof PdfArray) {
            _logger.info (MessageConstants.LOG_K_ELEM_IS_ARRY);
            Vector<PdfObject> kvec = ((PdfArray) k).getContent ();
            children = new LinkedList<> ();
            for (int i = 0; i < kvec.size (); i++) {
                PdfObject kelem = kvec.elementAt (i);
                try {
                    kelem = _module.resolveIndirectObject (kelem);
                }
                catch (IOException e) {}
                if (kelem instanceof PdfDictionary) {
                    PdfDictionary kdict = (PdfDictionary) kelem;
                    if (isStructElem (kdict)) {
                                _logger.info (MessageConstants.LOG_SUBTREE_BUILDING);

                                //check for non-zero before creating a new StructureElement
                                //cf. Govdocs file http://digitalcorpora.org/corp/nps/files/govdocs1/000/000153.pdf
                                //object 717 that has "/K 0" enters infinite(?) loop without this check
                                PdfObject kidsObject = kdict.get("K");
                                if(kidsObject instanceof PdfSimpleObject) {
                                    PdfSimpleObject kids = (PdfSimpleObject)kidsObject;
                                    Token tok = kids.getToken();
                                    if (tok instanceof Numeric && ((Numeric)tok).getValue() == 0) {
                                        //if the kids value is zero then there are no child objects; exit method
                                        _logger.info(MessageConstants.LOG_NO_CHILD_OBJS);
                                        children = null;
                                        return;
                                        
                                    }
                        }
                        StructureElement se = new StructureElement(kdict, _tree);
                        se.buildSubtree(recGuard - 1);
                        se.checkAttributes();
                        children.add(se);
                    }
                }
            }
            // It's possible that none of the elements of the array
            // were structure elements.  In this case, we change
            // children to null rather than have to check for an
            // empty vector.
            if (children.isEmpty ()) {
                _logger.info (MessageConstants.LOG_NO_CHILD_STRUCT_ELEM);
                children = null;
            }
        }
        
        // If this is a transient tree, we don't need the children
        // after we're done. Clear the tree to save memory.
        if (_tree.isTransient()) {
            children = null;
        }
    }

    /**
     *  Determine if the attributes of this element are
     *  valid.  If errors are detected, throws a PdfInvalidException.
     */
    public void checkAttributes () throws PdfException {
        PdfObject attr;
        
        // Use the variables _structIsInline and _attrIsBlock to
        // note when we've got a block-level-only attribute in
        // an inline structure element. We initially set
        // _structIsInline based on the structure type, but this
        // may be overridden by the Placement attribute.
        // Figure elements occupy an ambiguous position, so we
        // don't mark them as ILSE's.  Also, TR, TH and TD are
        // defined to be neither BLSE's nor ILSE's.
        _attrIsBlock = false;
        _structIsInline = !"Figure".equals (_structType) &&
            !"TH".equals (_structType) &&
            !"TD".equals (_structType) &&
            !"TR".equals (_structType) &&
            !StdStructTypes.isBlockLevel (_structType);
        
        try {
            attr = _module.resolveIndirectObject (_dict.get ("A"));
        }
        catch (Exception e) {
            throw new PdfInvalidException (MessageConstants.PDF_HUL_50); // PDF-HUL-50
        }
        if (attr == null) {
            // no attributes is fine
            return;
        }
        if (attr instanceof PdfArray) {
            // If we have an array, it may contain elements and
            // revision numbers.  A revision number may follow
            // an element, but there doesn't have to be one.
            Vector<PdfObject> attrVec = ((PdfArray) attr).getContent ();
            for (int i = 0; i < attrVec.size (); i++) {
                PdfObject attrElem;
                try {
                    attrElem = _module.resolveIndirectObject
                            (attrVec.elementAt (i));
                }
                catch (IOException e) {
                    _logger.log(Level.INFO, MessageConstants.PDF_HUL_51.getMessage(), e);
                    throw new PdfInvalidException (MessageConstants.PDF_HUL_51); // PDF-HUL-51
                }
                if (attrElem instanceof PdfDictionary) {
                    checkAttribute ((PdfDictionary) attrElem);
                }
                else if (attrElem instanceof PdfSimpleObject) {
                    try {
                        Numeric revnum = (Numeric) 
                            ((PdfSimpleObject)attrElem).getToken ();
                    }
                    catch (Exception e) {
                        _logger.log(Level.INFO, MessageConstants.LOG_REVISION_NUM_RETRIEVAL_EXCEP, e);
                        throw new PdfInvalidException (MessageConstants.PDF_HUL_52); // PDF-HUL-52
                    }
                }
                else {
                    throw new PdfInvalidException (MessageConstants.PDF_HUL_53); // PDF-HUL-53
                }
            }
        }
        else if (attr instanceof PdfDictionary) {
            checkAttribute ((PdfDictionary) attr);
        }
        else {
            throw new PdfInvalidException (MessageConstants.PDF_HUL_54); // PDF-HUL-54
        }
        if (_structIsInline && _attrIsBlock) {
            throw new PdfInvalidException (MessageConstants.PDF_HUL_55); // PDF-HUL-55
        }
    }


    /* Check if an attribute dictionary is reasonable. */
    private void checkAttribute (PdfDictionary attr)
                                throws PdfException
    {
        try {
            // Must have an entry named "O", whose value is a name.
            //PdfSimpleObject plugin = (PdfSimpleObject) attr.get ("O");
            //Name tok = (Name) plugin.getToken ();

            // If it has a Placement entry with a value other than
            // "Inline", then we allow block level attributes.
            PdfSimpleObject placement = 
                (PdfSimpleObject) attr.get ("Placement");
            if (placement != null && 
                !"Inline".equals (placement.getStringValue ())) {
                _structIsInline = false;
            }
            // Though I don't think the Adobe PDF bible actually
            // says so, it appears that the "attributes" are
            // simply other keys in the attribute dictionary.
            // Remember if we see attributes that can't go in BLSE's;
            // we'll check later if we're actually in a BLSE.
            if (attrIsBlockLevel (attr)) {
                _attrIsBlock = true;
            }
        }
        catch (Exception e) {
            throw new PdfInvalidException (MessageConstants.PDF_HUL_56); // PDF-HUL-56
        }
    }



    /* See if a dictionary is a structure element. 
       We identify it by the S and P elements, which are
       required, and by making sure that the Type element,
       if present, has a value of "StructElem".
    */
    private boolean isStructElem (PdfDictionary elem)
    {
        try {
            PdfObject typ = elem.get ("Type");
            if (typ != null && !"StructElem".equals
                      (((PdfSimpleObject) typ).getStringValue ())) {
                return false;
            }

            PdfObject s = _module.resolveIndirectObject (elem.get ("S"));
            // The structure type is supposed to be one of
            // a list of known structure types, or else is
            // mapped to one through the role map dictionary.
            // For the moment, just make sure it's a name.
            if (!(s instanceof PdfSimpleObject)) {
                return false;
            }
            Token tok = ((PdfSimpleObject) s).getToken ();
            if (!(tok instanceof Name)) {
                return false;
            }
            // It appears that there really isn't a requirement
            // to have structure types belong to the standard types.
            // Conditionalize this code out, pending more info.
            String st = ((Name) tok).getValue ();
            st = _tree.dereferenceStructType (st);
            // TODO: This check is indeed wrong and should be removed altogether
            boolean checkStandardTypes = false;
            if (checkStandardTypes) {
                if (!StdStructTypes.includes (st)) {
                    throw new PdfInvalidException(MessageConstants.PDF_HUL_57);
                }
            }
            else {
                // The structure type is a standard one. 
            }
            // The parent reference must be an indirect reference.
            // The documentation says it must refer to another
            // structure element dictionary, but it seems that it
            // must also be able to refer to the structure tree root. 
            // I'll allow both.
            PdfObject pref = elem.get ("P");
            if (!(pref instanceof PdfIndirectObj)) {
                return false;
            }
            // Make sure it refers to a dictionary (at least).
            PdfDictionary p = (PdfDictionary) 
                _module.resolveIndirectObject (pref);
            PdfSimpleObject ptype = (PdfSimpleObject) p.get ("Type");
            if (ptype != null) {
                String typename = ptype.getStringValue ();
                if (!"StructTreeRoot".equals (typename) &&
                    !"StructElem".equals (typename)) {
                    return false;
                }
            }
            // Passed all tests.
            return true;
        }
        catch (Exception e) {
            // Some assumption was violated
            return false;
        }
    }

    /* See if an attribute dictionary has attributes which
       are permitted only at block level. */
    private static boolean attrIsBlockLevel (PdfDictionary attrDict)
    {
        for (int i = 0; i < blockLevelAttrs.length; i++) {
            if (attrDict.get (blockLevelAttrs[i]) != null) {
                return true;
            }
        }
        return false;
    }

    /* Determine if a dictionary is a marked content dictionary.
       See Table 9.11 in the PDF 1.4 book. */
    private boolean isMarkedContent (PdfDictionary dict)
    {
        try {
            PdfSimpleObject typeObj = 
                        (PdfSimpleObject) dict.get ("Type");
            if (!"MCR".equals (typeObj.getStringValue ())) {
                return false;
            }
            // An MCID entry is required.
            PdfSimpleObject mcidObj =
                (PdfSimpleObject) _module.resolveIndirectObject
                        (dict.get ("MCID"));
            return mcidObj != null;
        }
        catch (Exception e) {
            return false;
        }
    }

    /* Determine if a dictionary is an object reference dictionary,
       as in table 9.12. */
    private boolean isObjectRef (PdfDictionary dict)
    {
        try {
            PdfSimpleObject typeObj = 
                        (PdfSimpleObject) dict.get ("Type");
            if (!"OBJR".equals (typeObj.getStringValue ())) {
                return false;
            }
            // An Obj entry is required. Must be an indirect object.
            PdfObject obj = _module.resolveIndirectObject
                        (dict.get ("Obj"));
            return obj != null;
        }
        catch (Exception e) {
            return false;
        }
        
    }
}
