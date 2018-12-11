package com.tracker.ui.controls.multiselect;

import com.tracker.utils.LocalizationUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Provides multi selection control
 */
public class MultiSelectDialog extends JDialog {

    private static final Dimension DIALOG_SIZE = new Dimension(400, 500);
    private static final Border DEFAULT_PANEL_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    private static final Border DEFAULT_LABEL_BORDER = BorderFactory.createEmptyBorder(10, 0, 10, 0);

    private JTextField filterValue;
    private JButton addFilterButton;
    private DefaultListModel<MultiSelectDialogItem> dialogListModel;
    private JList<MultiSelectDialogItem> dialogItemList;

    private MultiSelectDataSource dataSource;

    /**
     * Initialize new instance of {@link MultiSelectDialog}
     */
    public MultiSelectDialog(Frame parentFrame, String title, MultiSelectDataSource dataSource) {
        super(parentFrame, title, Dialog.ModalityType.DOCUMENT_MODAL);

        this.dataSource = dataSource;

        this.setPreferredSize(DIALOG_SIZE);
        this.setResizable(false);
        this.initialize();
    }

    /**
     * Shows the dialog
     */
    public void showDialog() {
        this.setVisible(true);
    }

    private void initialize() {
        JPanel panel = new JPanel();
        panel.setBorder(DEFAULT_PANEL_BORDER);
        this.setContentPane(panel);
        panel.setLayout(new BorderLayout());

        this.dialogItemList = this.createFilterList();

        panel.add(this.createNewFilterPanel(), BorderLayout.PAGE_START);
        panel.add(new JScrollPane(this.dialogItemList), BorderLayout.CENTER);
        panel.add(this.createDeleteButton(), BorderLayout.PAGE_END);
    }

    private JPanel createNewFilterPanel() {
        JPanel newFilterPanel = new JPanel();
        newFilterPanel.setLayout(new BorderLayout());

        this.filterValue = this.createFilterValueField();
        this.addFilterButton = this.createAddFilterButton();

        JPanel editFilterPaned = new JPanel();
        editFilterPaned.setLayout(new FlowLayout(FlowLayout.LEFT));
        editFilterPaned.add(this.filterValue);
        editFilterPaned.add(this.addFilterButton);

        newFilterPanel.add(this.createLabel(LocalizationUtils.getString("new_filter")), BorderLayout.PAGE_START);
        newFilterPanel.add(editFilterPaned, BorderLayout.CENTER);

        return newFilterPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBorder(DEFAULT_LABEL_BORDER);
        return label;
    }

    private JTextField createFilterValueField() {
        JTextField filterTextFiled = new JTextField();
        filterTextFiled.setPreferredSize(new Dimension(300, 30));
        filterTextFiled.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                this.onTextUpdate();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                this.onTextUpdate();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
            }

            private void onTextUpdate() {
                if (addFilterButton == null || filterValue == null) {
                    return;
                }

                String value = filterValue.getText().trim();
                addFilterButton.setEnabled(!value.isEmpty() && !dataSource.isFilterExists(value));
            }
        });

        return filterTextFiled;
    }

    private JButton createAddFilterButton() {
        JButton addFilterButton = new JButton(LocalizationUtils.getString("add"));
        addFilterButton.setEnabled(false);
        addFilterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (filterValue == null) {
                    return;
                }

                String newFilter = filterValue.getText().trim();
                if (newFilter.isEmpty()) {
                    return;
                }

                dataSource.create(newFilter);
                filterValue.setText("");

                if (dialogListModel == null) {
                    return;
                }

                dialogListModel.clear();
                dataSource.getFilters().forEach(dialogListModel::addElement);
            }
        });

        return addFilterButton;
    }

    private JList<MultiSelectDialogItem> createFilterList() {
        this.dialogListModel = new DefaultListModel<>();
        this.dataSource.getFilters().forEach(this.dialogListModel::addElement);

        JList<MultiSelectDialogItem> list = new JList<>(this.dialogListModel);
        list.setCellRenderer(new DialogItemRenderer());
        return list;
    }

    private JButton createDeleteButton() {
        JButton deleteButton = new JButton(LocalizationUtils.getString("delete"));
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (dialogItemList.getSelectedValuesList() == null || dialogItemList.getSelectedValuesList().size() == 0) {
                    return;
                }

                dialogItemList.getSelectedValuesList().forEach(selected -> dataSource.delete(selected.getId()));
                dialogListModel.clear();
                dataSource.getFilters().forEach(dialogListModel::addElement);
            }
        });

        return deleteButton;
    }
}
