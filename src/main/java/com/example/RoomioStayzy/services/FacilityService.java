package com.example.RoomioStayzy.services;

import com.example.RoomioStayzy.entities.Facility;
import com.example.RoomioStayzy.entities.Housing;
import com.example.RoomioStayzy.repositories.FacilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }
    public List<Facility> getFacilitiesByHousing(Housing housing) {
        return facilityRepository.findByHousing(housing);
    }

    public Facility getFacilityById(Long id) {
        Optional<Facility> facility = facilityRepository.findById(id);
        return facility.orElse(null);
    }

    public Facility saveFacility(Facility facility) {
        return facilityRepository.save(facility);
    }
    public void deleteFacility(Long id) {
        facilityRepository.deleteById(id);
    }
}
