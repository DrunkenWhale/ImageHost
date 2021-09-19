package com.example.imagehost.util.response;

import java.util.HashMap;

// 带有具体数据的响应

public class DataResponse<T> extends BaseResponse{

    public HashMap<String,T> map;

    public DataResponse(int status, String message, HashMap<String,T> map) {
        super(status, message);
        this.map = map;
    }

}
