package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.User;
import com.projects.storemanagement.exception.UserNotFoundException;
import com.projects.storemanagement.exception.UsernameAlreadyExistsException;
import com.projects.storemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(User user) {
        String username = user.getUsername();
        if(userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }

        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User user) {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        User existingUser = userRepository.findById(id).get();
        if(isUsernameNotValid(existingUser.getUsername(), user.getUsername())) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }

        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(existingUser);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private boolean isUsernameNotValid(String oldUsername, String newUsername) {
        return (!oldUsername.equals(newUsername)) && userRepository.existsByUsername(newUsername);
    }

}
