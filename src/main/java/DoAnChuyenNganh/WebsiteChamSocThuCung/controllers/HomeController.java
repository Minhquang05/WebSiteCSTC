package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Product;
import org.springframework.ui.Model;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/home";
    }

    @GetMapping("/home")
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
        return "/home/home";
    }

    @GetMapping("home/sorted/asc")
    public String showProductsSortedByPriceAsc(Model model) {
        List<Product>productList=new ArrayList<>();
        for(Product p:productService.getAllProductsSortedByPriceAsc())
        {
            if(p.getIsRemoved()==0)
                productList.add(p);
        }
        model.addAttribute("products",productList);
        return "/home/home";
    }

    @GetMapping("home/sorted/desc")
    public String showProductsSortedByPriceDesc(Model model) {
        List<Product>productList=new ArrayList<>();
        for(Product p:productService.getAllProductsSortedByPriceDesc())
        {
            if(p.getIsRemoved()==0)
                productList.add(p);
        }
        model.addAttribute("products",productList);
        return "/home/home";
    }

    @GetMapping("/home/dogFoods")
    public String showDogFood(Model model)
    {
        model.addAttribute("products",productService.getAllDogProduct());
        return "/home/home";
    }
}