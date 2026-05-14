package com.tellme.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellme.model.User;
import com.tellme.repository.UserRepository;
import com.tellme.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {

        if(userRepository.findByEmail(
                user.getEmail()
        ) != null){

            throw new RuntimeException(
                    "Email sudah digunakan"
            );
        }

        if(userRepository.findByNim(
                user.getNim()
        ) != null){

            throw new RuntimeException(
                    "NIM sudah digunakan"
            );
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));
    }

    @Override
    public User updateUser(Long id, User user) {

        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        existing.setNama(user.getNama());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        existing.setRole(user.getRole());

        return userRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        if(user.getRole().equals("ADMIN")){
            throw new RuntimeException("Admin tidak bisa dihapus");
        }

        userRepository.deleteById(id);
    }

    @Override
    public User login(
            String identifier,
            String password) {

        User user =
                userRepository
                .findByEmailAndPassword(
                        identifier,
                        password
                );

        if(user == null){

            user =
                    userRepository
                    .findByNimAndPassword(
                            identifier,
                            password
                    );
        }

        if(user == null){

            throw new RuntimeException(
                    "Email/NIM atau password salah"
            );
        }

        return user;
    }
}