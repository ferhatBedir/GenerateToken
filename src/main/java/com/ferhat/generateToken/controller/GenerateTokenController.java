package com.ferhat.generateToken.controller;

import com.ferhat.generateToken.model.UserInfo;
import com.ferhat.generateToken.service.GenerateTokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/createtoken")
public class GenerateTokenController {

    private GenerateTokenService generateTokenService;

    @Autowired
    public GenerateTokenController(GenerateTokenService generateTokenService) {
        this.generateTokenService = generateTokenService;
    }

    @PostMapping()
    public String createToken(@RequestBody UserInfo userInfo) {
        return generateTokenService.create(userInfo);
    }

    @GetMapping()
    public Claims decodeCreatedToken(@RequestParam(value = "token") String token) {
        return generateTokenService.decodeToken(token);
    }

}
