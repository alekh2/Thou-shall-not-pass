package com.flipkart.depcheck.analyzers;


import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by prasanth.narra on 17/01/17.
 */
public class AnalyzerService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AnalyzerService.class);
    private Reflections reflections;

    public AnalyzerService() {
        reflections = new Reflections("com.flipkart.depcheck.analyzers");
    }

    public List<Analyzer> getAnalyzers() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        final List<Analyzer> analyzers = new ArrayList<Analyzer>();
        Set<Class<? extends Analyzer>> subTypes = reflections.getSubTypesOf(Analyzer.class);
        for(Class<?> c : subTypes) {
            Analyzer a = (Analyzer) c.getConstructors()[0].newInstance();
            if(a.isEnabled())
                analyzers.add(a);
        }
        return analyzers;
    }
}
