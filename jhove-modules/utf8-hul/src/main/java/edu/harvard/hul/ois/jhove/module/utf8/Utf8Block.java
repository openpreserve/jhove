/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.utf8;


/**
 *  This class encapsulates a Unicode code block.
 *  
 *  Updated to Unicode 7.0.0.
 *
 *  @see edu.harvard.hul.ois.jhove.module.Utf8Module
 */
public enum Utf8Block {
    /* Unicode 6.0.0 blocks, derived from
     * &lt;http://www.unicode.org/Public/3.2-Update/Blocks-3.2.0.txt&gt;
     * and updated to Unicode 6.0.0  */
	LAT(0x0000, 0x007F, "Basic Latin"),
	LAT_1_SUPP(0x0080, 0x00FF, "Latin-1 Supplement"),
	LAT_1_EXT_A(0x0100, 0x017F, "Latin Extended-A"),
	LAT_1_EXT_B(0x0180, 0x024F, "Latin Extended-B"),
	IPA_EXT(0x0250, 0x02AF, "IPA Extensions"),
	SPACE_MOD(0x02B0, 0x02FF, "Spacing Modifier Letters"),
	COMB_DIACRITICAL(0x0300, 0x036F, "Combining Diacritical Marks"),
	GREEK_COPTIC(0x0370, 0x03FF, "Greek and Coptic"),
	CYRILLIC(0x0400, 0x04FF, "Cyrillic"),
	CYRILLIC_SUPP(0x0500, 0x052F, "Cyrillic Supplementary"),
	ARMENIAN(0x0530, 0x058F, "Armenian"),
	HEBREW(0x0590, 0x05FF, "Hebrew"),
	ARABIC(0x0600, 0x06FF, "Arabic"),
	SYRIAC(0x0700, 0x074F, "Syriac"),
	THAANA(0x0780, 0x07BF, "Thaana"),
	NKO(0x07C0, 0x07FF, "NKo"),
	MANDIAC(0x0840, 0x085F, "Mandaic"),
	DEVANAGARI(0x0900, 0x097F, "Devanagari"),
	BENGALI(0x0980, 0x09FF, "Bengali"),
	GURMUKHI(0x0A00, 0x0A7F, "Gurmukhi"),
	GUJARATI(0x0A80, 0x0AFF, "Gujarati"),
	ORIYA(0x0B00, 0x0B7F, "Oriya"),
	TAMIL(0x0B80, 0x0BFF, "Tamil"),
	TELUGU(0x0C00, 0x0C7F, "Telugu"),
	KANNADA(0x0C80, 0x0CFF, "Kannada"),
	MALAYALAM(0x0D00, 0x0D7F, "Malayalam"),
	SINHALA(0x0D80, 0x0DFF, "Sinhala"),
	THAI(0x0E00, 0x0E7F, "Thai"),
	LAO(0x0E80, 0x0EFF, "Lao"),
	TIBETAN(0x0F00, 0x0FFF, "Tibetan"),
	MYANMAR(0x1000, 0x109F, "Myanmar"),
	GEORGIAN(0x10A0, 0x10FF, "Georgian"),
	HANGUL_JAMO(0x1100, 0x11FF, "Hangul Jamo"),
	ETHIOPOC(0x1200, 0x137F, "Ethiopic"),
	CHEROKEE(0x13A0, 0x13FF, "Cherokee"),
	UNFD_CNDN_ABRGNL_SYLL(0x1400, 0x167F, "Unified Canadian Aboriginal Syllabics"),
	OGHAM(0x1680, 0x169F, "Ogham"),
	RUNIC(0x16A0, 0x16FF, "Runic"),
	TAGALOG(0x1700, 0x171F, "Tagalog"),
	HANUNOO(0x1720, 0x173F, "Hanunoo"),
	BUHID(0x1740, 0x175F, "Buhid"),
	TADBANWA(0x1760, 0x177F, "Tagbanwa"),
	KHMER(0x1780, 0x17FF, "Khmer"),
	MONGOLIAN(0x1800, 0x18AF, "Mongolian"),

	/* 1900-1D7F new for 4.0 */
	LIMBU(0x1900, 0x194F, "Limbu"),
	TAI_LE(0x1950, 0x197F, "Tai Le"),
	KHMER_SYM(0x19E0, 0x19FF, "Khmer Symbols"),

	COMB_DIACRITICAL_EXT(0x1AB0, 0x1AFF, "Combining Diacritical Marks Extended"),	// 7.0.0

	BALINESE(0x1B00, 0x1B7F, "Balinese"),
	BATAK(0x1BC0, 0x1BFF, "Batak"),
	PHONETIC_EXT(0x1D00, 0x1D7F, "Phonetic Extensions"),

	LATIN_EXT_ADD(0x1E00, 0x1EFF, "Latin Extended Additional"),
	GREEK_EXT(0x1F00, 0x1FFF, "Greek Extended"),
	GENERAL_PUNCT(0x2000, 0x206F, "General Punctuation"),
	SUPER_AND_SUB(0x2070, 0x209F, "Superscripts and Subscripts"),
	CURRENCY_SYM(0x20A0, 0x20CF, "Currency Symbols"),
	COMB_DIACRITICAL_SYM(0x20D0, 0x20FF, "Combining Diacritical Marks for Symbols"),
	LETTERLIKE_SYM(0x2100, 0x214F, "Letterlike Symbols"),
	NUMBER_FORMS(0x2150, 0x218F, "Number Forms"),
	ARROWS(0x2190, 0x21FF, "Arrows"),
	MATHS_OPS(0x2200, 0x22FF, "Mathematical Operators"),
	MISC_TECH(0x2300, 0x23FF, "Miscellaneous Technical"),
	CONTROL_PICS(0x2400, 0x243F, "Control Pictures"),
	OCR(0x2440, 0x245F, "Optical Character Recognition"),
	ENCL_ALPHANUMS(0x2460, 0x24FF, "Enclosed Alphanumerics"),
	BOX_DRAWING(0x2500, 0x257F, "Box Drawing"),
	BLOCK_ELEMS(0x2580, 0x259F, "Block Elements"),
	GEOM_SHAPES(0x25A0, 0x25FF, "Geometric Shapes"),
	MISC_SHAPES(0x2600, 0x26FF, "Miscellaneous Symbols"),
	DINGBATS(0x2700, 0x27BF, "Dingbats"),
	MISC_MATH_SYMS_a(0x27C0, 0x27EF, "Miscellaneous Mathematical Symbols-A"),
	SUPP_ARROWS_A(0x27F0, 0x27FF, "Supplemental Arrows-A"),
	BRAILLE_PATTS(0x2800, 0x28FF, "Braille Patterns"),
	SUPP_ARROWS_B(0x2900, 0x297F, "Supplemental Arrows-B"),
	MISC_MATH_SYMS_B(0x2980, 0x29FF, "Miscellaneous Mathematical Symbols-B"),
	SUPP_MATHS_OPS(0x2A00, 0x2AFF, "Supplemental Mathematical Operators"),
	LATIN_EXT_C(0x2C60, 0x2C7F, "Latin Extended-C"),
	CJK_RADICALS_SUPP(0x2E80, 0x2EFF, "CJK Radicals Supplement"),
	KANGXI_RADICALS(0x2F00, 0x2FDF, "Kangxi Radicals"),
	IDEOGRAPHIC_DESC_CHARS(0x2FF0, 0x2FFF, "Ideographic Description Characters"),
	CJK_SYMS_PUNCT(0x3000, 0x303F, "CJK Symbols and Punctuation"),
	HIRAGANA(0x3040, 0x309F, "Hiragana"),
	KATAKANA(0x30A0, 0x30FF, "Katakana"),
	BOPOMOFO(0x3100, 0x312F, "Bopomofo"),
	HANGUL_COMPAT_JAMO(0x3130, 0x318F, "Hangul Compatibility Jamo"),
	KANBUN(0x3190, 0x319F, "Kanbun"),
	BOPOMOFO_EXT(0x31A0, 0x31BF, "Bopomofo Extended"),
	KATAKANA_PHONETIC_EXT(0x31F0, 0x31FF, "Katakana Phonetic Extensions"),
	ENCL_CJK_LETT_MONTHS(0x3200, 0x32FF, "Enclosed CJK Letters and Months"),
	CJK_COMPAT(0x3300, 0x33FF, "CJK Compatibility"),
	CJK_UNIFIED_IDEOGRAPHS_EXT_A(0x3400, 0x4DBF, "CJK Unified Ideographs Extension A"),

	/* 4DC0-4DFF new for 4.0 */
	YIJING_HEX_SYMS(0x4DC0, 0x4DFF, "Yijing Hexagram Symbols"),

	CJK_UNIFIED_IDEOGRAPHS(0x4E00, 0x9FFF, "CJK Unified Ideographs"),
	YI_SYLLABLES(0xA000, 0xA48F, "Yi Syllables"),
	YI_RADICALS(0xA490, 0xA4CF, "Yi Radicals"),
	LATIN_EXT_D(0xA720, 0xA7FF, "Latin Extended-D"),
	PHAGS_PA(0xA840, 0xA87F, "Phags-pa"),
	MYNAMAR_EXT_B(0xA9E0, 0xA9FF, "Myanmar Extended-B"),		// 7.0.0
	ETHIOPIC_EXT_A(0xAB00, 0xAB2F, "Ethiopic Extended-A"),
	LATIN_EXT_E(0xAB30, 0xAB6F, "Latin Extended-E"),			// 7.0.0
	HANGUL_SYM(0xAC00, 0xD7AF, "Hangul Syllables"),
	HIGH_SURROG(0xD800, 0xDB7F, "High Surrogates"),
	HIGH_PRIVATE_SURROG(0xDB80, 0xDBFF, "High Private Use Surrogates"),
	LOW_SURROG(0xDC00, 0xDFFF, "Low Surrogates"),
	PRIVATE_USE(0xE000, 0xF8FF, "Private Use Area"),
	CJK_COMPAT_IDEOGRAPH(0xF900, 0xFAFF, "CJK Compatibility Ideographs"),
	ALPHA_PRES_FORMS_A(0xFB00, 0xFB4F, "Alphabetic Presentation Forms"),
	ARABIC_PRES_FORMS_A(0xFB50, 0xFDFF, "Arabic Presentation Forms-A"),
	VAR_SELS(0xFE00, 0xFE0F, "Variation Selectors"),
	COMB_HALF_MARKS(0xFE20, 0xFE2F, "Combining Half Marks"),
	CJK_COMPAT_FORMS(0xFE30, 0xFE4F, "CJK Compatibility Forms"),
	SMALL_FORMS_VAR(0xFE50, 0xFE6F, "Small Form Variants"),
	ARABIC_PRES_FORMS_B(0xFE70, 0xFEFF, "Arabic Presentation Forms-B"),
	HALFWDTH_FULLWDTH_FORMS(0xFF00, 0xFFEF, "Halfwidth and Fullwidth Forms"),
	SPECIALS(0xFFF0, 0xFFFF, "Specials"),

	LINEAR_B_SYLLABARY(0x10000, 0x1007F, "Linear B Syllabary"),
	LINEAR_B_IDEOGRAMS(0x10080, 0x100FF, "Linear B Ideograms"),
	AGEAN_NUMS(0x10100, 0x1013F, "Aegean Numbers"),
	COPTIC_EPACT_NUMS(0x102E0, 0x102FF, "Coptic Epact Numbers"),			// 7.0.0

	OLD_ITALIC(0x10300, 0x1032F, "Old Italic"),
	GOTHIC(0x10330, 0x1034F, "Gothic"),
	OLD_PERMIC(0x10350, 0x1037F, "Old Permic"),						// 7.0.0

	UGARITIC(0x10380, 0x1039F, "Ugaritic"),

	DESERET(0x10400, 0x1044F, "Deseret"),

	SHAVIAN(0x10450, 0x1047F, "Shavian"),
	OSMANYA(0x10480, 0x104AF, "Osmanya"),
	ELBASAN(0x10500, 0x1052F, "Elbasan"),						// 7.0.0
	CAUCASIAN_ALBANIAN(0x10530, 0x1056F, "Caucasian Albanian"),				// 7.0.0
	LINEAR_A(0x10600, 0x1077F, "Linear A"),						// 7.0.0
	CYPRIOT_SYLLAB(0x10800, 0x1083F, "Cypriot Syllabary"),
	PALMYRENE(0x10860, 0x1087F, "Palmyrene"),						// 7.0.0
	NABATAEAN(0x10880, 0x108AF, "Nabataean"),						// 7.0.0
	PHOENICIAN(0x10900, 0x1091F, "Phoenician"),
	OLD_NORTH_ARABIAN(0x10A80, 0x10A9F, "Old North Arabian"),				// 7.0.0
	MANICHAEAN(0x10AC0, 0x10AFF, "Manichaean"),						// 7.0.0
	PSALTER_PAHLAVI(0x10B80, 0x10BAF, "Psalter Pahlavi"),				// 7.0.0
	BRHAMI(0x11000, 0x1107F, "Brahmi"),
	MAHAJANI(0x11150, 0x1117F, "Mahajani"),						// 7.0.0
	SINHALA_ARCHAIC_NUMS(0x111E0, 0x111FF, "Sinhala Archaic Numbers"),		// 7.0.0
	KHOJKI(0x11200, 0x1124F, "Khojki"),							// 7.0.0
	KHUDAWADI(0x112B0, 0x112FF, "Khudawadi"),						// 7.0.0
	GRANTHA(0x11300, 0x1137F, "Grantha"),						// 7.0.0
	TIRHUTA(0x11480, 0x114DF, "Tirhuta"),						// 7.0.0
	SIDDHAM(0x11580, 0x115FF, "Siddham"),						// 7.0.0
	MODI(0x11600, 0x1165F, "Modi"),							// 7.0.0
	WARANG_CITI(0x118A0, 0x118FF, "Warang Citi"),					// 7.0.0
	PAU_CIN_HAU(0x11AC0, 0x11AFF, "Pau Cin Hau"),					// 7.0.0
	CUNEIFORM(0x12000, 0x120FF, "Cuneiform"),
	BAMUM_SUPP(0x16800, 0x168BF, "Bamum Supplement"),
	MRO(0x16A40, 0x16A6F, "Mro"),							// 7.0.0
	BASSA_VAH(0x16AD0, 0x16AFF, "Bassa Vah"),						// 7.0.0
	PAHAWH_HMONG(0x16B00, 0x16B8F, "Pahawh Hmong"),					// 7.0.0
	KANA_SUPP(0x1B000, 0x1B0FF, "Kana Supplement"),
	DUPLOYAN(0x1BC00, 0x1BC9F, "Duployan"),						// 7.0.0
	SHORTHAND_FORMAT_CTRLS(0x1BCA0, 0x1BCAF, "Shorthand Format Controls"),		// 7.0.0

	BYZANT_MUSIC_SYMS(0x1D000, 0x1D0FF, "Byzantine Musical Symbols"),
	MUSIC_SYMS(0x1D100, 0x1D1FF, "Musical Symbols"),
	COUNT_ROD_NUMS(0x1D360, 0x1D37F, "Counting Rod Numerals"),
	MATHS_ALPAHNUM_SYMS(0x1D400, 0x1D7FF, "Mathematical Alphanumeric Symbols"),
	MENDE_KIKAKUI(0x1E800, 0x1E8DF, "Mende Kikakui"),					// 7.0.0
	PLAYING_CARDS(0x1F0A0, 0x1F0FF, "Playing Cards"),
	MISC_SUMS_PICTOGRPHS(0x1F300, 0x1F3FF, "Miscellaneous Symbols and Pictographs"),
	EMOTICONS(0x1F600, 0x1F64F, "Emoticons"),
	ORNAMENTAL_DINGBATS(0x1F650, 0x1F67F, "Ornamental Dingbats"),			// 7.0.0
	TRANSPORT_MAPS_SYMS(0x1F680, 0x1F6FF, "Transport and Map Symbols"),
	ALCHEMICAL_SYMS(0x1F700, 0x1F77F, "Alchemical Symbols"),
	GEOM_SHAPES_EXT(0x1F780, 0x1F7FF, "Geometric Shapes Extended"),		// 7.0.0
	SUPP_ARROWS_C(0x1F800, 0x1F8FF, "Supplemental Arrows-C"),			// 7.0.0
	CJK_UNIFIED_IDEOGRAPHS_EXT_B(0x20000, 0x2A6DF, "CJK Unified Ideographs Extension B"),
	CJK_UNIFIED_IDEOGRAPHS_EXT_D(0x2B740, 0x2B78F, "CJK Unified Ideographs Extension D"),
	CJK_COMPAT_IDEOGRAPH_EXT(0x2F800, 0x2FA1F, "CJK Compatibility Ideographs Supplement"),
	TAGS(0xE0000, 0xE007F, "Tags"),

	/* E0100-E01EF new for 4.0 */
	VAR_SELS_SUPP(0xE0100, 0xE01EF, "Variation Selectors Supplement"),

	SUPP_PRIV_USE_A(0xF0000, 0xFFFFF, "Supplementary Private Use Area-A"),
	SUPP_PRIV_USE_b(0x100000, 0x10FFFF, "Supplementary Private Use Area-B");

	/**  End code. */
	public final int end;
	/**  Block name. */
	public final String name;
	/**  Start code. */
	public final int start;

	Utf8Block(final int start, final int end, final String name) {
		this.start = start;
		this.end = end;
		this.name = name;
	}

	public static Utf8Block blockFromInt(final int code) {
		for (Utf8Block block : Utf8Block.values()) {
			if (block.start <= code && block.end >= code) {
				return block;
			}
		}
		return null;
	}
}
