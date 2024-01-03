package com.example.backend.system_management;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SystemService {
    @Value("${system.entry-code}")
    private String entryCode;

    public boolean checkEntryCode(String code) {
        return entryCode.equals(code);
    }
}
