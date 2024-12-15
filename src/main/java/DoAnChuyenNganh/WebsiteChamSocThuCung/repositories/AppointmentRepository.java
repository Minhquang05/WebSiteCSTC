package DoAnChuyenNganh.WebsiteChamSocThuCung.repositories;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Appointment;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Tìm các cuộc hẹn của một bác sĩ trong một ngày cụ thể
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate date);

    // Tìm các cuộc hẹn của một bác sĩ theo trạng thái
    List<Appointment> findByDoctorAndStatus(Doctor doctor, Appointment.AppointmentStatus status);

    // Kiểm tra xem khung giờ đã được đặt chưa
    boolean existsByDoctorAndAppointmentDateAndAppointmentTime(
            Doctor doctor,
            LocalDate date,
            LocalTime time
    );

    // Tìm các cuộc hẹn chưa hoàn thành của một bác sĩ
    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor " +
            "AND a.appointmentDate >= :currentDate " +
            "AND a.status IN (:statuses)")
    List<Appointment> findUpcomingAppointments(
            @Param("doctor") Doctor doctor,
            @Param("currentDate") LocalDate currentDate,
            @Param("statuses") List<Appointment.AppointmentStatus> statuses
    );

    // Tìm các cuộc hẹn của một bác sĩ trong khoảng thời gian cụ thể
    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor " +
            "AND a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> findAppointmentsInRange(
            @Param("doctor") Doctor doctor,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Tìm các cuộc hẹn theo bác sĩ và ngày hẹn
    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor " +
            "AND a.appointmentDate = :date")
    List<Appointment> findByDoctorAndDate(
            @Param("doctor") Doctor doctor,
            @Param("date") LocalDate date
    );
}