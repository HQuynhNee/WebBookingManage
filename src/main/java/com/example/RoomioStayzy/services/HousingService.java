package com.example.RoomioStayzy.services;

import com.example.RoomioStayzy.entities.Facility;
import com.example.RoomioStayzy.entities.Housing;
import com.example.RoomioStayzy.entities.User;
import com.example.RoomioStayzy.repositories.FacilityRepository;
import com.example.RoomioStayzy.repositories.HousingRepository;
import com.example.RoomioStayzy.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class HousingService {

    private static final Logger log = LoggerFactory.getLogger(HousingService.class);
    private final HousingRepository housingRepository;
    private  final UserRepository userRepository;



    public HousingService(HousingRepository housingRepository, FacilityRepository facilityRepository, UserRepository userRepository) {
        this.housingRepository = housingRepository;
        this.userRepository = userRepository;
    }
    public Housing saveHousing(Housing housing) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = authentication.getName();
            User owner = userRepository.findByUsername(username);
            housing.setOwner(owner);
        } else {
            throw new RuntimeException("Authentication required");
        }
        return housingRepository.save(housing);
    }
    public List<Housing> getAllHousings() {
        return housingRepository.findAll();
    }
    public Housing updateStatus(Housing housing){
        return  housingRepository.save(housing);
    }
    public List<Housing> getAllHousingByOwner(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = authentication.getName();
            User owner = userRepository.findByUsername(username);
            return housingRepository.findByOwnerId(owner.getId());
        } else {
            throw new RuntimeException("Authentication required");
        }
    }
    public  List<Housing> getHousingsByStatus(Housing.Status status){
        return housingRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    public List<Housing> getHousingByTypeAndAddress(String roomCode, String area, Housing.Status status) {
        if (!roomCode.isEmpty() ) {
            return housingRepository.findByHouseTypeAndAddressLike(Housing.HouseType.valueOf(roomCode), area, status);
        } else {
            return housingRepository.findByHouseTypeAndAddressLike(null, area, status);
        }
    }

    public List<Housing> getHousingsByCriteria(String address, Housing.Status status, Housing.HouseType houseType) {
        if (address != null && status != null && houseType != null) {
            return housingRepository.findByAddressContainingAndStatusAndHouseType(address, status, houseType);
        } else if (address != null && status != null) {
            return housingRepository.findByAddressContainingAndStatus(address, status);
        } else if (address != null && houseType != null) {
            return housingRepository.findByAddressContainingAndHouseType(address, houseType);
        } else if (status != null && houseType != null) {
            return housingRepository.findByStatusAndHouseType(status, houseType);
        } else if (address != null) {
            return housingRepository.findByAddressContaining(address);
        } else if (status != null) {
            return housingRepository.findByStatusOrderByCreatedAtDesc(status);
        } else if (houseType != null) {
            return housingRepository.findByHouseType(houseType);
        } else {
            return housingRepository.findAll();
        }
    }
    public Housing getHousingById(Long id) {
        Optional<Housing> housing = housingRepository.findByIdWithFacilities(id);
        if (housing.isPresent()) {
            return housing.get();
        } else {
            throw new RuntimeException("Housing not found with ID: " + id);
        }
    }


    @Transactional
    public void addHousingAndFacilities(Housing housing) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = authentication.getName();
            User owner = userRepository.findByUsername(username);
            housing.setOwner(owner);
        } else {
            throw new RuntimeException("Authentication required");
        }
        for (Facility facility : housing.getFacilities()) {
            facility.setHousing(housing);
        }
        housingRepository.save(housing);
    }
    public void updateHousing(Housing housing, Long owner_id){
        Optional<User> owner = userRepository.findById(owner_id);
        housing.setOwner(owner.orElse(null));
        for (Facility facility : housing.getFacilities()) {
            facility.setHousing(housing);
        }
        housingRepository.save(housing);
    }
    public void deleteHousing(Long id){
        housingRepository.deleteById(id);
    }

}
