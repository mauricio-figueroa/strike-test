package strike.filesystem.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImp.class);

    private final FileRepository fileRepository;
    private final UserService userService;

    public FileServiceImp(final FileRepository fileRepository, final UserService userService) {
        this.fileRepository = fileRepository;
        this.userService = userService;
    }

    @Transactional
    public File uploadFile(final User user, final MultipartFile multipartFile, final String fileName)
            throws BusinessException {

        final String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        try {
            File file = new File(user, fileName, fileExtension, multipartFile.getBytes());
            fileRepository.save(file);
            return file;
        } catch (Exception e) {
            LOGGER.error("Error trying to upload file", e);
            throw new BusinessException("Error trying to upload file", 500);
        }
    }

    @Override
    public File shareFile(final User user, final Long fileID, final List<String> usernames)
            throws BusinessException {
        final File file = this.validateAndGetFile(user, fileID);
        final List<User> users = userService.findByUsernames(usernames);
        file.addAllowedUserList(users);
        fileRepository.save(file);
        return file;
    }

    @Override
    public File unShare(final User user, final Long fileID, final List<String> usernames)
            throws BusinessException {

        final File file = this.validateAndGetFile(user, fileID);
        final List<User> users = userService.findByUsernames(usernames);

        file.removeAllowedUserList(users);
        fileRepository.save(file);

        return file;
    }


    @Override
    public FileMetadataDTO getMetaData(final User user, final Long fileID) throws BusinessException {
        final File file = this.validateAndGetFile(user, fileID);

        return new FileMetadataDTO(file);
    }

    @Override
    @Transactional
    public List<FileMetadataDTO> getAllMetaData(final User user) {
        final Set<File> files = user.getFiles();
        return files.stream().map(FileMetadataDTO::new).collect(Collectors.toList());
    }

    @Override
    public File downloadFile(final User user, final Long fileID) throws BusinessException {

        return this.validateAndGetFile(user, fileID);
    }

    @Override
    public void deleteFile(final User user, final Long fileID) throws BusinessException {

        final File file = this.validateAndGetFile(user, fileID);
        fileRepository.delete(file);
    }

    @Override
    public void updateFile(
            final User user, final Long fileID, final UpdateFileNameDTO updateFileNameDTO)
            throws BusinessException {

        final File file = this.validateAndGetFile(user, fileID);

        file.setName(updateFileNameDTO.getNewName());
        fileRepository.save(file);
    }

    private File validateAndGetFile(final User user, final Long fileID) throws BusinessException {
        final Optional<File> fileOpt = fileRepository.findById(fileID);
        if (fileOpt.isPresent()) {
            final File file = fileOpt.get();
            if (file.getOwner().equals(user)) {
                return file;
            } else {
                LOGGER.error("The user {} is not the owner  of the file  {} ", user.getId(), fileID);
                throw FileNotOwnerException.create();
            }
        } else {
            LOGGER.error("File id {} not found", fileID);
            throw FileNotFoundException.create();
        }
    }
}
