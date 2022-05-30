package strike.filesystem.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import strike.filesystem.dto.FileMetadataDTO;
import strike.filesystem.dto.ShareFileRequestBody;
import strike.filesystem.dto.UpdateFileNameDTO;
import strike.filesystem.exception.BusinessException;
import strike.filesystem.model.File;
import strike.filesystem.model.User;
import strike.filesystem.service.FileService;

@RestController
public class FileController {

  private final FileService fileService;

  public FileController(final FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/upload")
  public ResponseEntity<?> uploadFile(
      @AuthenticationPrincipal final User user, @RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      fileService.uploadFile(user, file);
      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(message);
    } catch (Exception e) {
      System.out.println(e);
      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
    }
  }

  @PostMapping("/share")
  public ResponseEntity<?> shareFile(
      @AuthenticationPrincipal final User user,
      @RequestBody ShareFileRequestBody shareFileRequestBody)
      throws BusinessException {
    fileService.shareFile(
        user, shareFileRequestBody.getFileID(), shareFileRequestBody.getUsernames());
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/unshare")
  public ResponseEntity<?> unShareFile(
      @AuthenticationPrincipal final User user,
      @RequestBody ShareFileRequestBody shareFileRequestBody)
      throws BusinessException {
    fileService.unShare(
        user, shareFileRequestBody.getFileID(), shareFileRequestBody.getUsernames());
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/meta-data/{id}")
  public ResponseEntity<FileMetadataDTO> getMetaData(
      @AuthenticationPrincipal final User user, @PathVariable final Long id)
      throws BusinessException {
    final FileMetadataDTO metaData = fileService.getMetaData(user, id);
    return ResponseEntity.status(HttpStatus.OK).body(metaData);
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<byte[]> download(
      @AuthenticationPrincipal final User user, @PathVariable final Long id)
      throws BusinessException {

    final File file = fileService.downloadFile(user, id);
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    header.set(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=" + file.getFullName()) ;
    header.setContentLength(file.getFile().length);

    return ResponseEntity.status(HttpStatus.OK).headers(header).body(file.getFile());
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteFile(
      @AuthenticationPrincipal final User user, @PathVariable final Long id)
      throws BusinessException {

    fileService.deleteFile(user, id);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping(
      value = "/modify/{id}",
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> shareFile(
      @AuthenticationPrincipal final User user,
      @PathVariable final Long id,
      @RequestBody UpdateFileNameDTO updateFileNameDTO)
      throws BusinessException {
    fileService.updateFile(user, id, updateFileNameDTO);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
