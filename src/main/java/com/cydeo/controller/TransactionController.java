package com.cydeo.controller;

import com.cydeo.model.Transaction;
import com.cydeo.service.AccountService;
import com.cydeo.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransactionController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransactionController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/make-transfer")
    public String getMakeTransfer(Model model){
        model.addAttribute("transaction", Transaction.builder().build());
        model.addAttribute("accounts",accountService.listAllAccount());
        model.addAttribute("lastTransactions",transactionService.last10Transactions());

        return "transaction/make-transfer";
    }


}
