package com.service.account.bank.controller;

import com.service.account.bank.entity.Account;
import com.service.account.bank.entity.SavingAccount;
import com.service.account.bank.service.SavingAccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/saving-account")
public class SavingAccountController {

    private final SavingAccountService savingAccountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createSavingAccount(@RequestParam String accountNameHolder
    ) {
        Account account = savingAccountService.createSavingAccount(accountNameHolder);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/get-all-saving-accounts")
    public ResponseEntity<?> getAllSavingAccounts(Pageable pageable) {
        Page<SavingAccount> pageableSavingAccounts = savingAccountService.getAllSavingAccounts(pageable);


        return ResponseEntity.status(HttpStatus.OK).body(pageableSavingAccounts);
    }

}
