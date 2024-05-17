package com.example.todo.userapi.repository;

import com.example.todo.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {
//    @Query("SELECT COUNT(*) FROM User u WHERE u.email = ?1")
//    int findByEmail(String email);
    boolean existsByEmail(String email);
}
