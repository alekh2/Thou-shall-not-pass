package ABC.newplugin;
import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.mail.FolderNotFoundException;
import java.io.FileNotFoundException;

public class HelloWorldBuilder extends Builder implements SimpleBuildStep {

    private final String pathName;
    private final String projectName;

    @DataBoundConstructor
    public HelloWorldBuilder(String pathName, String projectName) {
        this.projectName = projectName;
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }

    public String getProjectName() {
        return projectName;
    }

    @Override
    public void perform(Run<?,?> build, FilePath workspace, Launcher launcher, TaskListener listener) throws FileNotFoundException {
        FileTester fileTester = new FileTester("/home/abc");

        if (fileTester.test_path("/home/bbc")){
            listener.getLogger().println("Path is correct!!");
        }

        else {
            throw new FileNotFoundException("file nahi mil rha hai");
        }

        //listener.getLogger().println(getProjectName() + "\n" + getPathName());

        //throw new FileNotFoundException("file nahi mil rha hai");
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
       public String getDisplayName() {
            return "Say hello world";
        }
    }
}

