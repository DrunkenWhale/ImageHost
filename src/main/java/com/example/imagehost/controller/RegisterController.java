package com.example.imagehost.controller;


import com.example.imagehost.model.User;
import com.example.imagehost.repository.UserRepository;
import com.example.imagehost.util.IdentifyCodeMap;
import com.example.imagehost.util.PasswordEncryptor;
import com.example.imagehost.util.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/auth/register")
@RestController
public class RegisterController {

    final
    UserRepository userRepository;

    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseBody
    public BaseResponse register(
            @RequestParam(value = "mailbox"      ,required = true) String mailbox,
            @RequestParam(value = "password"     ,required = true) String password,
            @RequestParam(value = "identify_code",required = true) int    identify_code){
//        springboot会自动判定参数是否缺失 好耶！
        if (!IdentifyCodeMap.identifyCodeMap.containsKey(mailbox)){
            // 未获取过验证码
            return new BaseResponse(0,"InvalidArgument");
        }
        if (new Date().getTime()-IdentifyCodeMap.identifyTimeMap.get(mailbox)>=7*60*1000){
            // 验证码超过七分钟 过期无效
            return new BaseResponse(0,"ExceedTimeLimit");
        }
        if(userRepository.findById(mailbox).isPresent()){
            // 用户已存在
            return new BaseResponse(0,"UserExist");
        }
        if (IdentifyCodeMap.identifyCodeMap.get(mailbox)==identify_code){
            // 验证码正确
            userRepository.save(new User(mailbox, PasswordEncryptor.generatorPasswordHash(password)));
            return new BaseResponse(1,"Succeed");
        }else{
            return new BaseResponse(0,"WrongIdentifyCode");
        }
    }

}
