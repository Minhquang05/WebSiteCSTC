package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import java.util.Optional;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Appointment;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.AppointmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;

    // Lấy tất cả các cuộc hẹn
    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Lấy cuộc hẹn theo ID
    @Transactional(readOnly = true)
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    // Lấy các khung giờ trống
    @Transactional(readOnly = true)
    public List<LocalTime> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorService.getDoctorById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bác sĩ"));

        // Lấy các giờ làm việc của bác sĩ
        List<LocalTime> workHourTimes = doctor.getWorkHours().stream()
                .map(workHour -> LocalTime.parse(workHour.getStartTime())) // Đảm bảo startTime là định dạng "HH:mm"
                .collect(Collectors.toList());

        // Lấy các giờ đã được đặt
        List<LocalTime> bookedTimes = appointmentRepository
                .findByDoctorAndAppointmentDate(doctor, date)
                .stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toList());

        // Lọc ra các giờ trống
        return workHourTimes.stream()
                .filter(time -> !bookedTimes.contains(time))
                .collect(Collectors.toList());
    }

    // Tạo cuộc hẹn mới
    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        // Kiểm tra trùng lịch
        boolean isTimeSlotAvailable = !appointmentRepository
                .existsByDoctorAndAppointmentDateAndAppointmentTime(
                        appointment.getDoctor(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime()
                );

        if (!isTimeSlotAvailable) {
            throw new RuntimeException("Khung giờ này đã được đặt");
        }

        // Đặt trạng thái mặc định
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);

        return appointmentRepository.save(appointment);
    }

    // Xác nhận cuộc hẹn
    @Transactional
    public Appointment confirmAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy cuộc hẹn"));

        if (appointment.getStatus() == Appointment.AppointmentStatus.PENDING) {
            appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
            return appointmentRepository.save(appointment);
        }

        throw new RuntimeException("Không thể xác nhận cuộc hẹn này");
    }

    // Hủy cuộc hẹn
    @Transactional
    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy cuộc hẹn"));

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    // Lấy các cuộc hẹn sắp tới của bác sĩ
    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointments(Long doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bác sĩ"));

        return appointmentRepository.findByDoctorAndStatus(
                doctor,
                Appointment.AppointmentStatus.PENDING
        );
    }

    // Lấy tất cả các cuộc hẹn của bác sĩ trong một khoảng thời gian
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsInRange(Long doctorId, LocalDate startDate, LocalDate endDate) {
        Doctor doctor = doctorService.getDoctorById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bác sĩ"));

        return appointmentRepository.findAppointmentsInRange(doctor, startDate, endDate);
    }
}