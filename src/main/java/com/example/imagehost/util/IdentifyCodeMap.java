package com.example.imagehost.util;

import java.util.HashMap;

// 用于储存
// - 用户邮箱与验证码的键值对
// - 用户邮箱与获取验证码时间的键值对

public class IdentifyCodeMap {
    public static HashMap<String,Integer> identifyCodeMap = new HashMap<>();
    public static HashMap<String, Long> identifyTimeMap = new HashMap<>();
}
