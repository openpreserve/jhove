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

import edu.harvard.hul.ois.jhove.CoreMessageConstants;

/**
 * Immutable class that reads a properties file containing JHOVE release
 * details. The version number, build date and format are kept up to date by
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
        throw new AssertionError(CoreMessageConstants.EXC_PRV_CNSTRCT + this.getClass().getName());
    }

    private ReleaseDetails(final String version, final Date buildDate) {
        this.version = version;
        this.buildDate = new Date(buildDate.getTime());
    }

    /**
     * @return the JHOVE software version number
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * @return the JHOVE software build date
     */
    public Date getBuildDate() {
        return new Date(this.buildDate.getTime());
    }

    /**
     * @return the JHOVE software rights statement
     */
    @SuppressWarnings("static-method")
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
                + ((this.buildDate == null) ? 0 : this.buildDate.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
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
        if (this.buildDate == null) {
            if (other.buildDate != null)
                return false;
        } else if (!this.buildDate.equals(other.buildDate))
            return false;
        if (this.version == null) {
            if (other.version != null)
                return false;
        } else if (!this.version.equals(other.version))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ReleaseDetails [version=" + this.version + ", buildDate="
                + this.buildDate + "]";
    }

    /**
     * @return the static immutable ReleaseDetails instance
     */
    public static ReleaseDetails getInstance() {
        return INSTANCE;
    }

    private static ReleaseDetails fromPropertyResource(
            final String propertyResourceName) {
        Properties props = new Properties();
        try (InputStream is = ReleaseDetails.class.getClassLoader()
                .getResourceAsStream(propertyResourceName)) {
            if (is == null) {
                throw new IllegalStateException(CoreMessageConstants.ERR_APP_PROP_MISS
                        + propertyResourceName);
            }
            try {
                props.load(is);
            } catch (IOException e) {
                is.close();
                throw new IllegalStateException(
                        CoreMessageConstants.ERR_APP_PROP_MISS
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
        Date date = new Date();
        try {
            date = formatter.parse(props.getProperty("jhove.release.date"));
        } catch (ParseException e) {
            /**
             * Safe to ignore this exception as release simply set to new date.
             */
        }
        return new ReleaseDetails(release, date);
    }
}
