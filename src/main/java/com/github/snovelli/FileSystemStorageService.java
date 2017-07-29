package com.github.snovelli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

class FileSystemStorageService implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

    private final Path root;

    public FileSystemStorageService(Path root) {
        this.root = root.resolve("storage");
    }

    @Override
    public void init() {
        root.toFile().mkdir();
    }

    @Override
    public void deleteAll() {
        root.toFile().delete();
    }

    @Override
    public void store(String userID, MultipartFile file) {
        logger.info("Storing: {}", file.getOriginalFilename());

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = getPathForUser(userID).resolve(file.getOriginalFilename());
            Files.write(path, bytes);

//           redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Stream<Path> loadAll(String userId) throws IOException {
        Path resolve = getPathForUser(userId);
        if (resolve.toFile().exists()) {
            return Files.walk(resolve.resolve("")).filter(path -> !path.toFile().isDirectory());
        }
        return Stream.empty();
    }

    private Path getPathForUser(String userId) {
        Path resolve = root.resolve(userId);

        if (!resolve.toFile().exists()) {
            resolve.toFile().mkdir();
        }

        return resolve;
    }

    @Override
    public Resource loadAsResource(String userId, String filename) {
        return new FileSystemResource(getPathForUser(userId).resolve(filename).toFile());
    }
}
