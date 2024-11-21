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
    @PostMapping("/submit")
    public String submitOrder(@Valid Order order, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList());
            return "/cart/checkout";  // Trả về lại trang checkout nếu có lỗi
        }

        // Lấy các sản phẩm trong giỏ hàng
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Nếu giỏ hàng trống, chuyển hướng về trang giỏ hàng
        }

        // Tính tổng số tiền thanh toán
        double totalPaid = 0.0;
        for (CartItem item : cartItems) {
            totalPaid += item.getProduct().getPrice() * item.getQuantity();
        }

        // Đảm bảo rằng totalPaid không phải là null
        if (totalPaid <= 0) {
            model.addAttribute("errors", "Total amount cannot be zero or negative.");
            return "/cart/checkout";  // Nếu tổng số tiền <= 0, thông báo lỗi
        }

        // Gọi service để tạo đơn hàng
        orderService.createOrder(order, cartItems, totalPaid);
        model.addAttribute("order", order);  // Thêm thông tin đơn hàng vào model để hiển thị
        return "cart/order-confirmation";  // Chuyển hướng tới trang xác nhận
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

    @GetMapping("/updateStatus/{id}")
    public String updateOrderStatus(@PathVariable Long id){
        orderService.updateOrderStatus(id);
        return "redirect:/order/admin";
    }

    @GetMapping("/{name}")
    public String showMyOrder(@PathVariable String name, Model model){
        User user = userService.findByUsername(name).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + name));
        model.addAttribute("orders", orderService.getByEmail(user.getEmail()));
        return "/orders/my-orders";
    }
}
