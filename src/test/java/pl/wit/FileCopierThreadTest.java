package pl.wit;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FileCopierThreadTest {

    private static final String TEST_DIR = "testDir";
    private static final String DEST_DIR = "destDir";

    @Test
    public void testFileCopierThread() throws IOException, InterruptedException {
        // Tworzenie testowych plików
        createTestFiles();
        File destDir = new File(DEST_DIR);

        if (!destDir.exists()) {
            destDir.mkdir();
        }

        // Utworzenie tablicy plików źródłowych
        Path[] srcFiles = Files.walk(Paths.get(TEST_DIR)).filter((Path path) ->
                (Paths.get(TEST_DIR).compareTo(path) != 0)).toArray(Path[]::new);
        int numFiles = (int) srcFiles.length;

        // Utworzenie puli wątków
        ExecutorService executor = Executors.newFixedThreadPool(numFiles);

        // Uruchomienie wątków FileCopierThread dla każdego pliku źródłowego
        for (Path srcFile : srcFiles) {
            File destFile = new File(destDir, srcFile.getFileName().toString());
            Runnable worker = new FileCopierThread(srcFile.toString(), destFile.getAbsolutePath());
            executor.execute(worker);
        }

        // Zakończenie puli wątków
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Sprawdzenie, czy wszystkie pliki zostały skopiowane
        Path[] destFiles = Files.walk(Paths.get(DEST_DIR)).filter((Path path) ->
                (Paths.get(DEST_DIR).compareTo(path) != 0)).toArray(Path[]::new);
        assertNotNull(destFiles);
        assertEquals(numFiles, destFiles.length);

        // Porównanie zawartości skopiowanych plików z plikami źródłowymi
        for (Path srcFile : srcFiles) {
            if(! Files.isDirectory(srcFile, LinkOption.NOFOLLOW_LINKS)){
                File destFile = new File(destDir, srcFile.getFileName().toString());
                assertTrue(areFilesEqual(srcFile, destFile.toPath()));
            }
        }
    }

    private void createTestFiles() throws IOException {
        File testDir = new File(TEST_DIR);
        testDir.mkdir();
        createFile(TEST_DIR + File.separator + "file1.txt", "Testowy plik 1");
        createFile(TEST_DIR + File.separator + "file2.txt", "Testowy plik 2");
        createFile(TEST_DIR + File.separator + "file3.txt", "Testowy plik 3");
    }

    private void createFile(String fileName, String content) throws IOException {
        File file = new File(fileName);
        Files.write(file.toPath(), content.getBytes());
    }

    private boolean areFilesEqual(Path path1, Path path2) throws IOException {
        byte[] file1 = Files.readAllBytes(path1);
        byte[] file2 = Files.readAllBytes(path2);
        return MessageDigest.isEqual(file1, file2);
    }
}
