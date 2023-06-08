import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileCopier {

    private static int fileCounter;

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

            fileCounter += filesList.size();

        } else {
            copyFile(src, destination);
        }
    }

    public static int getFileCounter() {
        return fileCounter;
    }

    private void copyFile(File srcFile, File destinationFile) {
        try (InputStream in = new FileInputStream(srcFile);
             OutputStream out = new FileOutputStream(destinationFile)) {
            int length;
            byte[] bytes = new byte[1024];
            while ((length = in.read(bytes)) > 0) {
                out.write(bytes, 0, length);
            }
        } catch (IOException e) {
            // TODO: nalezy dodac obsluge wyjatkow za pomoca LOGGER'a
            e.printStackTrace();
        }
    }
}
