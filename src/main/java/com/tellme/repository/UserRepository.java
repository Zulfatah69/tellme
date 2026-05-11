package com.tellme.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellme.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailAndPassword(String email, String password);

    User findByNimAndPassword(String nim, String password);

    User findByEmail(String email);

    User findByNim(String nim);
}