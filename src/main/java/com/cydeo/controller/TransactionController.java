package com.cydeo.controller;

import com.cydeo.dto.AccountDTO;
import com.cydeo.dto.TransactionDTO;
import com.cydeo.service.AccountService;
import com.cydeo.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

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
        model.addAttribute("transactionDTO", new TransactionDTO());
        model.addAttribute("accounts",accountService.listAllAccount());
        model.addAttribute("lastTransactions",transactionService.last10Transactions());

        return "transaction/make-transfer";
    }


    @PostMapping("/transfer")
    public String makeTransfer(@ModelAttribute("transactionDTO")@Valid TransactionDTO transactionDTO, BindingResult bindingResult,Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("accounts",accountService.listAllAccount());
            model.addAttribute("lastTransactions",transactionService.last10Transactions());
            return "transaction/make-transfer";
        }

        AccountDTO sender=accountService.retrieveByID(transactionDTO.getSender().getId());
        AccountDTO receiver=accountService.retrieveByID(transactionDTO.getReceiver().getId());
        transactionService.makeTransfer(sender,receiver,transactionDTO.getAmount(),new Date(),transactionDTO.getMessage());
        return "redirect:/make-transfer";
    }

    @GetMapping("/transaction/{id}")
    public String getTransactionList(@PathVariable("id") Long id, Model model){
        System.out.println(id);
        model.addAttribute("transactions",transactionService.findTransactionListById(id));
        return "transaction/transactions";
    }

}
