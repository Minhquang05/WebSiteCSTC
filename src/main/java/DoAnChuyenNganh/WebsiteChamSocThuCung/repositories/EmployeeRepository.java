package DoAnChuyenNganh.WebsiteChamSocThuCung.repositories;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}