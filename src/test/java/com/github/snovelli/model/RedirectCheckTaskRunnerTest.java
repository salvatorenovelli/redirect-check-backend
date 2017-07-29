package com.github.snovelli.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Paths;


@RunWith(MockitoJUnitRunner.class)
public class RedirectCheckTaskRunnerTest {


    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    RedirectCheckTaskRunner sut;

    private RedirectCheckTask task;
    private ExcelParser parser = new ExcelParser();

    @Before
    public void setUp() throws Exception {
        task = new RedirectCheckTask(Paths.get("C:\\Users\\Salvatore\\Downloads\\gillette.xlsx"));
        sut = new RedirectCheckTaskRunner(task, parser);
    }

    @Test
    public void run() throws Exception {
        sut.run();
    }

}