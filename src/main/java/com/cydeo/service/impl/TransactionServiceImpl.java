package com.cydeo.service.impl;

import com.cydeo.dto.AccountDTO;
import com.cydeo.dto.TransactionDTO;
import com.cydeo.entity.Transaction;
import com.cydeo.enums.AccountType;
import com.cydeo.exception.AccountOwnerShipException;
import com.cydeo.exception.BadRequestException;
import com.cydeo.exception.BalanceNotSufficientException;
import com.cydeo.exception.UnderConstructionException;
import com.cydeo.mapper.TransactionMapper;
import com.cydeo.repository.AccountRepository;
import com.cydeo.repository.TransactionRepository;
import com.cydeo.service.AccountService;
import com.cydeo.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TransactionServiceImpl implements TransactionService {

    @Value("${under_construction}")
    private boolean underConstruction;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;


    public TransactionServiceImpl(AccountService accountService, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public TransactionDTO makeTransfer(AccountDTO sender, AccountDTO receiver, BigDecimal amount, Date creationDate, String message) {
        if (!underConstruction) {

            validateAccount(sender, receiver);
            checkAccountOwnerShip(sender, receiver);
            executeBalanceAndUpdateIfRequired(amount, sender, receiver);

            TransactionDTO transactionDTO = new TransactionDTO(sender,receiver,amount,message,creationDate);
            return transactionRepository.save(transactionMapper.convertToEntity(transactionDTO);

        } else {
            throw new UnderConstructionException("App is under construction,please try again later.");
        }
    }

    private void executeBalanceAndUpdateIfRequired(BigDecimal amount,AccountDTO sender,AccountDTO receiver){
        if (checkSenderBalance(sender,amount)){
            sender.setBalance(sender.getBalance().subtract(amount));

            receiver.setBalance(receiver.getBalance().add(amount));
        }else {
            throw new BalanceNotSufficientException("Balance is not enough for this transfer");
        }
    }



    private void validateAccount(AccountDTO sender,AccountDTO receiver){

        if (sender==null||receiver==null){
            throw new BadRequestException("Sender account needs to be different than receiver account");
        }

        findAccountById(sender.getId());
        findAccountById(receiver.getId());


    }

    private boolean checkSenderBalance(AccountDTO sender,BigDecimal amount){
        return sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >=0;
    }

    private void checkAccountOwnerShip(AccountDTO sender,AccountDTO receiver) {
        if((sender.getAccountType().equals(AccountType.SAVING)||receiver.getAccountType().equals(AccountType.SAVING))
                && !sender.getUserId().equals(receiver.getUserId())){
            throw new AccountOwnerShipException("If one of the account is saving, user must be the same for sender and receiver");
        }
    }

        private void findAccountById(Long id) {
        accountService.retrieveByID(id);

    }



    @Override
    public List<TransactionDTO> findAllTransaction(){
        return transactionRepository.findAll().stream().map(transactionMapper::convertToDTO).collect(Collectors.toList());


    }

    @Override
    public List<TransactionDTO> last10Transactions(){

        List<Transaction> last10Transactions=transactionRepository.findLast10Transactions();
        return last10Transactions.stream().map(transactionMapper::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public List<Transaction> findTransactionListById(Long id){
        List<Transaction> transactionList=transactionRepository.findTransactionListByAccountId(id);
        return transactionList.stream().map(transactionMapper::convertToDTO).collect(Collectors.toList());
    }

}
