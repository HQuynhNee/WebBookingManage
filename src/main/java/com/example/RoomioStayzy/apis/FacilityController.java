package com.example.RoomioStayzy.apis;

import com.example.RoomioStayzy.entities.Facility;
import com.example.RoomioStayzy.services.FacilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping
    public ResponseEntity<List<Facility>> getAllFacilities() {
        List<Facility> facilities = facilityService.getAllFacilities();
        return new ResponseEntity<>(facilities, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Facility> createFacility(@RequestBody Facility facility) {
        Facility savedFacility = facilityService.saveFacility(facility);
        return new ResponseEntity<>(savedFacility, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable Long id) {
        Facility facility = facilityService.getFacilityById(id);
        if (facility == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facility);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facility> updateFacility(@PathVariable Long id, @RequestBody Facility facility) {
        facility.setId(id); // Ensure the ID stays the same
        Facility updatedFacility = facilityService.saveFacility(facility);
        return new ResponseEntity<>(updatedFacility, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFacility(@PathVariable Long id) {
        if (facilityService.getFacilityById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        facilityService.deleteFacility(id);
        return ResponseEntity.ok("Facility with ID " + id + " has been deleted.");
    }
}
