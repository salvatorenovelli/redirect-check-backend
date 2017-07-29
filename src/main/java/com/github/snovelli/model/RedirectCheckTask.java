package com.github.snovelli.model;

import com.github.snovelli.FileUploadController;
import lombok.ToString;
import net.jcip.annotations.ThreadSafe;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.nio.file.Path;

@ToString
@ThreadSafe
public class RedirectCheckTask {

    private final Path inputFile;
    private volatile Path outputFile;
    private volatile TaskStatus status = TaskStatus.PENDING;

    public RedirectCheckTask(Path inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFileName() {
        return inputFile.toFile().getName();
    }

    public String getStatus() {
        return status.name();
    }

    public void setStatus(TaskStatus newStatus) {
        status = newStatus;
    }

    public void setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
    }


    public Path getInputFile() {
        return inputFile;
    }

    public String getOutputUri() {
        return outputFile.getFileName().toString();
//        MvcUriComponentsBuilder
//                .fromMethodName(FileUploadController.class, "serveFile", outputFile.getFileName().toString())
//                .build().toString();
    }
}
