package com.localcentraldigital.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.localcentraldigital.ems.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {

    boolean existsByNameIgnoreCase(String name);
}
