package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Appointment;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Lấy tất cả lịch hẹn
    public List<Appointment> getAllAppointment() {
        return appointmentRepository.findAll();
    }

    // Lấy chi tiết lịch hẹn theo ID
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment with ID " + id + " does not exist."));
    }

    // Tạo mới lịch hẹn
    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    // Cập nhật trạng thái lịch hẹn
    public Appointment updateAppointmentState(Appointment appointment) {
        Appointment existingAppointment = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new IllegalStateException("Appointment with ID " + appointment.getId() + " does not exist."));
        existingAppointment.setAppointmentState(appointment.getAppointmentState());
        return appointmentRepository.save(existingAppointment);
    }
}
