package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Appointment;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.WorkHour;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.AppointmentService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.DoctorService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.WorkHourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final WorkHourService workHourService;

    @GetMapping
    public String showAppointmentList(Model model) {
        List<Appointment> appointmentList = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointmentList);
        return "appointments/appointment-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "appointments/create-appointment";
    }

    @PostMapping("/create")
    public String createAppointment(
            @Valid @ModelAttribute("appointment") Appointment appointment,
            BindingResult bindingResult,
            @RequestParam("apntDate") String date,
            @RequestParam("availableTime") String availableTime,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Kiểm tra validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctors", doctorService.getAllDoctors());
            return "appointments/create";
        }

        try {
            // Tìm WorkHour
            Long workHourId = Long.valueOf(availableTime);
            WorkHour workHour = workHourService.getWorkHourById(workHourId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid work hour Id: " + workHourId));

            // Tạo LocalDateTime
            String fullDateTimeString = date + " " + workHour.getStartTime().replace("h", ":00");
            LocalDateTime appointmentDateTime = LocalDateTime.parse(fullDateTimeString,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

// Đặt thông tin cho appointment
            appointment.setAppointmentDate(appointmentDateTime.toLocalDate());
            appointment.setAppointmentTime(appointmentDateTime.toLocalTime());
            appointment.setDoctor(workHour.getDoctor()); // Đặt bác sĩ từ WorkHour
            appointment.setStatus(Appointment.AppointmentStatus.PENDING);

            // Tạo appointment
            Appointment createdAppointment = appointmentService.createAppointment(appointment);

            // Thêm thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đặt lịch thành công cho " + createdAppointment.getCustomerName());

            return "redirect:/appointments/success";

        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ID giờ làm việc không hợp lệ.");
            return "redirect:/appointments/create";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/appointments/create";
        }
    }

    @GetMapping("/success")
    public String appointmentSuccess() {
        return "appointments/appointment-success";
    }

    @GetMapping("/delete/{id}")
    public String deleteAppointment(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            appointmentService.cancelAppointment(id); // Sử dụng phương thức hủy cuộc hẹn
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đã xóa lịch hẹn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Có lỗi xảy ra khi xóa lịch hẹn: " + e.getMessage());
        }
        return "redirect:/appointments";
    }

    @GetMapping("/detail/{id}")
    public String detailAppointment(@PathVariable Long id, Model model) {
        // Lấy cuộc hẹn theo ID
        Appointment appointment = appointmentService.getAppointmentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id: " + id));
        model.addAttribute("appointment", appointment);
        return "appointments/appointment-detail";
    }

    // Xử lý ngoại lệ
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(
            IllegalArgumentException ex,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/appointments";
    }
}