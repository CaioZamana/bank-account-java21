package com.service.account.bank.repository;

import com.service.account.bank.entity.LoanAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, Long> {
    Optional<LoanAccount> findByLoanLimit(BigDecimal loanLimit);

    Optional<LoanAccount> findByLoanBalance(BigDecimal loanBalance);
}
