package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeDoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/homedoctors")
    public String showDoctorList(@Param("keyword") String keyword, Model model) {
        List<Doctor> doctorList = new ArrayList<>();
        doctorList.addAll(doctorService.getAllDoctors());

        model.addAttribute("doctors", doctorList);
        return "/doctors/homedoctors";
    }
}
