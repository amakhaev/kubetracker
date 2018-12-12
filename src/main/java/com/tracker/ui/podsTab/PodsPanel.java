package com.tracker.ui.podsTab;

import com.tracker.ui.controls.linkLabel.LinkLabel;
import com.tracker.utils.LocalizationUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Function;

/**
 * Provides the panel that contains information about pods
 */
@Slf4j
public class PodsPanel extends JPanel {

    private static final Color RED = new Color(224,125,125);
    private static final Color GREEN = new Color(180,229,181);

    private boolean isAnyTabHasError;
    private JTabbedPane tabPane;
    private EnvironmentTabPanel devTabPanel;
    private EnvironmentTabPanel qaTabPanel;

    @Setter
    private Function<Boolean, Void> onUpdateWidget;

    @Setter
    private Runnable onFilterChanged;

    /**
     * Initialize new instance of {@link PodsPanel}
     */
    public PodsPanel() {
        this.setLayout(new BorderLayout());
        this.isAnyTabHasError = false;

        this.add(new JScrollPane(this.createControlPanel()), BorderLayout.LINE_START);
        this.add(this.createTabPanel(), BorderLayout.CENTER);
    }

    /**
     * Initialize the panel
     */
    public void initialize() {
        this.devTabPanel.initialize();
        this.qaTabPanel.initialize();
        this.checkStatusesAllTabs();
    }

    /**
     * Updates the list of pods
     */
    public void updateAll() {
        this.devTabPanel.updateList();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Cursor filterButtonCursor = new Cursor(Cursor.HAND_CURSOR);
        JLabel podFilterButton = new LinkLabel(LocalizationUtils.getString("manage_filter"));

        podFilterButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        podFilterButton.setCursor(filterButtonCursor);
        podFilterButton.setForeground(Color.GRAY);
        podFilterButton.setPreferredSize(new Dimension(150, 30));
        podFilterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (onFilterChanged != null) {
                    onFilterChanged.run();
                }
            }
        });

        panel.add(podFilterButton);

        return panel;
    }

    private JTabbedPane createTabPanel() {
        this.tabPane = new JTabbedPane();

        this.devTabPanel = this.createTab("dev");
        this.qaTabPanel = this.createTab("qa");

        this.tabPane.addTab(LocalizationUtils.getString("dev"), this.devTabPanel);
        this.tabPane.addTab(LocalizationUtils.getString("qa"), this.qaTabPanel);

        return this.tabPane;
    }

    private EnvironmentTabPanel createTab(String namespace) {
        EnvironmentTabPanel tabPanel = new EnvironmentTabPanel(namespace);
        tabPanel.setOnStatusChanged(this::checkStatusesAllTabs);
        return tabPanel;
    }

    private void checkStatusesAllTabs() {
        boolean hasError = this.devTabPanel.isAnyPodHasError() || this.qaTabPanel.isAnyPodHasError();

        this.tabPane.setBackgroundAt(0, this.devTabPanel.isAnyPodHasError() ? RED : GREEN);
        this.tabPane.setBackgroundAt(1, this.qaTabPanel.isAnyPodHasError() ? RED : GREEN);

        if (this.isAnyTabHasError != hasError) {
            this.isAnyTabHasError = hasError;

            if (this.onUpdateWidget != null) {
                this.onUpdateWidget.apply(this.isAnyTabHasError);
            }
        }
    }
}
