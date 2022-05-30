package strike.filesystem.exception;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public final class LoginException extends BusinessException {

  private static final long serialVersionUID = -32L;

  private static final String CREATION_ERROR_MESSAGE = "Error was occurred trying to login user";

  private LoginException(
      final String message, final int statusCode, final Throwable cause) {
    super(message, statusCode, cause);
  }

  public static LoginException create(final Throwable cause) {
    return new LoginException(CREATION_ERROR_MESSAGE, SC_INTERNAL_SERVER_ERROR, cause);
  }

  public static LoginException create() {
    return new LoginException(CREATION_ERROR_MESSAGE, SC_INTERNAL_SERVER_ERROR, null);
  }
}
