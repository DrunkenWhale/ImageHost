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
            simpleMailMessage.setFrom("OddImageHost@163.com");
            // 收件人
            simpleMailMessage.setTo(mailbox);
            // 邮件标题
            simpleMailMessage.setSubject("验证注册");
            // 生成验证码
            Integer identifyCode = (Integer)(int) (Math.random() * 9999999);
            // 邮件内容
            simpleMailMessage.setText("亲爱的"+mailbox+":您正在注册一个奇妙的图床账号，这里有一串神奇的代码，可以保证您之后畅通无阻，请记住他哦~:"
                    + identifyCode + "。请于七分钟内使用这串神奇代码进行来获取您的专属通行权限~~ヾ(✿ﾟ▽ﾟ)ノ" );
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
