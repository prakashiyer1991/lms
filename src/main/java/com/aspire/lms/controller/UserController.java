package com.aspire.lms.controller;

import com.aspire.lms.dto.request.UserRequest;
import com.aspire.lms.model.User;
import com.aspire.lms.service.interfaces.UserService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.aspire.lms.enums.Response.loan_created_successfully;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Timed
    @PostMapping(path = "upsert",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody
    ResponseEntity<User> upsert(
            @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(loan_created_successfully.getStatusCode()).body(userService.createUser(userRequest));
    }

}
