package com.github.snovelli.model;


import com.github.salvatorenovelli.redirectcheck.DefaultRedirectSpecAnalyser;
import com.github.salvatorenovelli.redirectcheck.ParallelRedirectSpecAnalyser;
import com.github.salvatorenovelli.redirectcheck.RedirectCheckResponseFactory;
import com.github.salvatorenovelli.redirectcheck.cli.ProgressMonitor;
import com.github.salvatorenovelli.redirectcheck.domain.DefaultRedirectChainAnalyser;
import com.github.salvatorenovelli.redirectcheck.domain.RedirectChainAnalyser;
import com.github.salvatorenovelli.redirectcheck.http.DefaultHttpConnectorFactory;
import com.github.salvatorenovelli.redirectcheck.io.RedirectSpecExcelParser;
import com.github.salvatorenovelli.redirectcheck.io.excel.RedirectCheckResponseExcelSerializer;
import com.github.salvatorenovelli.redirectcheck.model.RedirectCheckResponse;
import com.github.salvatorenovelli.redirectcheck.model.RedirectSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.github.snovelli.model.TaskStatus.*;

public class RedirectCheckTaskRunner implements Runnable {


    private static final Logger logger = LoggerFactory.getLogger(RedirectCheckTaskRunner.class);

    private static final int NUM_WORKERS = 10;
    private final RedirectCheckTask task;


    public RedirectCheckTaskRunner(RedirectCheckTask task) {
        this.task = task;
    }


    @Override
    public void run() {
        try {

            task.setStatus(IN_PROGRESS);
            List<RedirectSpecification> specs = parse(task.getInputFile());
            List<RedirectCheckResponse> responses = analyse(specs);
            serialise(specs, responses, getOutputFileName());
            task.setStatus(COMPLETED);

        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error("Error while running task", e);
            task.setStatus(TaskStatus.FAILED);
        }

    }

    private void serialise(List<RedirectSpecification> specs, List<RedirectCheckResponse> responses, String outputFileName) throws IOException {
        RedirectCheckResponseExcelSerializer output = new RedirectCheckResponseExcelSerializer(outputFileName);
        output.addInvalidSpecs(invalid(specs));
        output.addResponses(responses);
        output.write();
        task.setOutputFile(Paths.get(outputFileName));
    }

    private String getOutputFileName() {
        return removeExtension(task.getInputFile().toString()) + "_out.xlsx";
    }

    private List<RedirectCheckResponse> analyse(List<RedirectSpecification> specs) throws IOException, ExecutionException, InterruptedException {
        task.setStatus(TaskStatus.ANALYSING);
        DefaultRedirectChainAnalyser defaultRedirectChainAnalyser = new DefaultRedirectChainAnalyser(new DefaultHttpConnectorFactory());
        return analyseRedirects(valid(specs), defaultRedirectChainAnalyser, task.getTaskProgress()::tick);
    }

    private List<RedirectSpecification> parse(Path inputFile) throws IOException {
        task.setStatus(PARSING);
        RedirectSpecExcelParser parser = new RedirectSpecExcelParser(inputFile.toString());
        List<RedirectSpecification> specs = new ArrayList<>();
        parser.parse(specs::add);
        task.getTaskProgress().setTotalTicks(parser.getNumSpecs());
        return specs;
    }

    private String removeExtension(String inputFilename) {
        return inputFilename.substring(0, inputFilename.lastIndexOf("."));
    }

    private List<RedirectCheckResponse> analyseRedirects(List<RedirectSpecification> specs, RedirectChainAnalyser chainAnalyser, ProgressMonitor progressBar) throws IOException, ExecutionException, InterruptedException {
        ParallelRedirectSpecAnalyser analyser =
                new ParallelRedirectSpecAnalyser(NUM_WORKERS,
                        new DefaultRedirectSpecAnalyser(chainAnalyser,
                                new RedirectCheckResponseFactory(),
                                progressBar));
        return analyser.runParallelAnalysis(specs);

    }

    private List<RedirectSpecification> invalid(List<RedirectSpecification> specs) {
        return specs.stream().filter((specification) -> !specification.isValid()).collect(Collectors.toList());
    }

    private List<RedirectSpecification> valid(List<RedirectSpecification> specs) {
        return specs.stream().filter(RedirectSpecification::isValid).collect(Collectors.toList());
    }

}
