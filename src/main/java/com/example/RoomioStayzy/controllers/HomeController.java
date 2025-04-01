package com.example.RoomioStayzy.controllers;

import com.example.RoomioStayzy.entities.Housing;
import com.example.RoomioStayzy.services.HousingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private HousingService housingService;
    @GetMapping("/home")
    public String home(Model model,
                       @RequestParam(required = false) String roomCode,
                       @RequestParam(required = false) String area) {
        List<Housing> housingList;
        if (roomCode != null || area != null) {
            assert roomCode != null;
//            housingList= housingService.getAllHousings();
            housingList = housingService.getHousingByTypeAndAddress(roomCode, area, Housing.Status.APPROVED);
        } else {
//            housingList= housingService.getAllHousings();
            housingList = housingService.getHousingsByStatus(Housing.Status.APPROVED);
        }
        model.addAttribute("houses", housingList);
        return "user/homepage";
    }

}
