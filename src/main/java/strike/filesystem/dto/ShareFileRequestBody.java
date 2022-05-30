package strike.filesystem.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShareFileRequestBody {

  @JsonProperty("usernames")
  private final List<String> usernames;
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
