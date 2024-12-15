package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.WorkHour;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.WorkHourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/workHour")
@RequiredArgsConstructor
public class WorkHourController {
    private final WorkHourService workHourService;

    // Hiển thị form thêm giờ làm việc
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("workHour", new WorkHour());
        return "workHour/add-workHour";
    }

    // Thêm giờ làm việc mới
    @PostMapping("/add")
    public String addWorkHour(
            @Valid WorkHour workHour,
            BindingResult result,
            @RequestParam("time") List<String> timeList,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return "workHour/add-workHour";  // Trả về form nếu có lỗi
        }

        try {
            for (String time : timeList) {
                WorkHour newWorkHour = new WorkHour();
                newWorkHour.setStartTime(time);
                newWorkHour.setDoctor(workHour.getDoctor()); // Lưu bác sĩ hoặc các thông tin khác
                // Các thuộc tính khác từ workHour gốc nếu cần thiết
                workHourService.saveWorkHour(newWorkHour);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Thêm giờ làm việc thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/workHour";  // Chuyển hướng về trang danh sách sau khi thêm
    }

    // Hiển thị danh sách giờ làm việc
    @GetMapping
    public String listWorkHour(Model model) {
        try {
            List<WorkHour> workHours = workHourService.getAllWorkHour();  // Lấy tất cả giờ làm việc
            model.addAttribute("workHours", workHours);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không thể tải danh sách giờ làm việc");
            e.printStackTrace();
        }
        return "workHour/workHour-list";  // Trả về view danh sách
    }

    // Hiển thị form sửa giờ làm việc
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        WorkHour workHour = workHourService.getWorkHourById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid workHour Id: " + id));
        model.addAttribute("workHour", workHour);
        return "workHour/update-workHour";
    }

    // Cập nhật giờ làm việc
    @PostMapping("/update/{id}")
    public String updateWorkHour(
            @PathVariable("id") Long id,
            @Valid WorkHour workHour,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            workHour.setId(id);  // Đảm bảo giữ lại id khi có lỗi validation
            return "workHour/update-workHour";
        }

        try {
            workHourService.updateWorkHour(workHour);  // Cập nhật giờ làm việc
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật giờ làm việc thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/workHour";  // Chuyển hướng về danh sách sau khi cập nhật
    }

    // Xóa giờ làm việc
    @GetMapping("/delete/{id}")
    public String deleteWorkHour(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            WorkHour workHour = workHourService.getWorkHourById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid workHour Id: " + id));

            workHourService.deleteWorkHour(id);  // Xóa giờ làm việc
            redirectAttributes.addFlashAttribute("successMessage", "Xóa giờ làm việc thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/workHour";  // Chuyển hướng về trang danh sách sau khi xóa
    }
}
