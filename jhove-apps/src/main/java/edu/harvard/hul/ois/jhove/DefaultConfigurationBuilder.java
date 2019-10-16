package edu.harvard.hul.ois.jhove;

import java.io.File;
import java.lang.reflect.Field;

/** This class creates a default configuration if no valid configuration file
 *  is found. */
public class DefaultConfigurationBuilder {

    private final static String FILE_SEP = System.getProperty ("file.separator");
    private final static String HOME_DIR = System.getProperty ("user.home");
    private final static String JHOVE_DIR = HOME_DIR + FILE_SEP + "jhove";
    
    private File configFile;
    
    
    /** Constructor. A location for the file may be specified or
     *  left null, */
    public DefaultConfigurationBuilder (File location) {
        if (location != null) {
            configFile = location;
        }
        else {
            configFile = new File (JHOVE_DIR +
                      FILE_SEP + "conf" +
                      FILE_SEP + "jhove.conf");
        }
    }

    public File getConfigFile () {
        return configFile;
    }
    
    /** We can't have a static method in an Interface and override it, so we
     *  have to get a bit ugly to fake static inheritance. The advantage of
     *  this is that only the Modules with non-null default config file
     *  parameters have to implement the defaultConfigParams static field. */
    protected String[] getDefaultConfigParameters (Class<?> c) {
        try {
            Field dcpField = c.getField("defaultConfigParams");
            return (String[]) dcpField.get(null);
        }
        catch (Exception e) {
            return new String [] {};
        }
    }
}
