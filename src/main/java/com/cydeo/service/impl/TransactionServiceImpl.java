package com.cydeo.service.impl;

import com.cydeo.enums.AccountType;
import com.cydeo.exception.AccountOwnerShipException;
import com.cydeo.exception.BadRequestException;
import com.cydeo.exception.BalanceNotSufficientException;
import com.cydeo.exception.UnderConstructionException;
import com.cydeo.model.Account;
import com.cydeo.model.Transaction;
import com.cydeo.repository.AccountRepository;
import com.cydeo.repository.TransactionRepository;
import com.cydeo.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Service
public class TransactionServiceImpl implements TransactionService {

    @Value("${under_construction}")
    private boolean underConstruction;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction makeTransfer(Account sender, Account receiver, BigDecimal amount, Date creationDate, String message) {
        if (!underConstruction) {

            validateAccount(sender, receiver);
            checkAccountOwnerShip(sender, receiver);
            executeBalanceAndUpdateIfRequired(amount, sender, receiver);

            Transaction transaction = Transaction.builder().amount(amount).sender(sender.getId()).receiver(receiver.getId())
                    .createDate(creationDate).message(message).build();
            return transactionRepository.save(transaction);

        } else {
            throw new UnderConstructionException("App is under construction,please try again later.");
        }
    }

    private void executeBalanceAndUpdateIfRequired(BigDecimal amount,Account sender,Account receiver){
        if (checkSenderBalance(sender,amount)){
            sender.setBalance(sender.getBalance().subtract(amount));

            receiver.setBalance(receiver.getBalance().add(amount));
        }else {
            throw new BalanceNotSufficientException("Balance is not enough for this transfer");
        }
    }



    private void validateAccount(Account sender,Account receiver){

        if (sender==null||receiver==null){
            throw new BadRequestException("Sender account needs to be different than receiver account");
        }

        findAccountById(sender.getId());
        findAccountById(receiver.getId());


    }

    private boolean checkSenderBalance(Account sender,BigDecimal amount){
        return sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >=0;
    }

    private void checkAccountOwnerShip(Account sender,Account receiver) {
        if((sender.getAccountType().equals(AccountType.SAVING)||receiver.getAccountType().equals(AccountType.SAVING))
                && !sender.getUserId().equals(receiver.getUserId())){
            throw new AccountOwnerShipException("If one of the account is saving, user must be the same for sender and receiver");
        }
    }

        private void findAccountById(UUID id) {
        accountRepository.findById(id);

    }

    @Override
    public List<Transaction> findAllTransaction(){
        return transactionRepository.findAll();


    }

    @Override
    public List<Transaction> last10Transactions(){
        return transactionRepository.findLast10Transactions();

    }

    @Override
    public List<Transaction> findTransactionListById(UUID id){
        return transactionRepository.findTransactionListByAccountId(id);
    }


}
