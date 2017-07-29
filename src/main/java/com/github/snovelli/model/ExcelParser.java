package com.github.snovelli.model;


import com.github.salvatorenovelli.redirectcheck.io.RedirectSpecExcelParser;
import com.github.salvatorenovelli.redirectcheck.model.RedirectSpecification;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExcelParser {
    public List<RedirectSpecification> getSpecsFromFile(Path inputFilename) throws IOException {
        List<RedirectSpecification> specs = new ArrayList<>();
        RedirectSpecExcelParser parser = new RedirectSpecExcelParser(inputFilename.toString());
        parser.parse(specs::add);
        return specs;
    }
}
