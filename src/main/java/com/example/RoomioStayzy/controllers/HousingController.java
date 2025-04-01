package com.example.RoomioStayzy.controllers;

import com.example.RoomioStayzy.entities.Housing;
import com.example.RoomioStayzy.entities.User;
import com.example.RoomioStayzy.services.FacilityService;
import com.example.RoomioStayzy.services.HousingService;
import com.example.RoomioStayzy.services.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Base64;

@Controller
@RequestMapping("/house/")
public class HousingController {

    private static final String HOUSING_PATH = "house/";

    @Autowired
    private HousingService housingService;

    @Autowired
    private  FacilityService facilityService;



    @GetMapping("add")
    public String showAddHousingForm(Model model) {
        model.addAttribute("housing", new Housing());
        return HOUSING_PATH + "add";
    }
    @PostMapping("add")
    public String addHousing(@ModelAttribute Housing housing, Model model, @RequestParam("image") MultipartFile image) {
        try {
            if (housing.getFacilities() != null) {
                housing.getFacilities().removeIf(facility -> facility.getName() == null || facility.getName().isEmpty());
            }
            if (!image.isEmpty()) {
                String imagePath = saveImage(image);
                housing.setImage_url(imagePath);
            }
            housingService.addHousingAndFacilities(housing);
            model.addAttribute("message", "Housing and Facilities added successfully!");
            return "redirect:/owner";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "Error adding Housing or Facilities: " + e.getMessage());
            return "error";
        }
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Housing housing = housingService.getHousingById(id);
        if (housing == null) {
            model.addAttribute("message", "Housing not found!");
            return "error";
        }

        model.addAttribute("housing", housing);
        return "house/edit";
    }
    @PostMapping("/edit/{id}")
    public String updateHousing(@PathVariable Long id, @ModelAttribute Housing housing,
                                @RequestParam Long owner_id,
                                Model model,
                                @RequestParam(value = "image", required = false) MultipartFile image,
                                @RequestParam String old_image) {
        try {
            housing.setId(id);
            if (image != null && !image.isEmpty()) {
                String imagePath = saveImage(image);
                housing.setImage_url(imagePath); // Set the new image URL
            } else {
                housing.setImage_url(old_image);
            }
            housingService.updateHousing(housing, owner_id);
            model.addAttribute("message", "Housing updated successfully!");
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("message", "Error updating Housing: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }


    @GetMapping("/delete/{id}")
    public String deleteHousing(@PathVariable Long id, Model model) {
        try {
            housingService.deleteHousing(id);
            model.addAttribute("message", "Housing deleted successfully!");
            return "redirect:/house/list";
        } catch (Exception e) {
            model.addAttribute("message", "Error deleting Housing: " + e.getMessage());
            return "error";
        }
    }
    @GetMapping("/detail/{id}")
    public String showHousingDetails(@PathVariable Long id, Model model) {
        Housing housing = housingService.getHousingById(id);
        if (housing == null) {
            model.addAttribute("message", "Housing not found!");
            return "error";
        }
        model.addAttribute("housing", housing);
        String locationString = housing.getLatitude() + ", " + housing.getLongitude();
        System.out.println("Asdf "+ locationString);
        String base64EncodedLocation = Base64.getEncoder().encodeToString(locationString.getBytes());
        System.out.println("Asdf "+ base64EncodedLocation);
        model.addAttribute("base64",base64EncodedLocation);
        return "house/detail";
    }


    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploads/images/";
        File uploadDirPath = new File(uploadDir);
        if (!uploadDirPath.exists()) {
            uploadDirPath.mkdirs();
        }
        String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        File imageFile = new File(uploadDir + imageName);
        image.transferTo(imageFile);
        return "/uploads/images/" + imageName;
    }

}
