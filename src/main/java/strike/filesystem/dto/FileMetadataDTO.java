package strike.filesystem.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import strike.filesystem.model.File;

public class FileMetadataDTO {

  @JsonProperty("byte_weight")
  private int weight;

  @JsonProperty("file_name")
  private String name;

  @JsonProperty("creation_date")
  private LocalDateTime creationDate;

  @JsonProperty("modification_date")
  private LocalDateTime modificationDate;

  public FileMetadataDTO(File file) {
    this.weight = file.getFile().length;
    this.name = file.getName();
    this.creationDate = file.getCreationDate();
    this.modificationDate = file.getModificationDate();
  }
}
