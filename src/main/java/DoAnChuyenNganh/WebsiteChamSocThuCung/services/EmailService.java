package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendAppointmentConfirmation(String to, String customerName,
                                            String appointmentDate, String doctorName,
                                            String time) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Xác nhận đặt lịch khám thú y");

            String htmlContent = String.format("""
                <html>
                <body style='font-family: Arial, sans-serif;'>
                    <h2>Xác nhận đặt lịch khám</h2>
                    <p>Kính gửi %s,</p>
                    <p>Cảm ơn bạn đã đặt lịch khám tại phòng khám thú y của chúng tôi.</p>
                    <h3>Chi tiết lịch hẹn:</h3>
                    <ul>
                        <li>Ngày khám: %s</li>
                        <li>Thời gian: %s</li>
                        <li>Bác sĩ: %s</li>
                    </ul>
                    <p>Vui lòng đến đúng giờ để được phục vụ tốt nhất.</p>
                    <p>Nếu bạn cần thay đổi lịch hẹn, vui lòng liên hệ với chúng tôi trước 24 giờ.</p>
                    <p>Trân trọng,<br>Phòng khám thú y</p>
                </body>
                </html>
                """, customerName, appointmentDate, time, doctorName);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
            // Có thể thêm logging hoặc xử lý lỗi khác ở đây
        }
    }
}
