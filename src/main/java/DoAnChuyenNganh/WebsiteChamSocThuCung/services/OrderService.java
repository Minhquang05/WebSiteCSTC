package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Product;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.CartItem;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Order;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.OrderDetail;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.OrderDetailRepository;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService; // Assuming you have a CartService
    @Autowired
    private ProductService productService;
    @Transactional
    public Order createOrder(Order order, List<CartItem> cartItems, double totalPaid) {
        Date currentDate = new Date();
        order.setCreatedAt(currentDate);
        order.setOrderId(String.valueOf(Byte.parseByte("1")));
        order.setStatus(String.valueOf(totalPaid));
        order.setTotal(totalPaid);  // Đảm bảo gán giá trị totalPaid vào đơn hàng
        orderRepository.save(order);
        order = orderRepository.save(order);
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            Product product = item.getProduct();
            detail.setQuantity(item.getQuantity());
            product.setInventory(product.getInventory()-item.getQuantity());
            orderDetailRepository.save(detail);
            productService.updateProduct(product);
        }
        // Optionally clear the cart after order placement
        cartService.clearCart();

        return order;
    }

    public List<Order> getAllOrders(){ return orderRepository.findAll(); }

        public List<Order> getByEmail(String email) {
        List<Order> myOrder = new ArrayList<>();
        for(Order order : orderRepository.findAll())
        {
            if(order.getId().equals(email))
                myOrder.add(order);
        }
        return myOrder;
    }

    public Order updateOrderStatus(Long id){
        Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new IllegalStateException("Order with ID " +
                id + " does not exist."));
        existingOrder.setStatus(String.valueOf(Byte.parseByte("0")));
        return orderRepository.save(existingOrder);
    }
}
