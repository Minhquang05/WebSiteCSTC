package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Appointment;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.WorkHour;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.AppointmentService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.DoctorService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.WorkHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private WorkHourService workHourService;

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
    public String createAppointment( Appointment appointment, @RequestParam("apntDate") String date, @RequestParam("availableTime") String availableTime) {
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
           WorkHour workHour = workHourService.getWorkHourById(Long.valueOf(availableTime))
                   .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + availableTime));;
            date = date+ " "+workHour.getStartTime().replace("h",":00") ;
            System.out.println("Appointment date: "+ date);
            appointment.setAppointmentDate(df.parse(date));
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

    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.deleteAppointmentById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting appointment. Please try again.");
        }
        return "redirect:/appointments";
    }


    @GetMapping("/detail/{id}")
    public String detailAppointment(@PathVariable Long id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id).orElseThrow( () -> new IllegalArgumentException("Invalid doctor Id:" + id));
        model.addAttribute("appointment", appointment);
        return "/appointments/appointment-detail";
    }

}
