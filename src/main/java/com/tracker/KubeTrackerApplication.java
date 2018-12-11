package com.tracker;

import com.tracker.ui.KubeTrackerWidget;

import javax.swing.*;

/**
 * Provides the application file related to
 */
public class KubeTrackerApplication {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            KubeTrackerWidget kubeTrackerWidget = new KubeTrackerWidget();
            kubeTrackerWidget.show();
            kubeTrackerWidget.initialize();
        });
    }

}
