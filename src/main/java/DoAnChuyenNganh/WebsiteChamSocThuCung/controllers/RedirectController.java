package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RedirectController {

    @GetMapping("/payment/redirect")
    public String handleRedirect(
            @RequestParam("partnerCode") String partnerCode,
            @RequestParam("orderId") String orderId,
            @RequestParam("requestId") String requestId,
            @RequestParam("amount") String amount,
            @RequestParam("errorCode") String errorCode,
            @RequestParam("message") String message,
            @RequestParam("localMessage") String localMessage,
            @RequestParam("transId") String transId,
            @RequestParam("responseTime") String responseTime,
            @RequestParam("extraData") String extraData,
            @RequestParam("signature") String signature) {

        if (errorCode.equals("0")) {
            // Thanh toán thành công
            System.out.println("Giao dịch thành công. Mã đơn hàng: " + orderId);
            return "success"; // Trả về trang thành công
        } else {
            // Giao dịch thất bại
            System.out.println("Giao dịch thất bại. Lỗi: " + message);
            return "error"; // Trả về trang lỗi
        }
    }
}
