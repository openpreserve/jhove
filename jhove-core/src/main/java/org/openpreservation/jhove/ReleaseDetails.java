/**
 * 
 */
package org.openpreservation.jhove;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.App;

/**
 * Immutable class that reads a properties file containing JHOVE release
 * details. The version number, build date and format are kept up to date by the
 * Maven, which filters the properties as part of the build process.
 * 
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class ReleaseDetails {
    private static final String APPLICATION_PROPERTIES_PATH = "org/openpreservation/jhove/jhove.properties";
    private static final String RIGHTS = "Derived from software Copyright 2004-2011 "
            + "by the President and Fellows of Harvard College. "
            + "Version 1.7 to 1.11 independently released. "
            + "Version 1.12 onwards released by Open Preservation Foundation. "
            + "Released under the GNU Lesser General Public License.";

    private static final ReleaseDetails INSTANCE = fromPropertyResource(APPLICATION_PROPERTIES_PATH);

    private final String version;
    private final Date buildDate;

    private ReleaseDetails() {
        throw new AssertionError("Should never enter JhoveReleaseDetails().");
    }

    private ReleaseDetails(final String version, final Date buildDate) {
        this.version = version;
        this.buildDate = new Date(buildDate.getTime());
    }

    public String getVersion() {
        return this.version;
    }

    public Date getBuildDate() {
        return new Date(this.buildDate.getTime());
    }

    public String getRights() {
        return RIGHTS;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((buildDate == null) ? 0 : buildDate.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReleaseDetails other = (ReleaseDetails) obj;
        if (buildDate == null) {
            if (other.buildDate != null)
                return false;
        } else if (!buildDate.equals(other.buildDate))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ReleaseDetails [version=" + version + ", buildDate="
                + buildDate + "]";
    }

    public static ReleaseDetails getInstance() {
        return INSTANCE;
    }

    private static ReleaseDetails fromPropertyResource(
            final String propertyResourceName) {
        InputStream is = ReleaseDetails.class.getClassLoader()
                .getResourceAsStream(propertyResourceName);
        if (is == null) {
            throw new IllegalStateException("No application properties found: "
                    + propertyResourceName);
        }
        Properties props = new Properties();
        try {
            try {
                props.load(is);
            } catch (IOException e) {
                is.close();
                throw new IllegalStateException(
                        "No application properties found: "
                                + propertyResourceName, e);
            }
            is.close();
        } catch (IOException e) {
            // Problem closing, ignore and move on
        }
        return fromProperties(props);
    }

    private static ReleaseDetails fromProperties(final Properties props) {
        String release = props.getProperty("jhove.release.version");
        String dateFormat = props.getProperty("jhove.date.format");
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Date date;
        try {
            date = formatter.parse(props.getProperty("jhove.release.date"));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException("Couldn't parse release date.", e);
        }
        return new ReleaseDetails(release, date);
    }
}
