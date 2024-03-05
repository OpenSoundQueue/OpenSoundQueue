package com.example.backend.system_management;

import com.example.backend.util.PropertyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
public class PropertyService {
    @Autowired
    PropertyLoader propertyLoader;

    /**
     * set a certain property
     * @param property name of the property
     * @param value value of the property
     * @throws IOException
     */
    public void setProperty(String property, String value) throws IOException {
        Properties systemProperties = propertyLoader.loadProperties();
        systemProperties.setProperty(property, value);
        propertyLoader.saveProperties(systemProperties);
    }

    /**
     * get a certain property as string
     * @param property name of the property
     * @return value of the property
     * @throws IOException
     */
    public String getProperty(String property) throws IOException {
        Properties systemProperties = propertyLoader.loadProperties();
        return systemProperties.getProperty(property);
    }

    /**
     * gets a list property and splits the string into a list
     * @param property name of the property
     * @return value of the property as list
     * @throws IOException
     */
    public String[] getPropertyAsList(String property) throws IOException {
        Properties systemProperties = propertyLoader.loadProperties();
        return systemProperties.getProperty(property).split("\\{")[1].split("}")[0].split(",");
    }
}
