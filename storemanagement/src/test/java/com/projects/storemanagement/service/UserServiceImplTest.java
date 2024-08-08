package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.User;
import com.projects.storemanagement.exception.EmailAlreadyExistsException;
import com.projects.storemanagement.exception.UserNotFoundException;
import com.projects.storemanagement.exception.UsernameAlreadyExistsException;
import com.projects.storemanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals("user1", foundUser.getUsername());
    }

    @Test
    void testFindByIdThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void testCreate() {
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("password");
        user.setEmail("newEmail");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.existsByEmail("newEmail")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals("newUser", createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
    }

    @Test
    void testCreateThrowsUsernameException() {
        User user = new User();
        user.setUsername("existingUser");
        user.setPassword("password");
        user.setEmail("newEmail");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.create(user));
    }

    @Test
    void testCreateThrowsEmailException() {
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("password");
        user.setEmail("existingEmail");

        when(userRepository.existsByUsername("existingUser")).thenReturn(false);
        when(userRepository.existsByEmail("existingEmail")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.create(user));
    }

    @Test
    void testUpdate() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUser");
        existingUser.setPassword("oldPassword");
        existingUser.setEmail("oldEmail");
        when(userRepository.existsById(1L)).thenReturn(true);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("newPassword");
        updatedUser.setEmail("updatedEmail");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("updatedUser")).thenReturn(false);
        when(userRepository.existsByEmail("updatedEmail")).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(1L, updatedUser);

        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
        assertEquals("encodedNewPassword", result.getPassword());
        assertEquals("updatedEmail", result.getEmail());
    }

    @Test
    void testUpdateWithSameUsername() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUser");
        existingUser.setPassword("oldPassword");
        existingUser.setEmail("oldEmail");
        when(userRepository.existsById(1L)).thenReturn(true);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("oldUser");
        updatedUser.setPassword("newPassword");
        updatedUser.setEmail("newEmail");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("oldUser")).thenReturn(true);
        when(userRepository.existsByEmail("newEmail")).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(1L, updatedUser);

        assertNotNull(result);
        assertEquals("oldUser", result.getUsername());
        assertEquals("encodedNewPassword", result.getPassword());
        assertEquals("newEmail", result.getEmail());
    }

    @Test
    void testUpdateWithSameEmail() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUser");
        existingUser.setPassword("oldPassword");
        existingUser.setEmail("oldEmail");
        when(userRepository.existsById(1L)).thenReturn(true);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newUser");
        updatedUser.setPassword("newPassword");
        updatedUser.setEmail("oldEmail");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.existsByEmail("oldEmail")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(1L, updatedUser);

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        assertEquals("encodedNewPassword", result.getPassword());
        assertEquals("oldEmail", result.getEmail());
    }

    @Test
    void testUpdateThrowsNonExistingUserException() {
        User nonExistingUser = new User();
        nonExistingUser.setId(2L);
        nonExistingUser.setUsername("oldUser");
        nonExistingUser.setPassword("oldPassword");
        nonExistingUser.setEmail("oldEmail");
        when(userRepository.existsById(2L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.update(1L, nonExistingUser));
    }

    @Test
    void testUpdateThrowsExistingUsernameException() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUser");
        existingUser.setPassword("oldPassword");
        existingUser.setEmail("oldEmail");
        when(userRepository.existsById(1L)).thenReturn(true);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("existingUser");
        updatedUser.setPassword("newPassword");
        updatedUser.setEmail("newEmail");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.update(1L, updatedUser));
    }

    @Test
    void testUpdateThrowsExistingEmailException() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUser");
        existingUser.setPassword("oldPassword");
        existingUser.setEmail("oldEmail");
        when(userRepository.existsById(1L)).thenReturn(true);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newUser");
        updatedUser.setPassword("newPassword");
        updatedUser.setEmail("existingEmail");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("existingUser")).thenReturn(false);
        when(userRepository.existsByEmail("existingEmail")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.update(1L, updatedUser));
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("user1");

        assertTrue(foundUser.isPresent());
        assertEquals("user1", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsernameReturnsEmpty() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByUsername("user1");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testGetCurrentUser() {
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setUsername("testUser");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        User currentUser = userService.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals("testUser", currentUser.getUsername());
    }

    @Test
    void testGetCurrentUserThrowsExceptionWhenUserNotFound() {
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getCurrentUser());
    }

    @Test
    void testGetCurrentUserThrowsExceptionWhenAuthenticationIsNull() {
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getCurrentUser());
    }

    @Test
    void testGetCurrentUserThrowsExceptionWhenPrincipalIsNotUserDetails() {
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new Object());

        assertThrows(UserNotFoundException.class, () -> userService.getCurrentUser());
    }

}
