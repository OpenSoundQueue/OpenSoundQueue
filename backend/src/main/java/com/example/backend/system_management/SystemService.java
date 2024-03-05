package com.example.backend.system_management;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SystemService {
    PropertyService propertyService;

    public SystemService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    /**
     * checks the validity of an entry code
     * @param code entry code as string
     * @return boolean whether the code is valid
     * @throws IOException
     */
    public boolean checkEntryCode(String code) throws IOException {
        return propertyService.getProperty("system.entry-code").equals(code);
    }
}
