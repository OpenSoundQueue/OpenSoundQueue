package com.example.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class PropertyLoader {
    @Value("${system.properties.file.name}")
    private String PROPERTY_FILE_NAME;

    public Properties loadProperties() throws IOException {
        File initialFile = new File("./" + PROPERTY_FILE_NAME);
        InputStream targetStream = new FileInputStream(initialFile);
        Properties properties = new Properties();
        properties.load(targetStream);
        return properties;
    }

    public void saveProperties(Properties properties) throws IOException {
        File initialFile = new File("./" + PROPERTY_FILE_NAME);
        OutputStream targetStream = new FileOutputStream(initialFile);
        properties.store(targetStream, null);
    }

    public InputStream getPropertyStream() {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(PROPERTY_FILE_NAME);
    }
}
