package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentResultController {

    @GetMapping("/success")
    public String paymentSuccess() {
        // Xử lý khi thanh toán thành công
        return "Thanh toán thành công!";
    }

    @PostMapping("/notify")
    public ResponseEntity<String> paymentNotify(@RequestBody String data) {
        // Xử lý thông báo từ MoMo (IPN)
        System.out.println("Thông báo từ MoMo: " + data);
        return ResponseEntity.ok("OK");
    }
}
