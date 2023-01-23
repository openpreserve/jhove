/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import java.util.ArrayList;
import java.util.List;

/**
 *  A class for holding arrays of informative strings that will go into 
 *  properties of a PDF object. 
 */
public class PdfStrings 
{
    /** Encryption algorithm strings. */
    public final static String[] ALGORITHM = 
    {
        "Undocumented",
        "40-bit RC4 or AES",
        "40-bit or greater RC4 or AES",
        "Unpublished",
        "Document-defined",
        "256-bit AES"
    };

    /** Flags for FontDescriptor.  In PDF notation, bit 1
     * (not 0) is the low-order bit.
     */
    public final static String[] FONTDESCFLAGS =
    {
        "FixedPitch",    // 1
        "Serif",         // 2
        "Symbolic",      // 3
        "Script",        // 4
        "",              // 5
        "Nonsymbolic",   // 6
        "Italic",        // 7
        "",              // 8
        "",              // 9
        "",              // 10
        "",              // 11
        "",              // 12
        "",              // 13
        "",              // 14
        "",              // 15
        "",              // 16
        "AllCap",        // 17
        "SmallCap",      // 18
        "ForceBold"      // 19
    };

    /** Flags for user access permissions when revision 3 is specified. */
    public final static String[] USERPERMFLAGS3 =
    {
        "",             // 1, reserved
        "",             // 2, reserved
        "Print",        // 3
        "Modify",       // 4
        "Extract",      // 5
        "Add/modify annotations/forms",  // 6
        "",             // 7
        "",             // 8
        "Fill interactive form fields",  // 9
        "Extract for accessibility",     // 10
        "Assemble",     // 11
        "Print high quality"             // 12
    };

    /** Flags for user access permissions when revision 2 is specified. */
    public final static String[] USERPERMFLAGS2 =
    {
        "",             // 1, reserved
        "",             // 2, reserved
        "Print",        // 3
        "Modify",       // 4
        "Extract",      // 5
        "Add/modify annotations/forms",  // 6
        "",             // 7
        "",             // 8
        "",             // 9
        "",             // 10
        "",             // 11
        ""              // 12
    };

    /** Flags for annotations */
    public final static String[] ANNOTATIONFLAGS =
    {
        "Invisible",       // 1
        "Hidden",          // 2
        "Print",           // 3
        "NoZoom",          // 4
        "NoRotate",        // 5
        "NoView",          // 6
        "ReadOnly"        // 7
    };

    /** PDF Prefix names registry
     *  * This is a list with Prefixes of the PDF Prefix Names register. It is used as a dictionary for the developer
     *  prefix of developer extensions.
     *  Described in 3.6.4 Extensions to PDF: 
     *  https://www.adobe.com/content/dam/acom/en/devnet/acrobat/pdfs/adobe_supplement_iso32000.pdf
     *  Maintained in: https://github.com/adobe/pdf-names-list
     * . */
    public final static List<String> PREFIXNAMESREGISTY = new ArrayList<String>() {{
        add("_Y4Y"); // Yin4Yang BV
        add("ABCp"); // Zandent Ltd
        add("ADBE"); // Adobe Inc.
        add("ADLB"); // Adlib Publishing Systems
        add("AJIc"); // Aji, LLC
        add("ASGJ"); // AcroScript
        add("BECI"); // BEC GmbH & Co KG
        add("BFOO"); // BFO
        add("bPRO"); // BiPRO e.V.
        add("CALS"); // Callas Software gmbh
        add("cfca"); // China Finacial Certification Authority Co.Ltd
        add("CFCA"); // China Finacial Certification Authority Co.Ltd
        add("CIP4"); // International Cooperation for the Integration of Process in Prepress, Press and Postpress Association
        add("CLWN"); // pdfclown.org
        add("CRDF"); // Autonomy Cardiff
        add("diTe"); // disphere tech GmbH
        add("DLLB"); // Dual Lab
        add("DMSK"); // DMS One Zrt.
        add("DUFF"); // Duff Johnson Consulting
        add("Esko"); // Esko Software bv
        add("EVSC"); // Evansco, LLC
        add("FICL"); // Enfocus BV
        add("FNBC"); // ALSTOM (Switzerland) Ltd
        add("FOPN"); // FileOpen Systems Inc.
        add("FOXI"); // FOXIT SOFTWARE INC.
        add("GeoD"); // Mapthematics LLC
        add("GFSw"); // Goofyfootsoftware
        add("GGSL"); // Global Graphics
        add("GLAU"); // AS207960 Cyfyngedig
        add("glTF"); // ISO (via the 3D PDF Consortium)
        add("GTSm"); // ISO TC130/WG2 as described in ISO 21812
        add("GURG"); // Gurnet Group LLC
        add("GWG_"); // Ghent Workgroup
        add("HEBD"); // H-E-B Grocery
        add("HYBR"); // HYBRID Software Development NV
        add("ICTI"); // iCerti
        add("iPDF"); // InteractivePDF.org
        add("ISO_"); // ISO (via the 3D PDF Consortium)
        add("ITXT"); // 1T3XT BVBA
        add("K3SD"); // Kanrikogaku Kenkyusho, Ltd.,  Meguro Office
        add("KWSQ"); // Kawseq Consulting Pty Ltd
        add("LTUd"); // Office of the Chief Archivist of Lithuania
        add("MKLx"); // Michael Klink
        add("MSFT"); // Microsoft Corporation
        add("MSMO"); // Mortgage Industry Standards Maintenance Organization
        add("MTSJ"); // Mekentosj BV
        add("NORM"); // Normex s.r.o.
        add("NPTC"); // nepatec GmbH
        add("nptc"); // nepatec GmbH
        add("OOPS"); // JNJ
        add("PBAD"); // Instituto Nacional de Tecnologia da Informação
        add("pdfa"); // PDF Association
        add("PDTH"); // PDF Thingys
        add("PIER"); // Pierre Choutet
        add("Plib"); // PDFlib GmbH
        add("PTEX"); // PRAGMA ADE
        add("S4Cx"); // Sense4code s.r.o.
        add("SCIN"); // Scinaptic Communications
        add("SETA"); // Setasign
        add("sjda"); // Andrea Vacandio, Sejda.org
        add("slns"); // OneSpan
        add("SNLF"); // SNL Financial, LC
        add("SOFH"); // SOFHA Gmbh
        add("SRIT"); // Reindl-IT
        add("STEP"); // ISO (via the 3D PDF Consortium)
        add("TGSI"); // Telegenisys Inc.
        add("TMSL"); // Timeslice Ltd
        add("USCT"); // Administrative Office of the U.S. Courts
        add("VSMA"); // Visma Software International AS
        add("wgss"); // Wacom Co, Ltd
        add("WGSS"); // Wacom Co, Ltd
        add("WKTM"); // Walters Kluwer TeamMate
        add("WRPC"); // UAB "Superita"
        add("XMPi"); // XMPie Ltd.
        add("ZETO"); // ZETO Sp. z o.o. w Lublinie
    }};
    
    /** A private constructor just to make sure nobody
       instantiates the class by mistake. */
    private PdfStrings ()
    {
    }

}
