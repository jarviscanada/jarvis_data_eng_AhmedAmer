package ca.jrvs.apps.practice;

import java.util.regex.Pattern;

public class RegexComparator implements RegexExc{
    @Override
    public boolean jpegMatch(String filename) {
        return Pattern.matches("^\\w+\\.jpeg$|^\\w+\\.jpg$", filename);
    }

    @Override
    public boolean ipMatch(String ip) {
        return Pattern.matches("^[0-9]{1,4}\\.[0-9]{1,4}\\.[0-9]{1,4}\\.?[0-9]*$", ip);
    }

    @Override
    public boolean isEmptyLine(String line) {
        return Pattern.matches("^\\s*$", line);
    }
}
