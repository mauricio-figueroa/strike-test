package strike.filesystem.exception;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public final class UserAlreadyExistException extends BusinessException {

  private static final long serialVersionUID = -32L;

  private static final String CREATION_ERROR_MESSAGE = "User already exist";

  private UserAlreadyExistException(
      final String message, final int statusCode, final Throwable cause) {
    super(message, statusCode, cause);
  }

  public static UserAlreadyExistException create(final Throwable cause) {
    return new UserAlreadyExistException(CREATION_ERROR_MESSAGE, SC_BAD_REQUEST, cause);
  }

  public static UserAlreadyExistException create() {
    return new UserAlreadyExistException(CREATION_ERROR_MESSAGE, SC_BAD_REQUEST, null);
  }
}
