package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.servicies.AdminService;
import ru.kata.spring.boot_security.demo.servicies.RoleService;

import javax.annotation.PostConstruct;

@Component
public class TestClass {

    private final AdminService adminService;
    private final RoleService roleService;


    @Autowired
    public TestClass(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }


    @PostConstruct
    public void init() {

        if (
                (roleService.findAll().size() != 2) &&
                (adminService.findByEmail("admin@admin") == null) &&
                (adminService.findByEmail("petrov@petrov") == null) &&
                (adminService.findByEmail("fisher@fisher") == null)
        ) {

            Role adminRole = new Role("ROLE_ADMIN"); // создаём роль АДМИН
            Role userRole = new Role("ROLE_USER"); // создаём роль ЮЗЕР
            roleService.addRole(adminRole); // добавляем роль в базу
            roleService.addRole(userRole); // добавляем роль в базу
            User admin = new User("Artem", "Kuzmin", "admin@admin", 31); // создаём Админа, логин - admin@admin
            admin.setPassword("100"); //пароль админа - "100"
            admin.addRole(adminRole); // назначем роль Админу
            adminService.addUser(admin); // добавялем админа в базу
            User user1 = new User("Ivan", "Petrov", "petrov@petrov", 24); // создаём Юзера, логин - petrov@petrov
            user1.setPassword("100"); //пароль юзера - "100"
            user1.addRole(userRole); // назначаем роль юзеру
            adminService.addUser(user1); // добавляем юзера в базу
            User comboUser = new User("Sam", "Fisher", "fisher@fisher", 54); // создаём юзера с двумя ролями, логин - fisher@fisher
            comboUser.setPassword("100"); // пароль юзера - "100"
            comboUser.addRole(adminRole); // назначем роль админа
            comboUser.addRole(userRole); // назначем роль юзера
            adminService.addUser(comboUser); // добавляем юзера в базу
        }



    }

}
