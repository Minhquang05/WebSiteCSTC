package DoAnChuyenNganh.WebsiteChamSocThuCung.models;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HMACUtil {

    public static String encode(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacData = mac.doFinal(data.getBytes());
            return bytesToHex(hmacData);
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo chữ ký HMAC", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
