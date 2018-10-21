package org.bian.swaggercodegen.cmd;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import org.bian.swaggercodegen.config.CodegenConfigurator;
import org.bian.swaggercodegen.generator.BianGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.airlift.airline.Command;
import io.airlift.airline.Option;
import io.swagger.codegen.ClientOptInput;

// TODO: Auto-generated Javadoc
/**
 * User: lanwen Date: 24.03.15 Time: 20:22
 */

@Command(name = "generate", description = "Generate code with chosen lang")
public class Generate implements Runnable {

    /** The Constant LOG. */
    public static final Logger LOG = LoggerFactory.getLogger(Generate.class);

    /** The output. */
    @Option(name = {"-o", "--output"}, title = "output directory",
            description = "where to write the generated files (current dir by default)")
    private String output = "";

    /** The spec. */
    @Option(name = {"-i", "--input-spec"}, title = "spec file", required = true,
            description = "location of the swagger spec, as URL or file (required)")
    private String spec;

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

        // attempt to read from config file
        CodegenConfigurator configurator = new CodegenConfigurator();

        if (isNotEmpty(spec)) {
            configurator.setInputSpec(spec);
        }
        
        configurator.setLang("bian");

        if (isNotEmpty(output)) {
            configurator.setOutputDir(output);
        }
        
        final ClientOptInput clientOptInput = configurator.toClientOptInput();

        new BianGenerator().opts(clientOptInput).generate();
    }
}
