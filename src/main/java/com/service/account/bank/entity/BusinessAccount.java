package com.service.account.bank.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@Entity
public class BusinessAccount extends Account{
    private BigDecimal businessCreditLimit;
}
