package com.tracker.ui;

import com.tracker.ui.controls.multiselect.MultiSelectDialog;
import com.tracker.ui.podsTab.PodFilterAdapter;
import com.tracker.ui.podsTab.PodsPanel;
import com.tracker.ui.setting.SettingDialog;
import com.tracker.utils.EnvironmentUtils;
import com.tracker.utils.LocalizationUtils;
import com.tracker.utils.ResourceHelper;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Provides the widget to show information about kube
 */
@Slf4j
public class KubeTrackerWidget {

    private static final Dimension FRAME_SIZE = new Dimension(1000, 600);
    private static final Image APP_NORMAL_ICON = new ImageIcon(ResourceHelper.getPathToResource(ResourceHelper.TRAY_ICON)).getImage();
    private static final Image APP_YELLOW_ICON = new ImageIcon(ResourceHelper.getPathToResource(ResourceHelper.TRAY_WARN_ICON)).getImage();

    private Image currentIcon;
    private TrayIcon trayIcon;

    private JFrame frame;
    private PodsPanel podsPanel;

    private SettingDialog settingDialog;
    private MultiSelectDialog multiSelectDialog;

    /**
     * Initialize ew instance of {@link KubeTrackerWidget}
     */
    public KubeTrackerWidget() {
        this.currentIcon = APP_NORMAL_ICON;
        this.initializeFrame();
        this.initializeTray();
    }

    /**
     * Initializes the widget
     */
    public void initialize() {
        this.podsPanel.initialize();
        this.settingDialog = new SettingDialog(
                this.frame,
                LocalizationUtils.getString("settings"),
                () -> this.podsPanel.updateList()
        );

        this.multiSelectDialog = new MultiSelectDialog(
                this.frame,
                LocalizationUtils.getString("filter"),
                new PodFilterAdapter()
        );
    }

    /**
     * Shows the widget
     */
    public void show() {
        this.frame.setVisible(true);
    }

    private void initializeFrame() {
        this.frame = new JFrame(LocalizationUtils.getString("kube_tracker"));
        this.frame.setIconImage(this.currentIcon);

        Dimension screenResolution = EnvironmentUtils.getScreenResolution();
        this.frame.setBounds(
                (int)screenResolution.getWidth() / 2 - (int)FRAME_SIZE.getWidth() / 2,
                (int)screenResolution.getHeight() / 2 - (int)FRAME_SIZE.getHeight() / 2,
                (int)FRAME_SIZE.getWidth(),
                (int)FRAME_SIZE.getHeight()
        );

        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });

        this.frame.setJMenuBar(createMenu());
        this.frame.add(this.createTabPanel());
    }

    private void initializeTray() {
        if (!SystemTray.isSupported()) {
            log.error("SystemTray is not supported");
            return;
        }

        final SystemTray tray = SystemTray.getSystemTray();

        this.trayIcon = new TrayIcon(this.currentIcon, LocalizationUtils.getString("kube_tracker"));
        this.trayIcon.setImageAutoSize(true);

        trayIcon.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (!frame.isVisible()) {
                    frame.setVisible(true);
                }
                frame.requestFocus();
                frame.toFront();
            }
        });

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    private JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();

        JMenu fileMenu = new JMenu(LocalizationUtils.getString("file"));

        JMenuItem exitItem = new JMenuItem();
        exitItem.setAction(new AbstractAction(LocalizationUtils.getString("exit")) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        JMenuItem settingItem = new JMenuItem();
        settingItem.setAction(new AbstractAction(LocalizationUtils.getString("settings")) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                settingDialog.pack();
                settingDialog.setVisible(true);
            }
        });

        fileMenu.add(settingItem);
        fileMenu.add(exitItem);
        menu.add(fileMenu);
        return menu;
    }

    private JTabbedPane createTabPanel() {
        JTabbedPane tabPane = new JTabbedPane();

        JPanel databasePanel = new JPanel();

        this.podsPanel = new PodsPanel();

        this.podsPanel.setOnUpdateWidget(hasError -> {
            refreshIcons(hasError);
            return null;
        });

        this.podsPanel.setOnFilterChanged(() -> {
            multiSelectDialog.pack();
            multiSelectDialog.showDialog();
        });

        tabPane.addTab(LocalizationUtils.getString("pods"), this.podsPanel);
        tabPane.addTab(LocalizationUtils.getString("db"), databasePanel);
        return tabPane;
    }

    private void refreshIcons(boolean hasError) {
        this.currentIcon = hasError ? APP_YELLOW_ICON : APP_NORMAL_ICON;

        if (this.frame.getIconImage() != this.currentIcon) {
            this.frame.setIconImage(this.currentIcon);
        }

        if (!SystemTray.isSupported() || this.trayIcon == null) {
            return;
        }

        if (this.trayIcon.getImage() != this.currentIcon) {
            this.trayIcon.setImage(this.currentIcon);
        }
    }
}
