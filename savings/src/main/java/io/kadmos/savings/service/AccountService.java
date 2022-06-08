package io.kadmos.savings.service;

import io.kadmos.savings.entity.AccountEntity;
import io.kadmos.savings.entity.TransactionEntity;
import io.kadmos.savings.repository.AccountRepository;
import io.kadmos.savings.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class AccountService {
    private final String accountNumber;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private AccountEntity account;

    public AccountService(@Value("${app.account-number}") String accountNumber,
                          AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountNumber = accountNumber;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostConstruct
    public void getWorkingAccount() {
        account = accountRepository.findByAccountNumber(accountNumber).orElseGet(() -> {
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setAccountNumber(accountNumber);
            return accountRepository.save(accountEntity);
        });
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance() {
        return transactionRepository.getBalance(accountNumber).orElse(BigDecimal.valueOf(0,2));
    }

    @Transactional
    public BigDecimal changeBalance(BigDecimal amount) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);

        return getBalance();
    }
}
