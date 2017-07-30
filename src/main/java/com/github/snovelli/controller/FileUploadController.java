package com.github.snovelli.controller;

import com.github.snovelli.StorageService;
import com.github.snovelli.TaskQueue;
import com.github.snovelli.exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final TaskQueue taskQueue;

    @Autowired
    public FileUploadController(StorageService storageService, TaskQueue taskQueue) {
        this.storageService = storageService;
        this.taskQueue = taskQueue;
    }

//    @GetMapping("/")
//    public String listUploadedFiles(Model model) throws IOException {
//        model.addAttribute("tasks", taskQueue.listTasks());
//        return "uploadForm";
//    }

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
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) throws IOException, InterruptedException, ExecutionException {

        Path storedFile = storageService.store(getUserId(session), file);
        taskQueue.appendTask(storedFile);

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