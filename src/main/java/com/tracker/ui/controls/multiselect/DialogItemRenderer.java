package com.tracker.ui.controls.multiselect;

import javax.swing.*;
import java.awt.*;

/**
 * Provides the cell renderer for dialog item
 */
class DialogItemRenderer extends JLabel implements ListCellRenderer<MultiSelectDialogItem> {

    /**
     * Initialize new instance of {@link DialogItemRenderer}
     */
    DialogItemRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends MultiSelectDialogItem> jList,
                                                  MultiSelectDialogItem multiSelectDialogItem,
                                                  int i,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        this.setText(multiSelectDialogItem.getValue());
        this.setMinimumSize(new Dimension(100, 30));

        if (isSelected) {
            setBackground(jList.getSelectionBackground());
            setForeground(jList.getSelectionForeground());
        } else {
            setBackground(jList.getBackground());
            setForeground(jList.getForeground());
        }

        return this;
    }
}
