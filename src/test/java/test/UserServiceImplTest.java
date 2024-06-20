package test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import app.mappers.UserMapper;
import app.model.User;
import app.services.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class UserServiceImplTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        when(userMapper.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setPassword("password");

        String encodedPassword = "encodedPassword";
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);

        userService.saveUser(user);

        assertEquals(encodedPassword, user.getPassword());
        verify(userMapper, times(1)).save(user);
    }

    @Test
    public void testIsUserPresent_UserExists() {
        User user = new User();
        user.setUsername("testUser");

        when(userMapper.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        boolean isPresent = userService.isUserPresent(user);

        assertTrue(isPresent);
    }

    @Test
    public void testIsUserPresent_UserDoesNotExist() {
        User user = new User();
        user.setUsername("testUser");

        when(userMapper.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        boolean isPresent = userService.isUserPresent(user);

        assertFalse(isPresent);
    }

    @Test
    public void testFindById() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userMapper.findByUsername(username)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(username);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }
}

