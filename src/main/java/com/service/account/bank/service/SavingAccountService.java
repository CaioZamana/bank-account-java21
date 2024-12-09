package com.service.account.bank.service;

import com.service.account.bank.entity.SavingAccount;
import com.service.account.bank.exception.AccountNotFoundException;
import com.service.account.bank.repository.SavingAccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class SavingAccountService {

    private final SavingAccountRepository savingAccountRepository;

    public SavingAccount createSavingAccount(String accountHolder) {
        if (accountHolder == null || accountHolder.isBlank()) {
            throw new IllegalArgumentException("O titular da conta não pode ser nulo ou vazio.");
        }

        // Gerar número único para a conta
        String uniqueAccountNumber = generateUniqueAccountNumber();

        // Criar nova conta
        SavingAccount newAccount = new SavingAccount();
        newAccount.setAccountHolder(accountHolder);
        newAccount.setAccountNumber(uniqueAccountNumber);
        newAccount.setBalance(BigDecimal.valueOf(1000));
        newAccount.setInterestRate(BigDecimal.valueOf(6));
        newAccount.setCreationDate(LocalDate.now());

        // Salvar no repositório
        savingAccountRepository.save(newAccount);

        log.info("Conta criada com sucesso! accountNumber={} ", newAccount.getAccountNumber());
        return newAccount;
    }

    // Método para gerar um número único
    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%08d", new Random().nextInt(100000000));
        } while (savingAccountRepository.findByAccountNumber(accountNumber).isPresent());

        return accountNumber;
    }


    @Transactional
    public void applyMonthlyInterest(SavingAccount account) {
        try {
            // Verificar a taxa de juros e o saldo
            if (account.getInterestRate() == null || account.getInterestRate().compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("Conta {} possui taxa de juros inválida ou não definida. Ignorando.", account.getAccountNumber());
                return;
            }
            if (account.getBalance() == null || account.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("Conta {} possui saldo inválido ou negativo. Ignorando.", account.getAccountNumber());
                return;
            }

            // Logando a taxa de juros para depuração
            log.info("Taxa de juros anual para a conta {}: {}", account.getAccountNumber(), account.getInterestRate());

            // Calcular a taxa mensal corretamente: taxa anual / 12 e depois por 100
            BigDecimal monthlyRate = account.getInterestRate()
                    .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)  // Divisão por 12 meses com mais casas decimais
                    .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP); // Conversão para decimal (percentagem)

            // Verificando a taxa mensal calculada
            log.info("Taxa mensal para a conta {}: {}", account.getAccountNumber(), monthlyRate);

            // Calcular os juros
            BigDecimal interest = account.getBalance().multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);

            // Verificar se o valor de juros calculado não é zero
            if (interest.compareTo(BigDecimal.ZERO) > 0) {
                // Atualizar o saldo
                BigDecimal newBalance = account.getBalance().add(interest);
                account.setBalance(newBalance);

                // Salvar a conta no banco de dados
                savingAccountRepository.save(account);
                log.info("Juros aplicados para a conta: {}. Juros: {}. Novo saldo: {}", account.getAccountNumber(), interest, account.getBalance());
            } else {
                log.warn("Os juros calculados para a conta {} são zero ou negativos. Nenhuma alteração no saldo.", account.getAccountNumber());
            }
        } catch (Exception e) {
            log.error("Erro ao aplicar juros para a conta: {}", account.getAccountNumber(), e);
        }
    }

    @Transactional
    public void applyMonthlyInterestToAllAccounts() {
        int batchSize = 100; // Número de contas por lote
        int page = 0;

        log.info("Iniciando o processamento de juros para todas as contas, com tamanho de lote: {}", batchSize);

        Page<SavingAccount> accounts;
        do {
            log.info("Processando lote {}...", page + 1); // Log indicando o número do lote que está sendo processado
            accounts = savingAccountRepository.findAll(PageRequest.of(page, batchSize));

            if (accounts.isEmpty()) {
                log.info("Nenhuma conta encontrada no lote {}. Finalizando.", page + 1); // Log para quando não houver mais contas
            }

            for (SavingAccount account : accounts) {
                try {
                    log.info("Aplicando juros para a conta de número: {}", account.getAccountNumber()); // Log para cada conta
                    applyMonthlyInterest(account);
                } catch (Exception e) {
                    log.error("Erro ao aplicar juros na conta {}: {}", account.getAccountNumber(), e.getMessage(), e); // Log de erro caso ocorra uma exceção
                }
            }

            page++; // Incrementa para o próximo lote
        } while (!accounts.isEmpty());

        log.info("Processamento de juros para todas as contas concluído.");
    }

    public Page<SavingAccount> getAllSavingAccounts(Pageable pageable) {
        Pageable fixedPageable = PageRequest.of(
                pageable.getPageNumber(), // Mantém o número da página recebido
                10,                       // Define 10 registros por página
                Sort.by("accountHolder").ascending() // Ordenação alfabética
        );
        return savingAccountRepository.findAll(fixedPageable);
    }
}
