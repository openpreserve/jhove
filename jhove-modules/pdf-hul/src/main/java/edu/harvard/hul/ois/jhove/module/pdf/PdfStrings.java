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
        "Document-defined"
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
		add("ADBE"); // Adobe
    	add("ITXT"); // 1T3XT BVBA
    	add("CIP4"); // International Cooperation for the Integration of Process in Prepress, Press and Postpress Association
    	add("FOPN"); // FileOpen Systems Inc.
    	add("SNLF"); // SNL Financial, LC
    	add("K3SD"); // Kanrikogaku Kenkyusho, Ltd.,  Meguro Office
    	add("CRDF"); // Autonomy Cardiff
    	add("PDTH"); // PDF Thingys
    	add("GURG"); // Gurnet Group LLC
    	add("USCT"); // Administrative Office of the U.S. Courts
    	add("MTSJ"); // Mekentosj BV
    	add("AJIc"); // Aji, LLC
    	add("GFSw"); // Goofyfootsoftware
    	add("LTUd"); // Office of the Chief Archivist of Lithuania
    	add("MSFT"); // Microsoft Corporation
    	add("wgss"); // Wacom Co, Ltd
    	add("bPRO"); // BiPRO e.V.
    	add("HEBD"); // H-E-B Grocery
    	add("iPDF"); // InteractivePDF.org
    	add("CALS"); // Callas Software gmbh
    	add("OOPS"); // JNJ
    	add("KWSQ"); // Kawseq Consulting Pty Ltd
    	add("PIER"); // Pierre Choutet
    	add("SCIN"); // Scinaptic Communications
    	add("VSMA"); // Visma Software International AS
    	add("MSMO"); // Mortgage Industry Standards Maintenance Organization
    	add("DUFF"); // Adlib Publishing Systems
    	add("ADLB"); // ALSTOM (Switzerland) Ltd
    	add("FNBC"); // Andrea Vacandio, Sejda.org
    	add("sjda"); // ZETO Sp. z o.o. w Lublinie
    	add("ZETO"); // Setasign
    	add("SETA"); // SOFHA Gmbh
    	add("SOFH"); // AcroScript
    	add("ASGJ"); // UAB "Superita"
    	add("WRPC"); // Instituto Nacional de Tecnologia da Informação
    	add("PBAD"); // China Finacial Certification Authority Co.Ltd
    	add("cfca"); // China Finacial Certification Authority Co.Ltd
    	add("CFCA"); // Timeslice Ltd
    	add("TMSL"); // Wacom Co, Ltd
    	add("WGSS"); // Reindl-IT
    	add("SRIT"); // Global Graphics
    	add("GGSL"); // Evansco, LLC
    	add("EVSC"); // PDFlib GmbH
    	add("Plib"); // Walters Kluwer TeamMate
    	add("WKTM"); // Normex s.r.o.
    	add("NORM"); // PDF Association
    	add("pdfa"); // Michael Klink
    	add("MKLx"); // ISO (via the 3D PDF Consortium)
    	add("ISO_"); // ISO TC130/WG2 as described in ISO 21812
    	add("GTSm"); // 1T3XT BVBA
    }};
    
    /** A private constructor just to make sure nobody
       instantiates the class by mistake. */
    private PdfStrings ()
    {
    }

}
