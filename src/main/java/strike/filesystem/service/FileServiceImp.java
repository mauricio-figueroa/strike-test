package strike.filesystem.service;

import java.io.IOException;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import strike.filesystem.model.File;
import strike.filesystem.model.User;
import strike.filesystem.repository.FileRepository;

@Service
public class FileServiceImp implements FileService {

  private final FileRepository fileRepository;

  public FileServiceImp(final FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  @Transactional
  public void uploadFile(final User user, final MultipartFile multipartFile) throws IOException {
    File file = new File(user, multipartFile.getOriginalFilename(), multipartFile.getBytes());
    fileRepository.save(file);
  }
}
