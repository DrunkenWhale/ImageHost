package com.example.imagehost.util.response;

public class TokenResponse extends BaseResponse {

    public String token;

    public TokenResponse(int status, String message, String token) {
        super(status, message);
        this.token = token;
    }
    
}
