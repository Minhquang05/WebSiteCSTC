
package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Appointment;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.WorkHour;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.AppointmentService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.DoctorService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.EmailService;
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
import java.util.Optional;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EmailService emailService;

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
//    @PostMapping("/create")
//    public String createAppointment( Appointment appointment, @RequestParam("apntDate") String date, @RequestParam("availableTime") String availableTime) {
//        try{
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//            WorkHour workHour = workHourService.getWorkHourById(Long.valueOf(availableTime))
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + availableTime));;
//            date = date+ " "+workHour.getStartTime().replace("h",":00") ;
//            System.out.println("Appointment date: "+ date);
//            appointment.setAppointmentDate(df.parse(date));
//            appointment.setAppointmentState(0);
//            appointmentService.createAppointment(appointment);
//            return "redirect:/appointments";
//        }catch(ParseException e){
//            e.printStackTrace();
//        }
//        return "appointments/create-appointment";
//    }

    @PostMapping("/create")
    public String createAppointment(Appointment appointment,
                                    @RequestParam("apntDate") String date,
                                    @RequestParam("availableTime") String availableTime,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Xử lý thời gian
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            WorkHour workHour = workHourService.getWorkHourById(Long.valueOf(availableTime))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid workHour Id:" + availableTime));

            date = date + " " + workHour.getStartTime().replace("h", ":00");
            appointment.setAppointmentDate(df.parse(date));
            appointment.setAppointmentState(0);

            // Lưu cuộc hẹn
            appointmentService.createAppointment(appointment);

            // Lấy thông tin bác sĩ
            Doctor doctor = doctorService.getDoctorById(appointment.getDoctor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid doctor Id"));

            // Gửi email xác nhận
            emailService.sendAppointmentConfirmation(
                    appointment.getCustomerEmail(),
                    appointment.getCustomerName(),
                    new SimpleDateFormat("dd/MM/yyyy").format(appointment.getAppointmentDate()),
                    doctor.getName(),
                    workHour.getStartTime()
            );

            // Thêm thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đặt lịch thành công! Vui lòng kiểm tra email để xem chi tiết cuộc hẹn.");

            return "redirect:/appointments";

        } catch (ParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Có lỗi xảy ra khi xử lý thời gian. Vui lòng thử lại.");
            e.printStackTrace();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Có lỗi xảy ra khi đặt lịch. Vui lòng thử lại.");
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

    @GetMapping("/accept/{id}")
    public String acceptAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + id));
            appointment.setAppointmentState(1); // 1: Accepted
            appointmentService.updateAppointmentState(appointment);
            redirectAttributes.addFlashAttribute("successMessage", "Lịch hẹn đã được chấp nhận.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi chấp nhận lịch hẹn.");
        }
        return "redirect:/appointments";
    }

    @GetMapping("/reject/{id}")
    public String rejectAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + id));
            appointment.setAppointmentState(2); // 2: Rejected
            appointmentService.updateAppointmentState(appointment);
            redirectAttributes.addFlashAttribute("successMessage", "Lịch hẹn đã bị từ chối.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi từ chối lịch hẹn.");
        }
        return "redirect:/appointments";
    }

}
