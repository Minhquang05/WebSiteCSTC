package DoAnChuyenNganh.WebsiteChamSocThuCung.repositories;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Tìm kiếm sản phẩm theo tên (không phân biệt chữ hoa, chữ thường)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Sắp xếp sản phẩm theo giá tăng dần
    List<Product> findAllByOrderByPriceAsc();

    // Sắp xếp sản phẩm theo giá giảm dần
    List<Product> findAllByOrderByPriceDesc();
}
