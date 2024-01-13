package com.example.backend.rest;
// Generated by CodiumAI

import com.example.backend.ResponseDtos.PrivacySettingsDto;

import com.example.backend.system_management.PropertyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SystemRestTest {

    public PropertyService propertyService = Mockito.mock(PropertyService.class);

    public SystemRest systemRest = new SystemRest(propertyService);

    // GET request to /system/language returns language property value
    @Test
    public void test_getLanguage_returnsPropertyValue() throws IOException {
        // Set up the expected language property value
        String expectedLanguage = "English";
        Mockito.when(propertyService.getProperty("system.language")).thenReturn(expectedLanguage);

        // Send a GET request to /system/language
        ResponseEntity<Object> response = systemRest.getLanguage();
    
        // Verify that the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    
        // Verify that the response body contains the expected language property value
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(expectedLanguage, responseBody.get("language"));
    }

    // PATCH request to /system/language/set/{language} sets language property value
    @Test
    public void test_patch_request_sets_language_property_value() throws IOException {
        // Set up test data
        String language = "en";
        Map<String, String> dto = new HashMap<>();
        dto.put("language", language);
    
        // Mock the behavior of PropertyService.setProperty() method
        Mockito.doNothing().when(propertyService).setProperty("system.language", language);
    
        // Call the setLanguage() method of SystemRest
        ResponseEntity<Object> response = systemRest.setLanguage(language);
    
        // Verify that PropertyService.setProperty() method was called with the correct arguments
        Mockito.verify(propertyService).setProperty("system.language", language);
    
        // Verify the response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    // GET request to /system/privacy returns privacy settings
    @Test
    public void test_getPrivacySettings_returnsPrivacySettings() throws IOException {
        // Set up the expected privacy settings
        boolean isPrivate = true;
        String entryCode = "123456";
        Mockito.when(propertyService.getProperty("system.is-private")).thenReturn(String.valueOf(isPrivate));
        Mockito.when(propertyService.getProperty("system.entry-code")).thenReturn(entryCode);

        // Send a GET request to /system/privacy
        ResponseEntity<Object> response = systemRest.getPrivacySettings();
    
        // Verify the response status code is HttpStatus.OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    
        // Verify the response body contains the expected privacy settings
        Map<String, String> dto = (Map<String, String>) response.getBody();
        assertNotNull(dto);
        assertEquals(String.valueOf(isPrivate), dto.get("isPrivate"));
        assertEquals(entryCode, dto.get("entryCode"));
    }

    // GET request to /system/email-auth returns email authentication property value
    @Test
    public void test_getEmailAuth_returnsPropertyValue() throws IOException {
        // Set up the expected property value
        String expectedPropertyValue = "true";
        Mockito.when(propertyService.getProperty("system.email-auth")).thenReturn(expectedPropertyValue);

        // Make the GET request to /system/email-auth
        ResponseEntity<Object> response = systemRest.getEmailAuth();
    
        // Verify that the response status is HttpStatus.OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    
        // Verify that the response body contains the expected property value
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(expectedPropertyValue, responseBody.get("emailAuth"));
    }

    // PATCH request to /system/privacy/set sets privacy settings
    @Test
    public void test_patchRequestSetsPrivacySettings() throws IOException {
        // Create a PrivacySettingsDto object
        PrivacySettingsDto privacySettings = new PrivacySettingsDto(true, "1234");
    
        // Create a Map for the expected response
        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("isPrivate", "true");
        expectedResponse.put("entryCode", "1234");
    
        // Mock the behavior of getProperty method in PropertyService
        Mockito.when(propertyService.getProperty(Mockito.anyString())).thenReturn("1234");

        // Call the setPrivacySettings method with the privacySettings object
        ResponseEntity<Object> response = systemRest.setPrivacySettings(privacySettings);
    
        // Verify that the setProperty method in PropertyService is called with the correct arguments
        Mockito.verify(propertyService).setProperty("system.is-private", "true");
        Mockito.verify(propertyService).setProperty("system.entry-code", "1234");
    
        // Verify that the response contains the expected values
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    // PATCH request to /system/email-auth/set/{email-auth} sets email authentication property value
    @Test
    public void test_patchRequestSetsEmailAuthPropertyValue() throws IOException {
        // Set up test data
        String emailAuth = "true";
    
        // Set up expected result
        Map<String, String> expectedDto = new HashMap<>();
        expectedDto.put("emailAuth", emailAuth);
    
        // Mock propertyService.setProperty() method
        Mockito.doNothing().when(propertyService).setProperty("system.email-auth", emailAuth);
    
        // Call the method under test
        ResponseEntity<Object> response = systemRest.setEmailAuth(emailAuth);
    
        // Verify that propertyService.setProperty() was called with the correct arguments
        Mockito.verify(propertyService).setProperty("system.email-auth", emailAuth);
    
        // Verify the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());
    
        // Verify the response body
        assertEquals(expectedDto, response.getBody());
    }

    // GET request to /system/installation-state returns installation state
    @Test
    public void test_getInstallationState_returnsInstallationState() throws IOException {
        // Set up expected installation state
        String expectedState = "true";
        Mockito.when(propertyService.getProperty("system.installed")).thenReturn(expectedState);

        // Send GET request to /system/installation-state
        ResponseEntity<Object> response = systemRest.getInstallationState();
    
        // Verify that the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    
        // Verify that the response body contains the expected installation state
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(expectedState, responseBody.get("finished"));
    }

    // PATCH request to /system/installation-state/complete sets installation state to true
    @Test
    public void test_patchRequest_setsInstallationStateToTrue() throws IOException {

        // Set up the expected property value
        String expectedPropertyValue = "true";
    
        // Set up the mock behavior
        Mockito.doNothing().when(propertyService).setProperty("system.installed", expectedPropertyValue);
    
        // Make the PATCH request
        ResponseEntity<Object> response = systemRest.setInstallationComplete();
    
        // Verify that the property was set correctly
        Mockito.verify(propertyService).setProperty("system.installed", expectedPropertyValue);
    
        // Verify that the response has a 200 OK status
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // GET request to /system/supported-sources returns supported sources
    @Test
    public void test_getSupportedSources_returnsSupportedSources() throws IOException {
        // Set up the expected supported sources
        String[] expectedSources = {"source1", "source2", "source3"};
        Mockito.when(propertyService.getPropertyAsList("system.sources.supported")).thenReturn(expectedSources);

        // Send a GET request to /system/supported-sources
        ResponseEntity<Object> response = systemRest.getSupportedSources();
    
        // Verify that the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    
        // Verify that the response body contains the expected supported sources
        assertArrayEquals(expectedSources, (String[]) response.getBody());
    }

    // GET request to /system/sources returns enabled sources
    @Test
    public void test_getEnabledSources() throws IOException {
        // Set up the expected result
        String[] sources = {"source1", "source2"};
        Mockito.when(propertyService.getPropertyAsList("system.sources.enabled")).thenReturn(sources);

        // Make the GET request to /system/sources
        ResponseEntity<Object> response = systemRest.getEnabledSources();
    
        // Verify that the response status is HttpStatus.OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    
        // Verify that the response body contains the expected sources
        assertArrayEquals(sources, (String[]) response.getBody());
    }

    // PATCH request to /system/sources/set sets enabled sources
    @Test
    public void test_patchRequest_setsEnabledSources() throws IOException {
        // Create input map for the request body
        Map<String, List<String>> input = new HashMap<>();
        List<String> sources = new ArrayList<>();
        sources.add("source1");
        sources.add("source2");
        input.put("sources", sources);
    
        // Create expected response entity
        String[] expectedSources = {"source1", "source2"};
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(expectedSources, HttpStatus.OK);
    
        // Mock the getPropertyAsList method of PropertyService to return supported sources
        Mockito.when(propertyService.getPropertyAsList("system.sources.supported")).thenReturn(new String[]{"source1", "source2", "source3"});
    
        // Mock the setProperty method of PropertyService to verify it is called with the correct arguments
        Mockito.doNothing().when(propertyService).setProperty(Mockito.anyString(), Mockito.anyString());
    
        // Mock the getPropertyAsList method of PropertyService to return enabled sources
        Mockito.when(propertyService.getPropertyAsList("system.sources.enabled")).thenReturn(expectedSources);
    
        // Call the setSources method of SystemRest with the mock input
        ResponseEntity<Object> actualResponse = systemRest.setSources(input);
    
        // Verify that the getPropertyAsList method of PropertyService is called with the correct argument
        Mockito.verify(propertyService).getPropertyAsList("system.sources.supported");
    
        // Verify that the setProperty method of PropertyService is called with the correct arguments
        Mockito.verify(propertyService).setProperty("system.sources.enabled", "{source1,source2}");
    
        // Verify that the getPropertyAsList method of PropertyService is called with the correct argument
        Mockito.verify(propertyService).getPropertyAsList("system.sources.enabled");
    
        // Assert that the actual response matches the expected response
        assertEquals(expectedResponse, actualResponse);
    }

}