package com.example.RoomioStayzy.repositories;

import com.example.RoomioStayzy.entities.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HousingRepository extends JpaRepository<Housing, Long> {
    List<Housing> findByAddressContainingAndStatusAndHouseType(String address, Housing.Status status, Housing.HouseType houseType);

    List<Housing> findByAddressContainingAndStatus(String address, Housing.Status status);

    List<Housing> findByAddressContainingAndHouseType(String address, Housing.HouseType houseType);

    List<Housing> findByStatusAndHouseType(Housing.Status status, Housing.HouseType houseType);

    List<Housing> findByAddressContaining(String address);

    @Query("SELECT h FROM Housing h WHERE h.status = :status ORDER BY h.createdAt DESC")
    List<Housing> findByStatusOrderByCreatedAtDesc(@Param("status") Housing.Status status);

    List<Housing> findByHouseType(Housing.HouseType houseType);

    List<Housing> findByOwnerId(Long ownerId);

    @Query("SELECT h FROM Housing h LEFT JOIN FETCH h.facilities WHERE h.id = :id")
    Optional<Housing> findByIdWithFacilities(Long id);

    @Query("SELECT h FROM Housing h WHERE h.houseType = :houseType AND h.address LIKE %:address% AND h.status = :status ORDER BY h.createdAt DESC")
    List<Housing> findByHouseTypeAndAddressLike(@Param("houseType") Housing.HouseType houseType,
                                                @Param("address") String address,
                                                @Param("status") Housing.Status status);
    @Query("SELECT h FROM Housing h ORDER BY h.createdAt DESC")
    List<Housing> getAllHouses();
}