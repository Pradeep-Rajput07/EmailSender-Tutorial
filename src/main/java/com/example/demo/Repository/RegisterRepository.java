package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.RegisterEntity;

import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<RegisterEntity, Long> {
    Optional<RegisterEntity> findByEmail(String email);
}
