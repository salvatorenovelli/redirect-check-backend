package com.github.snovelli;

import com.github.snovelli.model.RedirectCheckTask;
import com.github.snovelli.model.RedirectCheckTaskRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@SessionScope
@Component
public class TaskQueue {


    private static final Logger logger = LoggerFactory.getLogger(TaskQueue.class);
    private static final AtomicInteger instance = new AtomicInteger(0);
    private final List<RedirectCheckTask> tasksPerUser = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService;
    private final int instanceNumber;


    @Autowired
    public TaskQueue(ExecutorService executorService) {
        instanceNumber = instance.getAndIncrement();
        logger.info("Instance {} created!", instanceNumber);
        this.executorService = executorService;

    }

    public void appendTask(Path storedFile) throws IOException, ExecutionException, InterruptedException {

        RedirectCheckTask task = new RedirectCheckTask(storedFile);
        tasksPerUser.add(task);
        executorService.submit(new RedirectCheckTaskRunner(task));
    }


    public List<RedirectCheckTask> listTasks() {
        logger.info("Getting tasks for instance {}", instanceNumber);
        return tasksPerUser;
    }


}
