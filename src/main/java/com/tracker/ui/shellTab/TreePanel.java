package com.tracker.ui.shellTab;

import io.fabric8.kubernetes.api.model.Pod;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Provides the panel that contains tree
 */
class TreePanel extends JPanel {

    private DefaultComboBoxModel<String> podDefaultComboBoxModel;

    /**
     * Initialize new instance of {@link TreePanel}
     */
    TreePanel() {
    }

    /**
     * Initialize the tree panel component
     */
    public void initialize() {
        this.podDefaultComboBoxModel = new DefaultComboBoxModel<>();

        JComboBox<String> comboBox = new JComboBox<>(this.podDefaultComboBoxModel);

        this.add(comboBox);
    }

    /**
     * Updates the list of accessible pods
     *
     * @param pods - the list of pods that should be set to list
     */
    void updatePodsList(List<Pod> pods) {
        if (this.podDefaultComboBoxModel == null) {
            return;
        }

        String oldSelectedPod = ((String)this.podDefaultComboBoxModel.getSelectedItem());
        this.podDefaultComboBoxModel.removeAllElements();

        String newSelectedPod = null;
        for (Pod pod: pods) {
            this.podDefaultComboBoxModel.addElement(pod.getMetadata().getName());
            if (oldSelectedPod != null && pod.getMetadata().getName().equals(oldSelectedPod)) {
                newSelectedPod = pod.getMetadata().getName();
            }
        }

        if (newSelectedPod != null) {
            this.podDefaultComboBoxModel.setSelectedItem(newSelectedPod);
        }

    }
}
