package pl.wit;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FileCopierThreadTest {

    private static final String TEST_DIR = "testDir";
    private static final String DEST_DIR = "destDir";

    @Test
    public void testFileCopierThread() throws IOException, InterruptedException {
        
    	createTestFiles();
        File srcDir = new File(TEST_DIR);
        File destDir = new File(DEST_DIR);

        // Utworz tablice plikow zrodlowych
        Stream<Path> srcFiles = Files.walk(Paths.get(TEST_DIR), null);
        assertNotNull(srcFiles);
        int numFiles = (int) srcFiles.count();

        // Utworz pule watkow
        ExecutorService executor = Executors.newFixedThreadPool(numFiles);

        // Uruchom watki FileCopierThread dla kazdego pliku zrodlowego 
        for (Path srcFile : srcFiles.toArray(Path[]::new)) {
            File destFile = new File(destDir, srcFile.toString());
            Runnable worker = new FileCopierThread(srcFile.toString(), destFile.getAbsolutePath());
            executor.execute(worker);
        }

        // Zakoncz pule watkow
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Sprawdz, czy wszystkie pliki zostaly skopiowane
        File[] destFiles = destDir.listFiles();
        assertNotNull(destFiles);
        assertEquals(numFiles, destFiles.length);

        // Porownaj zawartosc skopiowanych plikow z plikami zrodlowymi
        for (Path srcFile : srcFiles.toArray(Path[]::new)) {
            File destFile = new File(destDir, srcFile.toString());
            assertTrue(areFilesEqual(srcFile, destFile.toPath()));
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
