package com.service.account.bank.repository;

import com.service.account.bank.entity.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingAccountRepository extends JpaRepository<SavingAccount, Long> {

    Optional<SavingAccount> findByAccountNumber(String accountNumber);
}
