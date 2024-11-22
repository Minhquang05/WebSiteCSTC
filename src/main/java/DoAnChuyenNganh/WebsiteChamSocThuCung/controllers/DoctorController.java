package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Product;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Hiển thị danh sách bác sĩ
    @GetMapping
    public String getAllDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "doctors/doctors-list";
    }

    // Hiển thị form thêm bác sĩ mới
    @GetMapping("/add")
    public String getAddDoctorPage(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctors/add-doctor";
    }

    // Xử lý lưu thông tin bác sĩ mới
    @PostMapping("/add")
    public String createDoctor(@ModelAttribute Doctor doctor) {
        doctorService.saveDoctor(doctor);
        return "redirect:/doctors";
    }

    // Hiển thị form chỉnh sửa thông tin bác sĩ
    @GetMapping("/edit/{id}")
    public String getEditDoctorPage(@PathVariable Long id, Model model) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        if (doctor.isPresent()) {
            model.addAttribute("doctor", doctor.get());
            return "doctors/edit-doctor"; // Trang chỉnh sửa
        }
        return "redirect:/doctors";
    }

    // Xử lý cập nhật thông tin bác sĩ
    @PostMapping("/edit/{id}")
    public String updateDoctor(@PathVariable Long id, @ModelAttribute Doctor updatedDoctor) {
        Optional<Doctor> existingDoctor = doctorService.getDoctorById(id);
        if (existingDoctor.isPresent()) {
            Doctor doctor = existingDoctor.get();
            doctor.setName(updatedDoctor.getName());
            doctor.setSpecialization(updatedDoctor.getSpecialization());
            doctor.setPhone(updatedDoctor.getPhone());
            doctor.setEmail(updatedDoctor.getEmail());
            doctorService.saveDoctor(doctor);
        }
        return "redirect:/doctors"; // Sau khi cập nhật xong, chuyển về trang danh sách
    }

    // Hiển thị trang xác nhận xóa bác sĩ
    @GetMapping("/delete/{id}")
    public String getDeleteDoctorPage(@PathVariable Long id, Model model) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        if (doctor.isPresent()) {
            model.addAttribute("doctor", doctor.get());
            return "doctors/confirm-delete"; // Trang xác nhận xóa
        }
        return "redirect:/doctors";
    }

    // Xử lý xóa bác sĩ
    @PostMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/doctors"; // Sau khi xóa xong, chuyển về trang danh sách
    }

    @GetMapping("/details/{id}")
    public String showDoctorDetails(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor Id:" + id));
        model.addAttribute("doctor", doctor);
        return "/doctors/doctor-details";
    }

}
