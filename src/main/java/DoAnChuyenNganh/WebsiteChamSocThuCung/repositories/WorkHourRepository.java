package DoAnChuyenNganh.WebsiteChamSocThuCung.repositories;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.WorkHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkHourRepository extends JpaRepository<WorkHour, Long> {
    // Phương thức để tìm các WorkHour theo ID bác sĩ
    List<WorkHour> findByDoctorId(Long doctorId);

    // Nếu muốn có truy vấn tùy chỉnh hơn
    @Query("SELECT wh FROM WorkHour wh WHERE wh.doctor.id = :doctorId")
    List<WorkHour> findWorkHoursByDoctorId(@Param("doctorId") Long doctorId);
}