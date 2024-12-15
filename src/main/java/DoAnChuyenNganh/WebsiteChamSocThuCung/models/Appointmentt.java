package DoAnChuyenNganh.WebsiteChamSocThuCung.models;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Appointmentt {
    private String name;
    private String email;
    private LocalDateTime appointmentDate;
}