package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/access-denied")
    public String showPage(){
        return "/error/index";
    }
}