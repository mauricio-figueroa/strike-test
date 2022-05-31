package strike.filesystem.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import strike.filesystem.dto.FileMetadataDTO;
import strike.filesystem.dto.UpdateFileNameDTO;
import strike.filesystem.exception.BusinessException;
import strike.filesystem.model.AppUserRole;
import strike.filesystem.model.File;
import strike.filesystem.model.User;
import strike.filesystem.repository.FileRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceImpTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FileServiceImp target;

    @Mock
    private HttpServletRequest httpServletRequest;


    @BeforeEach
    public void setUpBeforeClass() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));
    }

    @Test
    public void givenUploadFileThenThrowException() {
        User user = new User();
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.csv");
        when(fileRepository.save(any())).thenThrow(new RuntimeException("test exception"));

        assertThrows(BusinessException.class, () -> {
            target.uploadFile(user, multipartFile, "test");
        });
    }

    @Test
    public void givenUploadFileSuccess() throws BusinessException {
        User user = new User();
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.csv");
        final File file = target.uploadFile(user, multipartFile, "test");

        assertNotNull(file, "file is null");
        verify(fileRepository).save(any());
    }

    @Test
    public void shareFileTestSuccess() throws BusinessException, IOException {
        User user = new User();
        Long fileID = 1L;
        List<String> usernames = ImmutableList.of("user_1", "user_2");
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        File file = new File(user, "test", "csv", multipartFile.getBytes());

        final User user1 = new User("user_2", "asd2", AppUserRole.USER, false, true);
        final User user2 = new User("user_1", "asd", AppUserRole.USER, false, true);

        when(fileRepository.findById(eq(fileID))).thenReturn(Optional.of(file));
        when(userService.findByUsernames(eq(usernames))).thenReturn(ImmutableList.of(user1, user2));

        assertEquals(1, file.getAllowedUsers().size(), "Allowed user list don't match");
        final File fileResponse = target.shareFile(user, fileID, usernames);

        verify(fileRepository).save(any());
        assertEquals(3, fileResponse.getAllowedUsers().size(), "Allowed user list don't match");

    }

    @Test
    public void unShareFileTestSuccess() throws BusinessException, IOException {
        User user = new User();
        Long fileID = 1L;
        List<String> usernames = ImmutableList.of("user_1", "user_2");
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        File file = new File(user, "test", "csv", multipartFile.getBytes());

        final User user1 = new User("user_2", "asd2", AppUserRole.USER, false, true);
        final User user2 = new User("user_1", "asd", AppUserRole.USER, false, true);
        file.addAllowedUsers(user1);
        file.addAllowedUsers(user2);

        when(fileRepository.findById(eq(fileID))).thenReturn(Optional.of(file));
        when(userService.findByUsernames(eq(usernames))).thenReturn(ImmutableList.of(user1, user2));

        assertEquals(3, file.getAllowedUsers().size(), "Allowed user list don't match");

        final File fileResponse = target.unShare(user, fileID, usernames);

        verify(fileRepository).save(any());
        assertEquals(1, fileResponse.getAllowedUsers().size(), "Allowed user list don't match");
    }


    @Test
    public void getMetadataSuccess() throws BusinessException {
        User user = new User();
        Long fileID = 1L;

        File file = new File(user, "test", "csv", "asdasd".getBytes());
        when(fileRepository.findById(eq(fileID))).thenReturn(Optional.of(file));

        final FileMetadataDTO metaData = target.getMetaData(user, fileID);


        assertNotNull(metaData, "meta data is null");
        assertEquals("test.csv", metaData.getName(), "File Name  don't match");
    }

    @Test
    public void getAllMetadataSuccess() {
        User user = new User();
        user.addAllowedFile(new File(user, "test", "csv", "asd".getBytes(StandardCharsets.UTF_8)));
        user.addAllowedFile(new File(user, "test1", "csv", "asd".getBytes(StandardCharsets.UTF_8)));
        user.addAllowedFile(new File(user, "test12", "csv", "asd".getBytes(StandardCharsets.UTF_8)));
        user.addAllowedFile(new File(user, "test123", "csv", "asd".getBytes(StandardCharsets.UTF_8)));

        final List<FileMetadataDTO> metadataList = target.getAllMetaData(user);

        assertNotNull(metadataList, "metadata list is null");
        assertEquals(4, metadataList.size(), "number of metadata files  doesn't match");
    }

    @Test
    public void deleteFileSuccess() throws BusinessException {
        User user = new User();
        Long fileID = 1L;

        File file = new File(user, "test", "csv", "asdasd".getBytes());
        when(fileRepository.findById(eq(fileID))).thenReturn(Optional.of(file));
        target.deleteFile(user, fileID);

        verify(fileRepository).delete(any());
    }

    @Test
    public void updateFileSuccess() throws BusinessException {
        User user = new User();
        Long fileID = 1L;

        File file = new File(user, "test", "csv", "asdasd".getBytes());
        when(fileRepository.findById(eq(fileID))).thenReturn(Optional.of(file));

        target.updateFile(user, fileID, new UpdateFileNameDTO("new_name"));

        verify(fileRepository).save(any());
    }


}