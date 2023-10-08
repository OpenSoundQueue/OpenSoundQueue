package com.example.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class PropertyLoader {
    @Value("${system.properties.location}")
    private String PROPERTY_FILE_PATH;

    public Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(PROPERTY_FILE_PATH);
        properties.load(fis);
        fis.close();
        return properties;
    }

    public void saveProperties(Properties properties) throws IOException {
        FileOutputStream fos = new FileOutputStream(PROPERTY_FILE_PATH);
        properties.store(fos, null);
        fos.close();
    }
}
