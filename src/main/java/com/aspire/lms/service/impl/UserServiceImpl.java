package com.aspire.lms.service.impl;

import com.aspire.lms.dto.request.UserRequest;
import com.aspire.lms.model.User;
import com.aspire.lms.repository.UserRepository;
import com.aspire.lms.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User createUser(UserRequest userRequest) {
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setName(userRequest.getName());
        user.setAddress(userRequest.getAddress());
        user.setEmail(userRequest.getEmailId());
        return userRepository.save(user);
    }

}
