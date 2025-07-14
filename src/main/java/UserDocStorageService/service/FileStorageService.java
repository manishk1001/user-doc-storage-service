package UserDocStorageService.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface FileStorageService {
    InputStream downloadFile(String userName, String fileName) throws FileNotFoundException;
    void uploadFile(String userName, MultipartFile file);
}
