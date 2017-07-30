package com.github.snovelli.model;

import lombok.ToString;
import net.jcip.annotations.ThreadSafe;

import java.nio.file.Path;

@ToString
@ThreadSafe
public class RedirectCheckTask {

    private final Path inputFile;
    private final TaskProgress taskProgress = new TaskProgress();
    private volatile Path outputFile;
    private volatile TaskStatus status = TaskStatus.PENDING;

    public RedirectCheckTask(Path inputFile) {
        this.inputFile = inputFile;
    }

    public TaskProgress getTaskProgress() {
        return taskProgress;
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


    private boolean isCompleted() {
        return status.equals(TaskStatus.COMPLETED);
    }

    public String getOutputUri() {
        if (isCompleted()) {
            return outputFile.getFileName().toString();
        }
        return "";
    }
}
