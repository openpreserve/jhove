/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 **********************************************************************/

package edu.harvard.hul.ois.jhove.handler;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import edu.harvard.hul.ois.jhove.AESAudioMetadata;
import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.App;
import edu.harvard.hul.ois.jhove.Checksum;
import edu.harvard.hul.ois.jhove.Document;
import edu.harvard.hul.ois.jhove.HandlerBase;
import edu.harvard.hul.ois.jhove.Identifier;
import edu.harvard.hul.ois.jhove.InternalSignature;
import edu.harvard.hul.ois.jhove.Message;
import edu.harvard.hul.ois.jhove.Module;
import edu.harvard.hul.ois.jhove.NisoImageMetadata;
import edu.harvard.hul.ois.jhove.OutputHandler;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.Rational;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.Signature;
import edu.harvard.hul.ois.jhove.SignatureType;
import edu.harvard.hul.ois.jhove.TextMDMetadata;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * OutputHandler for JSON output.
 *
 */
public class JsonHandler extends HandlerBase {
	/******************************************************************
	 * PRIVATE CLASS FIELDS.
	 ******************************************************************/

	/** Handler name. */
	private static final String NAME = "JSON";

	/** Handler release identifier. */
	private static final String RELEASE = "1.0";

	/** Handler release date. */
	private static final int[] DATE = { 2019, 10, 18 };

	/** Handler informative note. */
	private static final String NOTE = "";

	/** Handler rights statement. */
	private static final String RIGHTS = "Version 1.0 release by Open Preservation Foundation. "
			+ "Released under the GNU Lesser General Public License.";

	/******************************************************************
	 * PRIVATE INSTANCE FIELDS.
	 ******************************************************************/

	/** Main JSON builder */
	private JsonObjectBuilder jhoveBuilder;

	/* Sample rate. */
	private double _sampleRate;

	/******************************************************************
	 * CLASS CONSTRUCTOR.
	 ******************************************************************/

	/**
	 * Creates an JsonHandler.
	 */
	public JsonHandler() {
		super(NAME, RELEASE, DATE, NOTE, RIGHTS);
		_vendor = Agent.bnfInstance();
	}

	/** Constructor for use by subclasses. */
	public JsonHandler(String name, String release, int[] date, String note, String rights) {
		super(name, release, date, note, rights);
		_vendor = Agent.bnfInstance();
	}

	/******************************************************************
	 * PUBLIC INSTANCE METHODS.
	 ******************************************************************/

	/**
	 * Outputs minimal information about the application
	 */
	@Override
	public void show() {
		_level--;
	}

	/**
	 * Outputs detailed information about the application, including configuration,
	 * available modules and handlers, etc.
	 */
	@Override
	public void show(App app) {
		JsonObjectBuilder appBuilder = Json.createObjectBuilder();

		JsonObjectBuilder apiBuilder = Json.createObjectBuilder();
		apiBuilder.add("version", app.getRelease());
		apiBuilder.add("date", date.format(_je.getDate()));
		appBuilder.add("api", apiBuilder);

		String configFile = _je.getConfigFile();
		if (configFile != null) {
			appBuilder.add("configuration", configFile);
		}
		String s = _je.getSaxClass();
		if (s != null) {
			appBuilder.add("saxParser", s);
		}
		s = _je.getJhoveHome();
		if (s != null) {
			appBuilder.add("jhoveHome", s);
		}
		s = _je.getEncoding();
		if (s != null) {
			appBuilder.add("encoding", s);
		}
		s = _je.getTempDirectory();
		if (s != null) {
			appBuilder.add("tempDirectory", s);
		}
		appBuilder.add("bufferSize", _je.getBufferSize());

		JsonArrayBuilder modulesBuilder = Json.createArrayBuilder();
		for (String modKey : _je.getModuleMap().keySet()) {
			Module module = _je.getModule(modKey);
			modulesBuilder.add(
					Json.createObjectBuilder().add("module", module.getName()).add("release", module.getRelease()));

		}
		appBuilder.add("modules", modulesBuilder);

		JsonArrayBuilder oHandlersBuilder = Json.createArrayBuilder();
		for (String handlerKey : _je.getHandlerMap().keySet()) {
			OutputHandler handler = _je.getHandler(handlerKey);
			oHandlersBuilder.add(Json.createObjectBuilder().add("outputHandler", handler.getName()).add("release",
					handler.getRelease()));
		}
		appBuilder.add("outputHandlers", oHandlersBuilder);

		appBuilder.add("usage", app.getUsage());
		appBuilder.add("rights", app.getRights());

		jhoveBuilder.add("app", appBuilder);
	}

	/**
	 * Outputs information about the OutputHandler specified in the parameter
	 */
	@Override
	public void show(OutputHandler handler) {
		JsonObjectBuilder outputHandlerBuilder = Json.createObjectBuilder();
		outputHandlerBuilder.add("name", handler.getName());
		outputHandlerBuilder.add("release", handler.getRelease());
		outputHandlerBuilder.add("date", date.format(handler.getDate()));
		List<Document> list = handler.getSpecification();
		int n = list.size();
		if (n > 0) {
			JsonArrayBuilder specBuilder = Json.createArrayBuilder();
			for (int i = 0; i < n; i++) {
				specBuilder.add(showDocument(list.get(i)));
			}
			outputHandlerBuilder.add("specifications", specBuilder);
		}
		Agent vendor = handler.getVendor();
		if (vendor != null) {
			outputHandlerBuilder.add("vendor", showAgent(vendor, "Vendor"));
		}
		String s;
		if ((s = handler.getNote()) != null) {
			outputHandlerBuilder.add("note", s);
		}
		if ((s = handler.getRights()) != null) {
			outputHandlerBuilder.add("rights", s);
		}

		jhoveBuilder.add("handler", outputHandlerBuilder);
	}

	/**
	 * Outputs information about a Module
	 */
	@Override
	public void show(Module module) {
		JsonObjectBuilder modBuilder = Json.createObjectBuilder();
		modBuilder.add("name", module.getName());
		modBuilder.add("release", module.getRelease());
		modBuilder.add("date", HandlerBase.date.format(module.getDate()));

		String[] ss = module.getFormat();
		if (ss.length > 0) {
			modBuilder.add("formats", showArray(ss));
		}
		String s = module.getCoverage();
		if (s != null) {
			modBuilder.add("coverage", s);
		}
		ss = module.getMimeType();
		if (ss.length > 0) {
			modBuilder.add("mimeTypes", showArray(ss));
		}
		List<Signature> list1 = module.getSignature();
		if (list1 != null && !list1.isEmpty()) {
			JsonArrayBuilder sigBuilder = Json.createArrayBuilder();
			for (Signature sig : list1) {
				sigBuilder.add(showSignature(sig));
			}
			modBuilder.add("signatures", sigBuilder);
		}
		List<Document> list2 = module.getSpecification();
		if (list2 != null && !list2.isEmpty()) {
			JsonArrayBuilder docBuilder = Json.createArrayBuilder();
			for (Document doc : list2) {
				docBuilder.add(showDocument(doc));
			}
			modBuilder.add("specifications", docBuilder);
		}
		List<String> ftr = module.getFeatures();
		if (ftr != null && !ftr.isEmpty()) {
			JsonArrayBuilder featuresBuilder = Json.createArrayBuilder();
			for (String f : ftr) {
				featuresBuilder.add(f);
			}
			modBuilder.add("features", featuresBuilder);
		}

		JsonObjectBuilder methodBuilder = Json.createObjectBuilder();
		if ((s = module.getWellFormedNote()) != null) {
			methodBuilder.add("wellFormed", s);
		}
		if ((s = module.getValidityNote()) != null) {
			methodBuilder.add("validity", s);
		}
		if ((s = module.getRepInfoNote()) != null) {
			methodBuilder.add("repInfo", s);
		}
		modBuilder.add("methodology", methodBuilder);

		Agent vendor = module.getVendor();
		if (vendor != null) {
			modBuilder.add("vendor", showAgent(vendor, "Vendor"));
		}
		if ((s = module.getNote()) != null) {
			modBuilder.add("note", s);
		}
		if ((s = module.getRights()) != null) {
			modBuilder.add("rights", s);
		}

		jhoveBuilder.add("module", modBuilder);
	}

	/**
	 * Outputs the information contained in a RepInfo object
	 */
	@Override
	public void show(RepInfo info) {
		JsonObjectBuilder infoBuilder = Json.createObjectBuilder();

		Module module = info.getModule();
		_logger.info("Reporting RepInfo");
		if (_je.getSignatureFlag()) {
			_logger.info("Checking signatures only");
		}
		infoBuilder.add("uri", info.getUri());

		if (module != null) {
			infoBuilder.add("reportingModule", Json.createObjectBuilder().add("name", module.getName())
					.add("release", module.getRelease()).add("date", date.format(module.getDate())));
		}
		Date date = info.getCreated();
		if (date != null) {
			infoBuilder.add("created", toDateTime(date));
		}
		date = info.getLastModified();
		if (date != null) {
			infoBuilder.add("lastModified", toDateTime(date));
		}
		long size = info.getSize();
		if (size > -1) {
			infoBuilder.add("size", size);
		}
		String s = info.getFormat();
		if (s != null) {
			infoBuilder.add("format", s);
		}
		if ((s = info.getVersion()) != null) {
			infoBuilder.add("version", s);
		}
		String wfStr;
		if (!_je.getSignatureFlag()) {
			switch (info.getWellFormed()) {
			case RepInfo.TRUE:
				wfStr = "Well-Formed";
				break;

			case RepInfo.FALSE:
				wfStr = "Not well-formed";
				break;

			default:
				wfStr = "Unknown";
				break;
			}
			// If it's well-formed, append validity information
			if (info.getWellFormed() == RepInfo.TRUE) {
				switch (info.getValid()) {
				case RepInfo.TRUE:
					wfStr += " and valid";
					break;

				case RepInfo.FALSE:
					wfStr += ", but not valid";
					break;

				default:
					// case UNDETERMINED: add nothing
					break;
				}
			}
			_logger.info("Validity/WF status: " + wfStr);
			infoBuilder.add("status", wfStr);
		} else {
			// If we aren't checking signatures, we still need to say something.
			switch (info.getWellFormed()) {
			case RepInfo.TRUE:
				wfStr = "Well-Formed";
				break;

			default:
				wfStr = "Not well-formed";
				break;
			}
			infoBuilder.add("status", wfStr);
		}

		List<String> list1 = info.getSigMatch();
		if (list1 != null && !list1.isEmpty()) {
			JsonArrayBuilder sigBuilder = Json.createArrayBuilder();
			for (String sigm : list1) {
				sigBuilder.add(sigm);
			}
			infoBuilder.add("sigMatch", sigBuilder);
		}

		List<Message> list2 = info.getMessage();
		if (list2 != null && !list2.isEmpty()) {
			JsonArrayBuilder msgBuilder = Json.createArrayBuilder();
			for (Message msg : list2) {
				msgBuilder.add(showMessage(msg));
			}
			infoBuilder.add("messages", msgBuilder);
		}

		s = info.getMimeType();
		if (s != null) {
			infoBuilder.add("mimeType", s);
		}

		List<String> list3 = info.getProfile();
		if (list3 != null && !list3.isEmpty()) {
			JsonArrayBuilder profBuilder = Json.createArrayBuilder();
			for (String prof : list3) {
				profBuilder.add(prof);
			}
			infoBuilder.add("profiles", profBuilder);
		}

		Map<String, Property> map = info.getProperty();
		if (map != null && !map.isEmpty()) {
			JsonArrayBuilder propBuilder = Json.createArrayBuilder();
			for (String key : map.keySet()) {
				Property property = info.getProperty(key);
				propBuilder.add(showProperty(property));
			}
			infoBuilder.add("properties", propBuilder);
		}

		List<Checksum> list4 = info.getChecksum();
		if (list4 != null && !list4.isEmpty()) {
			JsonArrayBuilder checksumBuilder = Json.createArrayBuilder();
			for (Checksum ck : list4) {
				checksumBuilder.add(showChecksum(ck));
			}
			infoBuilder.add("properties", checksumBuilder);
		}

		if ((s = info.getNote()) != null) {
			infoBuilder.add("note", s);
		}

		jhoveBuilder.add("repInfo", infoBuilder);
	}

	/******************************************************************
	 * PRIVATE INSTANCE METHODS.
	 ******************************************************************/
	protected JsonObjectBuilder showAgent(Agent agent, String label) {
		JsonObjectBuilder agentBuilder = Json.createObjectBuilder();
		agentBuilder.add("kind", label);
		agentBuilder.add("name", agent.getName());
		agentBuilder.add("type", agent.getType().toString());
		String s = agent.getAddress();
		if (s != null) {
			agentBuilder.add("address", s);
		}
		if ((s = agent.getTelephone()) != null) {
			agentBuilder.add("telephone", s);
		}
		if ((s = agent.getFax()) != null) {
			agentBuilder.add("fax", s);
		}
		if ((s = agent.getEmail()) != null) {
			agentBuilder.add("email", s);
		}
		if ((s = agent.getWeb()) != null) {
			agentBuilder.add("web", s);
		}
		return agentBuilder;
	}

	protected JsonObjectBuilder showChecksum(Checksum checksum) {
		return Json.createObjectBuilder().add("checksum", checksum.getValue()).add("type",
				checksum.getType().toString());
	}

	protected JsonObjectBuilder showDocument(Document document) {
		JsonObjectBuilder docBuilder = Json.createObjectBuilder();
		docBuilder.add("title", document.getTitle());
		docBuilder.add("type", document.getType().toString());
		List<Agent> list1 = document.getAuthor();
		if (list1 != null && !list1.isEmpty()) {
			JsonArrayBuilder autBuilder = Json.createArrayBuilder();
			for (Agent ag : list1) {
				autBuilder.add(showAgent(ag, "Author"));
			}
			docBuilder.add("authors", autBuilder);
		}
		List<Agent> list2 = document.getPublisher();
		if (list2 != null && !list2.isEmpty()) {
			JsonArrayBuilder pubBuilder = Json.createArrayBuilder();
			for (Agent ag : list2) {
				pubBuilder.add(showAgent(ag, "Publisher"));
			}
			docBuilder.add("publishers", pubBuilder);
		}
		String s = document.getEdition();
		if (s != null) {
			docBuilder.add("edition", s);
		}
		if ((s = document.getDate()) != null) {
			docBuilder.add("date", s);
		}
		if ((s = document.getEnumeration()) != null) {
			docBuilder.add("enumeration", s);
		}
		if ((s = document.getPages()) != null) {
			docBuilder.add("pages", s);
		}
		List<Identifier> list3 = document.getIdentifier();
		if (list3 != null && !list3.isEmpty()) {
			JsonArrayBuilder idBuilder = Json.createArrayBuilder();
			for (Identifier id : list3) {
				idBuilder.add(showIdentifier(id));
			}
			docBuilder.add("identifiers", idBuilder);
		}
		if ((s = document.getNote()) != null) {
			docBuilder.add("note", s);
		}
		return docBuilder;
	}

	/**
	 * Do the final output. This should be in a suitable format for including
	 * multiple files between the header and the footer, and the XML of the header
	 * and footer must balance out.
	 */
	@Override
	public void showFooter() {
		JsonObjectBuilder mainBuilder = Json.createObjectBuilder();
		mainBuilder.add("jhove", jhoveBuilder);
		JsonObject jsonObject = mainBuilder.build();

		JsonWriter jsonWriter = Json.createWriter(_writer);
		jsonWriter.writeObject(jsonObject);
		jsonWriter.close();
	}

	/**
	 * Do the initial output. This should be in a suitable format for including
	 * multiple files between the header and the footer, and the XML of the header
	 * and footer must balance out.
	 */
	@Override
	public void showHeader() {
		jhoveBuilder = Json.createObjectBuilder();
		jhoveBuilder.add("name", _app.getName());
		jhoveBuilder.add("release", _app.getRelease());
		jhoveBuilder.add("date", HandlerBase.date.format(_app.getDate()));
		jhoveBuilder.add("executionTime", toDateTime(new Date()));
	}

	protected JsonObjectBuilder showIdentifier(Identifier identifier) {
		JsonObjectBuilder idBuilder = Json.createObjectBuilder();
		idBuilder.add("value", identifier.getValue());
		idBuilder.add("type", identifier.getType().toString());
		String note = identifier.getNote();
		if (note != null) {
			idBuilder.add("note", note);
		}
		return idBuilder;
	}

	protected JsonObjectBuilder showMessage(Message message) {
		JsonObjectBuilder msgBuilder = Json.createObjectBuilder();

		msgBuilder.add("message", message.getMessage());
		String submsg = message.getSubMessage();
		if (submsg != null) {
			msgBuilder.add("subMessage", submsg);
		}
		long offset = message.getOffset();
		if (offset > -1) {
			msgBuilder.add("offset", offset);
		}
		if (!message.getPrefix().isEmpty()) {
			msgBuilder.add("severity", message.getPrefix().toLowerCase());
		}
		String id = message.getJhoveMessage().getId();
		if (!(id == null || id.isEmpty() || JhoveMessages.NO_ID.equals(id))) {
			msgBuilder.add("id", message.getId());
		}
		return msgBuilder;
	}

	protected JsonObjectBuilder showSignature(Signature signature) {
		JsonObjectBuilder sigBuilder = Json.createObjectBuilder();

		String sigValue;
		if (signature.isStringValue()) {
			sigValue = signature.getValueString();
		} else {
			sigValue = signature.getValueHexString();
		}
		sigBuilder.add("type", signature.getType().toString());
		sigBuilder.add("value", sigValue);
		if (SignatureType.MAGIC.equals(signature.getType()) && ((InternalSignature) signature).hasFixedOffset()) {
			sigBuilder.add("offset", "0x" + Integer.toHexString(((InternalSignature) signature).getOffset()));
		}
		String note = signature.getNote();
		if (note != null) {
			sigBuilder.add("note", note);

		}
		String use = signature.getUse().toString();
		if (use != null) {
			sigBuilder.add("use", use);
		}
		return sigBuilder;
	}

	/* Do special conversions on values as needed. */
	protected String valueToString(Object obj) {
		if (obj instanceof Date) {
			return toDateTime((Date) obj);
		}
		return obj.toString();
	}

	protected JsonObjectBuilder showProperty(Property property) {
		JsonObjectBuilder propBuilder = Json.createObjectBuilder();

		PropertyArity arity = property.getArity();
		switch (arity) {
		case SCALAR:
			return showScalarProperty(property);
		case ARRAY:
			return showArrayProperty(property);
		case LIST:
			return showListProperty(property);
		case MAP:
			return showMapProperty(property);
		case SET:
			return showSetProperty(property);
		default:
			return propBuilder;
		}
	}

	protected JsonObjectBuilder showScalarProperty(Property property) {
		JsonObjectBuilder propBuilder = Json.createObjectBuilder();

		PropertyType propType = property.getType();
		switch (propType) {
		case BOOLEAN:
			Boolean b = (Boolean) property.getValue();
			propBuilder.add(property.getName(), b.booleanValue());
			break;
		case BYTE:
		case CHARACTER:
		case OBJECT:
			propBuilder.add(property.getName(), property.getValue().toString());
			break;
		case DATE:
			Date dt = (Date) property.getValue();
			propBuilder.add(property.getName(), toDateTime(dt));
			break;
		case DOUBLE:
			Double d = (Double) property.getValue();
			propBuilder.add(property.getName(), d.doubleValue());
			break;
		case FLOAT:
			Float f = (Float) property.getValue();
			propBuilder.add(property.getName(), f.floatValue());
			break;
		case INTEGER:
			Integer i = (Integer) property.getValue();
			propBuilder.add(property.getName(), i.intValue());
			break;
		case LONG:
			Long l = (Long) property.getValue();
			propBuilder.add(property.getName(), l.longValue());
			break;
		case AESAUDIOMETADATA:
			propBuilder.add(property.getName(), showAESAudioMetadata((AESAudioMetadata) property.getValue()));
			break;
		case NISOIMAGEMETADATA:
			propBuilder.add(property.getName(), showNisoImageMetadata((NisoImageMetadata) property.getValue()));
			break;
		case TEXTMDMETADATA:
			propBuilder.add(property.getName(), showTextMDMetadata((TextMDMetadata) property.getValue()));
			break;
		case SHORT:
			Short s = (Short) property.getValue();
			propBuilder.add(property.getName(), s.shortValue());
			break;
		case STRING:
			propBuilder.add(property.getName(), (String) property.getValue());
			break;
		case RATIONAL:
			propBuilder.add(property.getName(), showRational((Rational) property.getValue()));
			break;
		case PROPERTY:
			Property property2 = (Property) property.getValue();
			propBuilder.add(property.getName(), showProperty(property2));
			break;
		default:
			propBuilder.add(property.getName(), property.getValue().toString());
			break;
		}
		return propBuilder;
	}

	protected JsonObjectBuilder showListProperty(Property property) {
		JsonObjectBuilder propBuilder = Json.createObjectBuilder();
		List<?> propList = (List<?>) property.getValue();
		JsonArrayBuilder lPropBuilder = Json.createArrayBuilder();
		PropertyType type = property.getType();

		ListIterator<?> iter = propList.listIterator();
		while (iter.hasNext()) {
			Object val = iter.next();
			switch (type) {
			case BOOLEAN:
				lPropBuilder.add(((Boolean) val).booleanValue());
				break;
			case BYTE:
				lPropBuilder.add(valueToString(val));
				break;
			case CHARACTER:
				lPropBuilder.add(valueToString(val));
				break;
			case DATE:
				lPropBuilder.add(valueToString(val));
				break;
			case DOUBLE:
				lPropBuilder.add(((Double) val).doubleValue());
				break;
			case FLOAT:
				lPropBuilder.add(((Float) val).floatValue());
				break;
			case INTEGER:
				lPropBuilder.add(((Integer) val).intValue());
				break;
			case LONG:
				lPropBuilder.add(((Long) val).longValue());
				break;
			case OBJECT:
				lPropBuilder.add(valueToString(val));
				break;
			case SHORT:
				lPropBuilder.add(((Short) val).shortValue());
				break;
			case STRING:
				lPropBuilder.add(valueToString(val));
				break;
			case RATIONAL:
				lPropBuilder.add(showRational((Rational) val));
				break;
			case PROPERTY:
				lPropBuilder.add(showProperty((Property) val));
				break;
			case NISOIMAGEMETADATA:
				lPropBuilder.add(showNisoImageMetadata((NisoImageMetadata) property.getValue()));
				break;
			case AESAUDIOMETADATA:
				lPropBuilder.add(showAESAudioMetadata((AESAudioMetadata) property.getValue()));
				break;
			case TEXTMDMETADATA:
				lPropBuilder.add(showTextMDMetadata((TextMDMetadata) property.getValue()));
				break;
			default:
				break;
			}
		}
		propBuilder.add(property.getName(), lPropBuilder);
		return propBuilder;
	}

	protected JsonObjectBuilder showSetProperty(Property property) {
		JsonObjectBuilder propBuilder = Json.createObjectBuilder();
		Set<?> propSet = (Set<?>) property.getValue();
		JsonArrayBuilder lPropBuilder = Json.createArrayBuilder();
		PropertyType type = property.getType();

		Iterator<?> iter = propSet.iterator();
		while (iter.hasNext()) {
			Object val = iter.next();
			switch (type) {
			case BOOLEAN:
				lPropBuilder.add(((Boolean) val).booleanValue());
				break;
			case BYTE:
				lPropBuilder.add(valueToString(val));
				break;
			case CHARACTER:
				lPropBuilder.add(valueToString(val));
				break;
			case DATE:
				lPropBuilder.add(valueToString(val));
				break;
			case DOUBLE:
				lPropBuilder.add(((Double) val).doubleValue());
				break;
			case FLOAT:
				lPropBuilder.add(((Float) val).floatValue());
				break;
			case INTEGER:
				lPropBuilder.add(((Integer) val).intValue());
				break;
			case LONG:
				lPropBuilder.add(((Long) val).longValue());
				break;
			case OBJECT:
				lPropBuilder.add(valueToString(val));
				break;
			case SHORT:
				lPropBuilder.add(((Short) val).shortValue());
				break;
			case STRING:
				lPropBuilder.add(valueToString(val));
				break;
			case RATIONAL:
				lPropBuilder.add(showRational((Rational) val));
				break;
			case PROPERTY:
				lPropBuilder.add(showProperty((Property) val));
				break;
			case NISOIMAGEMETADATA:
				lPropBuilder.add(showNisoImageMetadata((NisoImageMetadata) property.getValue()));
				break;
			case AESAUDIOMETADATA:
				lPropBuilder.add(showAESAudioMetadata((AESAudioMetadata) property.getValue()));
				break;
			case TEXTMDMETADATA:
				lPropBuilder.add(showTextMDMetadata((TextMDMetadata) property.getValue()));
				break;
			default:
				break;
			}
		}
		propBuilder.add(property.getName(), lPropBuilder);
		return propBuilder;
	}

	protected JsonObjectBuilder showMapProperty(Property property) {
		JsonObjectBuilder propBuilder = Json.createObjectBuilder();
		JsonObjectBuilder lPropBuilder = Json.createObjectBuilder();
		Map<?, ?> propMap = (Map<?, ?>) property.getValue();
		PropertyType type = property.getType();
		Iterator<?> keyIter = propMap.keySet().iterator();
		while (keyIter.hasNext()) {
			Object key = keyIter.next();
			String keystr = key.toString();
			Object val = propMap.get(key);
			switch (type) {
			case BOOLEAN:
				lPropBuilder.add(keystr, ((Boolean) val).booleanValue());
				break;
			case BYTE:
				lPropBuilder.add(keystr, valueToString(val));
				break;
			case CHARACTER:
				lPropBuilder.add(keystr, valueToString(val));
				break;
			case DATE:
				lPropBuilder.add(keystr, valueToString(val));
				break;
			case DOUBLE:
				lPropBuilder.add(keystr, ((Double) val).doubleValue());
				break;
			case FLOAT:
				lPropBuilder.add(keystr, ((Float) val).floatValue());
				break;
			case INTEGER:
				lPropBuilder.add(keystr, ((Integer) val).intValue());
				break;
			case LONG:
				lPropBuilder.add(keystr, ((Long) val).longValue());
				break;
			case OBJECT:
				lPropBuilder.add(keystr, valueToString(val));
				break;
			case SHORT:
				lPropBuilder.add(keystr, ((Short) val).shortValue());
				break;
			case STRING:
				lPropBuilder.add(keystr, valueToString(val));
				break;
			case RATIONAL:
				lPropBuilder.add(keystr, showRational((Rational) val));
				break;
			case PROPERTY:
				lPropBuilder.add(keystr, showProperty((Property) val));
				break;
			case NISOIMAGEMETADATA:
				lPropBuilder.add(keystr, showNisoImageMetadata((NisoImageMetadata) property.getValue()));
				break;
			case AESAUDIOMETADATA:
				lPropBuilder.add(keystr, showAESAudioMetadata((AESAudioMetadata) property.getValue()));
				break;
			case TEXTMDMETADATA:
				lPropBuilder.add(keystr, showTextMDMetadata((TextMDMetadata) property.getValue()));
				break;
			default:
				break;
			}
		}
		propBuilder.add(property.getName(), lPropBuilder);
		return propBuilder;
	}

	/**
	 * Gives the length (number of elements) of a property
	 */
	protected int propertyLength(Property property) {
		int n = 0;
		try {
			switch (property.getArity()) {
			case SET:
				Set<?> propSet = (Set<?>) property.getValue();
				n = propSet.size();
				break;
			case LIST:
				List<?> propList = (List<?>) property.getValue();
				n = propList.size();
				break;
			case MAP:
				Map<?, ?> propMap = (Map<?, ?>) property.getValue();
				n = propMap.size();
				break;
			case ARRAY:
				// Ack! Is there any easy way to do this?
				switch (property.getType()) {
				case BOOLEAN:
					boolean[] boolArray = (boolean[]) property.getValue();
					n = boolArray.length;
					break;
				case BYTE:
					byte[] byteArray = (byte[]) property.getValue();
					n = byteArray.length;
					break;
				case CHARACTER:
					char[] charArray = (char[]) property.getValue();
					n = charArray.length;
					break;
				case DATE:
					Date[] dateArray = (Date[]) property.getValue();
					n = dateArray.length;
					break;
				case DOUBLE:
					double[] doubleArray = (double[]) property.getValue();
					n = doubleArray.length;
					break;
				case FLOAT:
					float[] floatArray = (float[]) property.getValue();
					n = floatArray.length;
					break;
				case INTEGER:
					int[] intArray = (int[]) property.getValue();
					n = intArray.length;
					break;
				case LONG:
					long[] longArray = (long[]) property.getValue();
					n = longArray.length;
					break;
				case OBJECT:
					Object[] objArray = (Object[]) property.getValue();
					n = objArray.length;
					break;
				case SHORT:
					short[] shortArray = (short[]) property.getValue();
					n = shortArray.length;
					break;
				case STRING:
					String[] stringArray = (String[]) property.getValue();
					n = stringArray.length;
					break;
				case RATIONAL:
					Rational[] rationalArray = (Rational[]) property.getValue();
					n = rationalArray.length;
					break;
				case PROPERTY:
					Property[] propArray = (Property[]) property.getValue();
					n = propArray.length;
					break;
				case NISOIMAGEMETADATA:
					NisoImageMetadata[] nisoArray = (NisoImageMetadata[]) property.getValue();
					n = nisoArray.length;
					break;
				case AESAUDIOMETADATA:
					AESAudioMetadata[] aesArray = (AESAudioMetadata[]) property.getValue();
					n = aesArray.length;
					break;
				case TEXTMDMETADATA:
					TextMDMetadata[] textMDArray = (TextMDMetadata[]) property.getValue();
					n = textMDArray.length;
					break;
				default:
					Object[] array2 = (Object[]) property.getValue();
					n = array2.length;
					break;
				}
				break;
			default:
				if (property.getValue().toString().length() == 0) {
					n = 0;
				} else {
					n = 1;
				}
				break;
			}
		} catch (Exception e) {
			// If something goes seriously wrong, return true to punt the property
			return 0;
		}
		return n;
	}

	/*
	 * The array property has so many special cases of its own that we break it out
	 * of showProperty
	 */
	protected JsonObjectBuilder showArrayProperty(Property property) {
		boolean[] boolArray = null;
		byte[] byteArray = null;
		char[] charArray = null;
		java.util.Date[] dateArray = null;
		double[] doubleArray = null;
		float[] floatArray = null;
		int[] intArray = null;
		long[] longArray = null;
		Object[] objArray = null;
		Property[] propArray = null;
		short[] shortArray = null;
		String[] stringArray = null;
		Rational[] rationalArray = null;
		NisoImageMetadata[] nisoArray = null;
		AESAudioMetadata[] aesArray = null;
		TextMDMetadata[] textMDArray = null;
		int n = 0;

		PropertyType propType = property.getType();
		switch (propType) {
		case BOOLEAN:
			boolArray = (boolean[]) property.getValue();
			n = boolArray.length;
			break;
		case BYTE:
			byteArray = (byte[]) property.getValue();
			n = byteArray.length;
			break;
		case CHARACTER:
			charArray = (char[]) property.getValue();
			n = charArray.length;
			break;
		case DATE:
			dateArray = (Date[]) property.getValue();
			n = dateArray.length;
			break;
		case DOUBLE:
			doubleArray = (double[]) property.getValue();
			n = doubleArray.length;
			break;
		case FLOAT:
			floatArray = (float[]) property.getValue();
			n = floatArray.length;
			break;
		case INTEGER:
			intArray = (int[]) property.getValue();
			n = intArray.length;
			break;
		case LONG:
			longArray = (long[]) property.getValue();
			n = longArray.length;
			break;
		case OBJECT:
			objArray = (Object[]) property.getValue();
			n = objArray.length;
			break;
		case SHORT:
			shortArray = (short[]) property.getValue();
			n = shortArray.length;
			break;
		case STRING:
			stringArray = (String[]) property.getValue();
			n = stringArray.length;
			break;
		case RATIONAL:
			rationalArray = (Rational[]) property.getValue();
			n = rationalArray.length;
			break;
		case PROPERTY:
			propArray = (Property[]) property.getValue();
			n = propArray.length;
			break;
		case NISOIMAGEMETADATA:
			nisoArray = (NisoImageMetadata[]) property.getValue();
			n = nisoArray.length;
			break;
		case AESAUDIOMETADATA:
			aesArray = (AESAudioMetadata[]) property.getValue();
			n = aesArray.length;
			break;
		case TEXTMDMETADATA:
			textMDArray = (TextMDMetadata[]) property.getValue();
			n = textMDArray.length;
			break;
		default:
			break;
		}

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		for (int i = 0; i < n; i++) {
			switch (propType) {
			case BOOLEAN:
				arrayBuilder.add(boolArray[i]);
				break;
			case BYTE:
				arrayBuilder.add(String.valueOf(byteArray[i]));
				break;
			case CHARACTER:
				arrayBuilder.add(String.valueOf(charArray[i]));
				break;
			case DATE:
				arrayBuilder.add(dateArray[i].toString());
				break;
			case DOUBLE:
				arrayBuilder.add(doubleArray[i]);
				break;
			case FLOAT:
				arrayBuilder.add(floatArray[i]);
				break;
			case INTEGER:
				arrayBuilder.add(intArray[i]);
				break;
			case LONG:
				arrayBuilder.add(longArray[i]);
				break;
			case OBJECT:
				arrayBuilder.add(valueToString(objArray[i]));
				break;
			case SHORT:
				arrayBuilder.add(shortArray[i]);
				break;
			case STRING:
				arrayBuilder.add(stringArray[i]);
				break;
			case RATIONAL:
				arrayBuilder.add(rationalArray[i].toString());
				break;
			case PROPERTY:
				arrayBuilder.add(showProperty(propArray[i]));
				break;
			case NISOIMAGEMETADATA:
				arrayBuilder.add(showNisoImageMetadata(nisoArray[i]));
				break;
			case AESAUDIOMETADATA:
				arrayBuilder.add(showAESAudioMetadata(aesArray[i]));
				break;
			case TEXTMDMETADATA:
				arrayBuilder.add(showTextMDMetadata(textMDArray[i]));
				break;
			default:
				arrayBuilder.add("<error>");
				break;
			}
		}
		return Json.createObjectBuilder().add(property.getName(), arrayBuilder);
	}

	/*
	 * Output the textMD metadata, which is its own special kind of property.
	 */
	protected JsonObjectBuilder showTextMDMetadata(TextMDMetadata textMD) {
		JsonObjectBuilder textmdBuilder = Json.createObjectBuilder();

		String s = textMD.getCharset();
		if (s != null) {
			textmdBuilder.add("textmd:charset", s);
		}
		if ((s = textMD.getByte_orderString()) != null) {
			textmdBuilder.add("textmd:byte_order", s);
		}
		if ((s = textMD.getByte_size()) != null) {
			textmdBuilder.add("textmd:byte_size", s);
		}
		if ((s = textMD.getCharacter_size()) != null) {
			textmdBuilder.add("textmd:character_size", s);
		}
		if ((s = textMD.getLinebreakString()) != null) {
			textmdBuilder.add("textmd:linebreak", s);
		}
		if ((s = textMD.getLanguage()) != null) {
			textmdBuilder.add("textmd:language", s);
		}
		if ((s = textMD.getMarkup_basis()) != null) {
			textmdBuilder.add("textmd:markup_basis", s);
		}
		if ((s = textMD.getMarkup_basis_version()) != null) {
			textmdBuilder.add("textmd:markup_basis_version", s);
		}
		if ((s = textMD.getMarkup_language()) != null) {
			textmdBuilder.add("textmd:markup_language", s);
		}
		if ((s = textMD.getMarkup_language_version()) != null) {
			textmdBuilder.add("textmd:markup_language_version", s);
		}
		return textmdBuilder;
	}

	/**
	 * Display the NISO image metadata formatted according to the MIX schema. The
	 * schema which is used may be 0.2 or 1.0 or 2.0, depending on the module
	 * parameters.
	 * 
	 * @param niso NISO image metadata
	 */
	protected JsonObjectBuilder showNisoImageMetadata(NisoImageMetadata niso) {
		if ("0.2".equals(_je.getMixVersion())) {
			return null; // don't handle mix version 0.2
		} else if ("1.0".equals(_je.getMixVersion())) {
			return showNisoImageMetadata(niso, true);
		} else {
			return showNisoImageMetadata(niso, false);
		}
	}

	/**
	 * Display the NISO image metadata formatted according to the MIX 1.0 schema.
	 */
	protected JsonObjectBuilder showNisoImageMetadata(NisoImageMetadata niso, boolean bMix10) {
		JsonObjectBuilder mixBuilder = Json.createObjectBuilder();

		JsonObjectBuilder ob = showNisoBasicDigitalObjectInformation(niso, bMix10);
		if (ob != null) {
			mixBuilder.add("mix:BasicDigitalObjectInformation", ob);
		}
		ob = showNisoBasicImageInformation(niso, bMix10);
		if (ob != null) {
			mixBuilder.add("mix:BasicImageInformation", ob);
		}
		ob = showNisoImageCaptureMetadata(niso, bMix10);
		if (ob != null) {
			mixBuilder.add("mix:ImageCaptureMetadata", ob);
		}
		ob = showNisoImageAssessmentMetadata(niso, bMix10);
		if (ob != null) {
			mixBuilder.add("mix:ImageAssessmentMetadata", ob);
		}
		ob = showChangeHistory(niso, bMix10);
		if (ob != null) {
			mixBuilder.add("mix:ChangeHistory", ob);
		}
		return mixBuilder;
	}

	/*
	 * The NISO Metadata output is split into multiple functions so that they're
	 * merely outrageously big rather than disgustingly big
	 */
	/* Top level element 1 of 5: BasicDigitalObjectInformation */
	protected JsonObjectBuilder showNisoBasicDigitalObjectInformation(NisoImageMetadata niso, boolean bMix10) {
		JsonObjectBuilder mixBuilder = Json.createObjectBuilder();

		String s = niso.getImageIdentifier();
		if (s != null) {
			mixBuilder.add("mix:ObjectIdentifier", Json.createObjectBuilder().add("mix:objectIdentifierType", "JHOVE")
					.add("mix:objectIdentifierValue", s));
		}
		long ln = niso.getFileSize();
		if (ln != NisoImageMetadata.NULL) {
			mixBuilder.add("mix:fileSize", ln);
		}
		if ((s = niso.getMimeType()) != null) {
			mixBuilder.add("mix:formatName", s);
		}
		if ((s = niso.getByteOrder()) != null) {
			// Convert strings to MIX 1.0 form
			if (s.startsWith("big")) {
				s = bMix10 ? "big_endian" : "big endian";
			} else if (s.startsWith("little")) {
				s = bMix10 ? "little_endian" : "little endian";
			}
			mixBuilder.add("mix:byteOrder", s);
		}
		int comp = niso.getCompressionScheme();
		int level = niso.getCompressionLevel();
		String compStr;
		switch (comp) {
		case 1:
			compStr = "Uncompressed";
			break;
		case 2:
			compStr = "CCITT 1D";
			break;
		case 3:
			compStr = "Group 3 Fax";
			break;
		case 4:
			compStr = "Group 4 Fax";
			break;
		case 5:
			compStr = "LZW";
			break;
		case 6:
			compStr = "JPEG";
			break;
		case 32773:
			compStr = "PackBits";
			break;
		case 34713:
			compStr = "JPEG2000 Lossy";
			break;
		case 34714:
			compStr = "JPEG2000 Lossless";
			break;
		default:
			compStr = "Unknown";
			break;
		}
		if (comp != NisoImageMetadata.NULL) {
			if (comp == 34713 || comp == 34714) {
				mixBuilder.add("mix:compressionScheme", compressionSchemeToString(comp));
				if (level != NisoImageMetadata.NULL) {
					mixBuilder.add("mix:compressionRatio", level);
				}
			} else if (bMix10) {
				mixBuilder.add("mix:compressionScheme", Integer.toString(comp));
			} else {
				mixBuilder.add("mix:compressionScheme", compStr);
			}
		}
		int n = niso.getChecksumMethod();
		s = niso.getChecksumValue();
		if (n != NisoImageMetadata.NULL || s != null) {
			if (n != NisoImageMetadata.NULL) {
				mixBuilder.add("mix:messageDigestAlgorithm", n);
			}
			if (s != null) {
				mixBuilder.add("mix:messageDigest", s);
			}
		}
		return mixBuilder;
	}

	/* 1.0, Top level element 2 of 5: BasicImageInformation */
	protected JsonObjectBuilder showNisoBasicImageInformation(NisoImageMetadata niso, boolean bMix10) {
		JsonObjectBuilder mixBuilder = Json.createObjectBuilder();
		boolean hasBuilder = false;
		long ln = niso.getImageWidth();
		if (ln != NisoImageMetadata.NULL) {
			mixBuilder.add("mix:imageWidth", ln);
			hasBuilder = true;
		}
		ln = niso.getImageLength();
		if (ln != NisoImageMetadata.NULL) {
			mixBuilder.add("mix:imageHeight", ln);
			hasBuilder = true;
		}
		int n = niso.getColorSpace();
		if (n != NisoImageMetadata.NULL) {
			if (bMix10) {
				mixBuilder.add("mix:colorSpace", n);
			} else {
				mixBuilder.add("mix:colorSpace", photometricInterpretationToString(n));
			}
			hasBuilder = true;
		}
		String s = niso.getProfileName();
		String s2 = niso.getProfileURL();
		if (s != null || s2 != null) {
			JsonObjectBuilder iccBuilder = Json.createObjectBuilder();
			if (s != null) {
				iccBuilder.add("mix:iccProfileName", s);
			}
			if (s2 != null) {
				iccBuilder.add("mix:iccProfileURL", s2);
			}
			mixBuilder.add("mix:IccProfile", iccBuilder);
			hasBuilder = true;
		}
		int[] iarray = niso.getYCbCrSubSampling();
		n = niso.getYCbCrPositioning();
		Rational[] rarray = niso.getYCbCrCoefficients();
		if (iarray != null || n != NisoImageMetadata.NULL || rarray != null) {
			JsonObjectBuilder yccBuilder = Json.createObjectBuilder();
			if (iarray != null && iarray.length >= 2) {
				yccBuilder.add("mix:YCbCrSubSampling", Json.createObjectBuilder()
						.add("mix:yCbCrSubsampleHoriz", iarray[0]).add("mix:yCbCrSubsampleVert", iarray[1]));
			}
			if (n != NisoImageMetadata.NULL) {
				yccBuilder.add("mix:yCbCrPositioning", n);
			}
			if (rarray != null) {
				if (bMix10) {
					yccBuilder.add("mix:yCbCrCoefficients", showArray(rarray));
				} else {
					yccBuilder.add("mix:yCbCrCoefficients",
							Json.createObjectBuilder().add("mix:lumaRed", showRational(rarray[0]))
									.add("mix:lumaGreen", showRational(rarray[1]))
									.add("mix:lumaBlue", showRational(rarray[2])));
				}
			}
			mixBuilder.add("mix:YCbCr", yccBuilder);
			hasBuilder = true;
		}
		rarray = niso.getReferenceBlackWhite();
		if (rarray != null) {
			if (bMix10) {
				mixBuilder.add("mix:referenceBlackWhite", showArray(rarray));
			} else {
				JsonArrayBuilder aBuilder = Json.createArrayBuilder();
				for (int i = 0; i < rarray.length - 1; i += 2) {
					JsonObjectBuilder cBuilder = Json.createObjectBuilder();
					// Tricky here.
					// The reference BW might be given as either RGB or yCbCr.
					String pi;
					if (niso.getColorSpace() == 6) { // yCbCr
						switch (i) {
						case 0:
							pi = "Y";
							break;
						case 2:
							pi = "Cb";
							break;
						case 4:
						default:
							pi = "Cr";
							break;
						}
					} else {
						switch (i) { // otherwise assume RGB
						case 0:
							pi = "R";
							break;
						case 2:
							pi = "G";
							break;
						case 4:
						default:
							pi = "B";
							break;
						}
					}
					cBuilder.add("mix:componentPhotometricInterpretation", pi);
					cBuilder.add("mix:footroom", showRational(rarray[i]));
					cBuilder.add("mix:headroom", showRational(rarray[i + 1]));
					aBuilder.add(cBuilder);
				}
				mixBuilder.add("mix:ReferenceBlackWhite", aBuilder);
			}
			hasBuilder = true;
		}
		// SpecialFormatCharacteristics limited to JPEG2000
		int lay = niso.getJp2Layers();
		int lev = niso.getJp2ResolutionLevels();
		String sizTiles = niso.getJp2Tiles();
		if (sizTiles != null || lay != NisoImageMetadata.NULL || lev != NisoImageMetadata.NULL) {
			JsonObjectBuilder jp2Builder = Json.createObjectBuilder();
			if (sizTiles != null) {
				if (bMix10) {
					jp2Builder.add("mix:tiles", sizTiles);
				} else {
					String[] sizes = sizTiles.split("x");
					jp2Builder.add("mix:tiles", showArray(sizes));
				}
			}
			if (lay != NisoImageMetadata.NULL) {
				jp2Builder.add("mix:qualityLayers", lay);
			}
			if (lev != NisoImageMetadata.NULL) {
				jp2Builder.add("mix:resolutionLevels", lev);
			}
			mixBuilder.add("mix:JPEG2000", jp2Builder);
			hasBuilder = true;
		}

		if (hasBuilder) {
			return mixBuilder;
		} else {
			return null;
		}
	}

	/* 1.0, Top level element 3 of 5: ImageCaptureMetadata */
	protected JsonObjectBuilder showNisoImageCaptureMetadata(NisoImageMetadata niso, boolean bMix10) {
		JsonObjectBuilder mixBuilder = Json.createObjectBuilder();
		boolean hasBuilder = false;

		String s = niso.getSourceType();
		if (s != null) {
			mixBuilder.add("mix:sourceType", s);
			hasBuilder = true;
		}
		s = niso.getSourceID();
		if (s != null) {
			mixBuilder.add("mix:sourceIDValue", s);
			hasBuilder = true;
		}
		double d = niso.getSourceXDimension();
		int n = niso.getSourceXDimensionUnit();
		if (d != NisoImageMetadata.NILL || n != NisoImageMetadata.NULL) {
			// Assume that both X and Y exist, or neither
			if (d != NisoImageMetadata.NILL) {
				mixBuilder.add("mix:sourceXDimensionValue", d);
			}
			if (n != NisoImageMetadata.NULL) {
				mixBuilder.add("mix:sourceXDimensionUnit", n);
			}
			d = niso.getSourceYDimension();
			n = niso.getSourceYDimensionUnit();
			if (d != NisoImageMetadata.NILL || n != NisoImageMetadata.NULL) {
				if (d != NisoImageMetadata.NILL) {
					mixBuilder.add("mix:sourceYDimensionValue", d);
				}
				if (n != NisoImageMetadata.NULL) {
					mixBuilder.add("mix:sourceYDimensionUnit", n);
				}
			}
			hasBuilder = true;
		}

		s = niso.getDateTimeCreated();
		if (s != null) {
			mixBuilder.add("mix:dateTimeCreated", s);
			hasBuilder = true;
		}
		s = niso.getImageProducer();
		if (s != null) {
			mixBuilder.add("mix:imageProducer", s);
			hasBuilder = true;
		}

		s = niso.getDeviceSource();
		if (s != null) {
			mixBuilder.add("mix:captureDevice", s);
			hasBuilder = true;
		}

		// Here's a chunk of XML for scanners.
		String mfg = niso.getScannerManufacturer();
		if (mfg != null) {
			mixBuilder.add("mix:scannerManufacturer", mfg);
			hasBuilder = true;
		}
		String model = niso.getScannerModelName();
		String modelNum = niso.getScannerModelNumber();
		String serNum = niso.getScannerModelSerialNo();
		if (model != null || modelNum != null || serNum != null) {
			hasBuilder = true;
			JsonObjectBuilder smBuilder = Json.createObjectBuilder();
			if (model != null) {
				smBuilder.add("mix:scannerModelName", model);
			}
			if (modelNum != null) {
				smBuilder.add("mix:scannerModelNumber", modelNum);
			}
			if (serNum != null) {
				smBuilder.add("mix:scannerModelSerialNo", serNum);
			}
			mixBuilder.add("mix:ScannerModel", smBuilder);
		}
		double xres = niso.getXPhysScanResolution();
		double yres = niso.getYPhysScanResolution();
		if (xres != NisoImageMetadata.NULL && yres != NisoImageMetadata.NULL) {
			double res = (xres > yres ? xres : yres);
			if (bMix10) {
				mixBuilder.add("mix:maximumOpticalResolution", res);
			} else {
				mixBuilder.add("mix:MaximumOpticalResolution",
						Json.createObjectBuilder().add("mix:xOpticalResolution", xres)
								.add("mix:yOpticalResolution", yres).add("mix:resolutionUnit", ".in"));
			}

			hasBuilder = true;
		}
		s = niso.getScanningSoftware();
		if (s != null) {
			hasBuilder = true;
			mixBuilder.add("mix:scanningSoftwareName", s);
			s = niso.getScanningSoftwareVersionNo();
			if (s != null) {
				mixBuilder.add("mix:scanningSoftwareVersionNo", s);
			}
		}

		// Now we'll hear from the digital cameras.
		s = niso.getDigitalCameraManufacturer();
		if (s != null) {
			mixBuilder.add("mix:digitalCameraManufacturer", s);
			hasBuilder = true;
		}
		String dcmodel = niso.getDigitalCameraModelName();
		String dcmodelNum = niso.getDigitalCameraModelNumber();
		String dcserNum = niso.getDigitalCameraModelSerialNo();
		if (dcmodel != null || dcmodelNum != null || dcserNum != null) {
			hasBuilder = true;
			JsonObjectBuilder smBuilder = Json.createObjectBuilder();
			if (dcmodel != null) {
				smBuilder.add("mix:digitalCameraModelName", dcmodel);
			}
			if (dcmodelNum != null) {
				smBuilder.add("mix:digitalCameraModelNumber", dcmodelNum);
			}
			if (dcserNum != null) {
				smBuilder.add("mix:mix:digitalCameraModelSerialNo", dcserNum);
			}
			mixBuilder.add("mix:DigitalCameraModel", smBuilder);
		}

		// Nest a buffer for CameraCaptureSettings
		JsonObjectBuilder ccsBuilder = Json.createObjectBuilder();
		boolean useCcSetBuf = false;
		d = niso.getFNumber();
		if (d != NisoImageMetadata.NULL) {
			ccsBuilder.add("mix:fNumber", d);
			useCcSetBuf = true;
		}
		d = niso.getExposureTime();
		if (d != NisoImageMetadata.NULL) {
			ccsBuilder.add("mix:exposureTime", d);
			useCcSetBuf = true;
		}
		n = niso.getExposureProgram();
		if (n != NisoImageMetadata.NULL) {
			if (bMix10) {
				ccsBuilder.add("mix:exposureProgram", n);
			} else {
				if (n > 8 || n < 0) {
					n = 0; // force "Not defined" for bad value
				}
				ccsBuilder.add("mix:exposureProgram", NisoImageMetadata.EXPOSURE_PROGRAM[n]);
			}
			useCcSetBuf = true;
		}
		s = niso.getExifVersion();
		if (s != null) {
			ccsBuilder.add("mix:exifVersion", s);
			useCcSetBuf = true;
		}
		Rational r = niso.getBrightness();
		if (r != null) {
			ccsBuilder.add("mix:brightnessValue", showRational(r));
			useCcSetBuf = true;
		}
		r = niso.getExposureBias();
		if (r != null) {
			ccsBuilder.add("mix:exposureBiasValue", showRational(r));
			useCcSetBuf = true;
		}
		r = niso.getMaxApertureValue();
		if (r != null) {
			ccsBuilder.add("mix:maxApertureValue", showRational(r));
			useCcSetBuf = true;
		}
		double[] darray = niso.getSubjectDistance();
		if (darray != null) {
			ccsBuilder.add("mix:subjectDistance", showArray(darray));
			useCcSetBuf = true;
		}
		n = niso.getMeteringMode();
		if (n != NisoImageMetadata.NULL) {
			if (bMix10) {
				ccsBuilder.add("mix:meteringMode", n);
			} else {
				s = meteringModeToString(n);
				if (s.startsWith("Center weighted")) {
					s = "Center weighted Average";
				}
				ccsBuilder.add("mix:MeteringMode", s);
			}
			useCcSetBuf = true;
		}
		n = niso.getFlash();
		if (n != NisoImageMetadata.NULL) {
			// First bit (0 = Flash did not fire, 1 = Flash fired)
			int firstBit = n & 1;
			ccsBuilder.add("mix:flash", NisoImageMetadata.FLASH_20[firstBit]);
			useCcSetBuf = true;
		}
		d = niso.getFocalLength();
		if (d != NisoImageMetadata.NULL) {
			ccsBuilder.add("mix:focalLength", d);
			useCcSetBuf = true;
		}
		r = niso.getFlashEnergy();
		if (r != null) {
			ccsBuilder.add("mix:flashEnergy", showRational(r));
			useCcSetBuf = true;
		}
		n = niso.getBackLight();
		if (n != NisoImageMetadata.NULL) {
			ccsBuilder.add("mix:backLight", n);
			useCcSetBuf = true;
		}
		d = niso.getExposureIndex();
		if (d != NisoImageMetadata.NULL) {
			ccsBuilder.add("mix:exposureIndex", d);
			useCcSetBuf = true;
		}
		n = niso.getAutoFocus();
		if (n != NisoImageMetadata.NULL) {
			ccsBuilder.add("mix:autoFocus", n);
			useCcSetBuf = true;
		}
		d = niso.getXPrintAspectRatio();
		double d2 = niso.getYPrintAspectRatio();
		if (d != NisoImageMetadata.NULL) {
			ccsBuilder.add("mix:xPrintAspectRatio", d);
			useCcSetBuf = true;
		}
		if (d2 != NisoImageMetadata.NULL) {
			ccsBuilder.add("mix:yPrintAspectRatio", d);
			useCcSetBuf = true;
		}
		if (useCcSetBuf) {
			mixBuilder.add("mix:CameraCaptureSettings", ccsBuilder);

			hasBuilder = true;
		}

		n = niso.getOrientation();
		if (n != NisoImageMetadata.NULL) {
			if (bMix10) {
				mixBuilder.add("mix:orientation", n);
			} else {
				final String[] orient = { "unknown", "normal*", "normal, image flipped", "normal, rotated 180\u00B0",
						"normal, image flipped, rotated 180\u00B0", "normal, image flipped, rotated cw 90\u00B0",
						"normal, rotated ccw 90\u00B0", "normal, image flipped, rotated ccw 90\u00B0",
						"normal, rotated cw 90\u00B0" };
				if (n > 8 || n < 0) {
					n = 0; // force "unknown" for bad value
				}
				mixBuilder.add("mix:orientation", orient[n]);
			}
			hasBuilder = true;
		}
		s = niso.getMethodology();
		if (s != null) {
			mixBuilder.add("mix:methodology", s);
			hasBuilder = true;
		}
		if (hasBuilder) {
			return mixBuilder;
		} else {
			return null;
		}
	}

	/* 1.0, Top level element 4 of 5: ImageAssessmentMetadata */
	protected JsonObjectBuilder showNisoImageAssessmentMetadata(NisoImageMetadata niso, boolean bMix10) {
		JsonObjectBuilder mixBuilder = Json.createObjectBuilder();
		boolean hasBuilder = false;

		JsonObjectBuilder smBuilder = Json.createObjectBuilder();
		boolean useMetricsBuf = false;

		int n = niso.getSamplingFrequencyPlane();
		if (n != NisoImageMetadata.NULL) {
			smBuilder.add("mix:samplingFrequencyPlane", n);
			useMetricsBuf = true;
		}
		n = niso.getSamplingFrequencyUnit();
		if (n != NisoImageMetadata.NULL) {
			if (bMix10) {
				smBuilder.add("mix:samplingFrequencyUnit", n);
			} else {
				final String sfu[] = { null, "no absolute unit of measurement", "in.", "cm" };
				if (n < 1 || n > 3) {
					n = 1;
				}
				smBuilder.add("mix:samplingFrequencyUnit", sfu[n]);
			}
			useMetricsBuf = true;
		}
		Rational r = niso.getXSamplingFrequency();
		if (r != null) {
			smBuilder.add("mix:xSamplingFrequency", showRational(r));
			useMetricsBuf = true;
		}
		r = niso.getYSamplingFrequency();
		if (r != null) {
			smBuilder.add("mix:ySamplingFrequency", showRational(r));
			useMetricsBuf = true;
		}
		if (useMetricsBuf) {
			mixBuilder.add("mix:SpatialMetrics", smBuilder);
			hasBuilder = true;
		}

		JsonObjectBuilder imeBuilder = Json.createObjectBuilder();
		boolean useColorEncBuf = false;

		int[] iarray = niso.getBitsPerSample();
		if (iarray != null) {
			imeBuilder.add("mix:bitsPerSample", showArray(iarray));
			imeBuilder.add("mix:bitsPerSampleUnit", "integer");
			// bitsPerSampleUnit can also be floating point. Don't ask me why.
			useColorEncBuf = true;
		}
		n = niso.getSamplesPerPixel();
		if (n != NisoImageMetadata.NULL) {
			imeBuilder.add("mix:samplesPerPixel", n);
			useColorEncBuf = true;
		}

		iarray = niso.getExtraSamples();
		if (iarray != null) {
			// extraSamples must be limited to
			// 0, 1, 2, or 3.
			n = iarray[0];
			if (n >= 0 && n <= 3) {
				if (bMix10) {
					imeBuilder.add("mix:extraSamples", showArray(iarray));
				} else {
					String[] sarray = new String[iarray.length];
					for (int ii = 0; ii < iarray.length; ii++) {
						sarray[ii] = NisoImageMetadata.EXTRA_SAMPLE_20[iarray[ii]];
						imeBuilder.add("mix:extraSamples", showArray(sarray));
					}

				}
				useColorEncBuf = true;
			}
		}

		String s = niso.getColormapReference();
		if (s != null) {
			imeBuilder.add("mix:colormapReference", s);
			useColorEncBuf = true;
		}

		// This is complete nonsense, but it's what the spec says
		iarray = niso.getGrayResponseCurve();
		if (iarray != null) {
			imeBuilder.add("mix:grayResponseCurve", showArray(iarray));
			useColorEncBuf = true;
		}

		n = niso.getGrayResponseUnit();
		if (n != NisoImageMetadata.NULL) {
			if (bMix10) {
				imeBuilder.add("mix:grayResponseUnit", n);
			} else if (n > 0 && n <= 5) {
				// Convert integer to text value; only values 1-5 are legal
				imeBuilder.add("mix:grayResponseUnit", NisoImageMetadata.GRAY_RESPONSE_UNIT_20[n - 1]);
			}
			useColorEncBuf = true;
		}

		r = niso.getWhitePointXValue();
		Rational r2 = niso.getWhitePointYValue();
		if (r != null && r2 != null) {
			imeBuilder.add("mix:WhitePoint", Json.createObjectBuilder().add("mix:whitePointXValue", showRational(r))
					.add("mix:whitePointYValue", showRational(r2)));
			useColorEncBuf = true;
		}

		// A chromaticities buffer to go in the color encoding buffer.
		JsonObjectBuilder pcBuilder = Json.createObjectBuilder();
		boolean useChromaBuf = false;
		r = niso.getPrimaryChromaticitiesRedX();
		if (r != null) {
			pcBuilder.add("mix:primaryChromaticitiesRedX", showRational(r));
			useChromaBuf = true;
		}
		r = niso.getPrimaryChromaticitiesRedY();
		if (r != null) {
			pcBuilder.add("mix:primaryChromaticitiesRedY", showRational(r));
			useChromaBuf = true;
		}
		r = niso.getPrimaryChromaticitiesGreenX();
		if (r != null) {
			pcBuilder.add("mix:primaryChromaticitiesGreenX", showRational(r));
			useChromaBuf = true;
		}
		r = niso.getPrimaryChromaticitiesGreenY();
		if (r != null) {
			pcBuilder.add("mix:primaryChromaticitiesGreenY", showRational(r));
			useChromaBuf = true;
		}
		r = niso.getPrimaryChromaticitiesBlueX();
		if (r != null) {
			pcBuilder.add("mix:primaryChromaticitiesBlueX", showRational(r));
			useChromaBuf = true;
		}
		r = niso.getPrimaryChromaticitiesBlueY();
		if (r != null) {
			pcBuilder.add("mix:primaryChromaticitiesBlueY", showRational(r));
			useChromaBuf = true;
		}
		if (useChromaBuf) {
			imeBuilder.add("mix:PrimaryChromaticities", pcBuilder);
			useColorEncBuf = true;
		}

		if (useColorEncBuf) {
			mixBuilder.add("mix:ImageColorEncoding", imeBuilder);
			hasBuilder = true;
		}

		JsonObjectBuilder tdBuilder = Json.createObjectBuilder();
		boolean useTargetBuf = false;
		n = niso.getTargetType();
		if (n != NisoImageMetadata.NULL) {
			tdBuilder.add("mix:targetType", n);
			useTargetBuf = true;
		}
		s = niso.getTargetIDManufacturer();
		if (s != null) {
			tdBuilder.add("mix:targetManufacturer", s);
			useTargetBuf = true;
		}
		s = niso.getTargetIDName();
		if (s != null) {
			tdBuilder.add("mix:targetName", s);
			useTargetBuf = true;
		}
		s = niso.getTargetIDNo();
		if (s != null) {
			tdBuilder.add("mix:targetNo", s);
			useTargetBuf = true;
		}
		s = niso.getTargetIDMedia();
		if (s != null) {
			tdBuilder.add("mix:targetMedia", s);
			useTargetBuf = true;
		}
		s = niso.getImageData();
		if (s != null) {
			tdBuilder.add("mix:externalTarget", s);
			useTargetBuf = true;
		}
		s = niso.getPerformanceData();
		if (s != null) {
			tdBuilder.add("mix:performanceData", s);
			useTargetBuf = true;
		}

		if (useTargetBuf) {
			mixBuilder.add("mix:TargetData", tdBuilder);
			hasBuilder = true;
		}

		if (hasBuilder) {
			return mixBuilder;
		} else {
			return null;
		}
	}

	/* 1.0, Top level element 5 of 5: ChangeHistory (without time travel) */
	protected JsonObjectBuilder showChangeHistory(NisoImageMetadata niso, boolean bMix10) {
		JsonObjectBuilder mixBuilder = Json.createObjectBuilder();
		boolean hasBuilder = false;

		String s = niso.getSourceData();
		if (s != null) {
			mixBuilder.add("mix:sourceData", s);
			hasBuilder = true;
		}
		s = niso.getProcessingAgency();
		if (s != null) {
			mixBuilder.add("mix:processingAgency", s);
			hasBuilder = true;
		}

		JsonObjectBuilder psBuilder = Json.createObjectBuilder();
		boolean useSftwBuf = false;
		s = niso.getProcessingSoftwareName();
		if (s != null) {
			psBuilder.add("mix:processingSoftwareName", s);
			useSftwBuf = true;
		}
		s = niso.getProcessingSoftwareVersion();
		if (s != null) {
			psBuilder.add("mix:processingSoftwareVersion", s);
			useSftwBuf = true;
		}
		s = niso.getOS();
		if (s != null) {
			psBuilder.add("mix:processingOperatingSystemName", s);
			useSftwBuf = true;
		}
		s = niso.getOSVersion();
		if (s != null) {
			psBuilder.add("mix:processingOperatingSystemVersion", s);
			useSftwBuf = true;
		}
		if (useSftwBuf) {
			mixBuilder.add("mix:ProcessingSoftware", psBuilder);
			hasBuilder = true;
		}
		String[] sarray = niso.getProcessingActions();
		if (sarray != null) {
			mixBuilder.add("mix:processingActions", showArray(sarray));
			hasBuilder = true;
		}

		if (hasBuilder) {
			return mixBuilder;
		} else {
			return null;
		}
	}

	/**
	 * Convert the metering mode value to one of the suggested values for MIX 2.0
	 */
	private String meteringModeToString(int n) {
		String s = NisoImageMetadata.METERING_MODE[1];
		if (n >= 1 && n <= 6) {
			s = NisoImageMetadata.METERING_MODE[n];
		}
		// Capitalize first letter
		return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
	}

	/**
	 * Convert the color space value (which is based on the TIFF
	 * PhotometricInterpretation convention) to one of the suggested values for MIX
	 * 2.0
	 */
	private String photometricInterpretationToString(int n) {
		switch (n) {
		case 0:
			return "WhiteIsZero";
		case 1:
			return "BlackIsZero";
		case 2:
			return "RGB";
		case 3:
			return "PaletteColor";
		case 4:
			return "TransparencyMask";
		case 5:
			return "CMYK";
		case 6:
			return "YCbCr";
		case 8:
			return "CIELab";
		case 9:
			return "ICCLab";
		case 10:
			return "ITULab";
		case 32803:
			return "CFA";
		case 34892:
			return "LinearRaw";
		default:
			return "Unknown";
		}
	}

	/**
	 * Convert compression scheme value (based on the TIFF compression convention)
	 * to a label
	 */
	private String compressionSchemeToString(int n) {
		for (int i = 0; i < NisoImageMetadata.COMPRESSION_SCHEME_INDEX.length; i++) {
			if (n == NisoImageMetadata.COMPRESSION_SCHEME_INDEX[i])
				return NisoImageMetadata.COMPRESSION_SCHEME[i];
		}
		return Integer.toString(n);
	}

	/**
	 * Display the audio metadata formatted according to the AES schema.
	 * 
	 * @param aes AES audio metadata
	 */
	protected JsonObjectBuilder showAESAudioMetadata(AESAudioMetadata aes) {
		JsonObjectBuilder aesBuilder = Json.createObjectBuilder();

		_sampleRate = aes.getSampleRate();

		String s = aes.getAnalogDigitalFlag();
		if (s != null) {
			aesBuilder.add("aes:analogDigitalFlag", s);
		}
		s = aes.getSchemaVersion();
		if (s != null) {
			aesBuilder.add("aes:schemaVersion", s);
		}
		s = aes.getFormat();
		if (s != null) {
			aesBuilder.add("aes:format", s);
		}
		s = aes.getSpecificationVersion();
		if (s != null) {
			aesBuilder.add("aes:specificationVersion", s);
		}
		s = aes.getAppSpecificData();
		if (s != null) {
			aesBuilder.add("aes:appSpecificData", s);
		}
		s = aes.getAudioDataEncoding();
		if (s != null) {
			aesBuilder.add("aes:audioDataEncoding", s);
		}
		int in = aes.getByteOrder();
		if (in != AESAudioMetadata.NULL) {
			aesBuilder.add("aes:byteOrder", (in == AESAudioMetadata.BIG_ENDIAN ? "BIG_ENDIAN" : "LITTLE_ENDIAN"));
		}
		long lin = aes.getFirstSampleOffset();
		if (lin != AESAudioMetadata.NULL) {
			aesBuilder.add("aes:firstSampleOffset", lin);
		}
		String[] use = aes.getUse();
		if (use != null) {
			aesBuilder.add("aes:use",
					Json.createObjectBuilder().add("aes:useType", use[0]).add("aes:otherType", use[1]));
		}
		s = aes.getPrimaryIdentifier();
		if (s != null) {
			String t = aes.getPrimaryIdentifierType();
			aesBuilder.add("aes:primaryIdentifier", s);
			if (t != null) {
				aesBuilder.add("aes:primaryIdentifierType", t);
			}
		}
		List<AESAudioMetadata.Face> facelist = aes.getFaceList();
		if (facelist != null && !facelist.isEmpty()) {
			// Add the face information, which is mostly filler.
			AESAudioMetadata.Face f = facelist.get(0);
			JsonObjectBuilder faceBuilder = Json.createObjectBuilder();

			AESAudioMetadata.TimeDesc startTime = f.getStartTime();
			if (startTime != null) {
				faceBuilder.add("aes:timeline", writeAESTimeRange(startTime, f.getDuration()));
			}
			int nchan = aes.getNumChannels();
			if (nchan != AESAudioMetadata.NULL) {
				faceBuilder.add("aes:numChannels", nchan);
			}
			String[] locs = aes.getMapLocations();
			JsonArrayBuilder streamsBuilder = Json.createArrayBuilder();
			for (int ch = 0; ch < nchan; ch++) {
				// write a stream description for each channel
				streamsBuilder
						.add(Json.createObjectBuilder().add("aes:channelNum", ch).add("aes:mapLocation", locs[ch]));
			}
			faceBuilder.add("aes:streams", streamsBuilder);
			aesBuilder.add("aes:face", faceBuilder);
		}

		// In the general case, a FormatList can contain multiple
		// FormatRegions. This doesn't happen with any of the current
		// modules; if it's needed in the future, simply set up an
		// iteration loop on formatList.
		List<AESAudioMetadata.FormatRegion> flist = aes.getFormatList();
		if (flist != null && !flist.isEmpty()) {
			AESAudioMetadata.FormatRegion rgn = flist.get(0);
			int bitDepth = rgn.getBitDepth();
			double sampleRate = rgn.getSampleRate();
			int wordSize = rgn.getWordSize();
			String[] bitRed = rgn.getBitrateReduction();
			// Build a FormatRegion subtree if at least one piece of data
			// that goes into it is present.
			JsonArrayBuilder formatListBuilder = Json.createArrayBuilder();
			JsonObjectBuilder formatRegionBuilder = Json.createObjectBuilder();
			if (bitDepth != AESAudioMetadata.NULL || sampleRate != AESAudioMetadata.NILL
					|| wordSize != AESAudioMetadata.NULL) {
				if (bitDepth != AESAudioMetadata.NULL) {
					formatRegionBuilder.add("aes:bitDepth", bitDepth);
				}
				if (sampleRate != AESAudioMetadata.NILL) {
					formatRegionBuilder.add("aes:sampleRate", sampleRate);
				}
				if (wordSize != AESAudioMetadata.NULL) {
					formatRegionBuilder.add("aes:wordSize", wordSize);
				}
				if (bitRed != null) {
					formatRegionBuilder.add("aes:bitrateReduction", Json.createObjectBuilder()
							.add("aes:codecName", bitRed[0]).add("aes:codecNameVersion", bitRed[1])
							.add("aes:codecCreatorApplication", bitRed[2])
							.add("aes:codecCreatorApplicationVersion", bitRed[3]).add("aes:codecQuality", bitRed[4])
							.add("aes:dataRate", bitRed[5]).add("aes:dataRateMode", bitRed[6]));
				}

				formatListBuilder.add(formatRegionBuilder);
				aesBuilder.add("aes:formatList", formatListBuilder);
			}
		}
		return aesBuilder;
	}

	/*
	 * Break out the writing of a timeRangeType element. This always gives a start
	 * time of 0. This is all FAKE DATA for the moment.
	 */
	private JsonObjectBuilder writeAESTimeRange(AESAudioMetadata.TimeDesc start, AESAudioMetadata.TimeDesc duration) {
		double sr = start.getSampleRate();
		if (sr == 1.0) {
			sr = _sampleRate;
		}

		JsonObjectBuilder timerangeBuilder = Json.createObjectBuilder();
		timerangeBuilder.add("tcf:startTime",
				Json.createObjectBuilder().add("tcf:frameCount", 30).add("tcf:timeBase", 1000)
						.add("tcf:videoField", "FIELD_1").add("tcf:countingMode", "NTSC_NON_DROP_FRAME")
						.add("tcf:hours", start.getHours()).add("tcf:minutes", start.getMinutes())
						.add("tcf:seconds", start.getSeconds()).add("tcf:frames", start.getFrames())
						.add("tcf:samples",
								Json.createObjectBuilder().add("tcf:sampleRate", "S" + Integer.toString((int) sr))
										.add("tcf:numberOfSamples", start.getSamples()))
						.add("tcf:filmFraming", Json.createObjectBuilder().add("tcf:framing", "NOT_APPLICABLE")
								.add("tcf:framingType", "tcf:ntscFilmFramingType")));
		if (duration != null) {
			sr = duration.getSampleRate();
			if (sr == 1.0) {
				sr = _sampleRate;
			}
			timerangeBuilder.add("tcf:duration",
					Json.createObjectBuilder().add("tcf:frameCount", 30).add("tcf:timeBase", 1000)
							.add("tcf:videoField", "FIELD_1").add("tcf:countingMode", "NTSC_NON_DROP_FRAME")
							.add("tcf:hours", duration.getHours()).add("tcf:minutes", duration.getMinutes())
							.add("tcf:seconds", duration.getSeconds()).add("tcf:frames", duration.getFrames())
							.add("tcf:samples",
									Json.createObjectBuilder().add("tcf:sampleRate", "S" + Integer.toString((int) sr))
											.add("tcf:numberOfSamples", duration.getSamples()))
							.add("tcf:filmFraming", Json.createObjectBuilder().add("tcf:framing", "NOT_APPLICABLE")
									.add("tcf:framingType", "tcf:ntscFilmFramingType")));
		}
		return timerangeBuilder;
	}

	protected JsonArrayBuilder showArray(int[] iarray) {
		JsonArrayBuilder aBuilder = Json.createArrayBuilder();
		for (int i : iarray) {
			aBuilder.add(i);
		}
		return aBuilder;
	}

	protected JsonArrayBuilder showArray(double[] darray) {
		JsonArrayBuilder dBuilder = Json.createArrayBuilder();
		for (double d : darray) {
			dBuilder.add(d);
		}
		return dBuilder;
	}

	protected JsonArrayBuilder showArray(String[] sarray) {
		JsonArrayBuilder sBuilder = Json.createArrayBuilder();
		for (String s : sarray) {
			if (s == null) {
				sBuilder.addNull();
			} else {
				sBuilder.add(s);
			}
		}
		return sBuilder;
	}

	protected JsonArrayBuilder showArray(Rational[] rarray) {
		JsonArrayBuilder rBuilder = Json.createArrayBuilder();
		for (Rational r : rarray) {
			if (r == null) {
				rBuilder.addNull();
			} else {
				rBuilder.add(showRational(r));
			}
		}
		return rBuilder;
	}

	protected JsonArrayBuilder showRational(Rational r) {
		JsonArrayBuilder rationalBuilder = Json.createArrayBuilder();
		long numer = r.getNumerator();
		long denom = r.getDenominator();
		rationalBuilder.add(numer);
		rationalBuilder.add(denom);
		return rationalBuilder;
	}
}
