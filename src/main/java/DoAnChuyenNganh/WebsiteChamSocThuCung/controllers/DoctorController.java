
package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Category;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.WorkHour;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.DoctorService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.WorkHourService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private WorkHourService workHourService;

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
        model.addAttribute("workhours", workHourService.getAllWorkHour());
        return "doctors/add-doctor";
    }

    // Xử lý lưu thông tin bác sĩ mới
    @PostMapping("/add")
    public String createDoctor(@ModelAttribute @Valid Doctor doctor,
                               @RequestParam List<Long> workTimes) {
        Set<WorkHour> workHours = workTimes.stream()
                .map(workHourService::getWorkHourById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        doctor.setWorkTime(workHours);
        doctorService.saveDoctor(doctor);
        return "redirect:/doctors";
    }



    // Hiển thị form chỉnh sửa thông tin bác sĩ
    @GetMapping("/edit/{id}")
    public String getEditDoctorPage(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor id:" + id));
        model.addAttribute("doctor", doctor);
        model.addAttribute("workhours", workHourService.getAllWorkHour());  // Truyền danh sách giờ làm việc vào model
        return "/doctors/edit-doctor";
    }


    // Xử lý cập nhật thông tin bác sĩ
    @PostMapping("/edit/{id}")
    public String updateDoctor(@PathVariable Long id,
                               @ModelAttribute Doctor updatedDoctor,
                               @RequestParam("avatar") File avatar) {
        Optional<Doctor> existingDoctor = doctorService.getDoctorById(id);
        if (existingDoctor.isPresent()) {
            Doctor doctor = existingDoctor.get();
            doctor.setName(updatedDoctor.getName());
            doctor.setSpecialization(updatedDoctor.getSpecialization());
            doctor.setPhone(updatedDoctor.getPhone());
            doctor.setEmail(updatedDoctor.getEmail());

            // Kiểm tra nếu có ảnh mới, tải lên và lưu vào thư mục
            if (avatar!=null) {
                try {
                    // Lưu ảnh vào thư mục tĩnh
//                    String avatarFilename = avatar.getOriginalFilename();
//                    File file = new File("src/main/resources/static/images/" + avatarFilename);
//                    avatar.transferTo(file);  // Lưu ảnh vào thư mục
                    doctor.setAvatar(avatar.getPath());  // Cập nhật avatar vào đối tượng bác sĩ
                } catch (Exception e) {
                    e.printStackTrace();  // Nếu có lỗi trong việc tải ảnh
                }
            }
            doctorService.saveDoctor(doctor);  // Lưu bác sĩ vào cơ sở dữ liệu
        }
        return "redirect:/doctors";  // Sau khi cập nhật xong, chuyển về trang danh sách bác sĩ
    }




    // Hiển thị trang xác nhận xóa bác sĩ
    @GetMapping("/delete/{id}")
    public String getDeleteDoctorPage(@PathVariable Long id, Model model) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        if (doctor.isPresent()) {
            doctorService.deleteDoctor(id);
            return "redirect:/doctors"; // Trang xác nhận xóa
        }
        return "redirect:/doctors";
    }

    @GetMapping("/detail/{id}")
    public String detailDoctor(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id).orElseThrow(() -> new IllegalArgumentException("Invalid doctor Id:" + id));
        model.addAttribute("doctor", doctor);
        return "/doctors/doctor-details";
    }

    @PostMapping("/add/{id}")
    public String addDoctor(@Valid Doctor doctor, BindingResult result, @RequestParam("avatar") MultipartFile file) {
        if (result.hasErrors()) {
            return "/doctors/add-doctor";
        }
        String avatarUrl = "";
        if (!file.isEmpty()) {
            try {
                File dir = new File("src/main/resources/static/images/");
                if (!dir.exists())
                    dir.mkdirs();
                if (!file.isEmpty()) {
                    String imagePath = "images/" + file.getOriginalFilename();
                    Files.write(Paths.get("src/main/resources/static/" + imagePath), file.getBytes());
                    avatarUrl=imagePath;
                }
            } catch (Exception e) {
                return "You failed to upload "   + " => " + e.getMessage();
            }
        }
        doctor.setAvatar(avatarUrl);
        doctorService.saveDoctor(doctor);
        return "redirect:/doctors";
    }


}
