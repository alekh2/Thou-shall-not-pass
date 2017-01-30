package ABC.newplugin;

/**
 * Created by alekh.raj on 30/01/17.
 */
public class FileTester {
    private String filePath;

    public FileTester(String filePath){
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean test_path(String filePath){
        return filePath == getFilePath();
    }
}
