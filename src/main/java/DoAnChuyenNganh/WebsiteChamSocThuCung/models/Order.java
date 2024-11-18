package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
        @Column(name = "CUSTOMER_NAME")
        private String customerName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "PHONE_NUMBER")
    @Length(min = 10, max = 10, message = "Phone must be 10 characters")
    @Pattern(regexp = "^[0-9]*$", message = "Phone must be number")
    private String phone;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @Column(name = "TOTAL")
    private double total;
    @Column(name = "ORDER_STATUS")
    private byte orderStatus;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
}
