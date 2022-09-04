package ru.kata.spring.boot_security.demo.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTOConverter;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.servicies.AdminService;
import ru.kata.spring.boot_security.demo.servicies.RoleService;
import ru.kata.spring.boot_security.demo.util.validation.UserDataValidator;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.exceptions.UserDataException;
import ru.kata.spring.boot_security.demo.util.validation.EmailAlreadyExistsValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainRestController {

    private final AdminService adminService;
    private final RoleService roleService;


    private final UserDTOConverter userDTOConverter;

    private final EmailAlreadyExistsValidator emailAlreadyExistsValidator;

    private final UserDataValidator userDataValidator;

    @Autowired
    public MainRestController(AdminService adminService, RoleService roleService,
                              UserDTOConverter userDTOConverter,
                              EmailAlreadyExistsValidator emailAlreadyExistsValidator,
                              UserDataValidator userDataValidator) {
        this.adminService = adminService;
        this.roleService = roleService;
        this.userDTOConverter = userDTOConverter;
        this.emailAlreadyExistsValidator = emailAlreadyExistsValidator;
        this.userDataValidator = userDataValidator;
    }


    @GetMapping()
    public ResponseEntity<List<UserDTO>> show() {
        List<UserDTO> userDTOList = adminService.showUsers().stream()
                .map(userDTOConverter::convertToUserDTO).toList();
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> allRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> showUser(@PathVariable("id") Long id) {
        UserDTO userDTO = userDTOConverter.convertToUserDTO(adminService.getUserById(id));
        return new ResponseEntity<>(userDTO, HttpStatus.OK);

    }

    @PatchMapping("/patch")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid User user,
                                                 BindingResult bindingResult) {
        emailAlreadyExistsValidator.validate(user, bindingResult);
        userDataValidator.validateUserFields(bindingResult);
        adminService.updateUser(user);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid User user,
                                             BindingResult bindingResult) {
        emailAlreadyExistsValidator.validate(user, bindingResult);
        userDataValidator.validateUserFields(user.getPassword(), bindingResult);
        adminService.addUser(user);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        adminService.deleteUserById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<User> showUser(Principal principal) {
        return new ResponseEntity<>((adminService.findByEmail(principal.getName())), HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleUserDataException(UserDataException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

}
