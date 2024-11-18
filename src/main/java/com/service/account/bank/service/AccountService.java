package com.service.account.bank.service;

import com.service.account.bank.entity.Account;
import com.service.account.bank.exception.AccountNotFoundException;
import com.service.account.bank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {

        validadeTransfer(fromAccountNumber, toAccountNumber, amount);

        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Conta de destino " + toAccountNumber + " não encontrada."));

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("Conta origem " + fromAccountNumber + " não encontrada."));

        BigDecimal balanceFrom = fromAccount.getBalance();
        if(balanceFrom.compareTo(amount) < 0){
            throw new RuntimeException("Saldo insuficiente na conta origem.");
        }

        BigDecimal newBalanceFrom = balanceFrom.subtract(amount);

        fromAccount.setBalance(newBalanceFrom);
        accountRepository.save(fromAccount);

        BigDecimal balanceTo = toAccount.getBalance();
        BigDecimal newBalanceTo = balanceTo.add(amount);
        toAccount.setBalance(newBalanceTo);
        accountRepository.save(toAccount);

        log.info("Transferência efetuada com sucesso fromAccount={} toAccount={} amount={}", fromAccount, toAccount, amount);
    }

    private void validadeTransfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior do que Zero.");
        }
        if (fromAccountNumber == null || fromAccountNumber.isBlank()) {
            throw new IllegalArgumentException("O número da conta origem não pode ser nulo ou vazio.");
        }
        if (toAccountNumber == null || toAccountNumber.isBlank()) {
            throw new IllegalArgumentException("O número da conta de destino não pode ser nulo ou vazio.");
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("O número da conta destino não pode ser igual ao número da conta origem.");
        }
    }


    @Transactional
    public void deposit(BigDecimal amount, String accountNumber){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser maior que zero.");
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("O número da conta não pode ser nulo ou vazio.");
        }

        Account accountToDeposit = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new RuntimeException("Conta com número "+ accountNumber + " não encontrada."));

        BigDecimal balance = accountToDeposit.getBalance();
        BigDecimal newBalance = balance.add(amount);
        accountToDeposit.setBalance(newBalance);

        accountRepository.save(accountToDeposit);

        log.info("Depósito de {} realizado com sucesso na conta {}. Novo saldo: {}", amount, accountNumber, newBalance);
    }

    @Transactional
    public void withdraw(BigDecimal amount, String accountNumber){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("O valor do saque deve ser maior que zero");
        }
        if (accountNumber == null || accountNumber.isBlank()){
            throw new IllegalArgumentException("O número da conta não pode ser nulo ou vazio");
        }

        Account accountToWithdraw = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new RuntimeException("Conta com número " + accountNumber + " não encontrada."));


        BigDecimal balance = accountToWithdraw.getBalance();

        if (balance.compareTo(amount) < 0) {
            log.warn("Tentativa de saque inválido. Valor: {}, Número da Conta: {}", amount, accountNumber);
            throw new RuntimeException("Saldo insuficiente para realizar o saque.");
        }

        BigDecimal newBalance = balance.subtract(amount);
        accountToWithdraw.setBalance(newBalance);
        accountRepository.save(accountToWithdraw);

        log.info("Saque de {} realizado com sucesso na conta {}. Novo saldo: {}", amount, accountNumber, newBalance);
    }


    public Account getBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Número de conta " + accountNumber + " não encontrada."));
    }


    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Numero de conta " + accountNumber + " não encontrado."));
    }


    public Page<Account> getAllAccounts(Pageable pageable) {
        Pageable fixedPageable = PageRequest.of(
                pageable.getPageNumber(), // Mantém o número da página recebido
                10,                       // Define 10 registros por página
                Sort.by("accountHolder").ascending() // Ordenação alfabética
        );
        return accountRepository.findAll(fixedPageable);
    }

}
