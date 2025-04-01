package com.example.RoomioStayzy.repositories;

import com.example.RoomioStayzy.entities.Facility;
import com.example.RoomioStayzy.entities.Housing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    List<Facility> findByHousing(Housing housing);
    List<Facility> findByAvailable(Boolean available);
    List<Facility> findByHousingAndAvailable(Housing housing, Boolean available);
    List<Facility> findByNameContainingIgnoreCase(String name);
}
