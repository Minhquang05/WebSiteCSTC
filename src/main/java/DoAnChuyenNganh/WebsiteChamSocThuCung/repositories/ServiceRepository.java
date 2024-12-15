package DoAnChuyenNganh.WebsiteChamSocThuCung.repositories;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.BusinessService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<BusinessService, Long> {
    // Các phương thức tùy chỉnh nếu cần
}