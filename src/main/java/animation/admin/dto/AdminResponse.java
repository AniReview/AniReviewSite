package animation.admin.dto;

import org.springframework.web.multipart.MultipartFile;

public record AdminResponse(Long id, String loginId, String nickName, String imageUrl) {
}
