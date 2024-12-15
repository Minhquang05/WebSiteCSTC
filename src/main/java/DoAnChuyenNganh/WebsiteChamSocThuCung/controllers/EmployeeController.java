package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.User;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String viewHomePage(Model model) {
        model.addAttribute("listEmployees", userService.loadEmployees());
        return "/employees/employees-list";
    }

    @GetMapping("/create")
    public String showNewEmployeeForm(Model model) {
        model.addAttribute("user", new User());
        return "/employees/add-employee";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        user.setUsername(user.getEmail());
        var errors = bindingResult.getFieldError("phone");
        List<String> check = userService.checkExistingUser(user.getEmail(), user.getUsername(), user.getPhone());
        if (errors != null) {
            check.add(errors.getDefaultMessage());
        }
        if (!check.isEmpty()) {
            model.addAttribute("errors", check);
            return "/employees/add-employee";
        }
        userService.save(user);
        userService.setEmployeeRole(user.getUsername());
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(value = "id") long id, Model model) {
        User employee = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Kiểm tra xem nhân viên có bị khóa không
        if (!employee.getAccountNonLocked()) {
            model.addAttribute("error", "Nhân viên này đã bị khóa và không thể chỉnh sửa.");
            return "redirect:/employees"; // Chuyển hướng về danh sách nhân viên
        }

        model.addAttribute("user", employee);
        return "/employees/update-employee";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid User user, BindingResult bindingResult, Model model) {
        User existingUser = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Kiểm tra xem nhân viên có bị khóa không
        if (!existingUser.getAccountNonLocked()) {
            model.addAttribute("error", "Không thể cập nhật thông tin của nhân viên đã bị khóa.");
            return "redirect:/employees"; // Chuyển hướng về danh sách nhân viên với thông báo lỗi
        }

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            return "employees/update-employee";
        }

        userService.updateEmployee(user);
        return "redirect:/employees";
    }

    // Khóa nhân viên
    @GetMapping("/lock/{id}")
    public String lockEmployee(@PathVariable Long id) {
        userService.lock(id); // Gọi phương thức lock trong UserService
        return "redirect:/employees";
    }

    // Mở khóa nhân viên
    @GetMapping("/unlock/{id}")
    public String unlockEmployee(@PathVariable Long id) {
        userService.unlock(id); // Gọi phương thức unlock trong UserService
        return "redirect:/employees";
    }
}