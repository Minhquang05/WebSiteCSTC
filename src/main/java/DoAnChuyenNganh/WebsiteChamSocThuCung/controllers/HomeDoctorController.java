package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Product;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    @GetMapping("/details/{id}")
    public String showDoctorDetails(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor Id:" + id));
        model.addAttribute("doctor", doctor);
        return "/doctors/doctor-details";
    }
}