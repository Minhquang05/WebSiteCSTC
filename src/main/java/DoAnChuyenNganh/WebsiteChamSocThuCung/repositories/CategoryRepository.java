package DoAnChuyenNganh.WebsiteChamSocThuCung.repositories;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Tùy chọn: Phương thức tìm kiếm danh mục theo ID và kiểm tra trạng thái khóa
    Optional<Category> findByIdAndLockedFalse(Long id);
}