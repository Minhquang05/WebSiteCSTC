package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.CartItem;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Product;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@SessionScope
public class CartService {

    private Map<Long, CartItem> cartItems = new HashMap<>(); // Lưu trữ sản phẩm trong giỏ hàng

    @Autowired
    private ProductRepository productRepository; // Repository để truy xuất sản phẩm

    // Thêm sản phẩm vào giỏ hàng
    public void addToCart(Long productId, int quantity) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Kiểm tra số lượng không vượt quá tồn kho
            if (quantity > product.getInventory()) {
                throw new IllegalArgumentException("Số lượng sản phẩm vượt quá tồn kho");
            }

            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            CartItem cartItem = cartItems.get(product.getId());

            if (cartItem != null) {
                int totalQuantity = cartItem.getQuantity() + quantity;

                // Kiểm tra số lượng tổng không vượt quá tồn kho
                if (totalQuantity <= product.getInventory()) {
                    cartItem.setQuantity(totalQuantity);  // Cập nhật số lượng nếu có trong giỏ
                } else {
                    throw new IllegalArgumentException("Số lượng sản phẩm vượt quá tồn kho");
                }
            } else {
                // Nếu sản phẩm chưa có trong giỏ hàng, thêm sản phẩm mới
                cartItems.put(product.getId(), new CartItem(product, quantity));
            }
        } else {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm với id: " + productId);
        }
    }

    // Lấy danh sách các item trong giỏ hàng
    public List<CartItem> getCartItems() {
        return cartItems.values().stream().collect(Collectors.toList());
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeFromCart(Long productId) {
        cartItems.remove(productId);
    }

    // Dọn giỏ hàng
    public void clearCart() {
        cartItems.clear();
    }

    // Tính tổng giá trị giỏ hàng
    public double calculateTotalPrice() {
        return cartItems.values().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    // Trả về số lượng sản phẩm trong giỏ hàng
    public int getCartItemCount() {
        return cartItems.size();  // Trả về số sản phẩm trong giỏ
    }

    // Trả về tổng số lượng sản phẩm (quantity) trong giỏ hàng
    public int getTotalQuantity() {
        return cartItems.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
