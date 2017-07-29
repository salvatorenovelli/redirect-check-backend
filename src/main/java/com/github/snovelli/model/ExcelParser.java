package com.github.snovelli.model;

import com.github.salvatorenovelli.model.RedirectSpecification;
import com.github.salvatorenovelli.redirectcheck.io.RedirectSpecExcelParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExcelParser {
    public List<RedirectSpecification> getSpecsFromFile(Path inputFilename) throws IOException, InvalidFormatException {
        List<RedirectSpecification> specs = new ArrayList<>();
        RedirectSpecExcelParser parser = new RedirectSpecExcelParser(inputFilename.toString());
        parser.parse(specs::add);
        return specs;
    }
}
