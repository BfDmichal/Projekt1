package pl.wit;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.*;

/**
* Klasa AppWindow reprezentuje główne okno aplikacji do kopiowania plików.
* @author Julia Stefanowicz
* @author Michał Nowicki
* @author Michał Przyborowski
* @author Mateusz Grabiński
* @version 1.0
*/

public class AppWindow extends JFrame implements ActionListener {
    JButton selectButton;
    JButton destinationButton;
    JButton startButton;
    File selectedFile = null;
    File destinationFile = null;
    JLabel sourcePath;
    JLabel destinationPath;
    JTextField fileNamePattern;

    /**
     * Tworzy nowe okno aplikacji.
     *
     * @throws HeadlessException jeśli jest brak zasobów okna
     */

    public AppWindow() throws HeadlessException {
        fileNamePattern = new JTextField(".*");
        sourcePath = new JLabel();
        destinationPath = new JLabel();
        fileNamePattern.setBounds(55, 150, 400, 35);
        fileNamePattern.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(fileNamePattern);
        sourcePath.setBounds(20, 45, 320, 30);
        this.add(sourcePath);
        destinationPath.setBounds(320, 35, 320, 50);
        this.add(destinationPath);
        setTitle("Aplikacja do kopiowania plików");
        setLayout(null);
        startButton = new JButton("START");
        selectButton = new JButton("Wybierz folder źródłowy");
        destinationButton = new JButton("Wybierz folder docelowy");
        startButton.setBounds(200, 250, 100, 40);
        selectButton.setBounds(20, 20, 200, 30);
        destinationButton.setBounds(320, 20, 200, 30);
        selectButton.addActionListener(this);
        destinationButton.addActionListener(this);
        startButton.addActionListener(this);
        add(startButton);
        add(selectButton);
        add(destinationButton);
        pack();
        this.setVisible(true);
    }

    /**
     * Obsługuje zdarzenia akcji.
     *
     * @param e zdarzenie akcji
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectButton) {
            FolderChooserDialog dialog = new FolderChooserDialog(this);
            dialog.setVisible(true);
            String folderPath = dialog.getSelectedFolderPath();
            if (folderPath != null) {
                selectedFile = new File(folderPath);
                sourcePath.setText("..." + folderPath.substring(folderPath.lastIndexOf(File.separator)));
                if (!selectedFile.isDirectory()) {
                    JOptionPane.showMessageDialog(null, "Nie wybrano katalogu.");
                }
            }
        }
        if (e.getSource() == destinationButton) {
            FolderChooserDialog dialog = new FolderChooserDialog(this);
            dialog.setVisible(true);
            String folderPath = dialog.getSelectedFolderPath();
            if (folderPath != null) {
                destinationFile = new File(folderPath);
                destinationPath.setText("..." + folderPath.substring(folderPath.lastIndexOf(File.separator)));
                if (!destinationFile.isDirectory()) {
                    JOptionPane.showMessageDialog(null, "Nie wybrano katalogu.");
                }
            }
        }

        if (e.getSource() == startButton) {
            if (fileNamePattern.getText().isEmpty()) {
                fileNamePattern.setText(".*");
            }
            if (selectedFile != null && destinationFile != null && selectedFile.isDirectory() && destinationFile.isDirectory()) {
                FileCopier fileCopier = new FileCopier();
                fileCopier.copyDir(selectedFile, destinationFile, fileNamePattern.getText());
                FileCopier.getPool().shutdown();

                try {
                    if(FileCopier.getPool().awaitTermination(1, TimeUnit.MINUTES)){
                        JOptionPane.showMessageDialog(null, "Ilość przekopiowanych plików: " + FileCopier.getFileCounter());
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Timeout!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch(InterruptedException exception){
                    LogManager.getLogger().error("Pool has been interrupted");
                }
                
                FileCopier.resetFileCounter();
                clearControlsValue();
            } else {
                JOptionPane.showMessageDialog(null, "Nie wybrano folderów");
            }
        }
    }

    private void clearControlsValue() {
        sourcePath.setText("");
        destinationPath.setText("");
        fileNamePattern.setText(".*");
        selectedFile = null;
        destinationFile = null;
    }

	public FolderChooserDialog getFolderChooserDialog() {
		return new FolderChooserDialog(this);
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public Object getMessage() {
		return null;
	}

	public File getDestinationFile() {
		return destinationFile;
	}

	public void setSelectedFile(File selectedFile2) {
		this.selectedFile = selectedFile2;
		
	}

	public void setDestinationFile(File destinationFile2) {
		this.destinationFile = destinationFile2;
		
	}
}
