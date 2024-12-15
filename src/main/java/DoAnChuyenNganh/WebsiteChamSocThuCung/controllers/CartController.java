package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.CartItem;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Order;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.CartService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.OrderService;
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

    @Autowired
    private OrderService orderService;

    // Hiển thị giỏ hàng
    @GetMapping({"/", "/{name}"})
    public String showCart(@PathVariable(required = false) String name, Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        double totalPrice = cartService.calculateTotalPrice();
        int totalQuantity = cartService.getTotalQuantity();  // Lấy tổng số lượng sản phẩm trong giỏ

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalQuantity", totalQuantity);  // Thêm vào model tổng số lượng

        if (name != null) {
            model.addAttribute("username", name);
        }

        return "cart/cart";
    }

    // Thêm sản phẩm vào giỏ hàng qua Ajax và trả về tổng số sản phẩm trong giỏ hàng
    @PostMapping("/add/{productId}")
    @ResponseBody
    public String addToCart(@PathVariable Long productId, @RequestParam int quantity) {
        try {
            cartService.addToCart(productId, quantity);  // Thêm sản phẩm vào giỏ hàng
            int totalQuantity = cartService.getTotalQuantity();  // Lấy tổng số lượng sản phẩm trong giỏ
            double totalPrice = cartService.calculateTotalPrice();  // Tính tổng giá trị giỏ hàng

            // Trả về thông tin giỏ hàng dưới dạng JSON
            return "{\"totalQuantity\":" + totalQuantity + ", \"totalPrice\":" + totalPrice + "}";
        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage() + "\"}";  // Nếu có lỗi, trả về thông báo lỗi
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng và trả về giỏ hàng đã được cập nhật
    @GetMapping("/remove/{productId}")
    @ResponseBody
    public String removeFromCart(@PathVariable Long productId) {
        try {
            cartService.removeFromCart(productId);  // Xóa sản phẩm khỏi giỏ hàng

            int totalQuantity = cartService.getTotalQuantity();  // Lấy tổng số lượng sản phẩm trong giỏ
            double totalPrice = cartService.calculateTotalPrice();  // Tính tổng giá trị giỏ hàng

            // Trả về thông tin giỏ hàng dưới dạng JSON
            return "{\"totalQuantity\":" + totalQuantity + ", \"totalPrice\":" + totalPrice + "}";
        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage() + "\"}";  // Nếu có lỗi, trả về thông báo lỗi
        }
    }

    // Dọn giỏ hàng và trả về giỏ hàng đã được làm sạch
    @GetMapping("/clear")
    @ResponseBody
    public String clearCart() {
        try {
            cartService.clearCart();  // Dọn giỏ hàng

            // Trả về thông tin giỏ hàng đã được làm sạch
            return "{\"totalQuantity\": 0, \"totalPrice\": 0}";
        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage() + "\"}";  // Nếu có lỗi, trả về thông báo lỗi
        }
    }

    // Hiển thị trang thanh toán (checkout)
    @GetMapping("/checkout")
    public String checkout(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        double totalPrice = cartService.calculateTotalPrice();
        int totalQuantity = cartService.getTotalQuantity();  // Lấy tổng số lượng sản phẩm trong giỏ

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalQuantity", totalQuantity);  // Thêm vào model tổng số lượng
        return "/cart/checkout";  // Trả về trang thanh toán
    }

    // Xử lý đơn hàng khi người dùng submit form thanh toán
    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String customerName,
                             @RequestParam String email,
                             @RequestParam String phone,
                             @RequestParam String address,
                             @RequestParam(required = false) String note, Model model) {

        List<CartItem> cartItems = cartService.getCartItems();
        double totalPrice = cartService.calculateTotalPrice();
        int totalQuantity = cartService.getTotalQuantity();  // Lấy tổng số lượng sản phẩm trong giỏ

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setEmail(email);
        order.setPhone(phone);
        order.setAddress(address);
        order.setNote(note != null ? note : "");  // Nếu không có ghi chú, mặc định là chuỗi rỗng.

        try {
            // Gọi service để tạo đơn hàng và lưu vào DB
            order = orderService.createOrder(order, cartItems, totalPrice);

            // Dọn giỏ hàng sau khi tạo đơn hàng thành công
            cartService.clearCart();

            // Thêm đơn hàng vào model để hiển thị trên trang xác nhận đơn hàng
            model.addAttribute("order", order);

            return "cart/orderDetails";  // Trang hiển thị chi tiết đơn hàng

        } catch (IllegalArgumentException e) {
            // Nếu có lỗi, trả về giỏ hàng và thông báo lỗi
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalQuantity", totalQuantity);  // Cập nhật tổng số lượng sản phẩm
            return "cart/cart";  // Trở về giỏ hàng với thông báo lỗi
        }
    }

    // Trả về số lượng sản phẩm trong giỏ hàng (dùng cho cập nhật giỏ hàng)
    @GetMapping("/count")
    @ResponseBody
    public int getCartCount() {
        return cartService.getTotalQuantity();  // Trả về tổng số lượng sản phẩm trong giỏ
    }
}
