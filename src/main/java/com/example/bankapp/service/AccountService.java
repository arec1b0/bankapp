package com.example.bankapp.service;

import com.example.bankapp.model.Account;
import com.example.bankapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "accounts", key = "#id")
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @CachePut(value = "accounts", key = "#id")
    public Account updateAccount(Long id, Account accountDetails) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setName(accountDetails.getName());
                    account.setBalance(accountDetails.getBalance());
                    return accountRepository.save(account);
                }).orElse(null);
    }

    @CacheEvict(value = "accounts", key = "#id")
    public boolean deleteAccount(Long id) {
        return accountRepository.findById(id)
                .map(account -> {
                    accountRepository.delete(account);
                    return true;
                }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Account> findAccountByIdAsync(Long id) {
        return CompletableFuture.completedFuture(accountRepository.findById(id).orElse(null));
    }
}
