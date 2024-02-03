package com.example.backend.rest;

import com.example.backend.Repository.Permissions;
import com.example.backend.ResponseDtos.ErrorDto;
import com.example.backend.ResponseDtos.PrivacySettingsDto;
import com.example.backend.annotations.AuthRequest;
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

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @GetMapping("/system/language")
    public ResponseEntity<Object> getLanguage(@RequestHeader(value = "X-API-KEY", required = false) String token) throws IOException {
        String language = propertyService.getProperty("system.language");

        Map<String, String> dto = new HashMap<>();
        dto.put("language", language);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @PatchMapping("/system/language/set/{language}")
    public ResponseEntity<Object> setLanguage(@RequestHeader(value = "X-API-KEY", required = false) String token, @PathVariable(name = "language") String language) throws IOException {
        propertyService.setProperty("system.language", language);

        Map<String, String> dto = new HashMap<>();
        dto.put("language", language);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @GetMapping("/system/privacy")
    public ResponseEntity<Object> getPrivacySettings(@RequestHeader(value = "X-API-KEY", required = false) String token) throws IOException {
        boolean isPrivate = Boolean.parseBoolean(propertyService.getProperty("system.is-private"));

        Map<String, String> dto = new HashMap<>();
        dto.put("isPrivate", isPrivate+"");
        dto.put("entryCode", propertyService.getProperty("system.entry-code"));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @PatchMapping("/system/privacy/set")
    public ResponseEntity<Object> setPrivacySettings(@RequestHeader(value = "X-API-KEY", required = false) String token, @RequestBody PrivacySettingsDto privacySettings) throws IOException {
        propertyService.setProperty("system.is-private", privacySettings.isPrivate()+"");
        if (privacySettings.isPrivate() && privacySettings.getEntryCode() != null) {
            propertyService.setProperty("system.entry-code", privacySettings.getEntryCode());
        }

        Map<String, String> dto = new HashMap<>();
        dto.put("isPrivate", privacySettings.isPrivate()+"");
        dto.put("entryCode", propertyService.getProperty("system.entry-code"));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @GetMapping("/system/email-auth")
    public ResponseEntity<Object> getEmailAuth(@RequestHeader(value = "X-API-KEY", required = false) String token) throws IOException {
        String emailAuth = propertyService.getProperty("system.email-auth");

        Map<String, String> dto = new HashMap<>();
        dto.put("emailAuth", emailAuth);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @PatchMapping("/system/email-auth/set/{email-auth}")
    public ResponseEntity<Object> setEmailAuth(@RequestHeader(value = "X-API-KEY", required = false) String token, @PathVariable(name = "email-auth") String emailAuth) throws IOException {
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

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @PatchMapping("/system/installation-state/complete")
    public ResponseEntity<Object> setInstallationComplete(@RequestHeader(value = "X-API-KEY", required = false) String token) throws IOException {
        propertyService.setProperty("system.installed", "true");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @GetMapping("/system/supported-sources")
    public ResponseEntity<Object> getSupportedSources(@RequestHeader(value = "X-API-KEY", required = false) String token) throws IOException {
        String[] sources = propertyService.getPropertyAsList("system.sources.supported");

        return new ResponseEntity<>(sources, HttpStatus.OK);
    }

    @GetMapping("/system/sources")
    public ResponseEntity<Object> getEnabledSources() throws IOException {
        String[] sources = propertyService.getPropertyAsList("system.sources.enabled");

        return new ResponseEntity<>(sources, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @PatchMapping("/system/sources/set")
    public ResponseEntity<Object> setSources(@RequestHeader(value = "X-API-KEY", required = false) String token, @RequestBody Map<String, List<String>> input) throws IOException {
        String[] supported = propertyService.getPropertyAsList("system.sources.supported");
        for (String s :input.get("sources")) {
            if (!List.of(supported).contains(s)) return new ResponseEntity<>(new ErrorDto("'" + s + "' is not supported by this current version"), HttpStatus.I_AM_A_TEAPOT);
        }
        propertyService.setProperty("system.sources.enabled", "{" + String.join(",", input.get("sources")) + "}");
        String[] sources = propertyService.getPropertyAsList("system.sources.enabled");

        return new ResponseEntity<>(sources, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @GetMapping("/system/settings")
    public ResponseEntity<Object> getSystemSettings(@RequestHeader(value = "X-API-KEY", required = false) String token) throws IOException {
        Map<String, Object> response = new HashMap<>();

        response.put("language", propertyService.getProperty("system.language"));
        response.put("isPrivate", propertyService.getProperty("system.is-private"));
        response.put("entryCode", propertyService.getProperty("system.entry-code"));
        response.put("emailAuth", propertyService.getProperty("system.email-auth"));
        response.put("supportedSources", propertyService.getPropertyAsList("system.sources.supported"));
        response.put("sources", propertyService.getPropertyAsList("system.sources.enabled"));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @PatchMapping("/system/settings/set")
    public ResponseEntity<Object> setSystemSettings(@RequestHeader(value = "X-API-KEY", required = false) String token, @RequestBody Map<String, Object> input) throws IOException {
        propertyService.setProperty("system.language", input.get("language")+"");
        propertyService.setProperty("system.is-private", input.get("isPrivate")+"");
        propertyService.setProperty("system.entry-code", input.get("entryCode")+"");
        propertyService.setProperty("system.email-auth", input.get("emailAuth")+"");
        propertyService.setProperty("system.sources.supported", "{" + String.join(",", (List<String>)input.get("supportedSources")) + "}");
        propertyService.setProperty("system.sources.enabled", "{" + String.join(",", (List<String>)input.get("sources")) + "}");

        return getSystemSettings(token);
    }
}
