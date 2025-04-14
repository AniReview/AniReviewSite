package animation.admin;

import animation.admin.dto.AdminCreate;
import animation.admin.dto.AdminResponse;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public AdminResponse create(AdminCreate adminCreate) {
        Admin admin = new Admin(
                adminCreate.loginId(),
                adminCreate.nickName(),
                adminCreate.password(),
                adminCreate.imageUrl());
        adminRepository.save(admin);

        return new AdminResponse(
                admin.getId(),
                adminCreate.loginId(),
                adminCreate.nickName(),
                adminCreate.imageUrl());
    }
}
