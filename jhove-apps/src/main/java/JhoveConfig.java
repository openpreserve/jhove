/**
 * 
 */


import java.io.File;

/**
 * @author cfw
 *
 */
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public final class JhoveConfig {
	// Something like this
	// NOTE the --config and other flag additions
    @Option(names = { "-c", "--config" },
    		description = "Path to an alternative jhove config file.")
    File config;
    @Option(names = { "-k", "--checksum" },
    		description = "Path to an alternative jhove config file.")
    boolean isCheckum;
    @Option(names = { "-m", "--module" },
    		description = "Name of the JHOVE module to invoke.")
    String moduleName;
    // I'd like to see us go multi file if we can
    @Parameters(arity = "0..*", paramLabel = "FILE", description = "Directories, files or URIs to process.")
    File[] toProcess;
}
