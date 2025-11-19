package com.jian.community.application.util;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

    String save(MultipartFile file, String subDir);
}
