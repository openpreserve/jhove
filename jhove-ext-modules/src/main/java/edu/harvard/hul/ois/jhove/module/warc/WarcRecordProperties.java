package edu.harvard.hul.ois.jhove.module.warc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jwat.warc.WarcConstants;
import org.jwat.warc.WarcRecord;

/**
 * Retrieves the WARC record data and delivers its properties as a map.
 * 
 * Based on the WARC property classes from JHOVE2.
 * 
 * @author jolf
 *
 */
public class WarcRecordProperties {
    /** The WARC record data.*/
    private final WarcRecordData data;
    /** The map for the properties of the WARC record.*/
    private Map<String, String> properties;
    
    /**
     * Constructor.
     * @param record The record to extract the properties from.
     */
    public WarcRecordProperties(WarcRecord record) {
        data = new WarcRecordData(record);
    }
    
    private final static String DATE_HEADER= "Warc-Date header value.";
    private final static String TARGET_URI_HEADER = "Warc-Target-URI header value.";
    private final static String WARCINFO_ID_HEADER= "Warc-Warcinfo-ID header value.";
    private final static String REFERS_TO_HEADER= "Warc-Refers-To header value.";
    private final static String CONCURRENT_TO_HEADER= "Warc-Concurrent-To header value.";
    private final static String IP_ADDRESS_HEADER= "Warc-IP-Address header value.";
    private final static String IP_ADDRESS_VERSION= "Ip-Address version.";
    private final static String PROTOCOL_VERSION_HEADER="ProtocolVersion header value.";

    /**
     * Retrieves the WARC record properties as a map.
     * Starts by extracting the base properties for all WARC records, 
     * then adds the specific properties for the given type of WARC record.
     * @return A map of the properties of the WARC record.
     */
    public Map<String, String> getProperties() {
        properties = new LinkedHashMap<>();
        
        setBaseProperties();
        
        // Would be better, if we could use java 1.7 string switch...
        if(data.warcType == null) {
            // Ignore.
        } else if(data.warcType.equalsIgnoreCase(WarcConstants.RT_CONTINUATION)) {
            setContinuationRecordProperties();
        } else if(data.warcType.equalsIgnoreCase(WarcConstants.RT_CONVERSION)) {
            setConversionRecordProperties();
        } else if(data.warcType.equalsIgnoreCase(WarcConstants.RT_METADATA)) {
            setMetadataRecordProperties();
        } else if(data.warcType.equalsIgnoreCase(WarcConstants.RT_REQUEST)) {
            setRequestRecordProperties();
        } else if(data.warcType.equalsIgnoreCase(WarcConstants.RT_RESOURCE)) {
            setResourceRecordProperties();
        } else if(data.warcType.equalsIgnoreCase(WarcConstants.RT_RESPONSE)) {
            setResponseRecordProperties();
        } else if(data.warcType.equalsIgnoreCase(WarcConstants.RT_REVISIT)) {
            setRevisitRecordProperties();
        } else if(data.warcType.equalsIgnoreCase(WarcConstants.RT_WARCINFO)) {
            setWarcInfoRecordProperties();
        }

        return properties;
    }
    
    /**
     * Set the base properties for all WARC records.
     */
    private void setBaseProperties() {
        setProperty(data.startOffset, "Record offset in WARC file.");
        setProperty(data.warcVersionStr, DATE_HEADER);
        setProperty(data.warcDate, DATE_HEADER);
        setProperty(data.warcRecordId, "Warc-Record-ID header value.");
        setProperty(data.recordIdScheme, "Record-ID-Scheme value.");
        setProperty(data.contentType, "Content-Type header value.");
        setProperty(data.contentLength, "Content-Length header value.");
        setProperty(data.warcType, "Warc-Type header value.");
        setProperty(data.warcBlockDigest, "Warc-Block-Digest header value.");
        setProperty(data.warcBlockDigestAlgorithm, "Block-Digest-Algorithm value.");
        
        setProperty(data.warcBlockDigestEncoding, "Block-Digest-Encoding value.");
        setProperty(data.isValidBlockDigest, "isValidBlockDigest boolean value.");
        setProperty(data.warcPayloadDigest, "Warc-Payload-Digest header value.");
        setProperty(data.warcPayloadDigestAlgorithm, "Payload-Digest-Algorithm value.");
        setProperty(data.warcPayloadDigestEncoding, "Payload-Digest-Encoding value.");
        setProperty(data.isValidPayloadDigest, "isValidPayloadDigest boolean value.");
        setProperty(data.warcTruncated, "Warc-Truncated header value.");
        setProperty(data.bHasPayload, "hasPayload value.");
        setProperty(data.payloadLength, "PayloadLength value.");
        setProperty(data.warcIdentifiedPayloadType, "Warc-Identified-Payload-Type header value.");
        
        setProperty(data.warcSegmentNumber, "Warc-Segment-Number header value.");
        setProperty(data.bIsNonCompliant, "isNonCompliant value.");
        setProperty(data.computedBlockDigest, "Computed Block-Digest header value.");
        setProperty(data.computedBlockDigestAlgorithm, "Computed Block-Digest-Algorithm value.");
        setProperty(data.computedBlockDigestEncoding, "Computed Block-Digest-Encoding value.");
        setProperty(data.computedPayloadDigest, "Computed Payload-Digest header value.");
        setProperty(data.computedPayloadDigestAlgorithm, "Computed Payload-Digest-Algorithm value.");
        setProperty(data.computedPayloadDigestEncoding, "Computed Payload-Digest-Encoding value.");
    }
    
    /**
     * Set the properties for the Continuation WARC record.
     */
    private void setContinuationRecordProperties() {
      setProperty(data.warcTargetUri, TARGET_URI_HEADER);
      setProperty(data.warcSegmentOriginId, "Warc-Segment-Origin-ID header value.");
      setProperty(data.warcSegmentTotalLength, "Warc-Segment-Total-Length header value.");
      setProperty(data.warcWarcinfoId, WARCINFO_ID_HEADER);
    }
    
    /**
     * Set the properties for the Conversion WARC record.
     */
    private void setConversionRecordProperties() {
      setProperty(data.warcTargetUri, TARGET_URI_HEADER);
      setProperty(data.warcRefersTo, REFERS_TO_HEADER);
      setProperty(data.warcWarcinfoId, WARCINFO_ID_HEADER);        
    }
    
    /**
     * Set the properties for the Metadata WARC record.
     */
    private void setMetadataRecordProperties() {
        setProperty(data.warcTargetUri, TARGET_URI_HEADER);
        setProperty(data.warcConcurrentToList, CONCURRENT_TO_HEADER);
        setProperty(data.warcRefersTo, REFERS_TO_HEADER);
        setProperty(data.warcIpAddress, IP_ADDRESS_HEADER);
        setProperty(data.ipVersion, IP_ADDRESS_VERSION);
        setProperty(data.warcWarcinfoId, WARCINFO_ID_HEADER);        
    }
    
    /**
     * Set the properties for the Request WARC record.
     */
    private void setRequestRecordProperties() {
        setProperty(data.warcTargetUri, TARGET_URI_HEADER);
        setProperty(data.warcConcurrentToList, CONCURRENT_TO_HEADER);
        setProperty(data.warcIpAddress, IP_ADDRESS_HEADER);
        setProperty(data.ipVersion, IP_ADDRESS_VERSION);
        setProperty(data.warcWarcinfoId, WARCINFO_ID_HEADER);
        setProperty(data.protocolVersion, PROTOCOL_VERSION_HEADER);
        setProperty(data.protocolUserAgent, "ProtocolUserAgent header value.");
    }
    
    /**
     * Set the properties for the Resource WARC record.
     */
    private void setResourceRecordProperties() {
        setProperty(data.warcTargetUri, TARGET_URI_HEADER);
        setProperty(data.warcConcurrentToList, CONCURRENT_TO_HEADER);
        setProperty(data.warcIpAddress, IP_ADDRESS_HEADER);
        setProperty(data.ipVersion, IP_ADDRESS_VERSION);
        setProperty(data.warcWarcinfoId, WARCINFO_ID_HEADER);        
    }

    /**
     * Set the properties for the Response WARC record.
     */
    private void setResponseRecordProperties() {
        setProperty(data.warcTargetUri, TARGET_URI_HEADER);
        setProperty(data.warcConcurrentToList, CONCURRENT_TO_HEADER);
        setProperty(data.warcIpAddress, IP_ADDRESS_HEADER);
        setProperty(data.ipVersion, IP_ADDRESS_VERSION);
        setProperty(data.warcWarcinfoId, WARCINFO_ID_HEADER);
        setProperty(data.resultCode, "ProtocolResultCode header value.");
        setProperty(data.protocolVersion, PROTOCOL_VERSION_HEADER);
        setProperty(data.protocolContentType, "ProtocolContentType header value.");        
        setProperty(data.protocolServer, "ServerName header value.");
    }

    /**
     * Set the properties for the Revisit WARC record.
     */
    private void setRevisitRecordProperties() {
        setProperty(data.warcTargetUri, TARGET_URI_HEADER);
        setProperty(data.warcProfile, "Warc-Profile header value.");
        setProperty(data.warcRefersTo, REFERS_TO_HEADER);
        setProperty(data.warcIpAddress, IP_ADDRESS_HEADER);
        setProperty(data.ipVersion, IP_ADDRESS_VERSION);
        setProperty(data.warcWarcinfoId, WARCINFO_ID_HEADER);        
    }
    
    /**
     * Set the properties for the WarcInfo WARC record.
     */
    private void setWarcInfoRecordProperties() {
        setProperty(data.warcFilename, "WarcFilename header value.");        
    }
    
    /**
     * Sets the given string property, if it has a valid value (not null and not empty).
     * @param variable The value for the property.
     * @param description The description and key of the property.
     */
    private void setProperty(String variable, String description) {
        if(variable != null && !variable.isEmpty()) {
            properties.put(description, variable);
        }
    }

    /**
     * Sets the given boolean property, if it has a valid value (not null).
     * @param variable The value for the property.
     * @param description The description and key of the property.
     */
    private void setProperty(Boolean variable, String description) {
        if(variable != null) {
            properties.put(description, variable.toString());
        }
    }

    /**
     * Sets the given string-list property, if it has a valid value (not null and not empty).
     * @param variable The value for the property.
     * @param description The description and key of the property.
     */
    private void setProperty(List<String> variable, String description) {
        if(variable != null && !variable.isEmpty()) {
            properties.put(description, variable.toString());
        }
    }

    /**
     * Sets the given long property, if it has a valid value (not null and not empty).
     * @param variable The value for the property.
     * @param description The description and key of the property.
     */
    private void setProperty(Long variable, String description) {
        if(variable != null) {
            properties.put(description, variable.toString());
        }
    }
}
