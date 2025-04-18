package animation.admin;

import animation.S3.S3Service;
import animation.admin.dto.AdminCreate;
import animation.admin.dto.AdminResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class AdminRestController {

    private final AdminService adminService;
    private final S3Service s3Service;

    public AdminRestController(AdminService adminService, S3Service s3Service) {
        this.adminService = adminService;
        this.s3Service = s3Service;
    }

    @PostMapping("/profileupload")
    public AdminResponse create(
            @RequestPart(value = "images") MultipartFile files,
            @RequestPart AdminCreate adminCreate) throws IOException {
        String url = s3Service.uploadFile(files);
        return adminService.create(url,adminCreate);
    }

    //Todo: 관리자는 삭제된 애니, 캐릭터 다 볼 수 있고 완전 삭제 ㄱㄴ + 되돌리기 ㄱㄴ
}
