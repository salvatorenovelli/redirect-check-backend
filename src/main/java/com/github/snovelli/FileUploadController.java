package com.github.snovelli;

import com.github.snovelli.exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final RedirectCheckWorker redirectCheckWorker;

    @Autowired
    public FileUploadController(StorageService storageService, RedirectCheckWorker redirectCheckWorker) {
        this.storageService = storageService;
        this.redirectCheckWorker = redirectCheckWorker;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model, HttpSession session) throws IOException {

        model.addAttribute("tasks", redirectCheckWorker.listTasks());
        model.addAttribute("files", storageService
                .loadAll(getUserId(session))
                .map(path -> createUriForFile(session, path))
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    private String createUriForFile(HttpSession session, Path path) {
        return MvcUriComponentsBuilder
                .fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString(), session)
                .build().toString();
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpSession session) {

        Resource file = storageService.loadAsResource(getUserId(session), filename);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {

        Path storedFile = storageService.store(getUserId(session), file);
        redirectCheckWorker.appendTask(storedFile);

        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    private String getUserId(HttpSession session) {
        return session.getId();
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}