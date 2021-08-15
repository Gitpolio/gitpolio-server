package com.gitpolio.gitpolioserver.repository;

import com.gitpolio.gitpolioserver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getByEmail(String email);
    Account getByName(String name);
    boolean existsByEmail(String email);
    boolean existsByName(String email);
}
