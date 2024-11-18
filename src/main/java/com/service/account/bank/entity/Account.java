package com.service.account.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED) // TABLE_PER_CLASS || JOINED || SINGLE_TABLE
public abstract class Account {

   /* SEQUENCE:
    Prefira quando o banco de dados suportar sequências nativas e você quiser maior controle e eficiência.
    Ideal para bancos como PostgreSQL e Oracle.
    IDENTITY:
    Use quando o banco não suportar sequências nativas ou se quiser simplicidade na configuração.
    Ideal para MySQL ou MariaDB. */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SEQUENCE || IDENTITY
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    private String accountHolder;

    private BigDecimal balance;

    @Temporal(TemporalType.DATE)
    private LocalDate creationDate;

    @PrePersist
    protected void onCreate() {
        if (this.creationDate == null) {
            this.creationDate = LocalDate.now();
        }
    }

}