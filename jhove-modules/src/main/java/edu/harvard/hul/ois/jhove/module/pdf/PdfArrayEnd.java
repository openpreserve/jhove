package edu.harvard.hul.ois.jhove.module.pdf;

/** A marker for the parser to return when it encounters the end of
 *  a PDF array. 
 */
public class PdfArrayEnd extends PdfPseudoObject {

    private Token token;
    
    public PdfArrayEnd (Token tok) {
        token = tok;
    }
    
    public Token getToken () {
        return token;
    }
}
