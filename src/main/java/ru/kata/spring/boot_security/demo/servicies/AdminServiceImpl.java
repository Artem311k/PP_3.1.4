package ru.kata.spring.boot_security.demo.servicies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositpries.UserRepository;
import ru.kata.spring.boot_security.demo.util.exceptions.UserNotFoundException;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Autowired
    public AdminServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> showUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void updateUser(User userToUpdate) {
        if (userToUpdate.getPassword().trim().equals("")) {
            userToUpdate.setPassword(userRepository.findById(userToUpdate.getId()).get().getPassword());
        } else {
            userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        }
        userRepository.save(userToUpdate);
    }

}
