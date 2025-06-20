package com.airwaymanagement.authservice.repository;

import com.airwaymanagement.authservice.model.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> { }
