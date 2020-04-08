package delivery.ze.challenge.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("unused")
@RequestMapping(produces = "application/vnd.error")
@ControllerAdvice(annotations = RestController.class)
@ResponseBody
@Log4j2
public class RestExceptionHandler {

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<List<ErrorDTO>> handle(Exception e) {
        log.error(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonList(new ErrorDTO(e.getMessage(), false)));
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<List<ErrorDTO>> handleValidation(ConstraintViolationException e) {
        List<ErrorDTO> errors = e.getConstraintViolations().stream()
                .map(cv -> new ErrorDTO(cv.getMessage(), getFieldName(cv.getPropertyPath()))).collect(toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<List<ErrorDTO>> handle(MethodArgumentNotValidException e) {
        List<FieldError> requestErrors = e.getBindingResult().getFieldErrors();

        List<ErrorDTO> errors = requestErrors.stream().map(er -> new ErrorDTO(er.getField(), er.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({ BaseException.class })
    public ResponseEntity<List<ErrorDTO>> handle(BaseException e) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
        HttpStatus status = responseStatus.value();
        return ResponseEntity.status(status).body(Collections.singletonList(new ErrorDTO(e.getMessage())));
    }

    private static String getFieldName(Path p) {
        String[] path = p.toString().split("\\.");
        if (path.length == 3) {
            return path[2];
        } else {
            return path[2] + "." + path[3];
        }
    }
}
