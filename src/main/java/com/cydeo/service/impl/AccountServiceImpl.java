package com.cydeo.service.impl;

import com.cydeo.enums.AccountStatus;
import com.cydeo.enums.AccountType;
import com.cydeo.model.Account;
import com.cydeo.repository.AccountRepository;
import com.cydeo.service.AccountService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createNewAccount(BigDecimal balance, Date createDate, AccountType accountType, Long userId) {

        Account account=Account.builder().id(UUID.randomUUID()).userId(userId)
                .balance(balance).accountType(accountType).creationDate(createDate)
                .accountStatus(AccountStatus.ACTIVE).build();
               return accountRepository.save(account);
    }

    @Override
    public List<Account> listAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(UUID id) {

        Account account=accountRepository.findById(id);
        account.setAccountStatus(AccountStatus.DELETED);

    }

    @Override
    public void activateAccount(UUID id) {

        Account account=accountRepository.findById(id);
        account.setAccountStatus(AccountStatus.ACTIVE);

    }

    @Override
    public Account retrieveByID(UUID id) {
        return accountRepository.findById(id);
    }
}
