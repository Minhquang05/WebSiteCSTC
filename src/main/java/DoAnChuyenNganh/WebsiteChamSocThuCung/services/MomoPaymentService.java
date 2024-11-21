package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MomoPaymentService {

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


    private final String apiUrl = "https://test-payment.momo.vn/gw_payment/transactionProcessor";

    public String createPaymentRequest(double amount, String orderId) {
        // Tạo thông tin cần thiết để gọi API
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("partnerCode", partnerCode);
        requestBody.put("accessKey", accessKey);
        requestBody.put("requestId", "order_" + orderId);
        requestBody.put("amount", String.valueOf(amount * 1000));  // Số tiền (đơn vị VND, nhân với 1000)
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", "Lịch hẹn thú cưng");
        requestBody.put("returnUrl", redirectUrl);
        requestBody.put("notifyUrl", ipnUrl);

        // Tạo yêu cầu HTTP
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, null, String.class, requestBody
            );
            return response.getBody();  // Trả về kết quả API Momo
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi trong quá trình tạo yêu cầu thanh toán";
        }
    }
}
