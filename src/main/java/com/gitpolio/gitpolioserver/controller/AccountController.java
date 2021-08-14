package com.gitpolio.gitpolioserver.controller;

import com.gitpolio.gitpolioserver.dto.AccountDto;
import com.gitpolio.gitpolioserver.dto.LoginInfoDto;
import com.gitpolio.gitpolioserver.dto.RegisterInfoDto;
import com.gitpolio.gitpolioserver.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountDto> register(@Valid @RequestBody RegisterInfoDto registerInfoDto) {
        return ResponseEntity.ok(accountService.register(registerInfoDto));
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginInfoDto loginInfoDto) {
        return ResponseEntity.ok(accountService.login(loginInfoDto));
    }
}
