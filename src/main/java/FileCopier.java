import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class FileCopier {

    private static int fileCounter;
    private static ExecutorService pool;

    public FileCopier() {
        fileCounter = 0;
    }

    public void copyDir(File src, File destination, String fileNamePattern) {
        if (src.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            List<String> filesList = Arrays.stream(src.listFiles())
                    .map(File::getName)
                    .filter(n -> n.matches(fileNamePattern))
                    .collect(Collectors.toList());

            filesList.forEach(n -> {
                File destFile = new File(destination, n);
                File srcFile = new File(src, n);
                copyDir(srcFile, destFile, fileNamePattern);
            });

        } else {
            copyFile(src, destination);
        }
    }

    public synchronized static int getFileCounter() {
        return fileCounter;
    }

    public static void resetFileCounter(){
        fileCounter = 0;
    }

    static synchronized void incrementFileCounter(){
        ++fileCounter;
    }

    static ExecutorService getPool(){
        return pool;
    }

    private void copyFile(File srcFile, File destinationFile) {
        if(pool == null || pool.isShutdown()){
            pool = Executors.newFixedThreadPool(ThreadParams.getThreadAmount());
        }

        pool.execute(new FileCopierThread(srcFile, destinationFile));
    }
}
