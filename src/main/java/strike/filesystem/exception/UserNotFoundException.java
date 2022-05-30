package strike.filesystem.exception;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public final class FileNotFoundException extends BusinessException {

  private static final long serialVersionUID = -32L;

  private static final String CREATION_ERROR_MESSAGE = "File not found";

  private FileNotFoundException(
      final String message, final int statusCode, final Throwable cause) {
    super(message, statusCode, cause);
  }

  public static FileNotFoundException create(final Throwable cause) {
    return new FileNotFoundException(CREATION_ERROR_MESSAGE, SC_NOT_FOUND, cause);
  }

  public static FileNotFoundException create() {
    return new FileNotFoundException(CREATION_ERROR_MESSAGE, SC_NOT_FOUND, null);
  }
}
