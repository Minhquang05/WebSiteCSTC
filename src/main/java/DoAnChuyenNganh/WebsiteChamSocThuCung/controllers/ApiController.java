package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.*;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
public class ApiController {
    @Autowired
    private ProductService productService;
    private Object id;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> home(Model model) {
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok().body(productList);
    }

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> doctor(Model model) {
        List<Doctor> doctorList = doctorService.getAllDoctors();
        return ResponseEntity.ok().body(doctorList);
    }

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<List<Category>> category(Model model) {
        List<Category> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok().body(categoryList);
    }

    @Autowired
    private WorkHourService workHourService;

    @GetMapping("/workhours")
    public ResponseEntity<List<WorkHour>> WorkHour(Model model) {
        List<WorkHour> workHourList = workHourService.getAllWorkHour();
        return ResponseEntity.ok().body(workHourList);
    }

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> Order(Model model) {
        List<Order> orderList = orderService.getAllOrders();
        return ResponseEntity.ok().body(orderList);
    }

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> User(Model model) {
        List<User> userList = userService.getAllUsers();
        return ResponseEntity.ok().body(userList);
    }

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> Appointment(Model model) {
        List<Appointment> appointmentList = appointmentService.getAllAppointment();
        return ResponseEntity.ok().body(appointmentList);
    }
}

