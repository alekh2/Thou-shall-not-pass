package com.flipkart.depcheck.app;

import com.flipkart.depcheck.constants.Arguments;
import com.flipkart.depcheck.reporting.Format;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by prasanth.narra on 11/01/17.
 */
public class CliParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(CliParser.class);
    private CommandLine line;
    public boolean isValid = true;

    public void parse(String[] args) throws ParseException, FileNotFoundException {
        final CommandLineParser parser = new DefaultParser();
        final Options options = createCommandLineOptions();
        this.line = parser.parse(options, args);
        if(line != null)
            validateArgs();
    }

    private Options createCommandLineOptions() {
        final Options options = new Options();
        final Option help = new Option(Arguments.HELP_SHORT, Arguments.HELP, false,
                "Print this message.");
        final Option advancedHelp = Option.builder().longOpt(Arguments.ADVANCED_HELP)
                .desc("Print the advanced help message.").build();
        final Option version = new Option(Arguments.VERSION_SHORT, Arguments.VERSION,
                false, "Print the version information.");
        final Option projectName = Option.builder().hasArg().argName("name").longOpt(Arguments.PROJECT)
                .desc("The name of the project being scanned. This is a required Arguments.")
                .build();
        final Option path = Option.builder(Arguments.SCAN_SHORT).argName("path").hasArg().longOpt(Arguments.SCAN)
                .desc("The path to scan - this option can be specified multiple times. Ant style"
                        + " paths are supported (e.g. path/**/*.jar).")
                .build();
        final Option excludes = Option.builder().argName("pattern").hasArg().longOpt(Arguments.EXCLUDE)
                .desc("Specify and exclusion pattern. This option can be specified multiple times"
                        + " and it accepts Ant style excludsions.")
                .build();
        final Option out = Option.builder(Arguments.OUT_SHORT).argName("path").hasArg().longOpt(Arguments.OUT)
                .desc("The folder to write reports to. This defaults to the current directory. "
                        + "It is possible to set this to a specific file name if the format Arguments is not set to ALL.")
                .build();
        final Option outputFormat = Option.builder(Arguments.OUTPUT_FORMAT_SHORT).argName("format").hasArg().longOpt(Arguments.OUTPUT_FORMAT)
                .desc("The output format to write to (XML, HTML, VULN, ALL). The default is HTML.")
                .build();
        final Option verboseLog = Option.builder(Arguments.VERBOSE_LOG_SHORT).argName("file").hasArg().longOpt(Arguments.VERBOSE_LOG)
                .desc("The file path to write verbose logging information.")
                .build();
        final Option symLinkDepth = Option.builder().argName("depth").hasArg().longOpt(Arguments.SYM_LINK_DEPTH)
                .desc("Sets how deep nested symbolic links will be followed; 0 indicates symbolic links will not be followed.")
                .build();
        final Option suppressionFile = Option.builder().argName("file").hasArg().longOpt(Arguments.SUPPRESSION_FILE)
                .desc("The file path to the suppression XML file.")
                .build();
        final Option hintsFile = Option.builder().argName("file").hasArg().longOpt(Arguments.HINTS_FILE)
                .desc("The file path to the hints XML file.")
                .build();
        //This is an option group because it can be specified more then once.
        final OptionGroup og = new OptionGroup();
        og.addOption(path);
        final OptionGroup exog = new OptionGroup();
        exog.addOption(excludes);
        options.addOptionGroup(og)
                .addOptionGroup(exog)
                .addOption(projectName)
                .addOption(out)
                .addOption(outputFormat)
                .addOption(version)
                .addOption(help)
                .addOption(advancedHelp)
                .addOption(symLinkDepth)
                .addOption(verboseLog)
                .addOption(suppressionFile)
                .addOption(hintsFile);
        return options;
    }

    private void validateArgs() throws FileNotFoundException, ParseException {
        validatePathExists(getScanFiles(), Arguments.SCAN);
        validatePathExists(getReportDirectory(), Arguments.OUT);
        if (!line.hasOption(Arguments.PROJECT)) {
            throw new ParseException("Missing '" + Arguments.PROJECT + "' argument; the scan cannot be run without the an project name.");
        }
        if (line.hasOption(Arguments.OUTPUT_FORMAT)) {
            final String format = line.getOptionValue(Arguments.OUTPUT_FORMAT);
            try {
                Format.valueOf(format);
            } catch (IllegalArgumentException ex) {
                final String msg = String.format("An invalid 'format' of '%s' was specified. "
                        + "Supported output formats are XML, HTML, VULN, or ALL", format);
                throw new ParseException(msg);
            }
        }
        if (line.hasOption((Arguments.SYM_LINK_DEPTH))) {
            try {
                final int i = Integer.parseInt(line.getOptionValue(Arguments.SYM_LINK_DEPTH));
                if (i < 0) {
                    throw new ParseException("Symbolic Link Depth (symLink) must be greater than zero.");
                }
            } catch (NumberFormatException ex) {
                throw new ParseException("Symbolic Link Depth (symLink) is not a number.");
            }
        }
    }

    private void validatePathExists(String[] paths, String argName) throws FileNotFoundException {
        for (String path : paths) {
            validatePathExists(path, argName);
        }
    }

    private void validatePathExists(String path, String argumentName) throws FileNotFoundException {
        if (path == null) {
            isValid = false;
            final String msg = String.format("Invalid '%s' argument: null", argumentName);
            throw new FileNotFoundException(msg);
        } else if (!path.contains("*") && !path.contains("?")) {
            File f = new File(path);
            if ("o".equalsIgnoreCase(argumentName.substring(0, 1)) && !"ALL".equalsIgnoreCase(this.getReportFormat())) {
                final String checkPath = path.toLowerCase();
                if (checkPath.endsWith(".html") || checkPath.endsWith(".xml") || checkPath.endsWith(".htm")) {
                    if (f.getParentFile() == null) {
                        f = new File(".", path);
                    }
                    if (!f.getParentFile().isDirectory()) {
                        isValid = false;
                        final String msg = String.format("Invalid '%s' argument: '%s'", argumentName, path);
                        throw new FileNotFoundException(msg);
                    }
                }
            } else if (!f.exists()) {
                isValid = false;
                final String msg = String.format("Invalid '%s' argument: '%s'", argumentName, path);
                throw new FileNotFoundException(msg);
            }
        } else if (path.startsWith("//") || path.startsWith("\\\\")) {
            isValid = false;
            final String msg = String.format("Invalid '%s' argument: '%s'%nUnable to scan paths that start with '//'.", argumentName, path);
            throw new FileNotFoundException(msg);
        } else if ((path.endsWith("/*") && !path.endsWith("**/*")) || (path.endsWith("\\*") && path.endsWith("**\\*"))) {
            final String msg = String.format("Possibly incorrect path '%s' from argument '%s' because it ends with a slash star; "
                    + "dependency-check uses ant-style paths", path, argumentName);
            LOGGER.warn(msg);
        }
    }

    public String getReportDirectory() {
        return line.getOptionValue(Arguments.OUT);
    }

    public String[] getScanFiles() {
        return line.getOptionValues(Arguments.SCAN);
    }

    public String getReportFormat() {
        return line.getOptionValue(Arguments.OUTPUT_FORMAT, Format.JSON.name());
    }

    public boolean isRunScan() {
        return (line != null) && isValid && line.hasOption(Arguments.SCAN);
    }

    public String[] getExcludeList() {
        return line.getOptionValues(Arguments.EXCLUDE);
    }

    public int getSymLinkDepth() {
        int value = 0;
        try {
            value = Integer.parseInt(line.getOptionValue(Arguments.SYM_LINK_DEPTH, "0"));
            if (value < 0) {
                value = 0;
            }
        } catch (NumberFormatException ex) {
            LOGGER.debug("Symbolic link was not a number");
        }
        return value;
    }

    public String getProjectName() {
        return line.getOptionValue(Arguments.PROJECT);
    }
}
