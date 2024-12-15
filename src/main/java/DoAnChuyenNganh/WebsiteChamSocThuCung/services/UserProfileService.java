package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.UserProfile;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile getUserProfile(Long userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
    }
}