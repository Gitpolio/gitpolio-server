package com.gitpolio.gitpolioserver.repository;

import com.gitpolio.gitpolioserver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getByEmail(String email);
    boolean existsByEmail(String email);
}
