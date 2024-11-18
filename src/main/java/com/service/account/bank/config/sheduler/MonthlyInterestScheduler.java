package com.service.account.bank.config.sheduler;

import com.service.account.bank.service.SavingAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@AllArgsConstructor
@Slf4j
public class MonthlyInterestScheduler {

    private final SavingAccountService savingAccountService;

    @Scheduled(cron = "0 0 0 1 * ?") // Executa no início de cada mês
    public void applyMonthlyInterest() {
        log.info("Iniciando a aplicação de juros mensais para todas as contas...");
        savingAccountService.applyMonthlyInterestToAllAccounts();
        log.info("Aplicação de juros mensais concluída.");
    }
}
