package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.servicies.AdminService;
import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;


    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;

    }

    @GetMapping()
    public String index(Model model, Principal principal) {
        model.addAttribute("currentUser", adminService.findByEmail(principal.getName()));
        return "admin/index";
    }

}
