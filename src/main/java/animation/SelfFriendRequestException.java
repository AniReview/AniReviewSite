package animation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SelfFriendRequestException extends RuntimeException {
    public SelfFriendRequestException(String message) {
        super(message);
    }
}
