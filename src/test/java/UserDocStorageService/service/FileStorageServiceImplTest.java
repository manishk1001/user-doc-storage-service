package UserDocStorageService.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceImplTest {

    private AmazonS3 s3Client;
    private FileStorageServiceImpl fileStorageService;

    @BeforeEach
    void setUp() {
        s3Client = mock(AmazonS3.class);
        fileStorageService = new FileStorageServiceImpl(s3Client);
        fileStorageService.bucketName = "test-bucket";
    }

    @Test
    void testUploadFile_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "hello".getBytes());

        fileStorageService.uploadFile("manish", file);

        verify(s3Client, times(1)).putObject(eq("test-bucket"), eq("manish/file.txt"), any(InputStream.class), any(ObjectMetadata.class));
    }

    @Test
    void testDownloadFile_Success() throws FileNotFoundException {
        String key = "manish/file.txt";
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(new S3ObjectInputStream(new ByteArrayInputStream("hello".getBytes()), null));

        when(s3Client.doesObjectExist("test-bucket", key)).thenReturn(true);
        when(s3Client.getObject("test-bucket", key)).thenReturn(s3Object);

        InputStream stream = fileStorageService.downloadFile("manish", "file.txt");
        assertNotNull(stream);
    }

    @Test
    void testDownloadFile_NotFound() {
        String key = "manish/missing.txt";
        when(s3Client.doesObjectExist("test-bucket", key)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> {
            fileStorageService.downloadFile("manish", "missing.txt");
        });

        assertTrue(ex.getMessage().contains("not found"));
    }
}

