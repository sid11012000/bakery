package com.example.bakery.repository;




import com.example.bakery.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByNameAndPassword(String name, String password);
}

