package strike.filesystem.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import strike.filesystem.dto.FileMetadataDTO;
import strike.filesystem.dto.UpdateFileNameDTO;
import strike.filesystem.exception.BusinessException;
import strike.filesystem.model.File;
import strike.filesystem.model.User;

public interface FileService {

  File uploadFile(final User user, final MultipartFile multipartFile, final String fileName) throws BusinessException;

  File shareFile(final User user, final Long fileID, final List<String> usernames)
      throws BusinessException;

  FileMetadataDTO getMetaData(final User user, final Long fileID) throws BusinessException;

  File downloadFile(final User user, final Long id) throws BusinessException;

  void deleteFile(final User user, final Long id) throws BusinessException;

  File unShare(final User user, final Long fileID, final List<String> usernames)
      throws BusinessException;

  void updateFile(final User user, final Long fileID, final UpdateFileNameDTO updateFileNameDTO)
      throws BusinessException;

  List<FileMetadataDTO> getAllMetaData(final User user) throws BusinessException;
}
