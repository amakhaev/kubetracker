package com.tracker.ui.podsTab;

import com.tracker.utils.LocalizationUtils;
import io.fabric8.kubernetes.api.model.ContainerState;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.PodStatus;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Provides the model to work with JTable
 */
@Slf4j
public class PodTableModel extends AbstractTableModel {

    private static final Color RED = new Color(224,125,125);
    private static final Color GREEN = new Color(180,229,181);

    @Setter
    private PodList pods;

    @Override
    public int getRowCount() {
        return this.pods == null ? 0 : this.pods.getItems().size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int c) {
        switch (c) {
            case 0:
                return LocalizationUtils.getString("pod_name");
            case 1:
                return LocalizationUtils.getString("namespace");
            case 2:
                return LocalizationUtils.getString("status");
            case 3:
                return LocalizationUtils.getString("restarts");
            case 4:
                return LocalizationUtils.getString("age");
        }

        return "";
    }

    @Override
    public Object getValueAt(int r, int c) {
        if (this.pods == null) {
            return "";
        }

        switch (c) {
            case 0:
                return this.pods.getItems().get(r).getMetadata().getName();
            case 1:
                return this.pods.getItems().get(r).getMetadata().getNamespace();
            case 2:
                return this.getStatusValue(r);
            case 3:
                return this.getRestartCount(r);
            case 4:
                return this.getPodAge(r);
        }

        return "";
    }

    /**
     * Gets the row background color by given pod state
     *
     * @param row - the row to apply color
     * @return the {@link Color} instance.
     */
    Color getRowBackgroundColor(int row) {
        if (this.pods.getItems().get(row).getStatus() == null ||
                this.pods.getItems().get(row).getStatus().getContainerStatuses() == null ||
                this.pods.getItems().get(row).getStatus().getContainerStatuses().isEmpty()) {
            return Color.WHITE;
        }

        ContainerStatus containerStatus = this.pods.getItems().get(row).getStatus().getContainerStatuses().get(0);
        return containerStatus.getReady() ? GREEN : RED;

    }

    private String getStatusValue(int row) {
        if (this.pods.getItems().get(row).getStatus() == null ||
                this.pods.getItems().get(row).getStatus().getContainerStatuses() == null ||
                this.pods.getItems().get(row).getStatus().getContainerStatuses().isEmpty() ||
                this.pods.getItems().get(row).getStatus().getContainerStatuses().get(0).getState() == null) {
            return "";
        }

        ContainerState state = this.pods.getItems().get(row).getStatus().getContainerStatuses().get(0).getState();
        if (state.getRunning() != null) {
            return LocalizationUtils.getString("running");
        } else if (state.getWaiting() != null) {
            return state.getWaiting().getReason();
        } else if (state.getTerminated() != null) {
            return state.getTerminated().getReason();
        } else {
            return LocalizationUtils.getString("unknown");
        }
    }

    private String getRestartCount(int row) {
        if (this.pods.getItems().get(row).getStatus() == null ||
                this.pods.getItems().get(row).getStatus().getContainerStatuses() == null ||
                this.pods.getItems().get(row).getStatus().getContainerStatuses().isEmpty()) {
            return "";
        }

        ContainerStatus status = this.pods.getItems().get(row).getStatus().getContainerStatuses().get(0);
        return String.valueOf(status.getRestartCount());
    }

    private String getPodAge(int row) {
        if (this.pods.getItems().get(row).getStatus() == null) {
            return "";
        }

        PodStatus status = this.pods.getItems().get(row).getStatus();
        if (status.getStartTime() == null) {
            return "";
        }

        try {
            Instant instant = Instant.parse(status.getStartTime());
            LocalDateTime podStartTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            LocalDateTime now = LocalDateTime.now();
            long days = ChronoUnit.DAYS.between(podStartTime, now);
            long hours = ChronoUnit.HOURS.between(podStartTime, now);
            long minutes = ChronoUnit.MINUTES.between(podStartTime, now);

            minutes -= hours * 60;
            hours -= days * 24;

            return (days == 0 ? "" : days + "d ") +
                    (hours == 0 ? "" : hours + "h ") +
                    minutes + "m";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }
}
