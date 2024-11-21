package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Order;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.OrderRepository;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.MomoPaymentService;
import ch.qos.logback.core.model.Model;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.redirectUrl}")
    private String redirectUrl;

    @Value("${momo.ipnUrl}")
    private String ipnUrl;
    private Object HmacUtils;

    @PostMapping("/create-payment")
    public String createPayment(@RequestParam Long amount, @RequestParam String orderId) {
        // Tạo đơn hàng mới
        Order order = new Order();
        order.setOrderId(orderId);
        order.setAmount(amount);
        order.setStatus("PENDING");
        orderRepository.save(order);

        // Tạo payload gửi đến Momo
        String requestId = UUID.randomUUID().toString();
        String requestType = "captureWallet";
        String orderInfo = "Thanh toán đơn hàng " + orderId;

        Map<String, Object> payload = new HashMap<>();
        payload.put("partnerCode", partnerCode);
        payload.put("accessKey", accessKey);
        payload.put("requestId", requestId);
        payload.put("amount", amount);
        payload.put("orderId", orderId);
        payload.put("orderInfo", orderInfo);
        payload.put("redirectUrl", redirectUrl);
        payload.put("ipnUrl", ipnUrl);
        payload.put("requestType", requestType);

        // Tạo signature
        String rawSignature = String.format(
                "accessKey=%s&amount=%s&extraData=&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                accessKey, amount, ipnUrl, orderId, orderInfo, partnerCode, redirectUrl, requestId, requestType
        );

        int signature;
        try {
            signature = HmacUtils.hashCode();
        } catch (Exception e) {
            return "Lỗi khi tạo chữ ký: " + e.getMessage();
        }
        payload.put("signature", signature);

        // Gửi yêu cầu đến Momo
        RestTemplate restTemplate = new RestTemplate();
        String momoEndpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
        ResponseEntity<Map> response = restTemplate.postForEntity(momoEndpoint, payload, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("payUrl"); // Trả về URL để redirect
        } else {
            return "Có lỗi xảy ra khi tạo giao dịch thanh toán.";
        }

    }
    @SneakyThrows
    @GetMapping("/payment-result")
    public String paymentResult(@RequestParam String orderId, @RequestParam String resultCode, Model model) {
        // 1. Xử lý kết quả từ MoMo
        String status = "Thất bại";
        if ("0".equals(resultCode)) {
            status = "Thành công";
        }

        // 2. Truyền dữ liệu cho view result.html
        model.addText("orderId");
        model.addText("status");
        model.addText("amount");

        return "result"; // Tên file HTML là result.html
    }

}

