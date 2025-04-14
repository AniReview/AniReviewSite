package animation.admin.dto;

import org.springframework.web.multipart.MultipartFile;

public record AdminCreate(String loginId, String password, String nickName) {
}
