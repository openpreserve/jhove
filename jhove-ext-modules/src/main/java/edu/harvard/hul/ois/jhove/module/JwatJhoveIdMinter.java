package edu.harvard.hul.ois.jhove.module;

import org.jwat.common.Diagnosis;
import org.jwat.common.DiagnosisType;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * A class to mint JhoveMessage IDs from Jwat Diagnosis objects.
 * 
 * IDs are of the form: <prefix><delimeter><id_number>.
 * Prefix and delimeter are set in the constructor.
 * The ID number is derived from the DiagnosisType ordinal.
 */
public class JwatJhoveIdMinter {
    /**
     * The default delimeter for message IDs minted
     */
    public static final String DEFAULT_DELIMETER = "-";
    public static final int DEFAULT_OFFSET = 100;
    /**
     * The prefix for message IDs minted, usually the full module name
     */
    private final String prefix;
    private final int offset;

    /**
     * The delimeter for message IDs minted. defaults to {@link #DEFAULT_DELIMETER}
     */

    /**
     * Enum constructor.
     * 
     * @param prefix The prefix for message IDs minted, usually the full module name
     * @param prefix The delimeter for message IDs minted
     */
    private JwatJhoveIdMinter(final String prefix, final String delimeter, final int offset) {
        this.prefix = prefix.toUpperCase() + delimeter;
        this.offset = offset;
    }

    public final JhoveMessage mint(Diagnosis diagnosis) {
        return JhoveMessages.getMessageInstance(this.getID(diagnosis.type), diagnosis.type.name(),
                createSubMessage(diagnosis));
    }

    public static final JwatJhoveIdMinter getInstance(final String prefix) {
        return JwatJhoveIdMinter.getInstance(prefix, DEFAULT_DELIMETER);
    }

    public static final JwatJhoveIdMinter getInstance(final String prefix, final String delimeter) {
        return JwatJhoveIdMinter.getInstance(prefix, delimeter, DEFAULT_OFFSET);
    }

    public static final JwatJhoveIdMinter getInstance(final String prefix, final String delimeter, final int offset) {
        if (prefix == null || prefix.isEmpty()) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }
        if (delimeter == null || delimeter.isEmpty()) {
            throw new IllegalArgumentException("Delimeter cannot be null or empty");
        }
        return new JwatJhoveIdMinter(prefix, delimeter, offset);
    }

    private final String getID(DiagnosisType type) {
        return this.prefix + (type.ordinal() + this.offset);
    }

    private static String createSubMessage(final Diagnosis d) {
        StringBuilder res = new StringBuilder();
        res.append("Entity: ").append(d.entity);
        for (String i : d.information) {
            res.append(", ").append(i);
        }
        return res.toString();
    }
}
