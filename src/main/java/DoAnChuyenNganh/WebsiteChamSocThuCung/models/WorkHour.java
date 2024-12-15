package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "work_hour")
public class WorkHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "Thời gian bắt đầu không được để trống")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Định dạng thời gian không hợp lệ (HH:mm)")
    @Column(name = "START_TIME", nullable = false)
    private String startTime;

    @NotBlank(message = "Thời gian kết thúc không được để trống")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Định dạng thời gian không hợp lệ (HH:mm)")
    @Column(name = "END_TIME", nullable = false)
    private String endTime;

    @Column(name = "IS_AVAILABLE", nullable = false)
    private boolean isAvailable = true;

    @Column(name = "NOTES")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "WORK_STATUS")
    private WorkStatus workStatus = WorkStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCTOR_ID", nullable = false)
    private Doctor doctor;

    @OneToMany(mappedBy = "workHour", fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new HashSet<>();

    // Hằng số thời gian cho mỗi cuộc hẹn
    private static final int APPOINTMENT_DURATION_MINUTES = 15;

    // Enum trạng thái làm việc
    public enum WorkStatus {
        ACTIVE,       // Đang hoạt động
        INACTIVE,     // Không hoạt động
        BLOCKED,      // Bị chặn
        ON_LEAVE      // Nghỉ phép
    }

    // Phương thức kiểm tra tính hợp lệ của khung giờ
    public boolean isValid() {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);

            LocalTime workDayStart = LocalTime.of(10, 0);
            LocalTime workDayEnd = LocalTime.of(19, 30);

            // Kiểm tra khung giờ có nằm trong phạm vi từ 9h00 đến 17h00 và thời gian kết thúc phải lớn hơn thời gian bắt đầu
            if (start.isBefore(workDayStart) || end.isAfter(workDayEnd)) {
                throw new IllegalArgumentException("Giờ làm việc phải nằm trong khoảng từ 10h00 đến 19h30");
            }

            return start.isBefore(end);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return false;  // Không hợp lệ nếu lỗi phân tích thời gian hoặc điều kiện không hợp lệ
        }
    }

    // Phương thức kiểm tra xem khung giờ có bị trùng không
    public boolean isOverlapping(WorkHour other) {
        if (other == null) return false;

        try {
            LocalTime thisStart = LocalTime.parse(this.startTime);
            LocalTime thisEnd = LocalTime.parse(this.endTime);
            LocalTime otherStart = LocalTime.parse(other.startTime);
            LocalTime otherEnd = LocalTime.parse(other.endTime);

            return !(thisEnd.isBefore(otherStart) || thisStart.isAfter(otherEnd));
        } catch (DateTimeParseException e) {
            return false;  // Không hợp lệ nếu lỗi phân tích thời gian
        }
    }

    // Phương thức đánh dấu khung giờ không khả dụng
    public void markAsUnavailable(String reason) {
        this.isAvailable = false;
        this.notes = reason;
        this.workStatus = WorkStatus.INACTIVE;
    }

    // Phương thức khôi phục khung giờ
    public void restore() {
        this.isAvailable = true;
        this.notes = null;
        this.workStatus = WorkStatus.ACTIVE;
    }

    // Tính toán số lượng cuộc hẹn tối đa dựa trên thời gian khung giờ
    public int getMaxAppointments() {
        long totalDurationMinutes = getDurationInMinutes();
        return (int) (totalDurationMinutes / APPOINTMENT_DURATION_MINUTES);
    }

    // Lấy số lượng cuộc hẹn hiện tại
    public int getCurrentAppointmentCount() {
        return this.appointments != null ? this.appointments.size() : 0;
    }

    // Kiểm tra xem có thể thêm cuộc hẹn không
    public boolean canAcceptMoreAppointments() {
        return getCurrentAppointmentCount() < getMaxAppointments();
    }

    // Phương thức để lấy thời gian bắt đầu của cuộc hẹn tiếp theo
    public LocalTime getNextAvailableAppointmentTime() {
        if (appointments == null || appointments.isEmpty()) {
            return LocalTime.parse(startTime);
        }

        // Sắp xếp các cuộc hẹn theo thời gian
        LocalTime latestAppointmentEndTime = appointments.stream()
                .map(appointment -> {
                    // Giả sử appointmentDate là LocalDate và appointmentTime là LocalTime
                    LocalDate appointmentDate = appointment.getAppointmentDate(); // Lấy ngày
                    LocalTime appointmentTime = appointment.getAppointmentTime(); // Lấy giờ
                    return LocalDateTime.of(appointmentDate, appointmentTime)
                            .plusMinutes(APPOINTMENT_DURATION_MINUTES)
                            .toLocalTime();
                })
                .max(LocalTime::compareTo)
                .orElse(LocalTime.parse(startTime)); // Giá trị mặc định nếu không có cuộc hẹn

        return latestAppointmentEndTime;
    }

    // Kiểm tra xem có thể đặt cuộc hẹn vào thời gian cụ thể không
    public boolean isAppointmentTimeAvailable(LocalTime proposedTime) {
        // Kiểm tra xem thời gian đề xuất có nằm trong khung giờ không
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        if (proposedTime.isBefore(start) || proposedTime.isAfter(end)) {
            return false;
        }

        // Kiểm tra xem thời gian này đã có cuộc hẹn chưa
        return appointments.stream()
                .noneMatch(appointment -> {
                    LocalTime appointmentTime = appointment.getAppointmentTime(); // Lấy giờ hẹn
                    return proposedTime.equals(appointmentTime);
                });
    }

    // Phương thức để lấy các khung giờ trống
    public Set<LocalTime> getAvailableAppointmentSlots() {
        Set<LocalTime> availableSlots = new HashSet<>();
        LocalTime currentTime = LocalTime.parse(startTime);
        LocalTime endTime = LocalTime.parse(this.endTime);

        while (currentTime.plusMinutes(APPOINTMENT_DURATION_MINUTES).compareTo(endTime) <= 0) {
            if (isAppointmentTimeAvailable(currentTime)) {
                availableSlots.add(currentTime);
            }
            currentTime = currentTime.plusMinutes(APPOINTMENT_DURATION_MINUTES);
        }

        return availableSlots;
    }

    // Phương thức kiểm tra xem khung giờ có đang hoạt động không
    public boolean isWorkingHour() {
        return this.workStatus == WorkStatus.ACTIVE && this.isAvailable;
    }

    // Phương thức để lấy thời gian còn trống
    public long getAvailableMinutes() {
        return getMaxAppointments() * APPOINTMENT_DURATION_MINUTES - (getCurrentAppointmentCount() * APPOINTMENT_DURATION_MINUTES);
    }

    // Phương thức thêm cuộc hẹn
    public void addAppointment(Appointment appointment) {
        if (this.appointments == null) {
            this.appointments = new HashSet<>();
        }
        this.appointments.add(appointment);
        appointment.setWorkHour(this);  // Đảm bảo mối quan hệ hai chiều
    }

    // Phương thức xóa cuộc hẹn
    public void removeAppointment(Appointment appointment) {
        if (this.appointments != null) {
            this.appointments.remove(appointment);
            appointment.setWorkHour(null);  // Đảm bảo mối quan hệ hai chiều
        }
    }

    // Phương thức tính thời lượng khung giờ
    public long getDurationInMinutes() {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            return java.time.Duration.between(start, end).toMinutes();
        } catch (DateTimeParseException e) {
            return 0;
        }
    }

    // Phương thức định dạng khung giờ
    public String formatTimeRange() {
        return String.format("%s - %s", startTime, endTime);
    }

//    // Phương thức lấy các cuộc hẹn chưa hoàn thành
//    public Set<Appointment> getUncompletedAppointments() {
//        return appointments.stream()
//                .filter(appointment -> !appointment.isCompleted())
//                .collect(Collectors.toSet());
//    }
}
