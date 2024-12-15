package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.CartItem;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Order;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.OrderDetail;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Product;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.OrderDetailRepository;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;

    @Transactional
    public Order createOrder(Order order, List<CartItem> cartItems, double totalPaid) {
        Date currentDate = new Date();
        order.setCreateDate(currentDate);
        order.setOrderStatus((byte) 1);  // 1: Đơn hàng mới
        order.setTotal(totalPaid);

        // Lưu đơn hàng vào cơ sở dữ liệu
        order = orderRepository.save(order);

        // Kiểm tra và lưu chi tiết đơn hàng, đồng thời giảm tồn kho
        for (CartItem item : cartItems) {
            Product product = item.getProduct();

            // Kiểm tra tồn kho trước khi tạo chi tiết đơn hàng
            if (product.getInventory() < item.getQuantity()) {
                throw new IllegalArgumentException("Không đủ tồn kho cho sản phẩm: " + product.getName());
            }

            // Cập nhật số lượng sản phẩm trong tồn kho
            product.setInventory(product.getInventory() - item.getQuantity());
            productService.updateProduct(product);

            // Tạo chi tiết đơn hàng
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());

            orderDetailRepository.save(detail);
        }

        // Dọn giỏ hàng sau khi tạo đơn hàng thành công
        cartService.clearCart();

        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getByEmail(String email) {
        List<Order> myOrder = new ArrayList<>();
        for (Order order : orderRepository.findAll()) {
            if (order.getEmail().equals(email)) {
                myOrder.add(order);
            }
        }
        return myOrder;
    }

    public Order updateOrderStatus(Long id) {
        Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new IllegalStateException("Order with ID " +
                id + " does not exist."));
        existingOrder.setOrderStatus((byte) 0);  // Cập nhật trạng thái đơn hàng
        return orderRepository.save(existingOrder);
    }
}
