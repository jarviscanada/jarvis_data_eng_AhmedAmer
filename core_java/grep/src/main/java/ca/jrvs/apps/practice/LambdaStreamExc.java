package ca.jrvs.apps.practice;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public interface LambdaStreamExc {

    /**
     * Create a String stream from an array of Strings
     * -
     * note: arbitrary number of Strings stored in an array
     * @param strings array of Strings
     * @return Stream of Strings
     */
    Stream<String> createStrStream(String ... strings);

    /**
     * Convert all strings to uppercase
     * -
     * Use createStrStream
     *
     * @param strings
     * @return Stream with uppercase Strings
     */
    Stream<String> toUpperCase(String ... strings);

    /**
     * Filter strings that contain the pattern
     * -
     * Pattern "a" will return another stream where no element contains "a"
     *
     * @param stringStream stream to filter
     * @param pattern pattern to exclude
     * @return Stream with pattern filtered out
     */
    Stream<String> filter(Stream<String> stringStream, String pattern);

    /**
     * Create an IntStream from an array of integers
     *
     * @param array array of ints
     * @return Stream of integers from the array
     */
    IntStream createIntStream(int[] array);

    /**
     * Convert a Stream to a List
     *
     * @param stream stream of elements to convert
     * @param <E> element type
     * @return List of elements from stream
     */
    <E> List<E> toList(Stream<E> stream);

    /**
     * Convert IntStream to List
     *
     * @param intStream
     * @return List of Integers
     */
    List<Integer> toList(IntStream intStream);

    /**
     * Create a IntStream from range
     * @param start
     * @param end
     * @return IntStream with ints from start to end
     */
    IntStream createIntStream(int start, int end);

    /**
     * Convert an IntStream to a doubleStream and compute the square root of each element
     * @param intStream
     * @return doubleStream of square-rooted values
     */
    DoubleStream squareRootIntStream(IntStream intStream);

    /**
     * Filter out all even integers in the IntStream
     * @param intStream
     * @return IntStream with only odd values
     */
    IntStream getOdd(IntStream intStream);

    /**
     * Return lambda function that prints a message with suffix and prefix
     * -
     * e.g. input:"message body" -> "prefix>messagebody<suffix"
     * @param prefix
     * @param suffix
     * @return Lambda Function
     */
    Consumer<String> getLambdaPrinter(String prefix, String suffix);

    /**
     * Use getLambdaPrinter to print each message with the formatted
     * style -> each message will have the prefix and suffix of the printer
     * @param messages
     * @param printer
     */
    void printMessages(String[] messages, Consumer<String> printer);

    /**
     * Print all odd numbers from an IntStream
     * -
     * Use with createIntStream and getLambdaPrinter methods
     * @param intStream
     * @param printer
     */
    void printOdd(IntStream intStream, Consumer<String> printer);

    /**
     * Square each number from the Stream of Lists of ints
     * @param ints
     * @return Stream of each value squared
     */
    Stream<Integer> flatNestedInt(Stream<List<Integer>> ints);
}
