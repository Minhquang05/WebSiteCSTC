package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.HMACUtil;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/momos")
public class MoMoPaymentController {

    @PostMapping("/momos/momo-payment")
    public ResponseEntity<?> createPayment() {
        ResponseEntity<String> response;
        try {
            // Thông tin cần thiết
            String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
            String partnerCode = "YOUR_PARTNER_CODE";
            String accessKey = "YOUR_ACCESS_KEY";
            String secretKey = "YOUR_SECRET_KEY";
            String orderInfo = "Thanh toán đơn hàng tại Shop X";
            String redirectUrl = "http://localhost:8080/payment-success";
            String ipnUrl = "http://localhost:8080/payment-notify";
            String requestId = String.valueOf(System.currentTimeMillis());
            String orderId = String.valueOf(System.currentTimeMillis());
            long amount = 100000; // Số tiền cần thanh toán (VNĐ)
            String requestType = "captureWallet";

            // Tạo chữ ký (Signature)
            String rawSignature = "accessKey=" + accessKey +
                    "&amount=" + amount +
                    "&extraData=" +
                    "&ipnUrl=" + ipnUrl +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + partnerCode +
                    "&redirectUrl=" + redirectUrl +
                    "&requestId=" + requestId +
                    "&requestType=" + requestType;

            String signature = HMACUtil.encode(secretKey, rawSignature);

            // Tạo request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("accessKey", accessKey);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", String.valueOf(amount));
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", redirectUrl);
            requestBody.put("ipnUrl", ipnUrl);
            requestBody.put("extraData", "");
            requestBody.put("requestType", requestType);
            requestBody.put("signature", signature);

            // Gửi yêu cầu tới MoMo API
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            response = restTemplate.postForEntity(endpoint, entity, String.class);

            // Tạo phản hồi dưới dạng JSON
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", "success");
            responseBody.put("data", response.getBody());

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Lỗi khi tạo yêu cầu thanh toán");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        // Trả về URL để khách hàng thanh toán
    }
    }
