package com.aspire.lms;


import com.aspire.lms.dto.request.UserRequest;
import com.aspire.lms.model.User;
import com.aspire.lms.repository.UserRepository;
import com.aspire.lms.service.impl.UserServiceImpl;
import com.aspire.lms.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        // Create a mock UserRequest
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test");
        userRequest.setAddress("123 Main St");
        userRequest.setEmailId("test@example.com");

        // Mock the userRepository.save() method
        User savedUser = new User();
        savedUser.setUserId(UUID.randomUUID().toString());
        savedUser.setName(userRequest.getName());
        savedUser.setAddress(userRequest.getAddress());
        savedUser.setEmail(userRequest.getEmailId());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Call the function
        User createdUser = userService.createUser(userRequest);

        // Verify that userRepository.save() was called with the correct argument
        verify(userRepository).save(argThat(user -> user.getName().equals("Test")));

        assertEquals("Test", createdUser.getName());
        assertEquals("123 Main St", createdUser.getAddress());
        assertEquals("test@example.com", createdUser.getEmail());
    }


}

