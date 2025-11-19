package com.jian.community.infrastructure.disk;

import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.application.util.FileStorage;
import com.jian.community.infrastructure.exception.FileStorageException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class DiskFileStorage implements FileStorage {

    private static final String ROOT_DIR_NAME = "files";
    private static final Path ROOT_PATH = Path.of(ROOT_DIR_NAME);

    public DiskFileStorage() throws IOException {
        if (!Files.exists(ROOT_PATH)) {
            Files.createDirectories(ROOT_PATH);
        }
    }

    public String save(MultipartFile file, String subDir) {
        try {
            // 디렉토리 생성
            Path directory = ROOT_PATH.resolve(subDir);
            Files.createDirectories(directory);

            // 고유 파일명 생성 (UUID + 원본 확장자)
            String extension = getFileExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + extension;

            Path targetPath = directory.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return targetPath.toString();

        } catch (IOException e) {
            throw new FileStorageException(ErrorMessage.FILE_STORE_FAILED);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}