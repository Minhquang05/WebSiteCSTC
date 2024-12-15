package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WorkHour")
public class WorkHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "START_TIME")
    private String StartTime;

//    @ManyToMany(mappedBy = "workTime", cascade = CascadeType.ALL)
//    private Set<Doctor> doctors = new HashSet<>();
    @ManyToMany(mappedBy = "workTime")
    private Set<Doctor> doctors = new HashSet<>();
//    @OneToMany(mappedBy = "WorkHour")
//    private List<WorkHourDetail>  workHourDetail;
}
