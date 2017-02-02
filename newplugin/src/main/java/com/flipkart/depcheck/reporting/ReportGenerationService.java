package com.flipkart.depcheck.reporting;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flipkart.depcheck.models.Dependency;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by prasanth.narra on 19/01/17.
 */
public class ReportGenerationService {
    private ObjectMapper mapper;
    private ObjectWriter writer;

    public ReportGenerationService() {
        mapper = new ObjectMapper();
        writer = mapper.writer(new DefaultPrettyPrinter());
    }

    public void generateReport(List<Dependency> nonWhiteListedDeps, String reportDirectory, String reportFormat, String projectName) throws IOException {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        writer.writeValue(Paths.get(reportDirectory,projectName.concat("."+reportFormat.toLowerCase())).toFile(), ImmutableMap.of("projectName",projectName,"hasNonWhiteListedDependencies",nonWhiteListedDeps.size()>0,"timeStamp",format.format(date),"nonWhiteListedDependencies",nonWhiteListedDeps));
    }
}
