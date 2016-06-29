package com.main;

import java.awt.Component;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class FileListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        Icon icon = null;
        if (value instanceof File) {
            File file = (File) value;
            value = file.getName();
            FileSystemView view = FileSystemView.getFileSystemView();
            icon = view.getSystemIcon(file);
        }

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 

        setIcon(icon);

        return this;

    }
}
