package com.github.snovelli.model;

import com.github.salvatorenovelli.model.RedirectCheckResponse;
import com.github.salvatorenovelli.model.RedirectSpecification;
import com.github.salvatorenovelli.redirectcheck.cli.ProgressMonitor;
import com.github.salvatorenovelli.redirectcheck.domain.DefaultRedirectChainAnalyser;
import com.github.salvatorenovelli.redirectcheck.domain.RedirectChainAnalyser;
import com.github.salvatorenovelli.redirectcheck.http.DefaultHttpConnectorFactory;
import com.github.salvatorenovelli.redirectcheck.io.excel.RedirectCheckResponseExcelSerializer;
import com.github.salvatorenovelli.redirectcheck.redirect.ParallelRedirectSpecAnalyser;
import com.github.salvatorenovelli.redirectcheck.redirectcheck.RedirectCheckResponseFactory;
import com.github.salvatorenovelli.seo.redirect.DefaultRedirectSpecAnalyser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.github.snovelli.model.TaskStatus.*;

public class RedirectCheckTaskRunner implements Runnable {


    private static final Logger logger = LoggerFactory.getLogger(RedirectCheckTaskRunner.class);

    private static final int NUM_WORKERS = 50;
    private final RedirectCheckTask task;
    private final ExcelParser parser;


    public RedirectCheckTaskRunner(RedirectCheckTask task, ExcelParser parser) {
        this.task = task;
        this.parser = parser;
    }


    @Override
    public void run() {
        try {

            task.setStatus(IN_PROGRESS);
            List<RedirectSpecification> specs = parse();
            List<RedirectCheckResponse> responses = analyse(specs);
            serialise(specs, responses);
            task.setStatus(COMPLETED);

        } catch (IOException | InvalidFormatException | InterruptedException | ExecutionException e) {
            logger.error("Error while running task", e);
            task.setStatus(TaskStatus.FAILED);
        }

    }

    private void serialise(List<RedirectSpecification> specs, List<RedirectCheckResponse> responses) throws IOException {
        String outputFileName = removeExtension(task.getInputFile().toString()) + "_out.xls";
        RedirectCheckResponseExcelSerializer output = new RedirectCheckResponseExcelSerializer(outputFileName);
        output.addInvalidSpecs(invalid(specs));
        output.addResponses(responses);
        output.write();
        task.setOutputFile(Paths.get(outputFileName));
    }

    private List<RedirectCheckResponse> analyse(List<RedirectSpecification> specs) throws IOException, ExecutionException, InterruptedException {
        task.setStatus(TaskStatus.ANALYSING);
        DefaultRedirectChainAnalyser defaultRedirectChainAnalyser = new DefaultRedirectChainAnalyser(new DefaultHttpConnectorFactory());
        return analyseRedirects(valid(specs), defaultRedirectChainAnalyser, () -> {
        });
    }

    private List<RedirectSpecification> parse() throws IOException, InvalidFormatException {
        task.setStatus(PARSING);
        return parser.getSpecsFromFile(task.getInputFile());
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
