/**
 * all Rest endpoints related to system settings
 */

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

    @GetMapping("/system/loginstate")
    public ResponseEntity<Object> getLoginState(@RequestHeader(value = "X-API-KEY", required = false) String token) throws IOException {
        String isPrivate = propertyService.getProperty("system.is-private");
        String emailAuth = propertyService.getProperty("system.email-auth");

        Map<String, String> dto = new HashMap<>();
        dto.put("isPrivate", isPrivate);
        dto.put("requireAuth", emailAuth);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * get current installation state
     * @return
     * @throws IOException
     */
    @GetMapping("/system/installation-state")
    public ResponseEntity<Object> getInstallationState() throws IOException {
        String state = propertyService.getProperty("system.installed");

        Map<String, String> dto = new HashMap<>();
        dto.put("finished", state);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * set installation state
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     * @throws IOException
     */
    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @PatchMapping("/system/installation-state/complete")
    public ResponseEntity<Object> setInstallationComplete(@RequestHeader(value = "X-API-KEY", required = false) String token) throws IOException {
        propertyService.setProperty("system.installed", "true");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * get all supported sources
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     * @throws IOException
     */
    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @GetMapping("/system/supported-sources")
    public ResponseEntity<Object> getSupportedSources(@RequestHeader(value = "X-API-KEY", required = false) String token) throws IOException {
        String[] sources = propertyService.getPropertyAsList("system.sources.supported");

        return new ResponseEntity<>(sources, HttpStatus.OK);
    }

    /**
     * get all enabled sources
     * @return
     * @throws IOException
     */
    @GetMapping("/system/sources")
    public ResponseEntity<Object> getEnabledSources() throws IOException {
        String[] sources = propertyService.getPropertyAsList("system.sources.enabled");

        return new ResponseEntity<>(sources, HttpStatus.OK);
    }

    /**
     * get all system settings
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     * @throws IOException
     */
    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @GetMapping("/system/settings")
    public ResponseEntity<Object> getSystemSettings(@RequestHeader(value = "X-API-KEY", required = false) String token) throws IOException {
        Map<String, Object> response = new HashMap<>();

        response.put("language", propertyService.getProperty("system.language"));
        response.put("isPrivate", Boolean.parseBoolean(propertyService.getProperty("system.is-private")));
        response.put("entryCode", propertyService.getProperty("system.entry-code"));
        response.put("emailAuth", Boolean.parseBoolean(propertyService.getProperty("system.email-auth")));
        response.put("fromEmail", propertyService.getProperty("system.from-email"));
        response.put("emailPassword", propertyService.getProperty("system.email-password"));
        response.put("emailHostString", propertyService.getProperty("system.email-host-string"));
        response.put("supportedSources", propertyService.getPropertyAsList("system.sources.supported"));
        response.put("enabledSources", propertyService.getPropertyAsList("system.sources.enabled"));


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * set all system settings
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @param input
     * @return
     * @throws IOException
     */
    @AuthRequest(requiredPermission = Permissions.MANAGE_SYSTEM_SETTINGS)
    @PatchMapping("/system/settings/set")
    public ResponseEntity<Object> setSystemSettings(@RequestHeader(value = "X-API-KEY", required = false) String token, @RequestBody Map<String, Object> input) throws IOException {
        propertyService.setProperty("system.language", input.get("language")+"");
        propertyService.setProperty("system.is-private", input.get("isPrivate")+"");
        propertyService.setProperty("system.entry-code", input.get("entryCode")+"");
        propertyService.setProperty("system.email-auth", input.get("emailAuth")+"");
        propertyService.setProperty("system.from-email", input.get("fromEmail")+"");
        propertyService.setProperty("system.email-password", input.get("emailPassword")+"");
        propertyService.setProperty("system.email-host-string", input.get("emailHostString")+"");
        propertyService.setProperty("system.sources.supported", "{" + String.join(",", (List<String>)input.get("supportedSources")) + "}");
        propertyService.setProperty("system.sources.enabled", "{" + String.join(",", (List<String>)input.get("enabledSources")) + "}");

        return getSystemSettings(token);
    }
}
