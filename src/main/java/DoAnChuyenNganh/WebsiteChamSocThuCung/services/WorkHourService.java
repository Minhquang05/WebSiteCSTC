package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Category;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.WorkHour;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.WorkHourDetail;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.WorkHourRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkHourService {
    @Autowired
    private WorkHourRepository workHourRepository;

    public List<WorkHour> getAllWorkHour() {
        return workHourRepository.findAll();
    }

    public Optional<WorkHour> getWorkHourById(Long id) {
        return workHourRepository.findById(id);
    }

    public WorkHour saveWorkHour(WorkHour workHour) { return workHourRepository.save(workHour);}

    public void updateWorkHour(@NotNull WorkHour workHour) {
        WorkHour existingWorkHour = workHourRepository.findById(workHour.getId())
                .orElseThrow(() -> new IllegalStateException("Category with ID " +
                        workHour.getId() + " does not exist."));
        existingWorkHour.setStartTime(workHour.getStartTime());
        workHourRepository.save(existingWorkHour);
    }

    public void deleteWorkHour(Long id) {
        workHourRepository.deleteById(id);
    }
}
