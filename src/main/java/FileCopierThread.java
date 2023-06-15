import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;

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
            LogManager.getLogger().debug(String.format("Trying to copy file %s to %s", srcPath, dstPath));
            Files.copy(Paths.get(srcPath), Paths.get(dstPath), LinkOption.NOFOLLOW_LINKS);
            FileCopier.incrementFileCounter();
            LogManager.getLogger().debug("Copied. fileCounter: " + FileCopier.getFileCounter());
        }
        catch(IOException e){
            LogManager.getLogger().error(e.getLocalizedMessage());
        }
    }
}
