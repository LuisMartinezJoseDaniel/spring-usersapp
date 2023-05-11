package com.danny.backend.usersapp.services;

import com.danny.backend.usersapp.models.entities.User;

import com.danny.backend.usersapp.models.request.UserRequest;
import com.danny.backend.usersapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> update(UserRequest user, Long id) {
        Optional<User> userOptional= this.findById(id);
        if(userOptional.isEmpty()){
            return  Optional.empty();
        }
        User userDb = userOptional.orElseThrow();
        userDb.setUsername(user.getUsername());
        userDb.setEmail(user.getEmail());


        return Optional.of(this.save(userDb)); // Retorna Optional<User>
    }

    @Override
    @Transactional
    public void remove(Long id) {
        this.userRepository.deleteById(id);
    }
}
