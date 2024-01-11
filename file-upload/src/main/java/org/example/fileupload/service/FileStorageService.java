package org.example.fileupload.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileStorageService {
    List<Path> storeFiles(List<MultipartFile> files) throws IOException;
    Long getFileSize(Path path) throws IOException;
}
