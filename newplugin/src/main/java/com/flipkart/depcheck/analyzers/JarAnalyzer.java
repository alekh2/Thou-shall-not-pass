package com.flipkart.depcheck.analyzers;

import com.flipkart.depcheck.exceptions.BadRequestException;
import com.flipkart.depcheck.models.Dependency;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by prasanth.narra on 17/01/17.
 */
public class JarAnalyzer implements Analyzer {

    public boolean isAcceptable(File file) {
        return false;
    }

    public String getName() {
        return "JAR ANALYZER";
    }

    public boolean isEnabled() {
        return true;
    }

    public List<Dependency> getAllDependencies(File file) {
        return null;
    }

    public List<Dependency> getNonWhitelistedDependencies(List<Dependency> allDependencies) throws BadRequestException, IOException {
        return null;
    }
}
