package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WorkHourDetail")
public class WorkHourDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "Doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "WorkHour_id")
    private WorkHour workHour;

    @Column(name = "PHONG")
    private String phong;
}
