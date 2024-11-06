package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp {

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new RuntimeException("Usage: java JavaGrepLambdaImp regex rootPath outFile");
        }

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            javaGrepLambdaImp.process();
        } catch (Exception e) {
            javaGrepLambdaImp.logger.error("ERROR: Unable to process", e);
        }
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();
        try (Stream<String> lineStream = Files.lines(inputFile.toPath())) {
            lines = lineStream.collect(Collectors.toList());
        } catch (IOException e) {
            this.logger.error("ERROR: Unable to read lines from ", e);
        }
        return lines;
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> files = new ArrayList<>();
        try (Stream<Path> fileStream = Files.walk(Paths.get(rootDir))) {
            files = fileStream.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            this.logger.error("ERROR: Unable to list files", e);
        }
        return files;
    }
}
