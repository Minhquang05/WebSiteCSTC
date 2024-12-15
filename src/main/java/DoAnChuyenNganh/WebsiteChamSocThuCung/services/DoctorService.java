package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.BusinessService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.DoctorRepository;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    // Lấy tất cả bác sĩ
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Lấy bác sĩ theo ID
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    // Lưu bác sĩ
    public void saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }

//    // Khóa bác sĩ
//    public void lockDoctor(Long id) {
//        Doctor doctor = doctorRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ với ID: " + id));
//        doctor.setLocked(true);
//        doctorRepository.save(doctor);
//    }
//
//    // Mở khóa bác sĩ
//    public void unlockDoctor(Long id) {
//        Doctor doctor = doctorRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ với ID: " + id));
//        doctor.setLocked(false);
//        doctorRepository.save(doctor);
//    }


    // Lấy tất cả dịch vụ
    public List<BusinessService> getAllServices() {
        return serviceRepository.findAll();
    }
}