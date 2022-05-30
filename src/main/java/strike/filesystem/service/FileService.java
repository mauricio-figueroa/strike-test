package strike.filesystem.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import strike.filesystem.dto.FileMetadataDTO;
import strike.filesystem.exception.BusinessException;
import strike.filesystem.model.File;
import strike.filesystem.model.User;

public interface FileService {

  void uploadFile(final User user, final MultipartFile multipartFile) throws IOException;

  void shareFile(final User user, final Long fileID, final List<String> usernames)
      throws BusinessException;

  FileMetadataDTO getMetaData(final User user, final Long fileID) throws BusinessException;

  File downloadFile(final User user, final Long id) throws BusinessException;
}
