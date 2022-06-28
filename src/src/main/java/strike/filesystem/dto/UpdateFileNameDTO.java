package strike.filesystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateFileNameDTO {

  @JsonProperty("new_name")
  private String newName;

  public UpdateFileNameDTO() {}

  public String getNewName() {
    return newName;
  }
}
