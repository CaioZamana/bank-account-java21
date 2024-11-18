package com.service.account.bank.config.sheduler;

import com.service.account.bank.entity.SavingAccount;
import com.service.account.bank.service.SavingAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ApplicationRunnerValidadionInterestRate implements CommandLineRunner {

    private final SavingAccountService savingAccountService;

    public ApplicationRunnerValidadionInterestRate(SavingAccountService savingAccountService) {
        this.savingAccountService = savingAccountService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Criando uma nova conta
        SavingAccount newAccount = savingAccountService.createSavingAccount("John Doe", "123456");

        // Simulando a criação da conta 1 ano atrás
        newAccount.setCreationDate(LocalDate.now().minusYears(1));

        // Exibindo a conta criada
        System.out.println("Conta criada com sucesso! Numero da conta: " + newAccount.getAccountNumber());


        // Aplicando juros manualmente para o ano completo usando o método do serviço
        savingAccountService.applyMonthlyInterest(newAccount);  // Aplica os juros para o primeiro mês

        // Mostrando o novo saldo após 1 mês de juros
        System.out.println("Novo saldo apos 1 mes de juros: " + newAccount.getBalance());

        // Agora aplicando juros para os 12 meses (1 ano)
        for (int i = 1; i < 12; i++) {
            // Aplicar juros para o próximo mês
            savingAccountService.applyMonthlyInterest(newAccount);
            System.out.println("Mes " + (i + 1) + " - Saldo: " + newAccount.getBalance());
        }

        // Mostrando o saldo final após 1 ano de juros
        System.out.println("Juros aplicados durante 1 ano, saldo final: " + newAccount.getBalance());
    }
}
