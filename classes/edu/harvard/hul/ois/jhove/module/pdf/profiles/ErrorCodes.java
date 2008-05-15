package edu.harvard.hul.ois.jhove.module.pdf.profiles;

/**
 * Created by IntelliJ IDEA. User: abr Date: Apr 24, 2008 Time: 12:57:53 PM To
 * change this template use File | Settings | File Templates.
 */
public class ErrorCodes {

    //pdf-a level a
    public static final class pdfa_a {
        public final static int not_a_compliant_tagged_pdf = 30001;

        public final static int not_a_compliant_pdfa_lvl_b = 30002            ;
        public static int exception_was_thrown = 39999;
    }


    //tagged pdf
    public static final class tagged {


        public static final int no_catalog_dict = 10001;

        public static final int catalog_dict_has_no_markinfo = 10002;

        public static final int markinfo_is_not_a_dict = 10003;

        public static final int markinfo_marked_is_false = 10004;

        public static final int marked_is_not_simple = 10005;

        public static final int exception_was_thrown = 19999;

    }
    public static class pdfa_b{

        public static int exception_was_thrown = 29999;
        public static int no_trailer_dict =20001;
        public static int trailer_dict_has_encrypt = 20002;
        public static int trailer_has_no_ID = 20003;
        public static int no_catalog_dict = 20004;
        public static int invalid_lang = 20005;
        public static int lang_entry_without_string = 20006;
        public static int catalog_dict_has_AA_entry = 20007;
        public static int catalog_dict_has_OCProperties_entry = 20008;
    }
}
