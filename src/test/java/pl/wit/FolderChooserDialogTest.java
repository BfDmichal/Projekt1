package pl.wit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class FolderChooserDialogTest {

    private AppWindow parent;
    private FolderChooserDialog dialog;

    @BeforeEach
    public void setUp() {
        parent = new AppWindow();
        dialog = new FolderChooserDialog(parent);
    }

    @Test
    public void testDialogComponents() {
        dialog.setVisible(true);

        // Sprawdzenie tytułu dialogu
        assertEquals("Select Folder", dialog.getTitle());
        // Sprawdzenie szerokości dialogu
        assertEquals(500, dialog.getWidth());
        // Sprawdzenie wysokości dialogu
        assertEquals(400, dialog.getHeight());
        // Sprawdzenie, czy dialog jest modalny
        assertTrue(dialog.isModal());
        // Sprawdzenie, czy rodzicem dialogu jest okno parent
        assertEquals(parent, dialog.getParent());

        // Pobranie kontenera zawartości dialogu
        Container contentPane = dialog.getContentPane();

        // Pobranie komponentów kontenera
        Component[] components = contentPane.getComponents();

        // Sprawdzenie pierwszego komponentu: JLabel
        assertTrue(components[0] instanceof JLabel);
        JLabel titleLabel = (JLabel) components[0];
        assertEquals("Select a folder:", titleLabel.getText());

        // Sprawdzenie drugiego komponentu: JFileChooser
        assertTrue(components[1] instanceof JFileChooser);
        JFileChooser fileChooser = (JFileChooser) components[1];
        // Sprawdzenie trybu wyboru plików na katalogi
        assertEquals(JFileChooser.DIRECTORIES_ONLY, fileChooser.getFileSelectionMode());
        // Sprawdzenie, czy przyciski sterujące są ukryte
        assertFalse(fileChooser.getControlButtonsAreShown());

        // Sprawdzenie trzeciego komponentu: JButton
        assertTrue(components[2] instanceof JButton);
        JButton selectButton = (JButton) components[2];
        assertEquals("Select", selectButton.getText());

        // Kliknięcie przycisku "Select"
        selectButton.doClick();

        // Sprawdzenie, czy dialog jest niewidoczny po kliknięciu przycisku
        assertFalse(dialog.isVisible());
    }
}
