package UserDocStorageService.controller;

import UserDocStorageService.response.ApiResponse;
import UserDocStorageService.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService storageService;

    @Operation(
            summary = "Upload a file for a user",
            description = "Uploads a file to S3 in the user's folder")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Void>> uploadFile(
            @RequestParam String userName,
            @RequestParam MultipartFile file
    ) {
        storageService.uploadFile(userName, file);
        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully.", null));
    }

    @Operation(
            summary = "Download a file from S3 storage",
            description = "Downloads the specified file from S3 for a given user")
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam String userName,
            @RequestParam String fileName
    ) throws Exception {
        InputStream inputStream = storageService.downloadFile(userName, fileName);
        byte[] bytes = inputStream.readAllBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }
}
