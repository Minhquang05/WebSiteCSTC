package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.WorkHour;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.WorkHourRepository;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.DoctorRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkHourService {
    private final WorkHourRepository workHourRepository;
    private final DoctorRepository doctorRepository;

    // Constructor tự định nghĩa
    public WorkHourService(WorkHourRepository workHourRepository, DoctorRepository doctorRepository) {
        this.workHourRepository = workHourRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<WorkHour> getAllWorkHour() {
        return workHourRepository.findAll(); // Giả sử bạn đang sử dụng Spring Data JPA
    }

    // Lấy khung giờ làm việc theo ID
    public Optional<WorkHour> getWorkHourById(Long id) {
        return workHourRepository.findById(id);
    }

    // Lưu khung giờ làm việc với kiểm tra và xử lý
    public WorkHour saveWorkHour(WorkHour workHour) {
        if (workHour.getDoctor() == null) {
            throw new IllegalArgumentException("WorkHour must be associated with a Doctor");
        }

        validateWorkHourTimeRange(workHour);
        validateWorkHourConflicts(workHour);

        return workHourRepository.save(workHour);
    }

    // Đăng ký khung giờ làm việc linh hoạt
    public WorkHour registerFlexibleWorkHour(Long doctorId, String startTime, String endTime) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ"));

        WorkHour workHour = new WorkHour();
        workHour.setDoctor(doctor);
        workHour.setStartTime(startTime);
        workHour.setEndTime(endTime);

        validateWorkHourTimeRange(workHour);
        validateWorkHourConflicts(workHour);

        return workHourRepository.save(workHour);
    }

    // Cập nhật khung giờ làm việc
    public WorkHour updateFlexibleWorkHour(Long workHourId, String startTime, String endTime) {
        WorkHour existingWorkHour = workHourRepository.findById(workHourId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung giờ"));

        existingWorkHour.setStartTime(startTime);
        existingWorkHour.setEndTime(endTime);

        validateWorkHourTimeRange(existingWorkHour);
        validateWorkHourConflicts(existingWorkHour);

        return workHourRepository.save(existingWorkHour);
    }

    // Cập nhật khung giờ làm việc với kiểm tra chi tiết
    public void updateWorkHour(@NotNull WorkHour workHour) {
        WorkHour existingWorkHour = workHourRepository.findById(workHour.getId())
                .orElseThrow(() -> new IllegalStateException("WorkHour with ID " + workHour.getId() + " does not exist."));

        if (workHour.getStartTime() != null) {
            existingWorkHour.setStartTime(workHour.getStartTime());
        }

        if (workHour.getEndTime() != null) {
            existingWorkHour.setEndTime(workHour.getEndTime());
        }

        if (workHour.getDoctor() != null) {
            existingWorkHour.setDoctor(workHour.getDoctor());
        }

        validateWorkHourTimeRange(existingWorkHour);
        validateWorkHourConflicts(existingWorkHour);

        workHourRepository.save(existingWorkHour);
    }

    // Xóa khung giờ làm việc với kiểm tra
    public void deleteWorkHour(Long id) {
        WorkHour workHour = workHourRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("WorkHour with ID " + id + " does not exist."));

        checkDeletionConstraints(workHour);
        workHourRepository.deleteById(id);
    }

    // Lấy khung giờ làm việc theo ID bác sĩ
    public List<WorkHour> getWorkHoursByDoctorId(Long doctorId) {
        return workHourRepository.findByDoctorId(doctorId).stream()
                .filter(workHour -> workHour.getDoctor() != null)
                .collect(Collectors.toList());
    }

    // Phương thức lấy các khung giờ rảnh của bác sĩ
    public List<WorkHour> getAvailableWorkHours(Long doctorId) {
        return workHourRepository.findByDoctorId(doctorId).stream()
                .filter(this::isWorkHourAvailable)
                .collect(Collectors.toList());
    }

    // Kiểm tra xem khung giờ có sẵn sàng không
    private boolean isWorkHourAvailable(WorkHour workHour) {
        // TODO: Implement logic kiểm tra khung giờ có sẵn sàng
        return true; // Placeholder
    }

    // Đánh dấu khung giờ không khả dụng
    public void markWorkHourAsUnavailable(Long workHourId) {
        WorkHour workHour = workHourRepository.findById(workHourId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung giờ"));

        // TODO: Implement logic đánh dấu khung giờ không khả dụng
        workHourRepository.save(workHour);
    }

    // Tìm khung giờ phù hợp nhất
    public Optional<WorkHour> findBestWorkHour(Long doctorId, int requiredDuration) {
        List<WorkHour> availableWorkHours = getAvailableWorkHours(doctorId);

        return availableWorkHours.stream()
                .filter(workHour -> calculateWorkHourDuration(workHour) >= requiredDuration)
                .findFirst();
    }

    // Tính thời lượng của khung giờ
    private int calculateWorkHourDuration(WorkHour workHour) {
        try {
            LocalTime start = LocalTime.parse(workHour.getStartTime());
            LocalTime end = LocalTime.parse(workHour.getEndTime());
            return (int) java.time.Duration.between(start, end).toMinutes();
        } catch (DateTimeParseException e) {
            return 0;
        }
    }

    // Kiểm tra xung đột giờ làm việc
    private void validateWorkHourConflicts(WorkHour workHour) {
        List<WorkHour> existingWorkHours = getWorkHoursByDoctorId(workHour.getDoctor().getId());

        boolean hasConflict = existingWorkHours.stream()
                .anyMatch(existingWorkHour ->
                        !existingWorkHour.getId().equals(workHour.getId()) &&
                                isTimeOverlap(existingWorkHour, workHour)
                );

        if (hasConflict) {
            throw new IllegalStateException("Giờ làm việc bị trùng lặp với các giờ làm việc khác");
        }
    }

    // Kiểm tra hai khung giờ có trùng nhau không
    private boolean isTimeOverlap(WorkHour workHour1, WorkHour workHour2) {
        try {
            LocalTime start1 = LocalTime.parse(workHour1.getStartTime());
            LocalTime end1 = LocalTime.parse(workHour1.getEndTime());
            LocalTime start2 = LocalTime.parse(workHour2.getStartTime());
            LocalTime end2 = LocalTime.parse(workHour2.getEndTime());

            LocalTime workDayStart = LocalTime.of(9, 0);
            LocalTime workDayEnd = LocalTime.of(17, 0);

            boolean isWithinWorkDay1 = !start1.isBefore(workDayStart) && !end1.isAfter(workDayEnd);
            boolean isWithinWorkDay2 = !start2.isBefore(workDayStart) && !end2.isAfter(workDayEnd);

            return isWithinWorkDay1 && isWithinWorkDay2 &&
                    !(end1.isBefore(start2) || start1.isAfter(end2));

        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Kiểm tra giới hạn giờ làm việc
    private void validateWorkHourTimeRange(WorkHour workHour) {
        try {
            LocalTime start = LocalTime.parse(workHour.getStartTime());
            LocalTime end = LocalTime.parse(workHour.getEndTime());

            LocalTime workDayStart = LocalTime.of(10, 0);
            LocalTime workDayEnd = LocalTime.of(19, 30);

            if (start.isBefore(workDayStart) || end.isAfter(workDayEnd)) {
                throw new IllegalArgumentException("Giờ làm việc phải nằm trong khoảng từ 10h00 đến 19h30");
            }

            if (!start.isBefore(end)) {
                throw new IllegalArgumentException("Thời gian bắt đầu phải trước thời gian kết thúc");
            }

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Định dạng thời gian không hợp lệ");
        }
    }

    // Kiểm tra các ràng buộc trước khi xóa
    private void checkDeletionConstraints(WorkHour workHour) {
        // TODO: Implement logic kiểm tra các ràng buộc trước khi xóa
    }
}