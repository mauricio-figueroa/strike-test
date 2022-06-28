package strike.filesystem.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.util.Strings;

public final class ErrorResponse {

  private final long timestamp;
  private final int statusCode;
  private final String message;
  private String path;

  private ErrorResponse(final int statusCode, final String message, final String path) {
    this.timestamp = System.currentTimeMillis();
    this.statusCode = statusCode;
    this.message = message;
    this.path = path;
  }

  public static ErrorResponse create(final int statusCode, final String message) {
    return new ErrorResponse(statusCode, message, Strings.EMPTY);
  }

  public long getTimestamp() {
    return timestamp;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getMessage() {
    return message;
  }

  public String getPath() {
    return path;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("timestamp", timestamp)
        .append("statusCode", statusCode)
        .append("message", message)
        .append("path", path)
        .toString();
  }
}
