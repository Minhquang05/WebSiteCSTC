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
    private OrderDetailRepository orderDetailsRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByName(String keyword) {
        return productRepository.findAll(keyword);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        productRepository.save(product);

        return product;
    }

    public Product updateProduct(@NotNull Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Product with ID " +
                        product.getId() + " does not exist."));
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setInventory(product.getInventory());
        existingProduct.setImgUrl(product.getImgUrl());
        existingProduct.setProductImages(product.getProductImages());
        return productRepository.save(existingProduct);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    public void updateProductStatus(Long id){
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product with ID " +
                        id + " does not exist."));
        existingProduct.setIsRemoved(Byte.parseByte("1"));
        productRepository.save(existingProduct);
    }

    public List<Product> getAllDogProduct(){
        List<Product> dogProducts = new ArrayList<>();
        for(Product p : productRepository.findAll())
        {
            if(p.getCategory().getId()==Long.parseLong("1")&&p.getIsRemoved()==0)
                dogProducts.add(p);
        }
        return dogProducts;
    }

    public List<Product> getAllProductsSortedByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();
    }
    public List<Product> getAllProductsSortedByPriceDesc() {
        return productRepository.findAllByOrderByPriceDesc();
    }
}
