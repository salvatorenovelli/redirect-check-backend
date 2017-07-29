package com.github.snovelli;

import com.github.snovelli.model.ExcelParser;
import com.github.snovelli.model.RedirectCheckTask;
import com.github.snovelli.model.RedirectCheckTaskRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@SessionScope
@Component
public class TaskQueue {


    private final List<RedirectCheckTask> tasksPerUser = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService;


    @Autowired
    public TaskQueue(ExecutorService executorService) {
        this.executorService = executorService;

    }

    public void appendTask(Path storedFile) throws IOException, ExecutionException, InterruptedException {

        RedirectCheckTask task = new RedirectCheckTask(storedFile);
        tasksPerUser.add(task);
        executorService.submit(new RedirectCheckTaskRunner(task, new ExcelParser()));
    }


    public List<RedirectCheckTask> listTasks() {
        return tasksPerUser;
    }


}
