package com.github.snovelli.model;

import lombok.ToString;

import java.nio.file.Path;

@ToString
public class RedirectCheckTask {
    private final Path inputFile;
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

}
