package strike.filesystem.exception;


public class BusinessException extends Exception {

  private final int statusCode;

  private static final long serialVersionUID = 32450931L;

  public BusinessException(final String message, final int statusCode, final Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
  }

  public BusinessException(final String message, final int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public ErrorResponse toApiErrorResponse() {
    return ErrorResponse.create(statusCode, getMessage());
  }

  public final int getStatusCode() {
    return statusCode;
  }
}
