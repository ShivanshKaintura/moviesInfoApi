package com.movieAPIPractice.api.authorization.repositories;


import com.movieAPIPractice.api.authorization.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);
}
