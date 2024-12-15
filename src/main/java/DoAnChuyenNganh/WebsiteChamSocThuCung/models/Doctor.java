package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mối quan hệ với WorkHour
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<WorkHour> workHours;

    @NotBlank(message = "Tên bác sĩ không được để trống")
    @Size(min = 2, max = 100, message = "Tên bác sĩ phải từ 2-100 ký tự")
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "IMAGE")
    @URL(message = "URL hình ảnh không hợp lệ")
    private String imgUrl;

    @ElementCollection
    @CollectionTable(name = "doctor_gallery", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "IMAGE_URL")
    @Size(max = 10, message = "Tối đa 10 hình ảnh")
    private List<@URL(message = "URL hình ảnh không hợp lệ") String> productImages;

    @NotBlank(message = "Chuyên môn không được để trống")
    @Size(min = 2, max = 100, message = "Chuyên môn phải từ 2-100 ký tự")
    @Column(name = "SPECIALIZATION")
    private String specialization;

    @Pattern(
            regexp = "^(\\+84|0)[3-9][0-9]{8}$",
            message = "Số điện thoại không hợp lệ. Ví dụ: 0912345678 hoặc +84912345678"
    )
    @Column(name = "PHONE")
    private String phone;

    @Email(
            message = "Địa chỉ email không hợp lệ",
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    )
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "AVATAR")
    @URL(message = "URL avatar không hợp lệ")
    private String avatar;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "doctor_worktime",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "work_hour_id")
    )
    @Size(max = 10, message = "Tối đa 10 khung giờ làm việc")
    private Set<WorkHour> workTime = new HashSet<>();

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Size(max = 50, message = "Tối đa 50 cuộc hẹn")
    private Set<Appointment> appointments = new HashSet<>();

    public void addWorkTime(WorkHour workHour) {
        if (this.workTime == null) {
            this.workTime = new HashSet<>();
        }
        this.workTime.add(workHour);
    }

    public void removeWorkTime(WorkHour workHour) {
        if (this.workTime != null) {
            this.workTime.remove(workHour);
        }
    }

    public void addAppointment(Appointment appointment) {
        if (this.appointments == null) {
            this.appointments = new HashSet<>();
        }
        this.appointments.add(appointment);
        appointment.setDoctor(this);
    }

    public void removeAppointment(Appointment appointment) {
        if (this.appointments != null) {
            this.appointments.remove(appointment);
            appointment.setDoctor(null);
        }
    }

    public boolean isAvailable() {
        return this.workTime != null && !this.workTime.isEmpty();
    }

    public int getAppointmentCount() {
        return this.appointments != null ? this.appointments.size() : 0;
    }

    public boolean isValid() {
        return this.name != null &&
                this.specialization != null &&
                this.phone != null &&
                this.email != null;
    }
}