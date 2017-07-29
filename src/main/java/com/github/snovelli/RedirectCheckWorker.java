package com.github.snovelli;


import com.github.snovelli.model.RedirectCheckTask;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SessionScope
@Component
public class RedirectCheckWorker {

    private final List<RedirectCheckTask> tasksPerUser = new CopyOnWriteArrayList<>();

    public void appendTask(Path storedFile) {
        tasksPerUser.add(new RedirectCheckTask(storedFile));
    }

    public List<RedirectCheckTask> listTasks() {
        return tasksPerUser;
    }
}
