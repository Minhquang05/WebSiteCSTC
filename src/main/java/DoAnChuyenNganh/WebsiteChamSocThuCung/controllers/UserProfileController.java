package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.UserProfile;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;
    //    @GetMapping("/profiles")
//    public String showprofiles(@ModelAttribute UserProfile userProfile) {
//        userProfileService.getUserProfile(userProfile.getId());
//        return "redirect:/profiles";
//    }
    @GetMapping("/profiles/{userId}")
    public String viewProfile(@ModelAttribute UserProfile userProfile) {
        userProfileService.getUserProfile(userProfile.getId());
        return "userProfile";  // Trả về trang userProfile.html
    }
}