package com.flipkart.depcheck.constants;

/**
 * Created by prasanth.narra on 11/01/17.
 */
public class Arguments {

    /**
     * The long CLI argument name specifying the directory/file to scan.
     */
    public static final String SCAN = "scan";
    /**
     * The short CLI argument name specifying the directory/file to scan.
     */
    public static final String SCAN_SHORT = "s";
    /**
     * The long CLI argument name specifying the directory to write the
     * reports to.
     */
    public static final String OUT = "out";
    /**
     * The short CLI argument name specifying the directory to write the
     * reports to.
     */
    public static final String OUT_SHORT = "o";
    /**
     * The long CLI argument name specifying the output format to write the
     * reports to.
     */
    public static final String OUTPUT_FORMAT = "format";
    /**
     * The short CLI argument name specifying the output format to write the
     * reports to.
     */
    public static final String OUTPUT_FORMAT_SHORT = "f";
    /**
     * The long CLI argument name specifying the name of the project to be
     * scanned.
     */
    public static final String PROJECT = "project";
    /**
     * The long CLI argument name asking for help.
     */
    public static final String HELP = "help";
    /**
     * The long CLI argument name asking for advanced help.
     */
    public static final String ADVANCED_HELP = "advancedHelp";
    /**
     * The short CLI argument name asking for help.
     */
    public static final String HELP_SHORT = "h";
    /**
     * The long CLI argument name asking for the version.
     */
    public static final String VERSION_SHORT = "v";
    /**
     * The short CLI argument name asking for the version.
     */
    public static final String VERSION = "version";
    /**
     * The short CLI argument name indicating the connection timeout.
     */
    public static final String CONNECTION_TIMEOUT_SHORT = "c";
    /**
     * The CLI argument name indicating the connection timeout.
     */
    public static final String CONNECTION_TIMEOUT = "connectiontimeout";
    /**
     * The CLI argument name for setting the location of the data directory.
     */
    public static final String VERBOSE_LOG = "log";
    /**
     * The short CLI argument name for setting the location of the data
     * directory.
     */
    public static final String VERBOSE_LOG_SHORT = "l";

    /**
     * The CLI argument name for setting the depth of symbolic links that
     * will be followed.
     */
    public static final String SYM_LINK_DEPTH = "symLink";
    /**
     * The CLI argument name for setting the location of the suppression
     * file.
     */
    public static final String SUPPRESSION_FILE = "suppression";
    /**
     * The CLI argument name for setting the location of the hint file.
     */
    public static final String HINTS_FILE = "hints";
    /**
     * Disables the Jar Analyzer.
     */
    public static final String DISABLE_JAR = "disableJar";
    /**
     * Disables the Archive Analyzer.
     */
    public static final String DISABLE_ARCHIVE = "disableArchive";
    /**
     * Disables the Python Distribution Analyzer.
     */
    public static final String DISABLE_PY_DIST = "disablePyDist";
    /**
     * Disables the Python Package Analyzer.
     */
    public static final String DISABLE_PY_PKG = "disablePyPkg";
    /**
     * Disables the Python Package Analyzer.
     */
    public static final String DISABLE_COMPOSER = "disableComposer";
    /**
     * Disables the Ruby Gemspec Analyzer.
     */
    public static final String DISABLE_RUBYGEMS = "disableRubygems";
    /**
     * Disables the Autoconf Analyzer.
     */
    public static final String DISABLE_AUTOCONF = "disableAutoconf";
    /**
     * Disables the Cmake Analyzer.
     */
    public static final String DISABLE_CMAKE = "disableCmake";
    /**
     * Disables the Assembly Analyzer.
     */
    public static final String DISABLE_ASSEMBLY = "disableAssembly";
    /**
     * Disables the Ruby Bundler Audit Analyzer.
     */
    public static final String DISABLE_BUNDLE_AUDIT = "disableBundleAudit";
    /**
     * Disables the Nuspec Analyzer.
     */
    public static final String DISABLE_NUSPEC = "disableNuspec";
    /**
     * Disables the Central Analyzer.
     */
    public static final String DISABLE_CENTRAL = "disableCentral";
    /**
     * Disables the Nexus Analyzer.
     */
    public static final String DISABLE_NEXUS = "disableNexus";
    /**
     * Disables the OpenSSL Analyzer.
     */
    public static final String DISABLE_OPENSSL = "disableOpenSSL";
    /**
     * Disables the Node.js Package Analyzer.
     */
    public static final String DISABLE_NODE_JS = "disableNodeJS";
    /**
     * The URL of the nexus server.
     */
    public static final String NEXUS_URL = "nexus";
    /**
     * Whether or not the defined proxy should be used when connecting to
     * Nexus.
     */
    public static final String NEXUS_USES_PROXY = "nexusUsesProxy";
    /**
     * Exclude path argument.
     */
    public static final String EXCLUDE = "exclude";
}
