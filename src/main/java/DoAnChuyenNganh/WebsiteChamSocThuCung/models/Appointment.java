package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(min = 2, max = 100, message = "Tên khách hàng phải từ 2-100 ký tự")
    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ",
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @Column(name = "CUSTOMER_EMAIL")
    private String customerEmail;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^(\\+0)?[3-9][0-9]{8,9}$",
            message = "Số điện thoại không hợp lệ. Ví dụ: 0912345678"
    )
    @Column(name = "CUSTOMER_PHONE")
    private String customerPhone;

    @NotNull(message = "Ngày hẹn không được để trống")
    @Column(name = "APPOINTMENT_DATE")
    private LocalDate appointmentDate;

    @NotNull(message = "Giờ hẹn không được để trống")
    @Column(name = "APPOINTMENT_TIME")
    private LocalTime appointmentTime;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    @Column(name = "NOTE")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "APPOINTMENT_STATE")
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCTOR_ID", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_HOUR_ID") // Đảm bảo rằng tên cột này đúng
    private WorkHour workHour; // Thêm thuộc tính workHour

    public void setWorkHour(WorkHour workHour){
        this.workHour = workHour;
    }

    public enum AppointmentStatus {
        PENDING("Chờ xác nhận"),
        CONFIRMED("Đã xác nhận"),
        COMPLETED("Đã hoàn thành"),
        CANCELLED("Đã hủy");

        private final String displayName;

        AppointmentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Các phương thức hỗ trợ
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(customerName, that.customerName) &&
                Objects.equals(customerEmail, that.customerEmail) &&
                Objects.equals(customerPhone, that.customerPhone) &&
                Objects.equals(appointmentDate, that.appointmentDate) &&
                Objects.equals(appointmentTime, that.appointmentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, customerEmail, customerPhone, appointmentDate, appointmentTime);
    }
}