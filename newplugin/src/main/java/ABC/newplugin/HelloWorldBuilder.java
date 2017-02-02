package ABC.newplugin;

import com.flipkart.depcheck.app.App;
import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import hudson.util.FormValidation;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class HelloWorldBuilder extends Builder implements SimpleBuildStep {

    private String pathName;
    private String projectName;
    private String logDirName;

    @DataBoundConstructor
    public HelloWorldBuilder(String pathName, String projectName, String logDirName) {
        this.projectName = projectName;
        this.pathName = pathName;
        this.logDirName = logDirName;
    }

    public String getPathName() {
        return pathName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLogDirName() {
        return logDirName;
    }

    public void setLogDirName(String logDirName) {
        this.logDirName = logDirName;
    }

    @Override
    public void perform(Run<?,?> build, FilePath workspace, Launcher launcher, TaskListener listener) throws FileNotFoundException {


        String arguments[] = new String[8];
        arguments[0] = "-scan";
        arguments[1] = getPathName();
        arguments[2] = "-out";
        arguments[3] = getLogDirName();
        arguments[4] = "-project";
        arguments[5] = getProjectName();
        arguments[6] = "-exclude";
        arguments[7] = "/Users/alekh.raj/Documents/creating_plugin/newplugin/work";


        try{
            App.main(arguments);
        } catch (RuntimeException e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sw.toString(); // st
            listener.getLogger().println("Exception occured : " +sw.toString());
            throw new FileNotFoundException("Exception occured : ");
        } catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sw.toString(); // st
            listener.getLogger().println("Exception occured : " +sw.toString());
            throw new FileNotFoundException("Exception occured : ");
        }

    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
        public String getDisplayName() {
            return "Dependency Checker";
        }

        public FormValidation doCheckPathName(@QueryParameter String pathName){
            Path path = Paths.get(pathName);
            if (Files.notExists(path)) {
                return FormValidation.error("The mentioned directory does not exist!");
            }
            else return FormValidation.ok();
        }

        public FormValidation doCheckLogDirName(@QueryParameter String logDirName){
            Path path = Paths.get(logDirName);
            if (Files.notExists(path)) {
                return FormValidation.error("The mentioned directory does not exist!");
            }
            else return FormValidation.ok();
        }

    }
}
