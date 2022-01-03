package com.authentication.identityprovider.internal.repository;

import com.authentication.identityprovider.internal.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUserKey(String userKey);
}
