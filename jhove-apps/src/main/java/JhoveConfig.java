
/**
 * 
 */

import java.io.File;

import edu.harvard.hul.ois.jhove.JhoveBase;
/**
 * @author cfw
 * @author Alfred Wutschka
 *
 */
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public final class JhoveConfig {
	//TODO a lot of tbd in here
	@Option(names = { "-c", "--config" }, description = "Path to an alternative jhove config file.")
	String configFile = JhoveBase.getConfigFileFromProperties();
	@Option(names = { "-x", "--x-tbd" }, description = "TBD")
	String saxClass = JhoveBase.getSaxClassFromProperties();
	@Option(names = { "-e", "--encoding" }, description = "TBD")
	String encoding = null;
	@Option(names = { "-t", "--t-tbd" }, description = "TBD")
	String tempDir = null;
	@Option(names = { "-b", "--buffer" }, description = "Buffer size for buffered I/O.")
	int bufferSize = -1;
	@Option(names = { "-m", "--module" }, description = "Name of the JHOVE module to invoke.")
	String moduleName = null;
	@Option(names = { "-h", "--output-handler" }, description = "TBD")
	String handlerName = null;
	@Option(names = { "-H", "--about-handler" }, description = "TBD")
	String aboutHandler = null;
	@Option(names = { "-l", "--l-tbd" }, description = "TBD")
	String logLevel = null;
	@Option(names = { "-o", "--output" }, description = "Outputfile Pathname.")
	String outputFile = null;
	@Option(names = { "-k", "--checksum" }, description = "Calculate checksums.")
	boolean checksum = false;
	@Option(names = { "-r", "--raw" }, description = "Display raw numeric flags.")
	boolean showRaw = false;
	@Option(names = { "-s", "--signature" }, description = "Check internal signatures only.")
	boolean signature = false;

	// I'd like to see us go multi file if we can
	@Parameters(arity = "0..*", paramLabel = "FILE", description = "Directories, files or URIs to process.")
	String[] dirFileOrUri = null;
}
