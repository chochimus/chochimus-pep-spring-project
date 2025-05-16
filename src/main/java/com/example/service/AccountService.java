package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.InvalidAccountException;
import com.example.exception.UnauthorizedException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerUser(Account account) {
        if(account.getUsername() == null || account.getUsername().isBlank()) {
            throw new InvalidAccountException("Username cannot be blank");
        }
        if(account.getPassword() == null || account.getPassword().length() < 4) {
            throw new InvalidAccountException("Password must be at least 4 characters long");
        }
        if(accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        return accountRepository.save(account);
    }

    public Account login(Account account) {
        return accountRepository.findByUsername(account.getUsername())
            .filter(result -> result.getPassword().equals(account.getPassword()))
            .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));
    }

}
