package strike.filesystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import strike.filesystem.model.User;
import strike.filesystem.service.StorageFileService;

@RestController()
public class FileController {

  private final StorageFileService storageFileService;

  @Autowired
  public FileController(final StorageFileService storageFileService) {
    this.storageFileService = storageFileService;
  }

  @PostMapping("/upload")
  public ResponseEntity<?> uploadFile(
      @AuthenticationPrincipal final User user, @RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      // storageService.save(file);
      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(message);
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
    }
  }
}
