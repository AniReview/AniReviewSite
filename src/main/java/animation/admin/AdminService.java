package animation.admin;

import animation.admin.dto.AdminCreate;
import animation.admin.dto.AdminResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public AdminResponse create(String imageUrl, AdminCreate adminCreate) {
        Admin admin = new Admin(
                adminCreate.loginId(),
                adminCreate.nickName(),
                adminCreate.password(),
                imageUrl);
        adminRepository.save(admin);

        return new AdminResponse(
                admin.getId(),
                adminCreate.loginId(),
                adminCreate.nickName(),
                imageUrl);
    }
}
