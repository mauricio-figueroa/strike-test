package strike.filesystem.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShareFileRequestBody {

  @NotNull
  @NotEmpty
  @JsonProperty("usernames")
  private final List<String> usernames;

  @NotNull
  @JsonProperty("file_id")
  private final Long fileID;

  public ShareFileRequestBody(final List<String> usernames, final Long fileID) {
    this.usernames = usernames;
    this.fileID = fileID;
  }

  public List<String> getUsernames() {
    return usernames;
  }

  public Long getFileID() {
    return fileID;
  }
}
