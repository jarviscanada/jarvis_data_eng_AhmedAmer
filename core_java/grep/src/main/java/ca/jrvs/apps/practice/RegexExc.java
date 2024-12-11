package ca.jrvs.apps.practice;

public interface RegexExc {

    /**
     * return true if file has jpeg or jpg extension
     * @param filename name of file
     */
    public boolean jpegMatch(String filename);

    /**
     * return true if IP address is valid
     * - possible values from 0.0.0.0 to 999.999.999.999
     * @param ip IP address
     */
    public boolean ipMatch(String ip);

    /**
     * return true if input line is empty (empty, only whitespace, tabs, etc...)
     * @param line line in question
     */
    public boolean isEmptyLine(String line);
}
