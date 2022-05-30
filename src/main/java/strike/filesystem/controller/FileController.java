package strike.filesystem.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strike.filesystem.dto.FileMetadataDTO;
import strike.filesystem.dto.ShareFileRequestBody;
import strike.filesystem.dto.UpdateFileNameDTO;
import strike.filesystem.exception.BusinessException;
import strike.filesystem.model.File;
import strike.filesystem.model.User;
import strike.filesystem.service.FileService;

@RestController
@RequestMapping("/file")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    public FileController(final FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(
            @AuthenticationPrincipal final User user,
            @RequestParam("file") MultipartFile fileToUpload)
            throws BusinessException {

        LOGGER.info("User {} Upload File {}", user.getId(), fileToUpload.getOriginalFilename());

        final File file = fileService.uploadFile(user, fileToUpload);

        return ResponseEntity.status(HttpStatus.OK).body(file);
    }

    @GetMapping("/{fileID}")
    public ResponseEntity<byte[]> download(
            @AuthenticationPrincipal final User user, @PathVariable final Long fileID)
            throws BusinessException {

        LOGGER.info("User {} download file {}", user.getId(), fileID);

        final File file = fileService.downloadFile(user, fileID);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFullName());
        header.setContentLength(file.getFile().length);

        return ResponseEntity.status(HttpStatus.OK).headers(header).body(file.getFile());
    }

    @PostMapping("/share")
    public ResponseEntity<?> shareFile(
            @AuthenticationPrincipal final User user,
            @RequestBody ShareFileRequestBody shareFileRequestBody)
            throws BusinessException {
        LOGGER.info("User {} share File {}", user.getId(), shareFileRequestBody.getFileID());

        fileService.shareFile(
                user, shareFileRequestBody.getFileID(), shareFileRequestBody.getUsernames());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/unshare")
    public ResponseEntity<?> unShareFile(
            @AuthenticationPrincipal final User user,
            @RequestBody ShareFileRequestBody shareFileRequestBody)
            throws BusinessException {

        LOGGER.info("User {} unshare File {}", user.getId(), shareFileRequestBody.getFileID());

        fileService.unShare(
                user, shareFileRequestBody.getFileID(), shareFileRequestBody.getUsernames());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/metadata")
    public ResponseEntity<List<FileMetadataDTO>> getMetaData(@AuthenticationPrincipal final User user)
            throws BusinessException {

        LOGGER.info("User {} get all files metadata files", user.getId());

        final List<FileMetadataDTO> metaData = fileService.getAllMetaData(user);

        return ResponseEntity.status(HttpStatus.OK).body(metaData);
    }

    @GetMapping("/metadata/{id}")
    public ResponseEntity<FileMetadataDTO> getMetaDataByFile(
            @AuthenticationPrincipal final User user, @PathVariable final Long id)
            throws BusinessException {

        LOGGER.info("User {} get metadata File {}", user.getId(), id);

        final FileMetadataDTO metaData = fileService.getMetaData(user, id);

        return ResponseEntity.status(HttpStatus.OK).body(metaData);
    }

    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> modifyName(
            @AuthenticationPrincipal final User user,
            @PathVariable final Long id,
            @RequestBody UpdateFileNameDTO updateFileNameDTO)
            throws BusinessException {

        LOGGER.info("User {} modify File name{}", user.getId(), id);

        fileService.updateFile(user, id, updateFileNameDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{fileID}")
    public ResponseEntity<?> deleteFile(
            @AuthenticationPrincipal final User user, @PathVariable final Long fileID)
            throws BusinessException {
        LOGGER.info("User {} delete file {}", user.getId(), fileID);

        fileService.deleteFile(user, fileID);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
