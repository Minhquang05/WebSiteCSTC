package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Event;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CalendarController {

    @GetMapping("/calendars")
    public String viewCalendar(Model model) {
        // Giả lập một số sự kiện
        List<Event> events = new ArrayList<>();
        events.add(new Event(1L, "Meeting", "Team meeting", LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        events.add(new Event(2L, "Lunch", "Lunch with client", LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3)));

        model.addAttribute("events", events);
        return "calendar"; // Trả về trang calendar.html
    }
}
