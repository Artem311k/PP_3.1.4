package ru.kata.spring.boot_security.demo.util.exceptions;

public class UserDataException extends RuntimeException {
    public UserDataException(String msg) {
        super(msg);
    }
}
