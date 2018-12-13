package com.tracker.ui.podsTab;

import com.tracker.utils.LocalizationUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
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

    /**
     * Initialize new instance of {@link PodsPanel}
     */
    public PodsPanel() {
        this.setLayout(new BorderLayout());
        this.isAnyTabHasError = false;

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

    private JTabbedPane createTabPanel() {
        this.tabPane = new JTabbedPane();
        this.tabPane.setTabPlacement(JTabbedPane.BOTTOM);

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
