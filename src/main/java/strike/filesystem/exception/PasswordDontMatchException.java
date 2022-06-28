package strike.filesystem.exception;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public final class PasswordDontMatchException extends BusinessException {

  private static final long serialVersionUID = -32L;

  private static final String CREATION_ERROR_MESSAGE = "Password don't match";

  private PasswordDontMatchException(
      final String message, final int statusCode, final Throwable cause) {
    super(message, statusCode, cause);
  }

  public static PasswordDontMatchException create(final Throwable cause) {
    return new PasswordDontMatchException(CREATION_ERROR_MESSAGE, SC_BAD_REQUEST, cause);
  }

  public static PasswordDontMatchException create() {
    return new PasswordDontMatchException(CREATION_ERROR_MESSAGE, SC_BAD_REQUEST, null);
  }
}
