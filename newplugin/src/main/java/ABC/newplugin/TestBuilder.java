package ABC.newplugin;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.FreeStyleProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;

import java.io.IOException;

/**
 * Created by alekh.raj on 27/01/17.
 */
public class TestBuilder extends Builder {
    private long time;

    TestBuilder(long time){
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        Thread.sleep(getTime());
        return true;
    }
    @Extension
    public static final class Descriptor extends BuildStepDescriptor<Builder>{
        @Override
        public String getDisplayName() {
            return "Test";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return aClass == FreeStyleProject.class;
        }
    }
}
