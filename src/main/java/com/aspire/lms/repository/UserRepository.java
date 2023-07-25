package com.aspire.lms.repository;

import com.aspire.lms.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByUserId(String userId);
}
