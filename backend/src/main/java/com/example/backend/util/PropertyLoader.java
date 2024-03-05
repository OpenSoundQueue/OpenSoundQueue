package com.example.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class PropertyLoader {
    @Value("${system.properties.file.name}")
    private String PROPERTY_FILE_NAME;

    /**
     * loads all properties of the system.properties file
     * @return properties as Properties
     * @throws IOException
     */
    public Properties loadProperties() throws IOException {
        File initialFile = new File("./" + PROPERTY_FILE_NAME);
        InputStream targetStream = new FileInputStream(initialFile);
        Properties properties = new Properties();
        properties.load(targetStream);
        targetStream.close();
        return properties;
    }

    /**
     * saves the properties to the system.properties file
     * @param properties
     * @throws IOException
     */
    public void saveProperties(Properties properties) throws IOException {
        File initialFile = new File("./" + PROPERTY_FILE_NAME);
        OutputStream targetStream = new FileOutputStream(initialFile);
        properties.store(targetStream, null);
        targetStream.close();
    }

    /**
     * gets system.properties as a resource stream
     * @return properties as InputStream
     */
    public InputStream getPropertyStream() {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(PROPERTY_FILE_NAME);
    }
}
