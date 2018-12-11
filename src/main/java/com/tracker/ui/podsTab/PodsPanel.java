package com.tracker.ui.podsTab;

import com.tracker.utils.LocalizationUtils;
import io.fabric8.kubernetes.api.model.PodList;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Provides the panel that contains information about pods
 */
@Slf4j
public class PodsPanel extends JPanel {

    private PodsPanelController controller;
    private PodTableModel podTableModel;
    private JLabel lastRefreshLabel;
    private LocalDateTime lastRefreshDate;

    @Setter
    private Function<Boolean, Void> onUpdateWidget;

    @Setter
    private Runnable onFilterChanged;

    /**
     * Initialize new instance of {@link PodsPanel}
     */
    public PodsPanel() {
        this.controller = new PodsPanelController();
    }

    /**
     * Initialize the panel
     */
    public void initialize() {
        this.setLayout(new BorderLayout());

        this.add(new JScrollPane(this.createControlPanel()), BorderLayout.LINE_START);
        this.add(new JScrollPane(this.createPodsTable()), BorderLayout.CENTER);

        this.initializeUpdatingLastRefreshField();
        this.runScheduledRefreshing();
    }

    /**
     * Updates the list of pods
     */
    public void updateList() {
        this.controller.getPods().subscribe(
                this::refreshList,
                e -> {
                    this.refreshList(new PodList());
                    log.error(e.getMessage());
                }
        );
    }

    private JTable createPodsTable() {
        this.podTableModel = new PodTableModel();
        this.updateList();

        return new PodTable(this.podTableModel);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.lastRefreshLabel = new JLabel();
        this.lastRefreshLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        this.lastRefreshLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.lastRefreshLabel.setPreferredSize(new Dimension(150, 30));

        Cursor filterButtonCursor = new Cursor(Cursor.HAND_CURSOR);
        JLabel filterButton = new JLabel(LocalizationUtils.getString("manage_filter"));
        filterButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterButton.setCursor(filterButtonCursor);
        filterButton.setForeground(Color.GRAY);
        filterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (onFilterChanged != null) {
                    onFilterChanged.run();
                }
            }
        });

        panel.add(this.lastRefreshLabel);
        panel.add(filterButton);

        return panel;
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
        if (this.onUpdateWidget != null) {
            this.onUpdateWidget.apply(this.anyPodHasErrors(pods));
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
