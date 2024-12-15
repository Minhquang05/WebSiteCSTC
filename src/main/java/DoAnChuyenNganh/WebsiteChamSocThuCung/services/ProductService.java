package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.ProductRepository;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Product;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailsRepository;  // Đảm bảo inject đúng cách

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Tìm kiếm sản phẩm theo tên (Chỉnh sửa để tìm kiếm theo từ khóa)
    public List<Product> getProductsByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);  // Phương thức tìm kiếm theo tên
    }

    // Lấy sản phẩm theo ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Thêm sản phẩm mới
    public Product addProduct(Product product) {
        productRepository.save(product);
        return product;
    }

    // Cập nhật sản phẩm
    public Product updateProduct(@NotNull Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Product with ID " + product.getId() + " does not exist."));

        // Cập nhật các trường của sản phẩm
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setInventory(product.getInventory());
        existingProduct.setImgUrl(product.getImgUrl());
        existingProduct.setProductImages(product.getProductImages());

        return productRepository.save(existingProduct);
    }

    // Xóa sản phẩm theo ID
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    // Cập nhật trạng thái sản phẩm (Ví dụ: Đặt trạng thái là đã xóa)
    public void updateProductStatus(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product with ID " + id + " does not exist."));
        existingProduct.setIsRemoved((byte) 1);  // Thay trạng thái sản phẩm thành đã xóa (1)
        productRepository.save(existingProduct);
    }

    // Lấy tất cả sản phẩm dành cho chó (Ví dụ: category ID = 1)
    public List<Product> getAllDogProduct() {
        List<Product> dogProducts = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            // Kiểm tra xem sản phẩm có thuộc category cho chó và chưa bị xóa
            if (p.getCategory().getId() == 1 && p.getIsRemoved() == 0) {
                dogProducts.add(p);
            }
        }
        return dogProducts;
    }

    // Sắp xếp tất cả sản phẩm theo giá tăng dần
    public List<Product> getAllProductsSortedByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    // Sắp xếp tất cả sản phẩm theo giá giảm dần
    public List<Product> getAllProductsSortedByPriceDesc() {
        return productRepository.findAllByOrderByPriceDesc();
    }
}
