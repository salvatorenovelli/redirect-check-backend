package com.github.snovelli;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(String userID, MultipartFile file);

    Stream<Path> loadAll(String userId) throws IOException;

    Resource loadAsResource(String userId, String filename);

    void deleteAll();

}