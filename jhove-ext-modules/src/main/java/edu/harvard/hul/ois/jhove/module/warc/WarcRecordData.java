package edu.harvard.hul.ois.jhove.module.warc;

import java.util.LinkedList;
import java.util.List;

import org.jwat.common.HeaderLine;
import org.jwat.common.HttpHeader;
import org.jwat.common.Payload;
import org.jwat.common.PayloadWithHeaderAbstract;
import org.jwat.warc.WarcConcurrentTo;
import org.jwat.warc.WarcHeader;
import org.jwat.warc.WarcRecord;

/**
 * Copied from JHOVE2 WARC module.
 * 
 * This class is a wrapper for the information available in an WARC record.
 * Since the WARC reader is not persistent its data must be moved to a simpler
 * data class which can be persisted instead.
 *
 * Note: Some populate methods currently do not include any functionality.
 * However they are included for backwards compatibility in case the ISO
 * standard changes and extra properties are required.
 *
 * @author nicl
 */
public class WarcRecordData {

	/** Start offset of record in input stream. */
	protected Long startOffset;
    /** Number of bytes consumed validating record. */
    protected Long consumed;

    /** WARC version read from header. */
    protected String warcVersionStr;

    /** WARC-Type read from header. */
    protected String warcType;
    /** WARC-Filename read from header. */
    protected String warcFilename;
    /** WARC-Record-Id read from header. */
    protected String warcRecordId;
    /** WARC-Date read from header. */
    protected String warcDate;
    /** Content-Length read from header. */
    protected String contentLength;
    /** Content-type read from header. */
    protected String contentType;
    /** WARC-Truncated read from header. */
    protected String warcTruncated;
    /** WARC-IP-Address read from header. */
    protected String warcIpAddress;
    /** List of WARC-Concurrent-To read from header. */
    protected List<String> warcConcurrentToList;
    /** WARC-Refers-To read from header. */
    protected String warcRefersTo;
    /** WARC-Target-URI read from header. */
    protected String warcTargetUri;
    /** WARC-Warcinfo-ID read from header. */
    protected String warcWarcinfoId;
    /** WARC-Identified-Payload-Type read from header. */
    protected String warcIdentifiedPayloadType;
    /** WARC-Profile read from header. */
    protected String warcProfile;
    /** WARC-Segment-Number read from header. */
    protected String warcSegmentNumber;
    /** WARC-Segment-Origin-ID read from header. */
    protected String warcSegmentOriginId;
    /** WARC-Segment-Total-Length read from header. */
    protected String warcSegmentTotalLength;

    /** Block digest read from header. */
    protected String warcBlockDigest;
    /** Block digest algorithm read from header. */
    protected String warcBlockDigestAlgorithm;
    /** Block digest encoding auto-detected from digest and algorithm. */
    protected String warcBlockDigestEncoding;
    /** Payload digest read from header. */
    protected String warcPayloadDigest;
    /** Payload digest algorithm read from header. */
    protected String warcPayloadDigestAlgorithm;
    /** Payload digest encoding auto-detected from digest and algorithm. */
    protected String warcPayloadDigestEncoding;

    /** Computed block digest. */
    protected String computedBlockDigest;
    /** Computed block digest algorithm. */
    protected String computedBlockDigestAlgorithm;
    /** Computed block digest encoding. */
    protected String computedBlockDigestEncoding;
    /** Computed payload digest, if applicable. */
    protected String computedPayloadDigest;
    /** Computed payload digest algorithm, if applicable. */
    protected String computedPayloadDigestAlgorithm;
    /** Computed payload digest encoding, if applicable. */
    protected String computedPayloadDigestEncoding;

    /** WARC-Record-Id scheme used. */
    protected String recordIdScheme;

    /** Boolean indicating whether this record is compliant or not. */
    protected Boolean bIsNonCompliant;
    /** Boolean indicating whether the block digest is valid or not. */
    protected Boolean isValidBlockDigest;
    /** Boolean indicating whether the payload digest is valid or not. */
    protected Boolean isValidPayloadDigest;

    /* Does the record have a payload. */
    protected Boolean bHasPayload;
    /** Payload length, without payload header (version block/HTTP header). */
    protected String payloadLength;

    /** IP vresion of WARC-IP-Address (4 or 6). */
    protected String ipVersion;

    /** Result-code read from HTTP header, if present. */
    protected String resultCode;
    /** Protocol version read from HTTP header, if present. */
    protected String protocolVersion;
    /** Content-type read from HTTP header, if present. */
    protected String protocolContentType;
    /** Server header entry read from HTTP header, if present. */
    protected String protocolServer;
    /** User-Agent header entry read from HTTP header, if present. */
    protected String protocolUserAgent;

    /**
     * Constructor required by the persistence layer.
     */
    public WarcRecordData() {
    }

    /**
     * Constructs an object using the data in the <code>WarcRecord</code>
     * object.
     * @param record parsed WARC record
     */
    public WarcRecordData(WarcRecord record) {
    	if (record == null) {
    		throw new IllegalArgumentException(MessageConstants.ERR_RECORD_DATA_NULL);
    	}
    	WarcHeader header = record.header;
        startOffset = record.getStartOffset();
        consumed = record.getConsumed();
        if (header.bValidVersionFormat) {
            this.warcVersionStr = header.versionStr;
        }
        this.warcType = header.warcTypeStr;
        this.warcFilename = header.warcFilename;
        this.warcRecordId = header.warcRecordIdStr;
        this.warcDate = header.warcDateStr;
        this.contentLength = header.contentLengthStr;
        this.contentType = header.contentTypeStr;
        this.warcTruncated = header.warcTruncatedStr;
        this.warcIpAddress = header.warcIpAddress;
        // TODO Clone List in WarcRecord's getter at some point.
        if (header.warcConcurrentToList != null && header.warcConcurrentToList.size() > 0) {
            this.warcConcurrentToList = new LinkedList<>();
            WarcConcurrentTo warcConcurrentTo;
            for (int i=0; i<header.warcConcurrentToList.size(); ++i) {
            	warcConcurrentTo = header.warcConcurrentToList.get(i);
            	if (warcConcurrentTo.warcConcurrentToStr != null) {
            		this.warcConcurrentToList.add(warcConcurrentTo.warcConcurrentToStr);
            	}
            }
        }
        this.warcRefersTo = header.warcRefersToStr;
        this.warcTargetUri = header.warcTargetUriStr;
        this.warcWarcinfoId = header.warcWarcinfoIdStr;
        this.warcIdentifiedPayloadType = header.warcIdentifiedPayloadTypeStr;
        this.warcProfile = header.warcProfileStr;
        this.warcSegmentNumber = header.warcSegmentNumberStr;
        this.warcSegmentOriginId = header.warcSegmentOriginIdStr;
        this.warcSegmentTotalLength = header.warcSegmentTotalLengthStr;
        /*
         * Warc-Block-Digest.
         */
        if (header.warcBlockDigest != null) {
            if ( header.warcBlockDigest.digestString != null
                    && header.warcBlockDigest.digestString.length() > 0) {
                warcBlockDigest = header.warcBlockDigest.digestString;
            }
            if (header.warcBlockDigest.algorithm != null
                    && header.warcBlockDigest.algorithm.length() > 0) {
                warcBlockDigestAlgorithm = header.warcBlockDigest.algorithm;
            }
            if (header.warcBlockDigest.encoding != null
                    && header.warcBlockDigest.encoding.length() > 0) {
                warcBlockDigestEncoding = header.warcBlockDigest.encoding;
            }
        }
        /*
         * Warc-Payload-Digest.
         */
        if (header.warcPayloadDigest != null) {
            if (header.warcPayloadDigest.digestString != null
                    && header.warcPayloadDigest.digestString.length() > 0) {
                warcPayloadDigest = header.warcPayloadDigest.digestString;
            }
            if (header.warcPayloadDigest.algorithm != null
                    && header.warcPayloadDigest.algorithm.length() > 0) {
                warcPayloadDigestAlgorithm = header.warcPayloadDigest.algorithm;
            }
            if (header.warcPayloadDigest.encoding != null
                    && header.warcPayloadDigest.encoding.length() > 0) {
                warcPayloadDigestEncoding = header.warcPayloadDigest.encoding;
            }
        }
        /*
         * Computed-Block-Digest.
         */
        if (record.computedBlockDigest != null) {
            if ( record.computedBlockDigest.digestString != null
                    && record.computedBlockDigest.digestString.length() > 0) {
                computedBlockDigest = record.computedBlockDigest.digestString;
            }
            if (record.computedBlockDigest.algorithm != null
                    && record.computedBlockDigest.algorithm.length() > 0) {
                computedBlockDigestAlgorithm = record.computedBlockDigest.algorithm;
            }
            if (record.computedBlockDigest.encoding != null
                    && record.computedBlockDigest.encoding.length() > 0) {
                computedBlockDigestEncoding = record.computedBlockDigest.encoding;
            }
        }
        /*
         * Computed-Payload-Digest.
         */
        if (record.computedPayloadDigest != null) {
            if (record.computedPayloadDigest.digestString != null
                    && record.computedPayloadDigest.digestString.length() > 0) {
                computedPayloadDigest = record.computedPayloadDigest.digestString;
            }
            if (record.computedPayloadDigest.algorithm != null
                    && record.computedPayloadDigest.algorithm.length() > 0) {
                computedPayloadDigestAlgorithm = record.computedPayloadDigest.algorithm;
            }
            if (record.computedPayloadDigest.encoding != null
                    && record.computedPayloadDigest.encoding.length() > 0) {
                computedPayloadDigestEncoding = record.computedPayloadDigest.encoding;
            }
        }
        /*
         * Record-Id scheme.
         */
        if (warcRecordId != null) {
            int idx = warcRecordId.indexOf(':');
            if (idx >= 0) {
                if (warcRecordId.startsWith("<")) {
                    recordIdScheme = warcRecordId.substring(1, idx);
                }
                else {
                    recordIdScheme = warcRecordId.substring(0, idx);
                }
            }
        }
        /*
         * Compliance.
         */
        bIsNonCompliant = !record.isCompliant();
        isValidBlockDigest = record.isValidBlockDigest;
        isValidPayloadDigest = record.isValidPayloadDigest;
        /*
         * Payload.
         */
        bHasPayload = record.hasPayload();
        Payload payload = record.getPayload();
        HeaderLine headerLine;
        if (payload != null) {
            PayloadWithHeaderAbstract payloadHeaderWrapped = payload.getPayloadHeaderWrapped();
            HttpHeader httpHeader = null;
            if (payloadHeaderWrapped instanceof HttpHeader) {
                httpHeader = (HttpHeader)payloadHeaderWrapped;
            }
            if (httpHeader != null) {
                payloadLength = Long.toString(httpHeader.getPayloadLength());
                protocolVersion = httpHeader.httpVersion;
                switch (httpHeader.headerType) {
                    case HttpHeader.HT_RESPONSE:
                        resultCode = httpHeader.statusCodeStr;
                        protocolContentType = httpHeader.contentType;
                        headerLine = httpHeader.getHeader("server");
                        if (headerLine != null && headerLine.value != null) {
                            protocolServer = headerLine.value;
                        }
                            break;
                    case HttpHeader.HT_REQUEST:
                        headerLine = httpHeader.getHeader("user-agent");
                        if (headerLine != null && headerLine.value != null) {
                            protocolUserAgent = headerLine.value;
                        }
                        break;
                    default:
                        break;
                }
            }
            else {
                payloadLength = Long.toString(payload.getTotalLength());
            }
        }
        /*
         * IpVersion, common for several record properties.
         */
        if (header.warcInetAddress != null) {
            if (header.warcInetAddress.getAddress().length == 4) {
                ipVersion = "4";
            }
            else {
                ipVersion = "6";
            }
        }
    }
}
