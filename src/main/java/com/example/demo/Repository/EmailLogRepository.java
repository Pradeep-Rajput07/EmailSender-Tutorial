package com.example.demo.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.EmailLogEntity;

import java.util.List;

public interface EmailLogRepository extends JpaRepository<EmailLogEntity, Long> {
    List<EmailLogEntity> findByRecipient(String recipient);
}
