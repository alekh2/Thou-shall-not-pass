package com.flipkart.depcheck.engine;

import com.flipkart.depcheck.analyzers.Analyzer;
import com.flipkart.depcheck.analyzers.AnalyzerService;
import com.flipkart.depcheck.exceptions.InvalidScanPathException;
import com.flipkart.depcheck.models.Dependency;
import org.apache.tools.ant.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by prasanth.narra on 11/01/17.
 */
public class Engine {

    private DirectoryScanner scanner;
    private AnalyzerService analyzerService;
    private List<Analyzer> analyzers;
    private ExecutorService executorService;

    public Engine() {
        scanner = new DirectoryScanner();
        analyzers = null;
        analyzerService = new AnalyzerService();
        executorService = Executors.newFixedThreadPool(10);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Engine.class);

    public List<Dependency> scanAndAnalyze(String[] pathsToScan, String[] pathsToExclude, int symLinkDepth) throws InvalidScanPathException, InterruptedException, ExecutionException {
        final Set<File> paths = scan(pathsToScan,pathsToExclude,symLinkDepth);
        Map<File,Analyzer> fileAnalyzerMap = filterAnalyzableFiles(paths);
        List<Future<List<Dependency>>> futuresList = analyzeFilteredFiles(fileAnalyzerMap);
        return collate(futuresList);
    }

    private List<Dependency> collate(List<Future<List<Dependency>>> futuresList) throws ExecutionException, InterruptedException {
        List<Dependency> nonWhiteListedDependenciesFound = new ArrayList<Dependency>();
        Set<String> hashSet = new HashSet<String>();
        for(Future<List<Dependency>> depFuture : futuresList) {
            List<Dependency> deps = depFuture.get();
            for(Dependency dep : deps)
                if(hashSet.add(dep.toString()))
                    nonWhiteListedDependenciesFound.add(dep);
        }
        return nonWhiteListedDependenciesFound;
    }

    private List<Future<List<Dependency>>> analyzeFilteredFiles(Map<File, Analyzer> fileAnalyzerMap) throws InterruptedException {
        List<Future<List<Dependency>>> futuresList = new ArrayList<Future<List<Dependency>>>();
        for(final Map.Entry entry : fileAnalyzerMap.entrySet()){
            Callable task = new Callable(){
                public List<Dependency> call() throws Exception {
                    Analyzer a = (Analyzer) entry.getValue();
                    return a.getNonWhitelistedDependencies(a.getAllDependencies((File)entry.getKey()));
                }
            };
            Future<List<Dependency>> future = executorService.submit(task);
            futuresList.add(future);
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.MINUTES);
        return futuresList;
    }

    public void initialize(String args[]) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        analyzers = analyzerService.getAnalyzers();
    }

    private Map<File, Analyzer> filterAnalyzableFiles(Set<File> paths) {
        Map<File,Analyzer> fileAnalyzerMap = new HashMap<File,Analyzer>();
        for(File file : paths)
            for(Analyzer analyzer : analyzers) {
                if(analyzer.isAcceptable(file))
                    fileAnalyzerMap.put(file,analyzer);
            }
        return fileAnalyzerMap;
    }

    private Set<File> scan(String[] pathsToScan, String[] pathsToExclude, int symLinkDepth) throws InvalidScanPathException {
        final List<String> antStylePaths = new ArrayList<String>();
        for (String file : pathsToScan) {
            final String antPath = ensureCanonicalPath(file);
            antStylePaths.add(antPath);
        }
        final Set<File> paths = new HashSet<File>();
        for (String file : antStylePaths) {
            LOGGER.debug("Scanning {}", file);
            String include = file.replace('\\', '/');
            File baseDir;
            if (include.startsWith("//")) {
                throw new InvalidScanPathException("Unable to scan paths specified by //");
            } else {
                final int pos = getLastFileSeparator(include);
                final String tmpBase = include.substring(0, pos);
                final String tmpInclude = include.substring(pos + 1);
                if (tmpInclude.indexOf('*') >= 0 || tmpInclude.indexOf('?') >= 0
                        || (new File(include)).isFile()) {
                    baseDir = new File(tmpBase);
                    include = tmpInclude;
                } else {
                    baseDir = new File(tmpBase, tmpInclude);
                    include = "**/*";
                }
            }
            scanner.setBasedir(baseDir);
            final String[] includes = {include};
            scanner.setIncludes(includes);
            scanner.setMaxLevelsOfSymlinks(symLinkDepth);
            if (symLinkDepth <= 0)
                scanner.setFollowSymlinks(false);
            if (pathsToExclude != null && pathsToExclude.length > 0)
                scanner.addExcludes(pathsToExclude);
            scanner.scan();
            if (scanner.getIncludedFilesCount() > 0) {
                for (String s : scanner.getIncludedFiles()) {
                    final File f = new File(baseDir, s);
                    LOGGER.debug("Found file {}", f.toString());
                    paths.add(f);
                }
            }
        }
        return paths;
    }

    protected String ensureCanonicalPath(String path) {
        String basePath;
        String wildCards = null;
        final String file = path.replace('\\', '/');
        if (file.contains("*") || file.contains("?")) {

            int pos = getLastFileSeparator(file);
            if (pos < 0) {
                return file;
            }
            pos += 1;
            basePath = file.substring(0, pos);
            wildCards = file.substring(pos);
        } else {
            basePath = file;
        }

        File f = new File(basePath);
        try {
            f = f.getCanonicalFile();
            if (wildCards != null) {
                f = new File(f, wildCards);
            }
        } catch (IOException ex) {
            LOGGER.warn("Invalid path '{}' was provided.", path);
            LOGGER.debug("Invalid path provided", ex);
        }
        return f.getAbsolutePath().replace('\\', '/');
    }

    private int getLastFileSeparator(String file) {
        if (file.contains("*") || file.contains("?")) {
            int p1 = file.indexOf('*');
            int p2 = file.indexOf('?');
            p1 = p1 > 0 ? p1 : file.length();
            p2 = p2 > 0 ? p2 : file.length();
            int pos = p1 < p2 ? p1 : p2;
            pos = file.lastIndexOf('/', pos);
            return pos;
        } else {
            return file.lastIndexOf('/');
        }
    }
}
