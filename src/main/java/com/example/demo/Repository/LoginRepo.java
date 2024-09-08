package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entity.RegisterEntity;

import java.util.Optional;

public interface LoginRepo extends JpaRepository<RegisterEntity, Long> {
    Optional<RegisterEntity> findByEmail(String email);
}
