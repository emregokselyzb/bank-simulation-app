package com.cydeo.service;

import com.cydeo.model.Account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionService makeTransfer(Account sender, Account receiver, BigDecimal amount, Date creationDate, String message);

    List<TransactionService> findAllTransaction();

    List<TransactionService> last10Transactions();

    List<TransactionService> findTransactionListById(UUID id);
}
