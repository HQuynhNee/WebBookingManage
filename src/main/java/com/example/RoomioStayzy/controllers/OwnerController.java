package com.example.RoomioStayzy.controllers;
import com.example.RoomioStayzy.entities.Housing;
import com.example.RoomioStayzy.services.HousingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private HousingService housingService;
    @GetMapping()
    public String home(Model model) {
        List<Housing> housingList = housingService.getAllHousingByOwner();
        model.addAttribute("housings", housingList);
        return "owner/index";
    }
    @GetMapping("/home")
    public String homepage() {
        return "owner/home";
    }
}
