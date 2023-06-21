package pl.wit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;

import static org.junit.jupiter.api.Assertions.*;

public class FileCopierTest {

    private static final String TEST_DIR = "testDir";
    private static final String DEST_DIR = "destDir";
    private static final String FILE_NAME_PATTERN = "file[0-9]+.txt";

    private FileCopier fileCopier;

    @BeforeEach
    public void setup() {
        fileCopier = new FileCopier();
    }

    @Test
    public void testCopyDir() throws IOException {
        // Tworzenie testowych plików
        createTestFiles();

        File srcDir = new File(TEST_DIR);
        File destDir = new File(DEST_DIR);

        // Wywołanie metody copyDir
        fileCopier.copyDir(srcDir, destDir, FILE_NAME_PATTERN);

        FileCopier.getPool().shutdown();

        try {
            if(! FileCopier.getPool().awaitTermination(1, TimeUnit.MINUTES)){
                fail("Timeout was reached for thread termination");
            }
        }
        catch(InterruptedException e){
            LogManager.getRootLogger().error(e.getMessage() + e.getStackTrace());
        }

        // Sprawdzenie czy pliki zostały skopiowane poprawnie do katalogu docelowego
        File[] destFiles = destDir.listFiles();
        assertNotNull(destFiles);
        assertEquals(3, destFiles.length);

        for (File destFile : destFiles) {
            // Sprawdzenie, czy nazwy plików pasują do wzorca nazwy pliku
            assertTrue(destFile.getName().matches(FILE_NAME_PATTERN));
        }

        // Sprawdzenie wartości licznika plików
        int fileCounter = FileCopier.getFileCounter();
        assertEquals(3, fileCounter);
    }

    private void createTestFiles() throws IOException {
        File testDir = new File(TEST_DIR);
        testDir.mkdir();

        // Tworzenie testowych plików
        createFile(TEST_DIR + File.separator + "file1.txt", "Testowy plik 1");
        createFile(TEST_DIR + File.separator + "file2.txt", "Testowy plik 2");
        createFile(TEST_DIR + File.separator + "file3.txt", "Testowy plik 3");

        File subDir1 = new File(TEST_DIR + "/subDir1");
        subDir1.mkdir();
        createFile(TEST_DIR + "/subDir1/file5.txt", "Plik podkatalogu");

        File subDir2 = new File(TEST_DIR + "/subdir2");
        subDir2.mkdir();
        createFile(TEST_DIR + "/subDir2/file6.txt", "Plik podkatalogu");
    }

    private void createFile(String fileName, String content) throws IOException {
        File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print(content);
        }
    }
}
