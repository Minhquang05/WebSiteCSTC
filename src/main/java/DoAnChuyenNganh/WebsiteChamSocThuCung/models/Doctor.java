
package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
    private String name;
    @Column(name = "IMAGE")
    private String imgUrl;
    @Column(name = "GALLERY")
    private List<String> productImages;
    private String specialization;
    private String phone;
    private String email;
    private String avatar;
    @ManyToMany
    @JoinTable(
            name = "doctor_worktime",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "WorkHour_id")
    )
    private Set<WorkHour> workTime = new HashSet<>();
    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments = new HashSet<>();

    public void setWorkTime(Set<WorkHour> workHours) {
    }

    public void setWorkTime(Stream<Long> workHours) {

    }
    // Getters and Setters
}
