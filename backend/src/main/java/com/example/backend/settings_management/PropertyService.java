package com.example.backend.settings_management;

import com.example.backend.util.PropertyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
public class PropertyService {
    @Autowired
    PropertyLoader propertyLoader;

    public void setProperty(String property, String value) throws IOException {
        Properties systemProperties = propertyLoader.loadProperties();
        systemProperties.setProperty(property, value);
        propertyLoader.saveProperties(systemProperties);
    }
}
