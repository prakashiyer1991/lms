package com.aspire.lms.service.interfaces;

import com.aspire.lms.dto.request.UserRequest;
import com.aspire.lms.model.User;

public interface UserService {
    User createUser(UserRequest userRequest);
}
