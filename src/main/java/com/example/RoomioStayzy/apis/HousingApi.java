package com.example.RoomioStayzy.apis;


import com.example.RoomioStayzy.entities.Housing;
import com.example.RoomioStayzy.services.HousingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/house")
public class HousingApi {

    private final HousingService housingService;

    public HousingApi(HousingService housingService) {
        this.housingService = housingService;
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Housing>> getHousingsByStatus(@PathVariable("status") String status) {
        try {
            Housing.Status housingStatus = Housing.Status.valueOf(status.toUpperCase());
            List<Housing> housings = housingService.getHousingsByStatus(housingStatus);
            return new ResponseEntity<>(housings, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Housing> updateHousingStatus(@PathVariable Long id, @RequestParam("status") String status) {
        try {
            Housing.Status housingStatus = Housing.Status.valueOf(status.toUpperCase());
            Housing housing = housingService.getHousingById(id);
            if (housing == null) {
                return ResponseEntity.notFound().build();
            }
            housing.setStatus(housingStatus);
            Housing updatedHousing = housingService.updateStatus(housing);
            return new ResponseEntity<>(updatedHousing, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Invalid status
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHousing(@PathVariable Long id) {
        Housing housing = housingService.getHousingById(id);
        if (housing == null) {
            return ResponseEntity.notFound().build();
        }
        housingService.deleteHousing(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("")
    public ResponseEntity<List<Housing>> getAllHousings() {
        List<Housing> housings = housingService.getAllHousings();
        return new ResponseEntity<>(housings, HttpStatus.OK);
    }
}
