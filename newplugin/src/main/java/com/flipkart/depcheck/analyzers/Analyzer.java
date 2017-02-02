package com.flipkart.depcheck.analyzers;

import com.flipkart.depcheck.models.Dependency;

import java.io.File;
import java.util.List;

/**
 * Created by prasanth.narra on 17/01/17.
 */
public interface Analyzer {

    public boolean isAcceptable(File file);

    public String getName();

    public boolean isEnabled();

    public List<Dependency> getAllDependencies(File file) throws Exception;

    public List<Dependency> getNonWhitelistedDependencies(List<Dependency> allDependencies) throws Exception;
}
