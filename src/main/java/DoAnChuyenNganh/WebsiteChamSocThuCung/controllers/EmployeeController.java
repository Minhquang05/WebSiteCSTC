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
    public String create(@Valid @ModelAttribute("user") User user,BindingResult bindingResult ,Model model) {
        user.setUsername(user.getEmail());
        var errors = bindingResult.getFieldError("phone");
        List<String> check = userService.checkExistingUser(user.getEmail(),user.getUsername(), user.getPhone());
        if(errors!=null){
            check.add(errors.getDefaultMessage());
        }
        if(!check.isEmpty())
        {
            model.addAttribute("errors", check);
            return "/employees/add-employee";
        }
        userService.save(user);
        userService.setEmployeeRole(user.getUsername());
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(value = "id") long id, Model model) {
        try {
            // Tìm kiếm nhân viên theo id
            User employee = userService.getUserById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với Id: " + id));

            // Nếu tìm thấy, thêm thông tin nhân viên vào model
            model.addAttribute("user", employee);

            // Trả về trang cập nhật thông tin
            return "employees/update-employee";
        } catch (IllegalArgumentException e) {
            // Ghi log lỗi hoặc thông báo
            System.err.println(e.getMessage());

            // Chuyển hướng về trang danh sách nhân viên với thông báo lỗi
            model.addAttribute("errorMessage", "Nhân viên với ID " + id + " không tồn tại.");
            return "redirect:/employees";
        }
    }


    //    @PostMapping("/update/{id}")
//    public String update(@PathVariable Long id,@Valid User user,BindingResult bindingResult,Model model){
//        var errors = bindingResult.getFieldError("phone");
//        List<String> check = new ArrayList<String>();
//        if(errors!=null){
//            check.add(errors.getDefaultMessage());
//        }
//        if(!check.isEmpty())
//        {
//            model.addAttribute("employee", user);
//            model.addAttribute("errors", check);
//            return "/employees/update-employee";
//        }
//        userService.updateEmployee(user);
//        return "redirect:/employees";
//    }
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            return "employees/update-employee";
        }

        userService.save(user);
        userService.setEmployeeRole(user.getUsername());
        return "redirect:/employees";
    }

    // Delete employee
//    @GetMapping("/delete/{id}")
//    public String deleteEmployee(@PathVariable Long id) {
//        userService.delete(id);
//        return "redirect:/employees";
//    }

    @GetMapping("/block/{id}")
    public String blockEmployee(@PathVariable Long id) {
        userService.updateUserStatus(id, 1); // 1 = blocked
        return "redirect:/employees";
    }

    @GetMapping("/unblock/{id}")
    public String unblockEmployee(@PathVariable Long id) {
        userService.updateUserStatus(id, 0); // 0 = active
        return "redirect:/employees";
    }

}