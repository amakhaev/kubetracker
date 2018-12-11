package com.tracker.utils;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Provides the helper to work with icons
 */
@UtilityClass
public class ResourceHelper {

    public static final String TRAY_ICON = "icons/tray_icon.ico";
    public static final String TRAY_WARN_ICON = "icons/tray_icon_warn.ico";

    private ResourceLoader resourceLoader = new ResourceLoader();

    /**
     * Gets the full path to resource.
     *
     * @param name - the name of resource.
     * @return the path.
     */
    public URL getPathToResource(String name) {
        return ResourceHelper.class.getClassLoader().getResource(name);
    }

    /**
     * Gets the property from file.
     *
     * @param key - the key to search.
     * @return the string result.
     */
    public String getProperty(String key) {
        return resourceLoader.prop.getProperty(key);
    }

    /**
     * Sets the value for property
     *
     * @param key - the key of property
     * @param value - the property value
     */
    public void setProperty(String key, String value) {
        resourceLoader.prop.setProperty(key, value);
        resourceLoader.save();
    }

    private class ResourceLoader {

        private String fileName = "config.properties";
        private Properties prop = new Properties();

        ResourceLoader() {
            try (InputStream input = new FileInputStream(this.fileName)) {
                prop.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        void save() {
            try (OutputStream output = new FileOutputStream(this.fileName)
            ) {
                prop.store(output, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

