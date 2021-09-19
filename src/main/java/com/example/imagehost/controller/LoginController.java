package com.example.imagehost.controller;


import com.example.imagehost.model.User;
import com.example.imagehost.repository.UserRepository;
import com.example.imagehost.util.JwtUtils;
import com.example.imagehost.util.response.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RequestMapping("/auth/login")
@RestController
public class LoginController {

    final    // 要记得加注解呐
    UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping
    @ResponseBody
    public TokenResponse login(
            @RequestParam(value = "mailbox" ,required = true) String mailbox,
            @RequestParam(value = "password",required = true) String password){
            System.out.println(userRepository);
            Optional<User> optionalUser = userRepository.findById(mailbox);
            if(optionalUser.isPresent()){
                // 用户存在
                User user = optionalUser.get();
                if (Objects.equals(user.getPassword(), password)){ // 使用equal可以安全判空
                    // 密码正确
                    return new TokenResponse(1,"Succeed", JwtUtils.generateJwt(mailbox));
                }else{
                    return new TokenResponse(0,"PasswordWrong",null);
                }
            }else{
                return new TokenResponse(0,"PasswordWrong",null);
            }
    }


}
