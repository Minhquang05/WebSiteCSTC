package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Category;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    public void lockCategoryById(Long id) {
        Category category = getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        category.setLocked(true); // Giả sử bạn có thuộc tính locked trong Category
        categoryRepository.save(category);
    }

    public void unlockCategoryById(Long id) {
        Category category = getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        category.setLocked(false); // Giả sử bạn có thuộc tính locked trong Category
        categoryRepository.save(category);
    }
}