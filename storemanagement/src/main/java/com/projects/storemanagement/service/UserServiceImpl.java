package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.User;
import com.projects.storemanagement.exception.EmailAlreadyExistsException;
import com.projects.storemanagement.exception.UserNotFoundException;
import com.projects.storemanagement.exception.UsernameAlreadyExistsException;
import com.projects.storemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public User create(User user) {
        String username = user.getUsername();
        if(userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }

        String email = user.getEmail();
        if(userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(Long id, User user) {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        User existingUser = userRepository.findById(id).get();
        if(isUsernameNotValid(existingUser.getUsername(), user.getUsername())) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
        if(isEmailNotValid(existingUser.getEmail(), user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            return user.orElseThrow(UserNotFoundException::new);
        }
        throw new UserNotFoundException();
    }

    private boolean isUsernameNotValid(String oldUsername, String newUsername) {
        return (!oldUsername.equals(newUsername)) && userRepository.existsByUsername(newUsername);
    }

    private boolean isEmailNotValid(String oldEmail, String newEmail) {
        return (!oldEmail.equals(newEmail)) && userRepository.existsByEmail(newEmail);
    }

}
