package com.devconnect.bakend.exceptions;

public class NotValidUser extends RuntimeException {
    public NotValidUser(String message) {
        super(message);
    }
}
