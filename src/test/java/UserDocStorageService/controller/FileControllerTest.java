package UserDocStorageService.controller;

import UserDocStorageService.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
@Import(FileControllerTest.MockServiceConfig.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileStorageService fileStorageService;

    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        public FileStorageService fileStorageService() {
            return mock(FileStorageService.class);
        }
    }

    @Test
    void testUploadFile_Success() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "dummy content".getBytes());

        doNothing().when(fileStorageService).uploadFile(any(), any());

        mockMvc.perform(multipart("/api/files/upload")
                        .file(mockFile)
                        .param("userName", "manish"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("File uploaded successfully."));
    }

    @Test
    void testDownloadFile_Success() throws Exception {
        byte[] content = "dummy content".getBytes();
        InputStream stream = new ByteArrayInputStream(content);

        when(fileStorageService.downloadFile("manish", "test.txt")).thenReturn(stream);

        mockMvc.perform(get("/api/files/download")
                        .param("userName", "manish")
                        .param("fileName", "test.txt"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=test.txt"))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(content));
    }
}
