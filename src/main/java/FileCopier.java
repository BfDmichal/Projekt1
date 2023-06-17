import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Julia Stefanowicz
 * @author Michał Nowicki
 * @author Michał Przyborowski
 * @author Mateusz Grabiński
 * @version 1.0
 * Klasa FileCopier jest odpowiedzialna za kopiowanie katalogów i plików z lokalizacji źródłowej do docelowej.
 */

public class FileCopier {

    private static int fileCounter;
        
    /**
     * Konstruuje nowy obiekt FileCopier i inicjalizuje fileCounter.
     */

    public FileCopier() {
        fileCounter = 0;
    }
    
    /**
     * Kopiuje pliki z katalogu źródłowego do katalogu docelowego na podstawie podanego wzorca nazwy pliku.
     * Jeśli źródłem jest katalog, rekurencyjnie kopiuje wszystkie pasujące pliki.
     * Jeśli źródłem jest plik, kopiuje plik do miejsca docelowego.
     *
     * @param src katalog źródłowy lub plik do skopiowania
     * @param destination katalog docelowy, do którego zostaną skopiowane pliki
     * @param fileNamePattern wzorzec wyrażenia regularnego do dopasowania nazw plików
     */


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
    /**
     * Pobiera całkowitą liczbę skopiowanych plików.
     *
     * @return całkowita liczba skopiowanych plików
     */
    
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
