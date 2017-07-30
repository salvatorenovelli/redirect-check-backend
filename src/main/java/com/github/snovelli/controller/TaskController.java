package com.github.snovelli.controller;


import com.github.snovelli.TaskQueue;
import com.github.snovelli.model.RedirectCheckTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class TaskController {

    private final TaskQueue taskQueue;

    @Autowired
    public TaskController(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }


    @GetMapping("/tasks")
    public List<RedirectCheckTask> getTask() {
        return taskQueue.listTasks();
    }
}
