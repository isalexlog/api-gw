package io.kadmos.savings.controller;

import io.kadmos.savings.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final AccountService accountService;

    public BalanceController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public BigDecimal getBalance() {
        return accountService.getBalance();
    }

    @PostMapping
    public BigDecimal changeBalance(@RequestBody BalanceDto balanceDto) {
        return accountService.changeBalance(balanceDto.getAmount());
    }
}
