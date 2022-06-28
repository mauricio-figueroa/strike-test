package strike.filesystem.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;


public class ValidationFailureResponseDTO {

  private List<String> errors;

  public ValidationFailureResponseDTO(final List<FieldError> fieldErrors) {
    errors = fieldErrors.stream().map(this::getFormattedError).collect(Collectors.toList());
  }

  private String getFormattedError(final FieldError fe) {
    final StringBuilder sb = new StringBuilder();
    sb.append(fe.getField()).append(" - ").append(fe.getDefaultMessage());
    return sb.toString();
  }

  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(final List<String> errors) {
    this.errors = errors;
  }
}
