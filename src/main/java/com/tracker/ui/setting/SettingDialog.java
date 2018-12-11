package com.tracker.ui.setting;

import com.tracker.domain.settings.SettingModel;
import com.tracker.domain.settings.SettingService;
import com.tracker.utils.LocalizationUtils;
import com.tracker.utils.ResourceHelper;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Provides the settings component
 */
public class SettingDialog extends JDialog {

    private static final Dimension DIALOG_SIZE = new Dimension(300, 300);
    private static final Border DEFAULT_LABEL_BORDER = BorderFactory.createEmptyBorder(10, 0, 10, 0);
    private static final Border DEFAULT_PANEL_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    private SettingModel settingModel;

    private JTextField fullNameValue;
    private JTextField shortNameValue;
    private JTextField passwordValue;

    @Setter
    private Runnable closeCallback;

    /**
     * Initialize new instance of {@link SettingDialog}
     */
    public SettingDialog(Frame parentFrame, String title, Runnable closeCallback) {
        super(parentFrame, title, Dialog.ModalityType.DOCUMENT_MODAL);
        this.settingModel = SettingService.INSTANCE.getSettings();
        this.closeCallback = closeCallback;
        this.setPreferredSize(DIALOG_SIZE);
        this.setResizable(false);
        this.initialize();
    }

    private void initialize() {
        JPanel panel = new JPanel();
        panel.setBorder(DEFAULT_PANEL_BORDER);
        this.setContentPane(panel);
        panel.setLayout(new BorderLayout());

        panel.add(this.createSettingsPanel(), BorderLayout.CENTER);
        panel.add(this.createBottomPanel(), BorderLayout.PAGE_END);
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.fullNameValue = this.createEditFiled(this.settingModel.getFullName());
        this.shortNameValue = this.createEditFiled(this.settingModel.getShortName());
        this.passwordValue = this.createPasswordFiled(this.settingModel.getPassword());

        panel.add(this.createLabel(LocalizationUtils.getString("full_user_name")));
        panel.add(this.fullNameValue);
        panel.add(this.createLabel(LocalizationUtils.getString("short_user_name")));
        panel.add(this.shortNameValue);
        panel.add(this.createLabel(LocalizationUtils.getString("user_pass")));
        panel.add(this.passwordValue);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.add(this.createButtonPanel(() -> {
                    this.save(
                            this.fullNameValue.getText(),
                            this.shortNameValue.getText(),
                            this.passwordValue.getText()
                    );
                    if (this.closeCallback != null) {
                        this.closeCallback.run();
                    }
                })
        );

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBorder(DEFAULT_LABEL_BORDER);
        return label;
    }

    private JTextField createEditFiled(String initialValue) {
        JTextField jTextField = new JTextField();
        jTextField.setText(initialValue);
        return jTextField;
    }

    private JTextField createPasswordFiled(String initialValue) {
        JTextField jPasswordField = new JPasswordField();
        jPasswordField.setText(initialValue);
        return jPasswordField;
    }

    private JPanel createButtonPanel(Runnable runnable) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(DEFAULT_PANEL_BORDER);

        JButton saveButton = new JButton(LocalizationUtils.getString("save"));
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (runnable != null) {
                    runnable.run();
                }
            }
        });

        JButton cancelButton = new JButton(LocalizationUtils.getString("cancel"));
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                SettingDialog.this.setVisible(false);
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }

    private void save(String fullNameValue, String shortNameValue, String password) {
        this.settingModel.setFullName(fullNameValue);
        this.settingModel.setShortName(shortNameValue);
        this.settingModel.setPassword(password);
        SettingService.INSTANCE.updateSettings(settingModel);
        this.setVisible(false);
    }
}
