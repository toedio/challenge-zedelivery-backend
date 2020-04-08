package delivery.ze.challenge.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO {

    private boolean known = true;
    private String message;
    private String field;

    public ErrorDTO(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public ErrorDTO(String message) {
        this.message = message;
    }

    public ErrorDTO(String message, boolean conhecida) {
        this.message = message;
        this.known = conhecida;
    }

}
