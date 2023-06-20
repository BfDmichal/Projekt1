package pl.wit;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Component;
import java.awt.Container;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class FolderChooserDialogTest {

    private AppWindow parent;
    private FolderChooserDialog dialog;

    @Before
    public void setUp() {
        parent = new AppWindow();
        dialog = new FolderChooserDialog(parent);
    }

    @Test
    public void testDialogComponents() {
        assertTrue(dialog.isVisible());
        assertEquals("Select Folder", dialog.getTitle());
        assertEquals(500, dialog.getWidth());
        assertEquals(400, dialog.getHeight());
        assertTrue(dialog.isModal());
        assertEquals(parent, dialog.getParent());

        Container contentPane = dialog.getContentPane();
        assertTrue(contentPane instanceof Border);

        Component[] components = contentPane.getComponents();

        assertTrue(components[0] instanceof JLabel);
        JLabel titleLabel = (JLabel) components[0];
        assertEquals("Select a folder:", titleLabel.getText());

        assertTrue(components[1] instanceof JFileChooser);
        JFileChooser fileChooser = (JFileChooser) components[1];
        assertEquals(JFileChooser.DIRECTORIES_ONLY, fileChooser.getFileSelectionMode());
        assertFalse(fileChooser.getControlButtonsAreShown());

        javax.swing.filechooser.FileFilter fileFilter = fileChooser.getFileFilter();
        assertTrue(fileFilter.accept(new File("testFolder")));
        assertFalse(fileFilter.accept(new File("testFile.txt")));

        assertTrue(components[2] instanceof JButton);
        JButton selectButton = (JButton) components[2];
        assertEquals("Select", selectButton.getText());

        selectButton.doClick();

        assertFalse(dialog.isVisible());
        assertEquals(fileChooser.getSelectedFile().getPath(), dialog.getSelectedFolderPath());
    }
}
