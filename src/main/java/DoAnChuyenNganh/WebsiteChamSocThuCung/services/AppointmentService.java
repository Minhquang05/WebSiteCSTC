package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Appointment;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Product;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.AppointmentRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointment(){
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id){
        return appointmentRepository.findById(id);
    }

    public Appointment createAppointment(Appointment appointment) {

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointmentState(@NotNull Appointment appointment) {
        Appointment existingAppointment = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new IllegalStateException("Appointment with ID " +
                        appointment.getId() + " does not exist."));
        existingAppointment.setAppointmentState(appointment.getAppointmentState());
        return appointmentRepository.save(existingAppointment);
    }
}
