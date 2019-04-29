package edu.harvard.hul.ois.jhove.module.pdf;

public class PdfDictionaryEnd extends PdfPseudoObject {

    private Token token;
    
    public PdfDictionaryEnd (Token tok) {
        token = tok;
    }
    
    public Token getToken () {
        return token;
    }
}
