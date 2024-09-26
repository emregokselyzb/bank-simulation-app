package com.cydeo.service;

import com.cydeo.dto.AccountDTO;

import java.util.List;
import java.util.UUID;

public interface AccountService {

   void createNewAccount(AccountDTO accountDTO);

    List<AccountDTO> listAllAccount();

    void deleteAccount(Long id);

    void activateAccount(Long id);

    AccountDTO retrieveByID(Long id);

    List<AccountDTO> listAllActiveAccount();

    void updateAccount(AccountDTO accountDTO);


}
