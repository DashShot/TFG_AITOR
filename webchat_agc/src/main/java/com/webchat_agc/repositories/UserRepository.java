package com.webchat_agc.repositories;

import com.webchat_agc.dto.User;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

//USAR REPOSITORIOS REACTIVEMONGO PARA LOS MENSAJES
 
@Repository
public interface UserRepository extends MongoRepository<User, String>{

    Optional<User> findByUsername(String username);

    //List<User> findAllByStatus(UserStatus online);
}
