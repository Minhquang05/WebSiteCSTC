package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Category;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;

    @GetMapping("/categories/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "/categories/add-category";
    }

    @PostMapping("/categories/add")
    public String addCategory(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "/categories/add-category";
        }
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/categories")
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "/categories/categories-list";
    }

    @GetMapping("/categories/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));

        // Kiểm tra xem danh mục đã bị khóa chưa
        if (category.isLocked()) {
            model.addAttribute("error", "Danh mục với ID " + id + " đã bị khóa và không thể chỉnh sửa.");
            return "error-page"; // Chuyển đến trang lỗi
        }

        model.addAttribute("category", category);
        return "/categories/update-category";
    }

    @PostMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, @Valid Category category,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            category.setId(id);
            return "/categories/update-category";
        }
        categoryService.updateCategory(category);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/categories";
    }

    @GetMapping("/categories/lock/{id}")
    public String lockCategory(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        categoryService.lockCategoryById(id); // Gọi phương thức khóa từ service
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/categories";
    }

    @GetMapping("/categories/unlock/{id}")
    public String unlockCategory(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        categoryService.unlockCategoryById(id); // Gọi phương thức mở khóa từ service
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/categories";
    }
}