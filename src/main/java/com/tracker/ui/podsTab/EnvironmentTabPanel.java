package com.tracker.ui.podsTab;

import com.tracker.utils.LocalizationUtils;
import io.fabric8.kubernetes.api.model.PodList;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Provides the panel to show specific tab panel
 */
@Slf4j
class EnvironmentTabPanel extends JPanel {

    private PodsPanelController controller;
    private PodTableModel podTableModel;
    private JLabel lastRefreshLabel;
    private LocalDateTime lastRefreshDate;

    @Getter
    private boolean anyPodHasError;

    @Setter
    private Runnable onStatusChanged;

    /**
     * Initialize new instance of {@link EnvironmentTabPanel}
     */
    EnvironmentTabPanel(String namespace) {
        this.controller = new PodsPanelController(namespace);
    }

    /**
     * Initialize the panel
     */
    public void initialize() {
        this.setLayout(new BorderLayout());

        this.add(this.createStatusPanel(), BorderLayout.PAGE_END);
        this.add(new JScrollPane(this.createPodsTable()), BorderLayout.CENTER);

        this.initializeUpdatingLastRefreshField();
        this.runScheduledRefreshing();
    }

    /**
     * Updates the list of pods
     */
    void updateList() {
        this.controller.getPods().subscribe(
                this::refreshList,
                e -> {
                    this.refreshList(new PodList());
                    log.error(e.getMessage());
                }
        );
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.GRAY);

        this.lastRefreshLabel = new JLabel(LocalizationUtils.getString("unknown"));
        panel.add(this.lastRefreshLabel);

        return panel;
    }

    private JTable createPodsTable() {
        this.podTableModel = new PodTableModel();
        this.updateList();

        return new PodTable(this.podTableModel);
    }

    private void runScheduledRefreshing() {
        this.controller.scheduleRefreshPods().subscribe(this::refreshList);
    }

    private boolean anyPodHasErrors(PodList pods) {
        if (pods == null || pods.getItems().isEmpty()) {
            return false;
        }

        return pods.getItems().stream().anyMatch(
                pod -> pod.getStatus() != null &&
                        pod.getStatus().getContainerStatuses() != null &&
                        !pod.getStatus().getContainerStatuses().isEmpty() &&
                        !pod.getStatus().getContainerStatuses().get(0).getReady()
        );
    }

    private void refreshList(PodList pods) {
        this.podTableModel.setPods(pods);
        this.podTableModel.fireTableDataChanged();
        this.lastRefreshDate = LocalDateTime.now();
        boolean currentStatus = this.anyPodHasErrors(pods);

        if (currentStatus != this.anyPodHasError) {
            this.anyPodHasError = currentStatus;

            if (this.onStatusChanged != null) {
                this.onStatusChanged.run();
            }
        }
    }

    private void updateLastRefreshLabel() {
        if (this.lastRefreshDate != null) {
            this.lastRefreshLabel.setText(
                    String.format(
                            LocalizationUtils.getString("last_refresh"),
                            ChronoUnit.SECONDS.between(this.lastRefreshDate, LocalDateTime.now())
                    )
            );
        }
    }

    private void initializeUpdatingLastRefreshField() {
        rx.Observable.interval(1, TimeUnit.SECONDS)
                .subscribe((aVoid) -> this.updateLastRefreshLabel());
    }
}
