package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Appointment;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.AppointmentService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public String showAppointmentList(Model model) {
        List<Appointment> appointmentList = appointmentService.getAllAppointment();
        model.addAttribute("appointments",appointmentList);
        return "appointments/appointment-list";  // Trang form đặt lịch khám
    }

    // Hiển thị trang đặt lịch khám
    @GetMapping("/create")
    public String showAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getAllDoctors()); // Lấy danh sách bác sĩ thú y
        return "appointments/create-appointment";  // Trang form đặt lịch khám
    }
    @PostMapping("/create")
    public String createAppointment(Appointment appointment, @RequestParam String appointmentDate, @RequestParam String time) {
        try{
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            appointmentDate = appointmentDate + " " + time.replace("h",":00 AM");
            System.out.println("Appointment date: "+ appointmentDate);
            appointment.setAppointmentDate(df.parse(appointmentDate));
            appointment.setAppointmentState(0);
            appointmentService.createAppointment(appointment);
            return "redirect:/appointments";
        }catch(ParseException e){
            e.printStackTrace();
        }
        return "appointments/create-appointment";
    }

    // Trang thành công
    @GetMapping("/success")
    public String appointmentSuccess() {
        return "appointments/appointment-success";  // Trang thông báo thành công
    }
}
