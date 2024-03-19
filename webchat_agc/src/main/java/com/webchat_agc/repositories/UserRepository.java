package com.webchat_agc.repositories;

import com.webchat_agc.dto.User;
import com.webchat_agc.dto.UserStatus;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

 
@Repository
public interface UserRepository extends MongoRepository<User, String>{

    User getByUsername(String username);

    //List<User> findAllByStatus(UserStatus online);
}
