package pl.wit;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;

import javax.swing.AbstractButton;

public class AppWindowTest {
    private AppWindow appWindow;

    @Before
    public void setUp() {
        appWindow = new AppWindow();
    }

    @Test
    public void testSelectButtonActionPerformed() {
        appWindow.selectButton.doClick();

        FolderChooserDialog folderChooserDialog = appWindow.getFolderChooserDialog();

        assertTrue(folderChooserDialog.isVisible());

        folderChooserDialog.setSelectedFolderPath("path/to/source/folder");
        ((AbstractButton) folderChooserDialog.selectButton).doClick();

        assertEquals("...source/folder", appWindow.sourcePath.getText());

        File selectedFile = appWindow.getSelectedFile();
        assertNotNull(selectedFile);
        assertTrue(selectedFile.isDirectory());

        folderChooserDialog.setSelectedFolderPath(null);
        ((AbstractButton) folderChooserDialog.selectButton).doClick();

        assertNull(appWindow.getSelectedFile());
        assertEquals("Nie wybrano katalogu.", appWindow.getMessage());
    }

    @Test
    public void testDestinationButtonActionPerformed() {
        appWindow.destinationButton.doClick();

        FolderChooserDialog folderChooserDialog = appWindow.getFolderChooserDialog();

        assertTrue(folderChooserDialog.isVisible());

        folderChooserDialog.setSelectedFolderPath("path/to/destination/folder");
        ((AbstractButton) folderChooserDialog.selectButton).doClick();

        assertEquals("...destination/folder", appWindow.destinationPath.getText());

        File destinationFile = appWindow.getDestinationFile();
        assertNotNull(destinationFile);
        assertTrue(destinationFile.isDirectory());

        folderChooserDialog.setSelectedFolderPath(null);
        ((AbstractButton) folderChooserDialog.selectButton).doClick();

        assertNull(appWindow.getDestinationFile());
        assertEquals("Nie wybrano katalogu.", appWindow.getMessage());
    }

    @Test
    public void testStartButtonActionPerformed() {
        File selectedFile = new File("path/to/source/folder");
        File destinationFile = new File("path/to/destination/folder");
        String fileNamePattern = ".*";

        appWindow.setSelectedFile(selectedFile);
        appWindow.setDestinationFile(destinationFile);
        appWindow.fileNamePattern.setText(fileNamePattern);

        appWindow.startButton.doClick();

        FileCopier fileCopier = new FileCopier();

        assertTrue(fileCopier.copyDirCalled);
        assertEquals(selectedFile, fileCopier.copyDirSelectedFile);
        assertEquals(destinationFile, fileCopier.copyDirDestinationFile);
        assertEquals(fileNamePattern, fileCopier.copyDirFileNamePattern);

        assertEquals("Ilosc przekopiowanych plikow: " + fileCopier.fileCounter, appWindow.getMessage());

        assertEquals("", appWindow.sourcePath.getText());
        assertEquals("", appWindow.destinationPath.getText());
        assertEquals(".*", appWindow.fileNamePattern.getText());
        assertNull(appWindow.getSelectedFile());
        assertNull(appWindow.getDestinationFile());

        appWindow.startButton.doClick();

        assertEquals("Nie wybrano folderow", appWindow.getMessage());
    }

    static // Wewnetrzna klasa pomocnicza do symulowania FileCopier
    class FileCopier {
        public boolean copyDirCalled = false;
        public File copyDirSelectedFile;
        public File copyDirDestinationFile;
        public String copyDirFileNamePattern;
        public int fileCounter = 5;

        public void copyDir(File selectedFile, File destinationFile, String fileNamePattern) {
            copyDirCalled = true;
            copyDirSelectedFile = selectedFile;
            copyDirDestinationFile = destinationFile;
            copyDirFileNamePattern = fileNamePattern;
        }

        public static int getFileCounter() {
            return 5;
        }
    }
}

