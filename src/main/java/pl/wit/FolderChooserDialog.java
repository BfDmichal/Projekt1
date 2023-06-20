package pl.wit;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

class FolderChooserDialog extends JDialog {

    private JFileChooser fileChooser;
    private String selectedFolderPath;

    public FolderChooserDialog(AppWindow parent) {
        super(parent, true);
        setTitle("Select Folder");
        setSize(500, 400);
        setLocationRelativeTo(parent);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setControlButtonsAreShown(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Folders only";
            }
        });
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    selectedFolderPath = selectedFile.getPath();
                }
                dispose();
            }
        });
        JLabel titleLabel = new JLabel("Select a folder:");

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(fileChooser, BorderLayout.CENTER);
        add(selectButton, BorderLayout.SOUTH);
    }

    public String getSelectedFolderPath() {
        return selectedFolderPath;
    }
}
