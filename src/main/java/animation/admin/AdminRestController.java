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
    @Autowired
    private S3Service s3Service;

    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/profileupload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "images") MultipartFile[] files) {
        for (MultipartFile file : files) {
            try {
                String url = s3Service.uploadFile(file);
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("File upload failed: " + e.getMessage());
            }
        }
        return ResponseEntity.ok("업로드 성공");
    }

    @PostMapping("/admins")
    public AdminResponse create(@RequestBody AdminCreate adminCreate) {
        return adminService.create(adminCreate);
    }
}
