import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.LinkOption;

class FileCopierThread implements Runnable {
    private String srcPath;
    private String dstPath;

    FileCopierThread(File srcFile, File dstFile){
        this.srcPath = srcFile.getAbsolutePath();
        this.dstPath = dstFile.getAbsolutePath();
    }

    FileCopierThread(String srcPath, String dstPath){
        this.srcPath = srcPath;
        this.dstPath = dstPath;
    }

    @Override
    public void run(){
        try {
            Files.copy(Paths.get(srcPath), Paths.get(dstPath), LinkOption.NOFOLLOW_LINKS);
        }
        catch(IOException e){
            //TODO: Logger - Logger z Javy czy z log4j?
            e.getLocalizedMessage();
            e.printStackTrace();
        }
    }
}