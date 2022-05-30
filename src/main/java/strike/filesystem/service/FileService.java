package strike.filesystem.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import strike.filesystem.model.User;

public interface FileService {

  void uploadFile(final User user, final MultipartFile multipartFile) throws IOException;
}
