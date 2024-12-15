
package DoAnChuyenNganh.WebsiteChamSocThuCung.services;


import DoAnChuyenNganh.WebsiteChamSocThuCung.models.Doctor;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public void saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }


    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    public Doctor findDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

}
