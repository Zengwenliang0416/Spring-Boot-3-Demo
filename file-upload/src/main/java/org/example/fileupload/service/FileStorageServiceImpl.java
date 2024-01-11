package org.example.fileupload.service;

import jakarta.annotation.PostConstruct;
import org.example.fileupload.exception.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation = Paths.get("uploaded-files");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public List<Path> storeFiles(List<MultipartFile> files) throws IOException {
        return files.stream().map(file -> {
            try {
                if (file.isEmpty()) {
                    throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
                }
                Path destinationFile = rootLocation.resolve(
                                Paths.get(file.getOriginalFilename()))
                        .normalize().toAbsolutePath();
                if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                    // Security check
                    throw new StorageException(
                            "Cannot store file outside current directory.");
                }
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                }
                return destinationFile;
            } catch (IOException e) {
                throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
            }
        }).collect(Collectors.toList());
    }
    @Override
    public Long getFileSize(Path path) throws IOException {
        return Files.size(path);
    }
}