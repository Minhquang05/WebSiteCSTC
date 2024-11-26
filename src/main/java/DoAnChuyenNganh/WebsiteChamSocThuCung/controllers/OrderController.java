package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.CartItem;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Order;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.User;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.CartService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.OrderService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.UserService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("order", new Order());
        return "/cart/checkout";
    }

    @GetMapping("/checkout/{name}")
    public String checkout(@PathVariable String name, Model model) {
        User user = userService.findByUsername(name).orElseThrow(() -> new IllegalArgumentException("No user found"));
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("order", new Order());
        return "/cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(@Valid Order order, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors())
        {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "/cart/checkout";
        }
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }
        double totalPaid = 0.0;
        for(CartItem item : cartItems){
            totalPaid += item.getProduct().getPrice()*item.getQuantity();
        }
        orderService.createOrder(order, cartItems, totalPaid);
        return "redirect:/order/confirmation";
    }
    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }

    @GetMapping("/admin")
    public String showOrder(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "/orders/orders-list";
    }

    @GetMapping("/employee")
    public String showOrderemployee(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "/orders/orders-list";
    }


    @GetMapping("/updateStatus/{id}")
    public String updateOrderStatus(@PathVariable Long id){
        orderService.updateOrderStatus(id);
        return "redirect:/order/admin";
    }

    @GetMapping("/updateStatusemployee/{id}")
    public String updateOrderStatusemployee(@PathVariable Long id){
        orderService.updateOrderStatus(id);
        return "redirect:/order/employee";
    }

    @GetMapping("/{name}")
    public String showMyOrder(@PathVariable String name, Model model){
        User user = userService.findByUsername(name).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + name));
        model.addAttribute("orders", orderService.getByEmail(user.getEmail()));
        return "/orders/my-orders";
    }
}
