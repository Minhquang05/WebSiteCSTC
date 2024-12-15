package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_profiles")
public class UserProfile {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @OneToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private User user;

        private String bio;  // Tiểu sử người dùng
        private String avatarUrl;  // URL của ảnh đại diện

        // Getter, Setter và constructor
}