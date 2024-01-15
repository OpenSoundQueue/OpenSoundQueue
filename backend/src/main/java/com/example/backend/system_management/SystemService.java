package com.example.backend.system_management;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SystemService {
    PropertyService propertyService;

    public SystemService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    public boolean checkEntryCode(String code) throws IOException {
        return propertyService.getProperty("system.entry-code").equals(code);
    }
}
