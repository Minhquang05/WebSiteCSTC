package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private LocalDateTime appointmentDate;

    @ManyToOne
    private Doctor doctor;  // Liên kết với bác sĩ
    // Getters and setters...
}
