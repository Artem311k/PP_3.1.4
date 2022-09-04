package ru.kata.spring.boot_security.demo.servicies;


import ru.kata.spring.boot_security.demo.models.User;

public interface UserService {

    User findByEmail(String email);


}
