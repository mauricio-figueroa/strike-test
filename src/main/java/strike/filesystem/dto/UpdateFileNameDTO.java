package strike.filesystem.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateFileNameDTO {

  @NotEmpty
  @JsonProperty("new_name")
  private String newName;

  public UpdateFileNameDTO() {}

  public String getNewName() {
    return newName;
  }
}
