package strike.filesystem.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FilenameUtils;
import strike.filesystem.dto.FileMetadataDTO;
import strike.filesystem.dto.UpdateFileNameDTO;
import strike.filesystem.exception.BusinessException;
import strike.filesystem.exception.FileNotFoundException;
import strike.filesystem.exception.FileNotOwnerException;
import strike.filesystem.model.File;
import strike.filesystem.model.User;
import strike.filesystem.repository.FileRepository;

@Service
public class FileServiceImp implements FileService {

  private final FileRepository fileRepository;
  private final UserService userService;

  public FileServiceImp(final FileRepository fileRepository, final UserService userService) {
    this.fileRepository = fileRepository;
    this.userService = userService;
  }

  @Transactional
  public void uploadFile(final User user, final MultipartFile multipartFile) throws IOException {
    final String originalName = FilenameUtils.getBaseName(multipartFile.getOriginalFilename());
    final String originalExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    File file = new File(user, originalName, originalExtension, multipartFile.getBytes());
    fileRepository.save(file);
  }

  @Override
  public void shareFile(final User user, final Long fileID, final List<String> usernames)
      throws BusinessException {
    final Optional<File> fileOpt = fileRepository.findById(fileID);

    if (fileOpt.isPresent()) {
      final File file = fileOpt.get();

      if (file.getOwner().equals(user)) {
        final List<User> users = userService.findByUsernames(usernames);
        file.addAllowedUserList(users);
        fileRepository.save(file);
      } else {
        throw FileNotOwnerException.create();
      }
    } else {
      throw FileNotFoundException.create();
    }
  }

  @Override
  public FileMetadataDTO getMetaData(final User user, final Long fileID) throws BusinessException {
    final Optional<File> fileOpt = fileRepository.findById(fileID);

    if (fileOpt.isPresent()) {
      final File file = fileOpt.get();
      if (file.getOwner().equals(user)) {
        return new FileMetadataDTO(file);
      } else {
        throw FileNotOwnerException.create();
      }
    } else {
      throw FileNotFoundException.create();
    }
  }

  @Override
  public File downloadFile(final User user, final Long id) throws BusinessException {
    final Optional<File> fileOpt = fileRepository.findById(id);

    if (fileOpt.isPresent()) {
      final File file = fileOpt.get();
      if (file.getOwner().equals(user)) {
        return file;
      } else {
        throw FileNotOwnerException.create();
      }
    } else {
      throw FileNotFoundException.create();
    }
  }

  @Override
  public void deleteFile(final User user, final Long id) throws BusinessException {

    final Optional<File> fileOpt = fileRepository.findById(id);

    if (fileOpt.isPresent()) {
      final File file = fileOpt.get();
      if (file.getOwner().equals(user)) {
        fileRepository.delete(file);
      } else {
        throw FileNotOwnerException.create();
      }
    } else {
      throw FileNotFoundException.create();
    }
  }

  @Override
  public void unShare(final User user, final Long fileID, final List<String> usernames)
      throws BusinessException {

    final Optional<File> fileOpt = fileRepository.findById(fileID);

    if (fileOpt.isPresent()) {
      final File file = fileOpt.get();

      if (file.getOwner().equals(user)) {
        final List<User> users = userService.findByUsernames(usernames);
        file.removeAllowedUserList(users);
        fileRepository.save(file);
      } else {
        throw FileNotOwnerException.create();
      }
    } else {
      throw FileNotFoundException.create();
    }
  }

  @Override
  public void updateFile(
      final User user, final Long fileID, final UpdateFileNameDTO updateFileNameDTO)
      throws BusinessException {

    final Optional<File> fileOpt = fileRepository.findById(fileID);

    if (fileOpt.isPresent()) {
      final File file = fileOpt.get();

      if (file.getOwner().equals(user)) {
        file.setName(updateFileNameDTO.getNewName());
        fileRepository.save(file);
      } else {
        throw FileNotOwnerException.create();
      }
    } else {
      throw FileNotFoundException.create();
    }
  }
}
