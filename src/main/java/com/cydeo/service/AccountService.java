package com.cydeo.service;

import com.cydeo.dto.AccountDTO;
import com.cydeo.enums.AccountType;
import com.cydeo.model.Account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AccountService {

   void createNewAccount(BigDecimal balance, Date createDate, AccountType accountType, Long userId);

    List<AccountDTO> listAllAccount();

    void deleteAccount(UUID id);

    void activateAccount(UUID id);

    AccountDTO retrieveByID(Long id);

    List<AccountDTO> listAllActiveAccount();

    void updateAccount(AccountDTO accountDTO);
}
