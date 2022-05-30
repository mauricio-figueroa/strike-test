package strike.filesystem.exception;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public final class UserNotFoundException extends BusinessException {

  private static final long serialVersionUID = -32L;

  private static final String CREATION_ERROR_MESSAGE = "User not found";

  private UserNotFoundException(
      final String message, final int statusCode, final Throwable cause) {
    super(message, statusCode, cause);
  }

  public static UserNotFoundException create(final Throwable cause) {
    return new UserNotFoundException(CREATION_ERROR_MESSAGE, SC_NOT_FOUND, cause);
  }

  public static UserNotFoundException create() {
    return new UserNotFoundException(CREATION_ERROR_MESSAGE, SC_NOT_FOUND, null);
  }
}
