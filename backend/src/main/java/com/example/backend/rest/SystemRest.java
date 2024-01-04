package com.example.backend.rest;

import com.example.backend.ResponseDtos.ErrorDto;
import com.example.backend.ResponseDtos.PrivacySettingsDto;
import com.example.backend.system_management.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SystemRest {
    PropertyService propertyService;

    public SystemRest(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/system/language")
    public ResponseEntity<Object> getLanguage() throws IOException {
        String language = propertyService.getProperty("system.language");

        Map<String, String> dto = new HashMap<>();
        dto.put("language", language);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PatchMapping("/system/language/set/{language}")
    public ResponseEntity<Object> setLanguage(@PathVariable(name = "language") String language) throws IOException {
        propertyService.setProperty("system.language", language);

        Map<String, String> dto = new HashMap<>();
        dto.put("language", language);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/system/privacy")
    public ResponseEntity<Object> getPrivacySettings() throws IOException {
        boolean isPrivate = Boolean.parseBoolean(propertyService.getProperty("system.is-private"));

        Map<String, String> dto = new HashMap<>();
        dto.put("isPrivate", isPrivate+"");
        dto.put("entryCode", propertyService.getProperty("system.entry-code"));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PatchMapping("/system/privacy/set")
    public ResponseEntity<Object> setPrivacySettings(@RequestBody PrivacySettingsDto privacySettings) throws IOException {
        propertyService.setProperty("system.is-private", privacySettings.isPrivate()+"");
        if (privacySettings.isPrivate() && privacySettings.getEntryCode() != null) {
            propertyService.setProperty("system.entry-code", privacySettings.getEntryCode());
        }

        Map<String, String> dto = new HashMap<>();
        dto.put("isPrivate", privacySettings.isPrivate()+"");
        dto.put("entryCode", propertyService.getProperty("system.entry-code"));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/system/email-auth")
    public ResponseEntity<Object> getEmailAuth() throws IOException {
        String emailAuth = propertyService.getProperty("system.email-auth");

        Map<String, String> dto = new HashMap<>();
        dto.put("emailAuth", emailAuth);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PatchMapping("/system/email-auth/set/{email-auth}")
    public ResponseEntity<Object> setEmailAuth(@PathVariable(name = "email-auth") String emailAuth) throws IOException {
        propertyService.setProperty("system.email-auth", emailAuth);

        Map<String, String> dto = new HashMap<>();
        dto.put("emailAuth", emailAuth);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/system/installation-state")
    public ResponseEntity<Object> getInstallationState() throws IOException {
        String state = propertyService.getProperty("system.installed");

        Map<String, String> dto = new HashMap<>();
        dto.put("finished", state);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PatchMapping("/system/installation-state/complete")
    public ResponseEntity<Object> setInstallationComplete() throws IOException {
        propertyService.setProperty("system.installed", "true");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/system/supported-sources")
    public ResponseEntity<Object> getSupportedSources() throws IOException {
        String[] sources = propertyService.getPropertyAsList("system.sources.supported");

        return new ResponseEntity<>(sources, HttpStatus.OK);
    }

    @GetMapping("/system/sources")
    public ResponseEntity<Object> getEnabledSources() throws IOException {
        String[] sources = propertyService.getPropertyAsList("system.sources.enabled");

        return new ResponseEntity<>(sources, HttpStatus.OK);
    }

    @PatchMapping("/system/sources/set")
    public ResponseEntity<Object> setSources(@RequestBody Map<String, List<String>> input) throws IOException {
        String[] supported = propertyService.getPropertyAsList("system.sources.supported");
        for (String s :input.get("sources")) {
            if (!List.of(supported).contains(s)) return new ResponseEntity<>(new ErrorDto("'" + s + "' is not supported by this current version"), HttpStatus.I_AM_A_TEAPOT);
        }
        propertyService.setProperty("system.sources.enabled", "{" + String.join(",", input.get("sources")) + "}");
        String[] sources = propertyService.getPropertyAsList("system.sources.enabled");

        return new ResponseEntity<>(sources, HttpStatus.OK);
    }
}
