package com.example.bank.exception;

public class UserIsBannedException extends Exception{
    public UserIsBannedException(String msg) {
        super(msg);
    }
}
