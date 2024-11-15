package DoAnChuyenNganh.WebsiteChamSocThuCung.services;

import DoAnChuyenNganh.WebsiteChamSocThuCung.Role;
import DoAnChuyenNganh.WebsiteChamSocThuCung.models.User;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.IRoleRepository;
import DoAnChuyenNganh.WebsiteChamSocThuCung.repositories.IUserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService{
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;

    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public void setDefaultRole(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
                user -> {
                    user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
                    userRepository.save(user);
                },
                () -> { throw new UsernameNotFoundException("User not found"); }
        );}

    public void setEmployeeRole(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
                user -> {
                    user.getRoles().add(roleRepository.findRoleById(Long.parseLong("3")));
                    userRepository.save(user);
                },
                () -> { throw new UsernameNotFoundException("User not found"); }
        );}

    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }

    public Optional<User> getUserById(Long id){ return userRepository.findById(id); }

    public Optional<User> findByUsername(String username) throws
            UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<String> checkExistingUser(String email, String username, String phone){
        List<String> errors = new ArrayList<String>();
        if(userRepository.findByEmail(email).isPresent())
            errors.add("Email already existed");
        if(userRepository.findByUsername(username).isPresent())
            errors.add("Username already existed");
        if(userRepository.findByPhone(phone).isPresent())
            errors.add("Phone already registered");
        return errors;
    }

    public List<User> loadEmployees(){
        List<User> employeeList = new ArrayList<User>();
        boolean isEmployee = false;
        for(User u : userRepository.findAll()){
            for(DoAnChuyenNganh.WebsiteChamSocThuCung.models.Role r : u.getRoles()){
                if(r.getId()==3)
                {
                    isEmployee = true;
                    break;
                }
            }
            if(isEmployee)
            {
                employeeList.add(u);
                isEmployee = false;
            }
        }
        return employeeList;
    }

    public User updateEmployee(@NotNull User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("User with ID " +
                        user.getId() + " does not exist."));
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        existingUser.setUsername(user.getUsername());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhone(user.getPhone());
        return userRepository.save(existingUser);
    }

    // Delete user by ID
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
