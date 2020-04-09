package delivery.ze.challenge.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class BadRequestException extends BaseException {
    private static final long serialVersionUID = 4865615905718225792L;

    public BadRequestException(String message) {
        super(message);
    }
}
