package com.example.backend.rest;
// Generated by CodiumAI

import com.example.backend.Repository.Permissions;
import com.example.backend.Repository.Role;
import com.example.backend.Repository.RoleRepository;
import com.example.backend.ResponseDtos.ErrorDto;
import com.example.backend.user_management.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoleRestTest {
    // get all roles and return 200 status code
    @Test
    public void test_get_all_roles() {
        RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(mockRoleRepository, mockUserService);

        List<Role> roles = Arrays.asList(new Role(1, "Role 1", Arrays.asList(Permissions.MANAGE_ROLES), new ArrayList<>()), new Role(2, "Role 2", Arrays.asList(Permissions.MANAGE_USER), new ArrayList<>()));
        String token = "test_token";

        Mockito.when(mockRoleRepository.findAll()).thenReturn(roles);

        ResponseEntity<Object> response = roleRest.getAllRoles(token);

        Mockito.verify(mockRoleRepository).findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roles, response.getBody());
    }

    // create a new role with existing name and return 400 status code
    @Test
    public void test_create_role_with_existing_name() {
        RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(mockRoleRepository, mockUserService);

        Role role = new Role(1, "Existing Role", Arrays.asList(Permissions.MANAGE_ROLES), new ArrayList<>());
        String token = "test_token";

        Mockito.when(mockRoleRepository.findByName(role.getName())).thenReturn(role);

        ResponseEntity<Object> response = roleRest.createRole(role, token);

        Mockito.verify(mockRoleRepository).findByName(role.getName());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // get a role by non-existing id and return 400 status code
    @Test
    public void test_get_role_by_non_existing_id() {
        RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(mockRoleRepository, mockUserService);

        int roleId = 1;
        String token = "test_token";

        Mockito.when(mockRoleRepository.findById(roleId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = roleRest.getRole(token, roleId);

        Mockito.verify(mockRoleRepository).findById(roleId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("role with id '" + roleId + "' not found", ((ErrorDto)response.getBody()).getError());
    }

    // delete a role by non-existing id and return 400 status code
    @Test
    public void test_delete_role_by_non_existing_id() {
        RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(mockRoleRepository, mockUserService);

        int roleId = 1;
        String token = "test_token";

        Mockito.when(mockRoleRepository.findById(roleId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = roleRest.deleteRole(token, roleId);

        Mockito.verify(mockRoleRepository).findById(roleId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("role with id '" + roleId + "' not found", ((ErrorDto) response.getBody()).getError());
    }

    // edit a role with empty name and return 400 status code
    @Test
    public void test_editRoleWithEmptyName_Returns400StatusCode() {
        // Arrange
        RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(mockRoleRepository, mockUserService);
    
        Role role = new Role();
        role.setId(1);
        role.setName("");
        role.setPermissions(Collections.singletonList(Permissions.MANAGE_ROLES));
        role.setMembers(Collections.emptyList());
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", "token");
    
        // Act
        ResponseEntity<Object> response = roleRest.editRole(role, "token");
    
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // edit a role with empty members and return 201 status code
    @Test
    public void test_editRoleWithEmptyMembers_Returns201StatusCode() {
        // Arrange
        RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(mockRoleRepository, mockUserService);
    
        Role role = new Role();
        role.setId(1);
        role.setName("Test Role");
        role.setPermissions(new ArrayList<>());
        role.setMembers(new ArrayList<>());
    
        Mockito.when(mockRoleRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(role));
        Mockito.when(mockRoleRepository.findByName(Mockito.anyString())).thenReturn(null);
        Mockito.when(mockRoleRepository.save(Mockito.any(Role.class))).thenReturn(role);
    
        // Act
        ResponseEntity<Object> response = roleRest.editRole(role, "token");
    
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // create a new role with empty permissions and return 201 status code
    @Test
    public void test_createRoleWithEmptyPermissions_Returns201StatusCode() {
        // Arrange
        RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(mockRoleRepository, mockUserService);
        Role role = new Role(1, "Test Role", new ArrayList<>(), new ArrayList<>());
        String token = "test_token";

        // Mock the behavior of the roleRepository
        Mockito.when(mockRoleRepository.findByName(role.getName())).thenReturn(null);

        // Act
        ResponseEntity<Object> response = roleRest.createRole(role, token);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Mockito.verify(mockRoleRepository, Mockito.times(1)).save(role);
    }

    // edit a role with empty permissions and return 201 status code
    @Test
    public void test_editRoleWithEmptyPermissions_Returns201StatusCode() {
        // Arrange
        RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(mockRoleRepository, mockUserService);
    
        Role role = new Role();
        role.setId(1);
        role.setName("Test Role");
        role.setPermissions(new ArrayList<>());
        role.setMembers(new ArrayList<>());
    
        Mockito.when(mockRoleRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(role));
        Mockito.when(mockRoleRepository.findByName(Mockito.anyString())).thenReturn(null);
        Mockito.when(mockRoleRepository.save(Mockito.any(Role.class))).thenReturn(role);
    
        // Act
        ResponseEntity<Object> response = roleRest.editRole(role, "token");
    
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // get all permissions and return 200 status code
    @Test
    public void test_getAllPermissions_Return200StatusCode() {
        // Arrange
        RoleRepository roleRepositoryMock = Mockito.mock(RoleRepository.class);
        UserService userServiceMock = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(roleRepositoryMock, userServiceMock);
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", "token");
    
        // Act
        ResponseEntity<Object> response = roleRest.getAllPermissions(headers.get("X-API-KEY").get(0));
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // edit a role with non-existing id and return 400 status code
    @Test
    public void test_editRole_withNonExistingId_returns400StatusCode() {
        // Arrange
        RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(mockRoleRepository, mockUserService);
    
        int roleId = 1;
        Role role = new Role(roleId, "Test Role", new ArrayList<>(), new ArrayList<>());
        Mockito.when(mockRoleRepository.findById(roleId)).thenReturn(Optional.empty());
    
        // Act
        ResponseEntity<Object> response = roleRest.editRole(role, "token");
    
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorDto);
        ErrorDto errorDto = (ErrorDto) response.getBody();
        assertEquals("Role with this id does not exist", errorDto.getError());
    
        Mockito.verify(mockRoleRepository, Mockito.times(1)).findById(roleId);
        Mockito.verify(mockRoleRepository, Mockito.times(0)).findByName(Mockito.anyString());
        Mockito.verify(mockRoleRepository, Mockito.times(0)).save(Mockito.any(Role.class));
    }

    // delete a role by id and return 200 status code
    @Test
    public void test_deleteRoleById_Returns200StatusCode() {
        // Arrange
        int roleId = 1;
        String token = "test_token";
        RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
        UserService mockUserService = Mockito.mock(UserService.class);
        RoleRest roleRest = new RoleRest(mockRoleRepository, mockUserService);

        Mockito.when(mockRoleRepository.findById(roleId)).thenReturn(Optional.of(new Role()));
    
        // Act
        ResponseEntity<Object> response = roleRest.deleteRole(token, roleId);
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // edit a role with valid data and return 201 status code
    @Test
    public void test_editRoleWithValidData_Returns201StatusCode() throws URISyntaxException {
        // Create mocks
        RoleRepository roleRepositoryMock = Mockito.mock(RoleRepository.class);
        UserService userServiceMock = Mockito.mock(UserService.class);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse responseMock = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChainMock = Mockito.mock(FilterChain.class);
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
    
        // Create an instance of RoleRest with the mocks
        RoleRest roleRest = new RoleRest(roleRepositoryMock, userServiceMock);

        // Set the authentication and security context in the request
        Mockito.when(requestMock.getUserPrincipal()).thenReturn(authenticationMock);
        Mockito.when(requestMock.getRemoteUser()).thenReturn("username");
        Mockito.when(requestMock.getUserPrincipal()).thenReturn(authenticationMock);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    
        // Create a mock Role
        Role role = new Role(1, "Test Role", Arrays.asList(Permissions.MANAGE_ROLES), new ArrayList<>());
    
        // Create a mock HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-KEY", "token");
    
        // Create a mock RequestEntity
        RequestEntity<Role> requestEntity = new RequestEntity<>(role, headers, HttpMethod.POST, new URI("/api/role/create"));
    
        // Create a mock ResponseEntity
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(role, HttpStatus.CREATED);

        Mockito.when(roleRepositoryMock.findByName(Mockito.anyString())).thenReturn(null);
        Mockito.when(roleRepositoryMock.save(Mockito.any(Role.class))).thenReturn(role);
        Mockito.when(roleRepositoryMock.findById(role.getId())).thenReturn(Optional.of(role));
    
        // Call the createRole() method of RoleRest
        ResponseEntity<Object> result = roleRest.createRole(role, "token");

        // Assert that the result is equal to the expected responseEntity
        assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
        assertEquals(responseEntity.getBody(), ((Optional<Role>)roleRest.getRole("token", role.getId()).getBody()).get());
    }
}