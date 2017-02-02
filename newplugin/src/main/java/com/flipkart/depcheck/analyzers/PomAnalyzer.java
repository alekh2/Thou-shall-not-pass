package com.flipkart.depcheck.analyzers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.depcheck.models.BackendResponse;
import com.flipkart.depcheck.exceptions.BadRequestException;
import com.flipkart.depcheck.models.Dependency;
import com.jcabi.aether.Aether;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by prasanth.narra on 17/01/17.
 */
public class PomAnalyzer implements Analyzer {

    private ObjectMapper objectMapper;

    public PomAnalyzer(){
        objectMapper = new ObjectMapper();
    }

    public boolean isAcceptable(File file) {
        return file.getName().equals("pom.xml");
    }

    public String getName() {
        return "POM ANALYZER";
    }

    public boolean isEnabled() {
        return true;
    }

    public List<Dependency> getAllDependencies(File file) throws IOException, XmlPullParserException, DependencyResolutionException {
        MavenProject proj = loadProject(file);
        proj.setRemoteArtifactRepositories(
                Arrays.asList(
                        (ArtifactRepository) new MavenArtifactRepository(
                                "maven-central", "http://repo1.maven.org/maven2/", new DefaultRepositoryLayout(),
                                new ArtifactRepositoryPolicy(), new ArtifactRepositoryPolicy()
                        )
                )
        );
        Aether aether = new Aether(proj, new File("/Users/alekh.raj/dev/test/DepCheck/logs/repo"));
        List<org.apache.maven.model.Dependency> dependencies = proj.getDependencies();
        Iterator<org.apache.maven.model.Dependency> it = dependencies.iterator();
        List<Dependency> allDependencies = new ArrayList();
        while (it.hasNext()) {
            org.apache.maven.model.Dependency depend = it.next();
            final Collection<Artifact> deps = aether.resolve(
                    new DefaultArtifact(depend.getGroupId(), depend.getArtifactId(), depend.getClassifier(), depend.getType(), depend.getVersion()),
                    JavaScopes.RUNTIME
            );
            Iterator<Artifact> artIt = deps.iterator();
            while (artIt.hasNext()) {
                Artifact art = artIt.next();
                allDependencies.add(new Dependency(art.getGroupId(),art.getArtifactId(),art.getVersion(),art.getExtension()));
            }
        }
        return allDependencies;
    }

    public List<Dependency> getNonWhitelistedDependencies(List<Dependency> allDependencies) throws BadRequestException, IOException {
        String response = "{\"hasNonWhiteListedDependencies\":\"true\",\"nonWhiteListedDependencies\":[{\"vendor\":\"org.slf4j\",\"product\":\"slf4j-api\",\"version\":\"1.7.21\",\"type\":\"jar\"}]}";//HttpUtil.hit("", HttpMethod.POST,"");
//        String response = "{\"hasNonWhiteListedDependencies\":\"false\",\"nonWhiteListedDependencies\":[]}";
        for(Dependency dep : allDependencies){
            if(dep.getProduct().equals("org.hamcrest")) {
                response = "{\"hasNonWhiteListedDependencies\":\"true\",\"nonWhiteListedDependencies\":[{\"vendor\":\"com.h2database\",\"product\":\"h2\",\"version\":\"1.3.176\",\"type\":\"jar\"}]}";//HttpUtil.hit("", HttpMethod.POST,"");
                break;
            }
        }
        BackendResponse backendResponse = objectMapper.readValue(response, BackendResponse.class);
        return backendResponse.getNonWhiteListedDependencies();
    }

    private MavenProject loadProject(File pomFile) throws IOException, XmlPullParserException {
        MavenProject ret = null;
        MavenXpp3Reader mavenReader = new MavenXpp3Reader();
        if (pomFile != null && pomFile.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(pomFile);
                Model model = mavenReader.read(reader);
                model.setPomFile(pomFile);
                ret = new MavenProject(model);
            }
            finally {
                reader.close();
            }
        }
        return ret;
    }
}
