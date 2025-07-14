package UserDocStorageService.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final AmazonS3 s3Client;

    @Value("${s3.bucket.name}")
    String bucketName;

    @Override
    public InputStream downloadFile(String userName, String fileName){
        String key = userName + "/" + fileName;
        logger.info("Attempting to download file from S3: key={}", key);
        try {
            if (!s3Client.doesObjectExist(bucketName, key)) {
                logger.warn("File not found in bucket {}: key={}", bucketName, key);
                throw new FileNotFoundException("File '" + fileName + "' not found for user: " + userName);
            }

            S3Object object = s3Client.getObject(bucketName, key);
            logger.info("File successfully downloaded: key={}", key);
            return object.getObjectContent();

        } catch (AmazonS3Exception e) {
            logger.error("S3 error during download: {}", e.getMessage(), e);
            throw e;
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadFile(String userName, MultipartFile file) {
        String key = userName + "/" + file.getOriginalFilename();
        logger.info("Attempting to upload file to S3: key={}", key);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(bucketName, key, inputStream, metadata);
            logger.info("File uploaded successfully: key={}", key);

        } catch (AmazonS3Exception e) {
            logger.error("S3 error during upload: {}", e.getMessage(), e);
            throw new RuntimeException("S3 error: " + e.getMessage(), e);

        } catch (IOException e) {
            logger.error("I/O error during upload: {}", e.getMessage(), e);
            throw new RuntimeException("File upload failed due to I/O error", e);
        }
    }
}
