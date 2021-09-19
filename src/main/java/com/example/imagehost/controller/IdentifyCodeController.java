package com.example.imagehost.controller;

import com.example.imagehost.util.IdentifyCodeMap;
import com.example.imagehost.util.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/auth/identify")
@RestController
public class IdentifyCodeController {

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping
    @ResponseBody
    public BaseResponse getIdentifyCode (
            @RequestParam("mailbox") String mailbox) throws MailException {

        if (IdentifyCodeMap.identifyCodeMap.containsKey(mailbox) &&
                new Date().getTime()-IdentifyCodeMap.identifyTimeMap.get(mailbox)<=2*60*1000){
            // 之前发送过验证码 且间隔小于120s
            return new BaseResponse(0,"FrequentSubmission");
        }

        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            // 发件人
            simpleMailMessage.setFrom("ReciteWord@163.com");
            // 收件人
            simpleMailMessage.setTo(mailbox);
            // 邮件标题
            simpleMailMessage.setSubject("验证注册");
            // 生成验证码
            Integer identifyCode = (Integer)(int) (Math.random() * 9999999);
            // 邮件内容
            simpleMailMessage.setText("您的验证码是" + identifyCode + "(我想不出好文案了)");
            javaMailSender.send(simpleMailMessage);
            IdentifyCodeMap.identifyCodeMap.put(mailbox,identifyCode);
            IdentifyCodeMap.identifyTimeMap.put(mailbox,new Date().getTime());
            return new BaseResponse(1,"Succeed");
        }catch (MailException err){
            err.printStackTrace();
            return new BaseResponse(0,"UnknownError");
        }
    }


}
