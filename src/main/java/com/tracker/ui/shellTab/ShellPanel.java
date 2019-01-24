package com.tracker.ui.shellTab;

import com.tracker.ui.PodsController;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * Provides the panel that contains commands to work with pods
 */
public class ShellPanel extends JPanel {

    private RSyntaxTextArea textArea;
    private TreePanel treePanel;

    private PodsController podsController;

    /**
     * Initialize new instance of {@link ShellPanel}
     */
    public ShellPanel() {
        this.podsController = PodsController.getInstance("dev");
    }

    /**
     * Initialize UI for pane
     */
    public void initialize() {
        this.setLayout(new BorderLayout());

        this.add(this.createTreePanel(), BorderLayout.LINE_START);
        this.add(this.createCommandPanel(), BorderLayout.CENTER);

        this.podsController.scheduleRefreshPods().subscribe(podList -> this.treePanel.updatePodsList(podList.getItems()));
        this.podsController.getPods().subscribe(podList -> this.treePanel.updatePodsList(podList.getItems()));
    }

    private TreePanel createTreePanel() {
        this.treePanel = new TreePanel();
        this.treePanel.initialize();
        return this.treePanel;
    }

    private JPanel createCommandPanel() {
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new BorderLayout());

        this.textArea = new RSyntaxTextArea();
        this.textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        this.textArea.setCodeFoldingEnabled(true);
        commandPanel.add(new RTextScrollPane(this.textArea), BorderLayout.CENTER);

        return commandPanel;
    }
}
