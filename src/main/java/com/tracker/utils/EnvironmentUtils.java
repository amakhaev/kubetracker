package com.tracker.utils;

import lombok.experimental.UtilityClass;

import java.awt.*;

/**
 * Provides the utils related to specific environment
 */
@UtilityClass
public class EnvironmentUtils {

    /**
     * Gets the screen resolution
     *
     * @return the {@link Dimension} instance
     */
    public Dimension getScreenResolution() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        return new Dimension(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
    }
}
