package com.tracker.ui.podsTab;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Provides the pods table
 */
public class PodTable extends JTable {

    private PodTableModel tableModel;

    /**
     * Initialize new instance of {@link PodTable}
     */
    PodTable(PodTableModel tableModel) {
        super(tableModel);

        this.tableModel = tableModel;
        this.setRowHeight(30);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setDefaultRenderer(Object.class, this.getCellRenderer());
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component component = super.prepareRenderer(renderer, row, column);
        int rendererWidth = component.getPreferredSize().width;
        TableColumn tableColumn = getColumnModel().getColumn(column);
        tableColumn.setPreferredWidth(
                Math.max(rendererWidth + getIntercellSpacing().width + 10, tableColumn.getPreferredWidth())
        );
        return component;
    }

    private DefaultTableCellRenderer getCellRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(tableModel.getRowBackgroundColor(row));
                return c;
            }
        };
    }

}
