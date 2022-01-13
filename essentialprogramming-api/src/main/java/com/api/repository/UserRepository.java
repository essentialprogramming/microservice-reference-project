package com.api.repository;

import com.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserKey(String userKey);

    boolean existsByEmail(String email);

    @Modifying
    @Query("DELETE FROM user u WHERE u.email = :email")
    void deleteUserByEmail(@Param("email") String email);
}
