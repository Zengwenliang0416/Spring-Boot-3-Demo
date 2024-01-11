package org.example.fileupload.controller;


import org.example.fileupload.entity.FileInfo;
import org.example.fileupload.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<Path> storedFiles = fileStorageService.storeFiles(files);

            // 创建一个包含所有文件信息的列表
            List<FileInfo> fileInfos = storedFiles.stream().map(path -> {
                // 假设fileStorageService可以返回每个文件的大小
                long size = 0;
                try {
                    size = fileStorageService.getFileSize(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return new FileInfo(path.getFileName().toString(), size);
            }).collect(Collectors.toList());

            // 返回包含文件信息的列表
            return ResponseEntity.ok().body(fileInfos);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Could not upload the files: " + e.getMessage());
        }
    }
}
