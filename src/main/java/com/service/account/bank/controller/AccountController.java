package com.service.account.bank.controller;

import com.service.account.bank.entity.Account;
import com.service.account.bank.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account/operation")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam String fromAccountNumber,
                                           @RequestParam String toAccountNumber,
                                           @RequestParam BigDecimal amount) {
            accountService.transfer(fromAccountNumber, toAccountNumber, amount);
            return ResponseEntity.ok("Transferência realizada com sucesso");

    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam String toAccount,
                                          @RequestParam BigDecimal amount) {

            accountService.deposit(amount, toAccount);
            return ResponseEntity.ok("Depósito efetuado com sucesso!");

    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam String fromAccount,
                                           @RequestParam BigDecimal amount) {
            accountService.withdraw(amount, fromAccount);
            return ResponseEntity.ok("Saque efetuado com sucesso!");
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestParam String accountToBalance) {
            Account account = accountService.getBalance(accountToBalance);
            if (account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Conta não encontrada para o número: " + accountToBalance);
            }
            return ResponseEntity.ok(account);
    }

    @GetMapping("/get-account/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber){
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/get-all-accounts")
    public ResponseEntity<?> getAllAccount(@PageableDefault(size = 40, sort = "accountHolder", direction = Sort.Direction.ASC)
                                           Pageable pageable){
        Page<Account> pageableAccounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageableAccounts);
    }

}
