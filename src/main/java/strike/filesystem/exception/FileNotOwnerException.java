package strike.filesystem.exception;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

public final class FileNotOwnerException extends BusinessException {

  private static final long serialVersionUID = -32L;

  private static final String CREATION_ERROR_MESSAGE = "you cannot operate with files that do not belong to you";

  private FileNotOwnerException(
      final String message, final int statusCode, final Throwable cause) {
    super(message, statusCode, cause);
  }

  public static FileNotOwnerException create(final Throwable cause) {
    return new FileNotOwnerException(CREATION_ERROR_MESSAGE, SC_FORBIDDEN, cause);
  }

  public static FileNotOwnerException create() {
    return new FileNotOwnerException(CREATION_ERROR_MESSAGE, SC_FORBIDDEN, null);
  }
}
