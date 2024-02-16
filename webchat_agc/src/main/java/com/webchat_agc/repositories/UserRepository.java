package com.webchat_agc.repositories;

import com.webchat_agc.dto.User;
import com.webchat_agc.dto.UserStatus;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
 
public interface UserRepository extends MongoRepository<User, String>{

    List<User> findAllByStatus(UserStatus online);

}
