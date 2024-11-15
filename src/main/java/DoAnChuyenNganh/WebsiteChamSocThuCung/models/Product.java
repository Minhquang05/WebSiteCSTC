package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "IMAGE")
    private String imgUrl;
    @Column(name = "GALLERY")
    private List<String> productImages;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRICE")
    @Min(0)
    private double price;
    @Column(name = "INVENTORY")
    @Min(0)
    private int inventory;
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    @Column(name = "IS_REMOVED")
    private byte isRemoved;
}