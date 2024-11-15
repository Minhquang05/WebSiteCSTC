package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.CartItem;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @GetMapping
    public String showCart(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();

        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getProduct().getPrice()*item.getQuantity();
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", cartService.getCartItems());
        return "/cart/cart";
    }

    @GetMapping("/{name}")
    public String showCustomerCart(@PathVariable String name, Model model) {
        List<CartItem> cartItems = cartService.getCartItems();

        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getProduct().getPrice()*item.getQuantity();
        }
        model.addAttribute("username", name);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", cartService.getCartItems());
        return "/cart/cart";
    }


    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int
            quantity) {
        cartService.addToCart(productId, quantity);
        return "redirect:/home";
    }
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
