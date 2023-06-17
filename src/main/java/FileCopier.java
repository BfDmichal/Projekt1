import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
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

    private static ExecutorService pool;

        
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

        } else {
            copyFile(src, destination);
        }
    }

    /**
     * Pobiera całkowitą liczbę skopiowanych plików.
     *
     * @return całkowita liczba skopiowanych plików
     */
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
