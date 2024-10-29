package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {
    /**
     * Top Level search workflow
     * @throws IOException
     */
    void process() throws IOException;

    /**
     *Traverse a given directory and return all files
     * @param rootDir input directory
     * @return files under the rootDir
     */
    List<File> listFiles(String rootDir);

    /**
     * Read a file and return all lines
     * Utilizes FileReader, BufferedReader, and character encoding
     *
     * @param inputFile file to be read
     * @return lines inside file
     * @throws IllegalArgumentException if inputFile is not a valid file
     */
    List<String> readLines(File inputFile);

    /**
     * check if a line has the regex pattern inputted by user
     * @param line input string
     * @return true if match is found
     */
    boolean containsPattern(String line);

    /**
     * Writes line to a file
     * Uses FileOutputStream, OutputStreamWriter, and BufferedWriter
     *
     * @param lines matched line
     * @throws IOException if write to file fails
     */
    void writeToFile(List<String> lines) throws IOException;

    String getRootPath();

    void setRootPath(String rootPath);

    String getRegex();

    void setRegex(String regex);

    String getOutFile();

    void setOutFile(String outFile);
}
