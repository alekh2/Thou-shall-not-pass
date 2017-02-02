package com.flipkart.depcheck.app;

import com.flipkart.depcheck.engine.Engine;
import com.flipkart.depcheck.exceptions.InvalidScanPathException;
import com.flipkart.depcheck.exceptions.NothingToScanException;
import com.flipkart.depcheck.models.Dependency;
import com.flipkart.depcheck.reporting.ReportGenerationService;
import lombok.AllArgsConstructor;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by prasanth.narra on 11/01/17.
 */
@AllArgsConstructor
public class App {

    private CliParser cliParser;
    private Engine engine;
    private ReportGenerationService reportGenerationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String args[]) throws IOException, ParseException, NothingToScanException, InvalidScanPathException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException, ExecutionException {
            String arguments[] = new String[8];
            arguments[0] = "-scan";
            arguments[1] = "/Users/alekh.raj/Documents/creating_plugin/newplugin/";
            arguments[2] = "-out";
            arguments[3] = "/Users/alekh.raj/Documents/creating_plugin/newplugin/";
            arguments[4] = "-project";
            arguments[5] = "Test";
            arguments[6] = "-exclude";
            arguments[7] = "/Users/alekh.raj/Documents/creating_plugin/newplugin/work";

        App app = new App(new CliParser(),new Engine(),new ReportGenerationService());
        app.report(app.analyze(arguments));
        return;
    }

    private void report(List<Dependency> dependencies) throws IOException {
        reportGenerationService.generateReport(dependencies,cliParser.getReportDirectory(), cliParser.getReportFormat(), cliParser.getProjectName());
    }

    public List<Dependency> analyze(String args[]) throws FileNotFoundException, ParseException, NothingToScanException, InvalidScanPathException, IllegalAccessException, InvocationTargetException, InstantiationException, InterruptedException, ExecutionException {
        cliParser.parse(args);
        if(!cliParser.isRunScan())
            throw new NothingToScanException();
        engine.initialize(args);
        return engine.scanAndAnalyze(cliParser.getScanFiles(),
                cliParser.getExcludeList(), cliParser.getSymLinkDepth());
    }
}
