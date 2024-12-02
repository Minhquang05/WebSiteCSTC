package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Column(name = "CUSTOMER_EMAIL")
    private String customerEmail;
    @Column(name = "CUSTOMER_PHONE")
    private String customerPhone;
    @Column(name = "APPOINTMENT_DATE")
    private Date appointmentDate;
    @Column(name = "NOTE")
    private String note;
    @Column(name ="APPOINTMENT_STATE")
    private int appointmentState;

    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;
    private byte isRemoved;

    public Appointment orElseThrow(Object o) {
        return null;
    }

    public void setIsRemoved(byte isRemoved) {
        this.isRemoved = isRemoved;
    }

    public byte getIsRemoved() {
        return isRemoved;
    }


    // Getters và setters

}
