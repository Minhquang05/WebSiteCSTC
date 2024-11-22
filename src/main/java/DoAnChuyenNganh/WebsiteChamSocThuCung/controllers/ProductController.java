package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.services.ProductService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Product;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String showProductList(@Param("keyword") String keyword, Model model) {
        List<Product>productList=new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            for(Product p:productService.getProductsByName(keyword))
            {
                if(p.getIsRemoved()==0)
                    productList.add(p);
            }
        }
        else {
            for(Product p:productService.getAllProducts())
            {
                if(p.getIsRemoved()==0)
                    productList.add(p);
            }
        }
        model.addAttribute("products",productList);
        return "/products/products-list";
    }


    @GetMapping("/sorted/asc")
    public String showProductsSortedByPriceAsc(Model model) {
        model.addAttribute("products", productService.getAllProductsSortedByPriceAsc());
        return "/products/products-list";
    }


    @GetMapping("/sorted/desc")
    public String showProductsSortedByPriceDesc(Model model) {
        model.addAttribute("products", productService.getAllProductsSortedByPriceDesc());
        return "/products/products-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories()); //Load categories
        return "/products/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result, @RequestParam("images") MultipartFile[] imageFiles) {
        if (result.hasErrors()) {
            return "/products/add-product";
        }
        List<String> imagePaths = new ArrayList<>();
        if (imageFiles.length != 0) {
            try {
                File uploadDir = new File("src/main/resources/static/images/");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                for (MultipartFile imageFile : imageFiles) {
                    String imagePath = "images/" + imageFile.getOriginalFilename();
                    Files.write(Paths.get("src/main/resources/static/" + imagePath), imageFile.getBytes());
                    imagePaths.add(imagePath);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        product.setProductImages(imagePaths);
        product.setImgUrl(product.getProductImages().getFirst());
        productService.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/update-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid Product product,
                                BindingResult result,
                                @RequestParam("imageFiles") MultipartFile[] imageFiles,
                                Model model) {
        if (result.hasErrors()) {
            product.setId(id); // set id to keep it in the form in case of errors
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("product", product);
            return "/products/update-product"; // Adjust this path as necessary
        }

        List<String> imagePaths = new ArrayList<>();
        if (imageFiles != null && imageFiles.length > 0) {
            try {
                File uploadDir = new File("src/main/resources/static/images/");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                for (MultipartFile imageFile : imageFiles) {
                    if (!imageFile.isEmpty()) {
                        String imagePath = "images/" + imageFile.getOriginalFilename();
                        Files.write(Paths.get("src/main/resources/static/" + imagePath), imageFile.getBytes());
                        imagePaths.add(imagePath);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<String> existingImages = product.getProductImages();
        if (existingImages != null) {
            imagePaths.addAll(existingImages);
        }
        product.setProductImages(imagePaths);
        product.setImgUrl(product.getProductImages().getFirst());
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.updateProductStatus(id);
        return "redirect:/products";
    }

    @GetMapping("/details/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "/products/products-details";
    }
}
