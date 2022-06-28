package strike.filesystem.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateFileNameDTO {

  @NotEmpty
  @JsonProperty("new_name")
  private String newName;

  public UpdateFileNameDTO() {}

  public UpdateFileNameDTO(final String newName) {
    this.newName = newName;
  }

  public String getNewName() {
    return newName;
  }
}
