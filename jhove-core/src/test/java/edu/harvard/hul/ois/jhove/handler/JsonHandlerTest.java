package edu.harvard.hul.ois.jhove.handler;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.harvard.hul.ois.jhove.AESAudioMetadata;
import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.App;
import edu.harvard.hul.ois.jhove.Checksum;
import edu.harvard.hul.ois.jhove.ChecksumType;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.JhoveException;
import edu.harvard.hul.ois.jhove.Module;
import edu.harvard.hul.ois.jhove.OutputHandler;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.Rational;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.TextMDMetadata;

@RunWith(JUnit4.class)
public class JsonHandlerTest {
	private static final Logger LOGGER = Logger.getLogger(JsonHandlerTest.class
			.getName());

	private static final String TIME_PATTERN = "\"[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}([+-][0-9]{2}:[0-9]{2})?\"";
	private static final String DATE_PATTERN = "\"date\":\"[^\"]+\"";
	private static final String DATE_REPLACEMENT = "\"date\":\"2010-01-01\"";
	private static final String RELEASE_PATTERN = "\"release\":\"[^\"]+\"";
	private static final String RELEASE_REPLACEMENT = "\"release\":\"DUMMY\"";
	private static final String DIR_PATTERN = "\"tempDirectory\":\"[^\"]+\"";
	private static final String DIR_REPLACEMENT = "\"tempDirectory\":\"DUMMY\"";
	private static final String CONF_PATTERN = "\"configuration\":\"[^\"]+\"";
	private static final String CONF_REPLACEMENT = "\"configuration\":\"DUMMY\"";
	private static final String RIGHTS_PATTERN = "\"rights\":\"[^\"]+\"";
	private static final String RIGHTS_REPLACEMENT = "\"rights\":\"DUMMY\"";
	private static final String VENDOR_PATTERN = "\"vendor\":\\{[^\\}]+\\}";
	private static final String VENDOR_REPLACEMENT = "\"vendor\":{\"kind\":\"Vendor\"}";
	private static final String DUMMY = "\"DUMMY\"";
	private static final String DUMMY_CK = "8747e564eb53cb2f1dcb9aae0779c2aa";
	private static final String BYTESTREAM = "BYTESTREAM";
	private static final String APP_JSON = 
			"\"name\":\"TEST\",\"release\":\"DUMMY\",\"date\":\"2010-01-01\",\"executionTime\":\"DUMMY\"";
	private static final String API_JSON = 
			"\"app\":{\"api\":{\"version\":\"1.0\",\"date\":\"2010-01-01\"}," +
			"\"configuration\":\"DUMMY\",\"jhoveHome\":\"TEST\",\"encoding\":\"utf-8\",\"tempDirectory\":\"DUMMY\"," +
		    "\"bufferSize\":131072,\"modules\":[{\"module\":\"BYTESTREAM\",\"release\":\"DUMMY\"}]," +
		    "\"outputHandlers\":[{\"outputHandler\":\"Audit\",\"release\":\"DUMMY\"}," +
		    "{\"outputHandler\":\"JSON\",\"release\":\"DUMMY\"},{\"outputHandler\":\"TEXT\",\"release\":\"DUMMY\"}," +
		    "{\"outputHandler\":\"XML\",\"release\":\"DUMMY\"}],\"usage\":\"usage\",\"rights\":\"DUMMY\"}";
	private static final String HANDLER_JSON = 
			"\"handler\":{\"name\":\"JSON\",\"release\":\"DUMMY\",\"date\":\"2010-01-01\"," +
			"\"vendor\":{\"kind\":\"Vendor\",\"name\":\"Bibliothèque nationale de France\",\"type\":\"Educational\"," +
			"\"web\":\"http://www.bnf.fr\"},\"note\":\"\"," +
			"\"rights\":\"DUMMY\"}";  
	private static final String MODULE_JSON = 
			"\"module\":{\"name\":\"BYTESTREAM\",\"release\":\"DUMMY\",\"date\":\"2010-01-01\"," +
			"\"formats\":[\"bytestream\"],\"mimeTypes\":[\"application/octet-stream\"]," +
			"\"features\":[\"edu.harvard.hul.ois.jhove.canValidate\",\"edu.harvard.hul.ois.jhove.canCharacterize\"]," +
			"\"methodology\":{\"wellFormed\":\"All bytestreams are well-formed\"}," +
			"\"vendor\":{\"kind\":\"Vendor\"}," +
			"\"note\":\"This is the default format\",\"rights\":\"DUMMY\"}";  
	private static final String INFO_JSON =
			"{\"uri\":\"file://dummy.file\"," +
		    "\"reportingModule\":{\"name\":\"BYTESTREAM\",\"release\":\"DUMMY\",\"date\":\"2010-01-01\"}," +
		    "\"size\":1,\"format\":\"bytestream\",\"status\":\"Well-Formed and valid\",\"sigMatch\":[\"BYTESTREAM\"]," +
		    "\"mimeType\":\"application/octet-stream\",\"properties\":[{\"checksum\":\"" +
		    DUMMY_CK + "\",\"type\":\"MD5\"}]}";
	/** Handler string "Find: " */
	private static final String FIND = "Find: ";

	private static App mockApp;
	private static JhoveBase je;

	private File outputFile;
	private StringWriter outString;
	private PrintWriter writer;
	private JsonHandler handler;

	@BeforeClass
	public static void setUpBeforeClass() throws JhoveException {
		mockApp = new App("TEST", "1.0", new int[]{2019,10,28}, "usage", "rights");
		je = new JhoveBase();
		je.setLogLevel("INFO");
		String fileConf = JsonHandlerTest.class.getResource("/jhove_test.conf").getPath();
		LOGGER.info("jhove.conf in:[" + fileConf + "]");
		je.init(fileConf, null);
	}
	
	@Before
	public void setUp() throws IOException {
		// Prepare for a new test
		this.outputFile = File.createTempFile("jhove_", ".json");
		
		outString = new StringWriter();
	    writer = new PrintWriter(outString);
		
		this.handler = new JsonHandler();
		this.handler.setApp(mockApp);
		this.handler.setBase(je);
		this.handler.setWriter(writer);
	}

	@After
	public void tearDown() {
		if (this.outputFile != null && this.outputFile.exists()) {
			this.outputFile.delete();
		}
	}

	public void buildJson(JsonObjectBuilder builder) {
        JsonObject jsonObject = builder.build();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeObject(jsonObject);
	}

	public void buildJson(JsonArrayBuilder builder) {
		JsonObjectBuilder job = Json.createObjectBuilder().add("ARRAY", builder);
        JsonObject jsonObject = job.build();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeObject(jsonObject);
	}

	@Test
	public void testShow() {
        handler.showHeader();
        handler.show();
        handler.showFooter();
        handler.close();
		
		String result = outString.toString().replaceAll(TIME_PATTERN, DUMMY)
				.replaceAll(DATE_PATTERN, DATE_REPLACEMENT)
				.replaceAll(RELEASE_PATTERN, RELEASE_REPLACEMENT);
	    assertEquals(
	    		"{\"jhove\":{" + APP_JSON + "}}", result);
    
	}
	
	@Test
	public void testShowApp() {
        handler.showHeader();
       	handler.show(mockApp);
        handler.showFooter();
        handler.close();
        
		String result = outString.toString().replaceAll(TIME_PATTERN, DUMMY)
				.replaceAll(DATE_PATTERN, DATE_REPLACEMENT)
				.replaceAll(RELEASE_PATTERN, RELEASE_REPLACEMENT)
				.replaceAll(CONF_PATTERN, CONF_REPLACEMENT)
				.replaceAll(RIGHTS_PATTERN, RIGHTS_REPLACEMENT)
				.replaceAll(DIR_PATTERN, DIR_REPLACEMENT);
		LOGGER.info(FIND + result);
		String expected = "{\"jhove\":{" + APP_JSON + "," + API_JSON + "}}";

	    assertEquals(expected, result);
	}
	
	@Test
	public void testShowOutputHandler() {
		OutputHandler jsonHandler = je.getHandler("JSON");
		
        handler.showHeader();
       	handler.show(jsonHandler);
        handler.showFooter();
        handler.close();
        
		String result = outString.toString().replaceAll(TIME_PATTERN, DUMMY)
				.replaceAll(DATE_PATTERN, DATE_REPLACEMENT)
				.replaceAll(RELEASE_PATTERN, RELEASE_REPLACEMENT)
				.replaceAll(CONF_PATTERN, CONF_REPLACEMENT)
				.replaceAll(RIGHTS_PATTERN, RIGHTS_REPLACEMENT)
				.replaceAll(DIR_PATTERN, DIR_REPLACEMENT);
		LOGGER.info(FIND + result);
		String expected = "{\"jhove\":{" + APP_JSON + "," + HANDLER_JSON + "}}";
		 
	    assertEquals(expected, result);
	}
	
	@Test
	public void testShowModule() {
		Module module = je.getModule(BYTESTREAM);
		
        handler.showHeader();
       	handler.show(module);
        handler.showFooter();
        handler.close();
        
		String result = outString.toString().replaceAll(TIME_PATTERN, DUMMY)
				.replaceAll(DATE_PATTERN, DATE_REPLACEMENT)
				.replaceAll(RELEASE_PATTERN, RELEASE_REPLACEMENT)
				.replaceAll(CONF_PATTERN, CONF_REPLACEMENT)
				.replaceAll(RIGHTS_PATTERN, RIGHTS_REPLACEMENT)
				.replaceAll(DIR_PATTERN, DIR_REPLACEMENT)
				.replaceAll(VENDOR_PATTERN, VENDOR_REPLACEMENT);
		LOGGER.info(FIND + result);
		String expected = "{\"jhove\":{" + APP_JSON + "," + MODULE_JSON + "}}";
		 
	    assertEquals(expected, result);
	}

	@Test
	public void testShowRepInfo() {
		Module module = je.getModule(BYTESTREAM);
		RepInfo info = new RepInfo("file://dummy.file");
		info.setModule(module);
		info.setFormat(module.getFormat()[0]);
		info.setMimeType(module.getMimeType()[0]);
		info.setSigMatch(module.getName());
		info.setChecksum(new Checksum(DUMMY_CK, ChecksumType.MD5));
		info.setSize(1);
		
        handler.showHeader();
       	handler.show(info);
        handler.showFooter();
        handler.close();
        
		String result = outString.toString().replaceAll(TIME_PATTERN, DUMMY)
				.replaceAll(DATE_PATTERN, DATE_REPLACEMENT)
				.replaceAll(RELEASE_PATTERN, RELEASE_REPLACEMENT)
				.replaceAll(CONF_PATTERN, CONF_REPLACEMENT)
				.replaceAll(RIGHTS_PATTERN, RIGHTS_REPLACEMENT)
				.replaceAll(DIR_PATTERN, DIR_REPLACEMENT)
				.replaceAll(VENDOR_PATTERN, VENDOR_REPLACEMENT);
		LOGGER.info(FIND + result);
		String expected = "{\"jhove\":{" + APP_JSON + ",\"repInfo\":[" + INFO_JSON + "]}}";
		 
	    assertEquals(expected, result);
	}

	@Test
	public void testShowRepInfos() {
		Module module = je.getModule(BYTESTREAM);
		RepInfo info = new RepInfo("file://dummy.file");
		info.setModule(module);
		info.setFormat(module.getFormat()[0]);
		info.setMimeType(module.getMimeType()[0]);
		info.setSigMatch(module.getName());
		info.setChecksum(new Checksum(DUMMY_CK, ChecksumType.MD5));
		info.setSize(1);
		
        handler.showHeader();
       	handler.show(info);
       	handler.show(info);
        handler.showFooter();
        handler.close();
        
		String result = outString.toString().replaceAll(TIME_PATTERN, DUMMY)
				.replaceAll(DATE_PATTERN, DATE_REPLACEMENT)
				.replaceAll(RELEASE_PATTERN, RELEASE_REPLACEMENT)
				.replaceAll(CONF_PATTERN, CONF_REPLACEMENT)
				.replaceAll(RIGHTS_PATTERN, RIGHTS_REPLACEMENT)
				.replaceAll(DIR_PATTERN, DIR_REPLACEMENT)
				.replaceAll(VENDOR_PATTERN, VENDOR_REPLACEMENT);
		LOGGER.info(FIND + result);
		String expected = "{\"jhove\":{" + APP_JSON + ",\"repInfo\":[" + INFO_JSON + "," + INFO_JSON + "]}}";
		 
	    assertEquals(expected, result);
	}

	@Test
	public void testShowVendor() {
		OutputHandler jsonHandler = je.getHandler("JSON");
		Agent v = jsonHandler.getVendor(); 
		
        JsonObjectBuilder json = handler.showAgent(v, "OTHER");
        buildJson(json);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"kind\":\"OTHER\",\"name\":\"Bibliothèque nationale de France\"," +
				"\"type\":\"Educational\",\"web\":\"http://www.bnf.fr\"}";
		
	    assertEquals(expected, result);
	}

	@Test
	public void testShowScalarProperty() throws IOException {
		final Property prop = new Property("test", PropertyType.INTEGER, PropertyArity.SCALAR, 2);
		JsonObjectBuilder b = this.handler.showScalarProperty(prop);
		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"test\":2}";
		
	    assertEquals(expected, result);
	}
	
	@Test
	public void testShowListProperty() throws IOException {
		final List<Double> testList = Arrays.asList(new Double[]{1.0, 2.0});
		Property prop = new Property("test", PropertyType.DOUBLE, PropertyArity.LIST, testList);
		JsonObjectBuilder b = this.handler.showListProperty(prop);
		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"test\":[1.0,2.0]}";
		
	    assertEquals(expected, result);
	}

	@Test
	public void testShowSetProperty() throws IOException {
		// use a LinkedHashSet to be sure of the output order...
		final Set<Rational> testSet = new LinkedHashSet<>(
				Arrays.asList(new Rational[]{new Rational(300, 1), new Rational(4, 2)}));
		Property prop = new Property("test", PropertyType.RATIONAL, PropertyArity.SET, testSet);
		JsonObjectBuilder b = this.handler.showSetProperty(prop);
		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"test\":[[300,1],[4,2]]}";
		
	    assertEquals(expected, result);
	}

	@Test
	public void testShowMapProperty() throws IOException {
		final Map<String,String> testMap = Collections.singletonMap("mykey", "myvalue");
		Property prop = new Property("test", PropertyType.STRING, PropertyArity.MAP, testMap);
		JsonObjectBuilder b = this.handler.showMapProperty(prop);
		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"test\":{\"mykey\":\"myvalue\"}}";
		
	    assertEquals(expected, result);
	}

	@Test
	public void testShowArrayMapProperty() throws IOException {
		final boolean[] testArray = new boolean[]{true, false, true};
		Property prop = new Property("test", PropertyType.BOOLEAN, PropertyArity.ARRAY, testArray);
		JsonObjectBuilder b = this.handler.showArrayProperty(prop);
		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"test\":[true,false,true]}";
		
	    assertEquals(expected, result);
	}

	@Test
	public void testShowTextMD() throws IOException {
		final TextMDMetadata textMD = new TextMDMetadata();
		textMD.setCharset("UTF-8");
		textMD.setLanguage("fr");
	
		JsonObjectBuilder b = this.handler.showTextMDMetadata(textMD);
		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"textmd:charset\":\"UTF-8\",\"textmd:byte_order\":\"big\"," +
				"\"textmd:linebreak\":\"CR/LF\",\"textmd:language\":\"fre\"}";

	    assertEquals(expected, result);
	}

	@Test
	public void testShowAESAudioMetadata() throws IOException {
		final AESAudioMetadata aes = new AESAudioMetadata();
		aes.setFormat("audio/wav");
	
		JsonObjectBuilder b = this.handler.showAESAudioMetadata(aes);
		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"aes:schemaVersion\":\"1.02b\",\"aes:format\":\"audio/wav\"," +
				"\"aes:face\":{\"aes:timeline\":{\"tcf:startTime\":{\"tcf:frameCount\":30,\"tcf:timeBase\":1000," +
				"\"tcf:videoField\":\"FIELD_1\",\"tcf:countingMode\":\"NTSC_NON_DROP_FRAME\",\"tcf:hours\":0," +
				"\"tcf:minutes\":0,\"tcf:seconds\":0,\"tcf:frames\":0," +
				"\"tcf:samples\":{\"tcf:sampleRate\":\"S44100\",\"tcf:numberOfSamples\":0}," +
				"\"tcf:filmFraming\":{\"tcf:framing\":\"NOT_APPLICABLE\",\"tcf:framingType\":\"tcf:ntscFilmFramingType\"}}}," +
				"\"aes:streams\":[]}}";

	    assertEquals(expected, result);
	}

	@Test
	public void testShowArrayInt() throws IOException {
		final int[] iArrayTest = { 1, 2, 3 };
		JsonArrayBuilder b = this.handler.showArray(iArrayTest);

		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"ARRAY\":[1,2,3]}";
		
	    assertEquals(expected, result);
	}
	
	@Test
	public void testShowArrayDouble() throws IOException {
		final double[] dArrayTest = { -1.0, 0, 1.0 };
		JsonArrayBuilder b = this.handler.showArray(dArrayTest);

		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"ARRAY\":[-1.0,0.0,1.0]}";
		
	    assertEquals(expected, result);
	}

	@Test
	public void testShowArrayString() throws IOException {
		final String[] sArrayTest = { null, "", "DUMMY" };
		JsonArrayBuilder b = this.handler.showArray(sArrayTest);

		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"ARRAY\":[null,\"\",\"DUMMY\"]}";
		
	    assertEquals(expected, result);
	}

	
	@Test
	public void testShowArrayRational() throws IOException {
		final Rational[] rArrayTest = { new Rational(1L,1L), new Rational(-1, 2) };
		JsonArrayBuilder b = this.handler.showArray(rArrayTest);

		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"ARRAY\":[[1,1],[-1,2]]}";
		
	    assertEquals(expected, result);
	}

	@Test
	public void testShowRational() throws IOException {
		final Rational r = new Rational(123456,43211);
		JsonArrayBuilder b = this.handler.showRational(r);

		buildJson(b);
        handler.close();
        
		String result = outString.toString();
		LOGGER.info(FIND + result);
		final String expected = "{\"ARRAY\":[123456,43211]}";
		
	    assertEquals(expected, result);
	}
}
