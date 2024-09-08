package com.example.demo.Repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.EmailEntity;


@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

	boolean existsByEmailId(String emailId);
   
}
