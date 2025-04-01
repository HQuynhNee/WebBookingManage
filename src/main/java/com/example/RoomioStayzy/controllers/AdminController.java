package com.example.RoomioStayzy.controllers;


import com.example.RoomioStayzy.entities.Housing;
import com.example.RoomioStayzy.services.HousingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private HousingService housingService;

    @GetMapping("")
    public String home(Model model,
                       @RequestParam(required = false) String roomCode,
                       @RequestParam(required = false) String area) {
        List<Housing> housingList;
        if (roomCode != null || area != null) {
            assert roomCode != null;
            housingList = housingService.getHousingByTypeAndAddress(roomCode, area, Housing.Status.PENDING);
        } else {
            housingList = housingService.getHousingsByStatus(Housing.Status.PENDING);
        }

        model.addAttribute("housingList", housingList);
        return "admin/index";
    }
    @GetMapping("/home")
    public String homepage() {
        return "admin/home";
    }


}
