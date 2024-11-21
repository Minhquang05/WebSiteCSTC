package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Order;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class IPNController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/ipn")
    public String handleIPN(@RequestBody Map<String, Object> ipnData) {
        String orderId = (String) ipnData.get("orderId");
        String errorCode = (String) ipnData.get("errorCode");

        Optional<Order> orderOptional = orderRepository.findByOrderId(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus("0".equals(errorCode) ? "SUCCESS" : "FAILED");
            orderRepository.save(order);
        }

        return "OK"; // Trả về cho Momo để xác nhận đã nhận IPN
    }
}

