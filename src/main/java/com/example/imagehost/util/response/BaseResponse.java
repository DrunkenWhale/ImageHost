package com.example.imagehost.util.response;


import java.io.Serializable;

/* 反回的时候构建相同的对象 */
/* 所有的返回都继承这个对象 */
public class BaseResponse implements Serializable {

    public int status;
    public String message;

    public BaseResponse(int status,String message){
        this.message = message;
        this.status = status;
    }

}
