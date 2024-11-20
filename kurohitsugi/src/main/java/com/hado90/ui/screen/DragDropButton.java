package com.hado90.ui.screen;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

public class DragDropButton extends JButton {
    public DragDropButton(String type, Color btnColor, String pathType) {
        super("Drag or Select " + type);
        setPreferredSize(new Dimension(150, 40));
        setBackground(btnColor);
        setFont(new Font("Arial", Font.PLAIN, 15));
        setForeground(Color.WHITE);

        new DropTarget(this, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent event) {
                try {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked")
                    List<File> droppedFiles = (List<File>) event.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        if (file.isDirectory() || file.getName().toLowerCase().endsWith(".zip")) {
                            updatePathAndButton(pathType, file.getAbsolutePath(), DragDropButton.this);
                        } else {
                            JOptionPane.showMessageDialog(DragDropButton.this, "Only folders or ZIP files are allowed.");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".zip");
                }

                @Override
                public String getDescription() {
                    return "Folders and ZIP files";
                }
            });

            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                updatePathAndButton(pathType, selectedFile.getAbsolutePath(), this);
            }
        });
    }

    private void updatePathAndButton(String pathType, String newPath, DragDropButton button) {
        DashScreen dashScreen = (DashScreen) SwingUtilities.getWindowAncestor(button);
        File file = new File(newPath);

        if (file.isDirectory() || file.getName().toLowerCase().endsWith(".zip")) {
            // Set the paths in DashScreen
            switch (pathType) {
                case "Student Bulk File Path":
                    dashScreen.studentBulkFilePath = newPath;
                    dashScreen.setStudentPathSet(true);
                    break;
                case "Test Bulk File Path":
                    dashScreen.testBulkFilePath = newPath;
                    dashScreen.setTestPathSet(true);
                    break;
                case "Output File Path":
                    dashScreen.outputFilePath = newPath;
                    dashScreen.setOutputPathSet(true);
                    break;
            }
            button.setText("Path set: " + newPath);
            button.setBackground(Color.GREEN);
        } else {
            JOptionPane.showMessageDialog(button, "Invalid selection. Only folders or ZIP files are allowed.");
            button.setBackground(Color.RED);
        }
    }
}