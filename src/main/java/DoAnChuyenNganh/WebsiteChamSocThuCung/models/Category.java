package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    @NotBlank(message = "Tên là bắt buộc")
    private String name;

    @Column(name = "IS_REMOVED")
    private int isRemoved;

    @Column(name = "IS_LOCKED")
    private Boolean locked; // Kiểu dữ liệu Boolean (đối tượng)

    // Phương thức trả về giá trị của 'locked', nếu không sử dụng Lombok
    public boolean isLocked() {
        return locked != null && locked; // Trả về true nếu locked không null và là true, ngược lại trả về false
    }
}