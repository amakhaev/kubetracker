package com.tracker.utils;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Provides the utility class to work with gson
 */
@UtilityClass
public class GsonUtility {

    private Gson gson = new Gson();

    /**
     * Parse json and create POJO
     *
     * @param jsonAsString - the target json
     * @param resultClass - the result class of POJO
     * @return the T instance
     */
    public <T> T parse(String jsonAsString, Class<T> resultClass) {
        return gson.fromJson(jsonAsString, resultClass);
    }

    /**
     * Parse json and create POJO
     *
     * @param jsonAsString - the target json
     * @param resultClass - the result class of POJO
     * @return the T instance
     */
    public <T> T parse(String jsonAsString, Type resultClass) {
        return gson.fromJson(jsonAsString, resultClass);
    }

    /**
     * Parse json and create POJO
     *
     * @param jsonReader - the target json
     * @param resultClass - the result class of POJO
     * @return the T instance
     */
    public <T> T parse(Reader jsonReader, Class<T> resultClass) {
        return gson.fromJson(jsonReader, resultClass);
    }

    /**
     * Writes the json to file
     *
     * @param object - the object to write
     * @param writer the writer
     */
    public void write(Object object, Writer writer) {
        gson.toJson(object, writer);
    }
}
